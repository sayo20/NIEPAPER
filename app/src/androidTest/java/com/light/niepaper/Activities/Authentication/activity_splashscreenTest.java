package com.light.niepaper.Activities.Authentication;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.light.niepaper.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static org.junit.Assert.*;
import static android.app.PendingIntent.getActivity;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.light.niepaper.R.id.layout_mainscreen;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;
/**
 * Created by sayo on 7/6/2017.
 */
@RunWith(AndroidJUnit4.class)

public class activity_splashscreenTest {
//    @Before
//    public void setUp() throws Exception {
//
//    }
    @Rule
    public ActivityTestRule<activity_splashscreen> mSplashActivityTestRule = new
        ActivityTestRule<activity_splashscreen>(activity_splashscreen.class);
    @Test
    public void testForRegister(){
        onView(withId(R.id.signup)).perform(click());
        onView(withId(R.id.et_name)).check(matches(allOf(isDescendantOfA(withId(R.id.rlayout_egisterscreen)))));
    }

    @Test
    public void testForlogin(){
        onView(withId(R.id.login)).perform(click());
        //to check that login screen is displayed by checking for a widget
        onView(withId(R.id.emaillogin)).check(matches(allOf(isDescendantOfA(withId(R.id.layout_login)))));
    }

}