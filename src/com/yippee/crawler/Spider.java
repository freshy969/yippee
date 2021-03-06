package com.yippee.crawler;

import com.yippee.crawler.frontier.URLFrontier;
import com.yippee.db.crawler.DocAugManager;
import com.yippee.db.crawler.model.DocAug;
import com.yippee.util.Configuration;
import org.apache.log4j.Logger;

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
	private String threadName;
	private Spider[] spiders;
	private Araneae araneae;
	private boolean running;
	DocAugManager dam;
	RobotsModule robotsModule;
    private static final int MAX_SIZE = 1048576; // no more than one MEG!

	/**
	 * The -not so default- constructor. It keeps references to the whole thread
	 * pool of araneae (in order to shutdown if needed from the Frontier), the
	 * URLFrontier, its own thread it and the other spiders.
	 *
	 * @param urlFrontier
	 * @param threadName
	 * @param spiders
	 * @param araneae
	 */
	public Spider(URLFrontier urlFrontier, String threadName, Spider[] spiders, Araneae araneae) {
		this.urlFrontier = urlFrontier;
		this.threadName = threadName;
		this.spiders = spiders;
		this.araneae = araneae;
		running = true;
		dam = new DocAugManager();
		robotsModule = new RobotsModule();
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
				logger.debug("About to pull a URL");
				Message msg = urlFrontier.pull();
				URL urlToCrawl = (msg != null) ? msg.getURL() : null;
				
				if(urlToCrawl == null || dam.read(urlToCrawl.toString()) != null ){
					Thread.sleep(10000);
					continue;
				}
				
				//Hack to skip urls that have a #
				// because http://www.upenn.edu#content is the same as http://www.upenn.edu#content
				if(urlToCrawl.toString().contains("#")){ 
					continue;
				}
				
				logger.debug("Pulled url: " + urlToCrawl);
				
				HttpModule httpModule = new HttpModule(urlToCrawl);
				logger.debug("Got content from url: " + urlToCrawl);
				
				
				//IllegalArgument 
				String content = ""; 
				try{
					content = httpModule.getContent();
					
				} catch(IllegalArgumentException e) {
					logger.error("IllegalArgumentException", e);
					continue;
				}

                // There was an error or the content was huge
				if (!httpModule.isValid() || content.length() > MAX_SIZE) continue;

				DocAug docAug = new DocAug();
				docAug.setDoc(content);
				docAug.setUrl(urlToCrawl.toString());
				docAug.setId(urlToCrawl.toString());

				logger.info("Try to save DocAug for " + urlToCrawl.toString());
				
				dam.push(docAug);
				System.out.println("Pushed: " + urlToCrawl.toString());
				
				
				LinkTextExtractor linkEx = new LinkTextExtractor();
				ArrayList<String> links;
				try {
					logger.debug("About to extract links");
					links = linkEx.smartExtract(urlToCrawl, content);
				} catch (CrawlerException e) {
					logger.info("Crawler Exception: ", e);
					continue;
				} catch(NullPointerException e){
					System.out.println("Null Pointer in ");
					logger.info("NullPointer in LinkExtractor", e);
					continue;
				}
				logger.debug("Done extracting links");

				if(links.size() > 0) logger.info("Found some links on:" + urlToCrawl);

				logger.debug("Asking robots for each link");
				for (String newUrl : links){
					if (newUrl == null || newUrl.contains("https")) {
                        logger.debug("Skip: " + newUrl);
						continue;
					}
					URL url;
					try {
						url = new URL(newUrl);
						//logger.info("About to ask robots about: " + url);
					} catch (MalformedURLException e) {
						logger.warn("Malformed URL Exception");
						continue; // skip that url
					}

					try{
						if (robotsModule.allowedToCrawl(url) && !urlFrontier.isSeen(url.toString())){
                            //logger.info("Sending.............");
							//Configuration.getInstance().getPastryEngine().sendURL(url);
                            Message newMessage = new Message(url.toString());
                            if (newMessage.getType() == Message.Type.NEW) {
                                //urlFrontier.push(newMessage);
                                Configuration.getInstance().getPastryEngine().sendURL(url);
                            }
						} else {
                            logger.info("Robots returned false or duplicate exists");
                        }
					}catch(IllegalStateException e){
						logger.warn("IllegalStateException", e);
					}
				}
			} catch (InterruptedException e) {
				//e.printStackTrace();
				logger.debug("Thread " + Thread.currentThread().getName() + ": Shutting down..");
				//running = false;
				continue;
			}

		}

	}
}
