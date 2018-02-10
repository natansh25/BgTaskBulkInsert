package com.example.natan.backgroundtasks;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.example.natan.backgroundtasks.AsyncTaskLoader.MainActivityAsyncLoader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.contrib.RecyclerViewActions;

/**
 * Created by natan on 2/9/2018.
 */


@RunWith(AndroidJUnit4.class)
public class RecyclerViewTest {

    private static final int ITEM_BELOW_THE_FOLD = 4;


    @Rule
    public ActivityTestRule<MainActivityAsyncLoader> mMainActivityAsyncLoaderActivityTestRule
            = new ActivityTestRule<>(MainActivityAsyncLoader.class);


    @Test
    public void scrollToPosition() {

        onView(ViewMatchers.withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BELOW_THE_FOLD, click()));

    }

    /*@Test
    public void itemInMiddleOfList_hasSpecialText() {
        // First, scroll to the view holder using the isInTheMiddle matcher.
        onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollToHolder(isInTheMiddle()));


    }*/

}
