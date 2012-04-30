package com.yippee.crawler;

import com.yippee.crawler.frontier.URLFrontier;
import com.yippee.db.crawler.DocAugManager;
import com.yippee.db.crawler.model.DocAug;
import com.yippee.indexer.Parser;
import com.yippee.util.Configuration;
import com.yippee.util.LinkTextExtractor;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class implements a spider worker,the building block of the Araneae
 * ThreadPool. All workers share the same task queue in order to handle
 * crawling URLs from the URLFrontier at will.
 */
public class Spider implements Runnable {
    /**
     * Create logger in the Log4j hierarchy named by by software component
     */
    static Logger logger = Logger.getLogger(Spider.class);
    private URLFrontier urlFrontier;
    private String id;
    private Spider[] spiders;
    private Araneae araneae;
    private boolean running;
    DocAugManager dam;

    /**
     * The -not so default- constructor. It keeps references to the whole thread
     * pool of araneae (in order to shutdown if needed from the Frontier), the
     * URLFrontier, its own thread it and the other spiders.
     *
     * @param urlFrontier
     * @param id
     * @param spiders
     * @param araneae
     */
    public Spider(URLFrontier urlFrontier, String id, Spider[] spiders, Araneae araneae) {
        this.urlFrontier = urlFrontier;
        this.id = id;
        this.spiders = spiders;
        this.araneae = araneae;
        running = true;
        dam = new DocAugManager();
    }

    /**
     * The run method is a standard entry point to run or execute in each
     * worker  thread. The Runnable  interface defines  this method, run,
     * meant to contain the code executed in the thread.
     * <p/>
     * In our case, when the spider runs simply downloads page content, based
     * on a number of conditions.
     */
    public void run() {
        logger.info("Thread " + Thread.currentThread().getName() + ": Starting");
        while (running && Configuration.getInstance().isUp()) {
            try {
            	
                Message msg = urlFrontier.pull();
                
                URL urlToCrawl = msg.getURL();
                
                logger.info("Got url to crawl: " + urlToCrawl.toString());
                
                Parser parser = new Parser();
                Document doc = null;
                HttpModule httpModule = new HttpModule(urlToCrawl);

                DocAug docAug = new DocAug();
                docAug.setDoc(httpModule.getContent());
                docAug.setUrl(urlToCrawl.toString());
                docAug.setId(urlToCrawl.toString() + " + timestamp");
                try {
                    logger.info("About to parse doc: " + urlToCrawl.toString());

                    doc = parser.parseDoc(docAug);
                    
                    logger.info("Doc parsed: " + urlToCrawl.toString());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                logger.debug("Pushing something to: " + Configuration.getInstance().getBerkeleyDBRoot());
                logger.debug("\t from URL: " + urlToCrawl.toString());
                dam.push(docAug);
                
                LinkTextExtractor linkEx = new LinkTextExtractor();
                logger.info("About to extract links from: " + urlToCrawl.toString());
                linkEx.extract(urlToCrawl.toString(), doc);
                logger.info("Done extracting links from: " + urlToCrawl.toString());

                ArrayList<String> links = linkEx.getLinks();
                
                boolean foundLinks = links.size() > 0;
                if(foundLinks)  logger.info("Found links on: " + urlToCrawl.toString());
                else logger.info("Found no links on: " + urlToCrawl.toString());
                
                for(String s : links){
                	logger.debug("Found link: " + s);
                	urlFrontier.push(new Message(s));
                	foundLinks = true;
                }
                
               
                
                //Store state periodically
                if(System.nanoTime() % 10000 == 0){
                	urlFrontier.save();
                	logger.info("Stored frontier state");
                }
                
                RobotsModule robotsModule = new RobotsModule();
                logger.info("About to send found links.");
                for (String newUrl : links){
                    URL url;
                    try {
                        url = new URL(newUrl);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        continue; // skip that url
                    }
                    
                    //For now, bypass checking robots
                    Configuration.getInstance().getPastryEngine().sendURL(url);
                    
//                    try{
//                    	 if (robotsModule.alowedToCrawl(url)){
//                             Configuration.getInstance().getPastryEngine().sendURL(url);
//                         }
//                    }catch(IllegalStateException e){
//                    	logger.warn("IllegalStateException", e);
//                    }
                   
                }
            } catch (InterruptedException e) {
                //e.printStackTrace();
                logger.debug("Thread " + Thread.currentThread().getName() + ": Shutting down..");
                running = false;
                break;
            }

        }

    }
}
