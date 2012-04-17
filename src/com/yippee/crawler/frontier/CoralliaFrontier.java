package com.yippee.crawler.frontier;

import com.yippee.crawler.Message;

public class CoralliaFrontier implements URLFrontier{

    public Message pull() throws InterruptedException {
        return null;
    }

    public void push(Message message) {

    }

    public boolean save() {
        return false;
    }

    public boolean load() {
        return false;
    }
}
