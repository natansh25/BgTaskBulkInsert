package com.example.natan.backgroundtasks;

import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.proto.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.natan.backgroundtasks.AsyncTaskLoader.MainActivityAsyncLoader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by natan on 2/9/2018.
 */
@RunWith(AndroidJUnit4.class)
public class MenuTest {

    @Rule
    public ActivityTestRule<MainActivityAsyncLoader> mMainActivityAsyncLoaderActivityTestRule
            = new ActivityTestRule<>(MainActivityAsyncLoader.class);

    @Test
    public void menuTest()
    {
        openContextualActionModeOverflowMenu();

        onView(withText("Settings"))
                .perform(click());

    }

// back navigation code
    @Test
    public void BackButtonTest()
    {

        openContextualActionModeOverflowMenu();

        onView(withText("Settings"))
                .perform(click());


        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());


        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));


    }


}
