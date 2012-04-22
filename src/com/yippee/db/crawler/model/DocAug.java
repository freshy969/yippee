package com.yippee.db.crawler.model;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

import java.util.Date;
import java.util.Map;

@Entity
public class DocAug{
	
	/**
	 * Unique id for this document crawled at some time
	 */
    @PrimaryKey
    private String id;
    /**
     * url for this document
     */
    private String url;
    /**
     * String for the contents of the crawled document
     */
    private String doc;
    /**
     * time doc was crawled
     */
    private Date time;
    /**
     * Headers received when doc was crawled
     */
    private Map<String, String> headers;

    public DocAug(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

}
