package com.yippee.db.model;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

import java.util.Date;

import static com.sleepycat.persist.model.Relationship.*;


@Entity
public class DocAug {
    @PrimaryKey
    private int id;
    private String url;
    private String doc;
    private Date time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        doc = doc;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


}
