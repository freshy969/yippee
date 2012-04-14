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
    private byte[] id;
    
    /**
     * string for this word
     */
    @PrimaryKey
    private String word;

    public Word(){
    }

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


}
