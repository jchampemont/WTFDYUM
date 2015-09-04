/*
 * Copyright (C) 2015 WTFDYUM
 *
 * This file is part of the WTFDYUM project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeanchampemont.wtfdyum.service.feature;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.jeanchampemont.wtfdyum.WTFDYUMApplication;
import com.jeanchampemont.wtfdyum.dto.Event;
import com.jeanchampemont.wtfdyum.dto.Principal;
import com.jeanchampemont.wtfdyum.dto.User;
import com.jeanchampemont.wtfdyum.dto.type.EventType;
import com.jeanchampemont.wtfdyum.service.feature.impl.TweetUnfollowFeatureService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WTFDYUMApplication.class)
public class TweetUnfollowFeatureServiceTest extends AbstractFeatureServiceTest {
	
	/**
	 * _init.
	 */
    @Before
	public void _init() {
    	super._init();
		sut = new TweetUnfollowFeatureService(principalService, userService, twitterService, TWEET_TEXT);
		ReflectionTestUtils.setField(sut, "featureRedisTemplate", featureRedisTemplate);
	}

    /**
     * Checks for cron test.
     */
    @Test
    public void hasCronTest() {
    	assertThat(sut.hasCron()).isTrue();
    }
    
    /**
     * Cron test.
     *
     * @throws Exception the exception
     */
    @Test
    public void cronTest() throws Exception {
    	 final Principal principal = principal(1L);

         final Set<Long> followers = followers(principal, (s, f) -> s.thenReturn(f));

         final List<User> unfollowers = unfollowers(principal, followers, (s, u) -> s.thenReturn(u));

         Set<Event> events = sut.cron(1L);

         verifyUnfollowTweet(principal, unfollowers.get(0));
         verifyUnfollowTweet(principal, unfollowers.get(1));
         
         assertThat(events.contains(new Event(EventType.UNFOLLOW, unfollowers.get(0).getScreenName())));
         assertThat(events.contains(new Event(EventType.UNFOLLOW, unfollowers.get(1).getScreenName())));
    }
    
    /**
     * Complete cron test.
     *
     * @throws Exception the exception
     */
    @Test
    public void completeCronTest() throws Exception {
    	final Principal principal = principal(1L);

        final Set<Long> followers = followers(principal, (s, f) -> s.thenReturn(f));
        
        sut.completeCron(1L);
        
        // New followers list should be saved
        verify(userService, times(1)).saveFollowers(1L, followers);
    }
}
