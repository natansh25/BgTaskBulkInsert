package com.example.natan.backgroundtasks;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.anything;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.natan.backgroundtasks.AsyncTaskLoader.MainActivityAsyncLoader;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * Created by natan on 2/9/2018.
 */

@RunWith(AndroidJUnit4.class)

public class IdlingResourceMain {

    @Rule
    public ActivityTestRule<MainActivityAsyncLoader> mActivityTestRule =
            new ActivityTestRule<>(MainActivityAsyncLoader.class);

    private IdlingResource mIdlingResource;


    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
        Espresso.registerIdlingResources(mIdlingResource);
    }









}
