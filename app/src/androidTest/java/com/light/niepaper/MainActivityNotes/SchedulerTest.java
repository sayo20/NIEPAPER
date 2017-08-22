package com.light.niepaper.MainActivityNotes;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.light.niepaper.Activities.Scheduler.Scheduler;
import com.light.niepaper.Activities.Scheduler.Schedules;
import com.light.niepaper.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.*;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.app.PendingIntent.getActivity;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
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
public class SchedulerTest {
    @Rule
    public ActivityTestRule<Scheduler> mSplashActivityTestRule = new
            ActivityTestRule<Scheduler>(Scheduler.class);

    @Test
    public void createSchedules(){
        String testschedtitle = "Instrumentation Test";
        String testschedtotdo = "Finish chapter 6";

        onView(withId(R.id.edit_sched_titles)).perform(typeText(testschedtitle), closeSoftKeyboard());
        onView(withId(R.id.schedmessage)).perform(typeText(testschedtotdo), closeSoftKeyboard());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getContext());
        onView(withText(R.string.saveTextx)).perform(click());
        onView(withId(R.id.fab_menuscheduler)).check(matches(allOf(isDescendantOfA(withId(R.id.layout_schedules)),isDisplayed())));
    }

}