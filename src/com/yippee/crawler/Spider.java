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

                logger.info("start spider ");
                Message msg = urlFrontier.pull();
                URL urlToCrawl = msg.getURL();
                //url.getURL()
                Parser parser = new Parser();
                Document doc = null;
                HttpModule httpModule = new HttpModule(urlToCrawl);

                DocAug docAug = new DocAug();
                docAug.setDoc(httpModule.getContent());
                docAug.setUrl(urlToCrawl.toString());
                try {
                    doc = parser.parseDoc(docAug);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                logger.info("Pushing something " + Configuration.getInstance().getBerkeleyDBPath());
                logger.info("1");
                DocAugManager dam = new DocAugManager(Configuration.getInstance().getBerkeleyDBPath());
                logger.info("1");
                dam.push(docAug);
                logger.info("2");
                LinkTextExtractor linkEx = new LinkTextExtractor();
                ArrayList<String> links = linkEx.getLinks();
                RobotsModule robotsModule = new RobotsModule();
                for (String newUrl : links){
                    logger.info("Found URL " + newUrl);
                    URL url;
                    try {
                        url = new URL(newUrl);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        continue; // skip that url
                    }
                    if (robotsModule.alowedToCrawl(url)){
                        Configuration.getInstance().getPastryEngine().sendURL(url);
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
