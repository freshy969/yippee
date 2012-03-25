package edu.upenn.cis.cis555.persistence;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

import java.util.Date;

import static com.sleepycat.persist.model.Relationship.*;


@Entity
public class DocAug {
    @PrimaryKey
    private int id;
    private String url;
    private String Doc;
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
        return Doc;
    }

    public void setDoc(String doc) {
        Doc = doc;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


}
