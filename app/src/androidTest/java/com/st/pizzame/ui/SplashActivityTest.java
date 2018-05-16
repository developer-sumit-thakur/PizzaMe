package com.st.pizzame.ui;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.st.pizzame.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by sumit.thakur on 5/15/18.
 */
@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {
    @Rule
    public ActivityTestRule<SplashActivity> splashActivityTestRule = new ActivityTestRule<>(SplashActivity.class);


    private SplashActivity splashActivity;

    @Before
    public void setup() {
        splashActivity = splashActivityTestRule.getActivity();
    }

    @Test
    public void testSplashActivity() throws Exception {
        onView(withId(R.id.splash_container)).check(matches((isDisplayed())));
    }

    @Test
    public void testMainActivityIntent() throws Exception {
        Intent expectedIntent = new Intent(splashActivity, MainActivity.class);
        assertNotNull(expectedIntent);
    }
}