package com.jeanchampemont.wtfdyum.utils;

import java.util.ArrayList;

import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;

/**
 * The Class ResponseListMockForTest.
 *
 * @param <T>
 *            the generic type
 */
public class ResponseListMockForTest<T> extends ArrayList<T>implements ResponseList<T> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2964295762238022575L;

    /*
     * (non-Javadoc)
     * 
     * @see twitter4j.TwitterResponse#getAccessLevel()
     */
    @Override
    public int getAccessLevel() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see twitter4j.ResponseList#getRateLimitStatus()
     */
    @Override
    public RateLimitStatus getRateLimitStatus() {
        throw new UnsupportedOperationException();
    }
}
