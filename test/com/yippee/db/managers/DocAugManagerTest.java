package com.yippee.db.managers;

import static org.junit.Assert.*;

import com.yippee.db.model.DocAug;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class DocAugManagerTest {
    DocAugManager docAugManager;
    DocAug docAug;

    @Before
    public void setUp(){
        // every time it creates a new docAug, with a new timestamp, thus timestamp notEquals
        docAug = new DocAug();
        docAug.setDoc("<root><this><is><a><doc></doc></a></is></this></root>");
        docAug.setTime(new Date());
        docAug.setUrl("http://this.is.ate.st");
        docAug.setId("1");
        docAugManager = new DocAugManager();
    }

    @Test
    public void testInsert(){
        assertTrue(docAugManager.create(docAug));
    }
    @Test
    public void testRead(){
        assertEquals(docAugManager.read("1").getId(), docAug.getId());
    }

    @Test
    public void testDelete(){
        assertTrue(docAugManager.delete("1"));
    }
    @After
    public void tearDown(){
        docAugManager.close();
    }

}
