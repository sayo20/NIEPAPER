package com.light.niepaper.Activities.Authentication;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.light.niepaper.Activities.Authentication.RegisterActivity;
import com.light.niepaper.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

import static android.support.test.espresso.Espresso.onView;
/**
 * Created by sayo on 7/5/2017.
 */
@RunWith(AndroidJUnit4.class)

public class ARegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> mRegisterTestRule = new
            ActivityTestRule<RegisterActivity>(RegisterActivity.class);
    @Test
    public void checkRegisterProcess(){
        String username = "Test User";
        String emailAddress = "testuser@gmail.com";
        String password = "Password";

        onView(ViewMatchers.withId(R.id.et_name)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.et_email)).perform(typeText(emailAddress), closeSoftKeyboard());
        onView(withId(R.id.et_passwd)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        onView(withId(R.id.schedulder)).check(matches(allOf(isDescendantOfA(withId(R.id.layout_mainscreen)),isDisplayed())));
    }

}