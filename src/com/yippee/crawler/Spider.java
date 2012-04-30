package com.yippee.crawler;

import com.yippee.crawler.frontier.URLFrontier;
import com.yippee.db.crawler.DocAugManager;
import com.yippee.db.crawler.model.DocAug;
import com.yippee.indexer.Parser;
import com.yippee.util.Configuration;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

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
                //url.getURL()
                Parser parser = new Parser();
                Document doc = null;
                HttpModule httpModule = new HttpModule(urlToCrawl);

                String content = httpModule.getContent();

                if (!httpModule.isValid()) continue; // There was an error!

                DocAug docAug = new DocAug();
                docAug.setDoc(content);
                docAug.setUrl(urlToCrawl.toString());
                docAug.setId(urlToCrawl.toString() + "timestamp");

                dam.push(docAug);
                LinkTextExtractor linkEx = new LinkTextExtractor();
                ArrayList<String> links = null;
                try {
                    links = linkEx.smartExtract(urlToCrawl, content);
                } catch (CrawlerException e) {
                    System.out.println("ERROR!!!");
                    continue;
                }

                //Store state periodically --
                // TODO: it will either be another thread or a per url basis
//                if(System.nanoTime() % 100 == 0){
//                	urlFrontier.save();
//                	logger.debug("Stored frontier state");
//                }

                RobotsModule robotsModule = new RobotsModule();
                int i = 0;
                for (String newUrl : links){
                    if (newUrl == null || newUrl.contains("https")) {
                        continue;
                    }
                    //logger.info("Found URL " + ++i);
                    URL url;
                    try {
                        url = new URL(newUrl);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        continue; // skip that url
                    }
                    
                    try{
                    	 if (robotsModule.alowedToCrawl(url)){
                             Configuration.getInstance().getPastryEngine().sendURL(url);
                         }
                    }catch(IllegalStateException e){
                    	logger.warn("IllegalStateException", e);
                    }
                   
                }
            } catch (InterruptedException e) {
                //e.printStackTrace();
                logger.info("Thread " + Thread.currentThread().getName() + ": Shutting down..");
                running = false;
                break;
            }

        }

    }
}
