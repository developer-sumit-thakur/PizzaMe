package com.st.pizzame.ui;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.st.pizzame.R;
import com.st.pizzame.model.LocationView;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by sumit.thakur on 5/16/18.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private MainActivity mainActivity;
    private JSONArray mockedJsonArray = new JSONArray();

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setup() {
        mainActivity = mainActivityTestRule.getActivity();
    }

    @Test
    public void testNoDataAvailable() throws Throwable {

        //assert(locationView.onError("Data not available."));
        mainActivityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.onError("Data not available.");
            }
        });
        onView(withId(R.id.error_text)).check(matches((isDisplayed())));
    }

    @Test
    public void testDataAvailable() throws Throwable {

        mainActivityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.onDataLoaded(mockedJsonArray);
            }
        });

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }
}