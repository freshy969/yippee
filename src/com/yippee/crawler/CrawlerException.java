package com.yippee.crawler;

/**
 * A crawler custom exception so lower layers can safely notify upper layers
 */
class CrawlerException extends Exception {
    /**
     * The subtype of the custom exception
     */
    private String subType;

    /**
     * Default constructor
     * initializes custom exception variable to none
     */
    public CrawlerException() {
        super();
        subType = "";

    }

    /**
     * Custom Exception Constructor -- adds handling of the subType
     */
    public CrawlerException(String message, String subType) {

        super(message);
        this.subType = subType;
    }

    public String getSubType() {
        return subType;
    }
}
