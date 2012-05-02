package com.yippee.db.crawler.model;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * The model for the Duplicate URL
 */
@Entity
public class DuplicateURL {
    /**
	 * Unique id is the full url
	 */
    @PrimaryKey
    private String url;
    /**
     * A minor statistic kept
     */
    private int hits;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void addHit(){
        hits++;
    }
}
