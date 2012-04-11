package com.yippee.db.model;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

import java.util.Date;
import java.util.Map;

@Entity
public class Word{
	
	/**
	 * Unique id for this word
	 */
    @PrimaryKey
    private String id;
    /**
     * string for this word
     */
    private String word;

    public Word(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


}
