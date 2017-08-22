package com.light.niepaper.MainActivityNotes;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.light.niepaper.Activities.Authentication.profileActivity;
import com.light.niepaper.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.app.PendingIntent.getActivity;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by sayo on 7/6/2017.
 */
@RunWith(AndroidJUnit4.class)
public class profileActivityTest {
    @Rule
    public ActivityTestRule<profileActivity> mSplashActivityTestRule = new
            ActivityTestRule<profileActivity>(profileActivity.class);

    @Test
    public void testScheduleButton(){
        onView(withId(R.id.schedulder)).perform(click());
        onView(withId(R.id.fab_menuscheduler)).check(matches(allOf(isDescendantOfA(withId(R.id.layout_schedules)),isDisplayed())));
        onView(withId(R.id.toolbarschedules)).perform(click());

    }
    @Test
    public void testNoteButton(){
        onView(withId(R.id.notebook)).perform(click());
        onView(withId(R.id.fab_menunotes)).check(matches(allOf(isDescendantOfA(withId(R.id.layout_makenote)),isDisplayed())));
        onView(withId(R.id.toolbarmakenote)).perform(click());

    }
    @Test
    public void testChatButton(){
        onView(withId(R.id.chat)).perform(click());
        onView(withId(R.id.fab_menugroupname)).check(matches(allOf(isDescendantOfA(withId(R.id.layout_chatgroups)),isDisplayed())));
        onView(withId(R.id.toolbargroupname)).perform(click());

    }
    @Test
    public void testOverflowmenu(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getContext());
        onView(withText(R.string.action_settings)).perform(click());
        onView(withId(R.id.emaillogin)).check(matches(allOf(isDescendantOfA(withId(R.id.layout_login)))));
    }
}