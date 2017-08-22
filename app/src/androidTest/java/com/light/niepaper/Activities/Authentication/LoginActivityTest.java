package com.light.niepaper.Activities.Authentication;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.light.niepaper.R;

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
public class LoginActivityTest
{
    String emailAddress = "testuser@gmail.com";
    String password = "Password";
    @Rule
    public ActivityTestRule<LoginActivity> mLoginTestRule = new
            ActivityTestRule<LoginActivity>(LoginActivity.class);
    @Test
    public void runLogin(){
        onView(withId(R.id.emaillogin)).perform(typeText(emailAddress), closeSoftKeyboard());
        onView(withId(R.id.passwordlogin)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        //onView(withText("Login in Process...")).check(matches(isDisplayed()));
        onView(withId(R.id.schedulder)).check(matches(allOf(isDescendantOfA(withId(R.id.layout_mainscreen)),isDisplayed())));
    }
}