package com.example.natan.backgroundtasks;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.natan.backgroundtasks.AsyncTaskLoader.MainActivityAsyncLoader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by natan on 2/9/2018.
 */

@RunWith(AndroidJUnit4.class)

public class TestingIdleState {

    @Rule
    public ActivityTestRule<MainActivityAsyncLoader> mMainActivityAsyncLoaderActivityTestRule
            = new ActivityTestRule<>(MainActivityAsyncLoader.class);



    @Test
    public void menuTest()
    {
        openContextualActionModeOverflowMenu();

        onView(withText("SERVICE_TEST"))
                .perform(click());

        onView(ViewMatchers.withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(4, click()));




    }







}
