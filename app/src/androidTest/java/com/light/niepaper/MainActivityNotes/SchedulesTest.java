package com.light.niepaper.MainActivityNotes;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.light.niepaper.Activities.Notepad.textPad;
import com.light.niepaper.Activities.Scheduler.Schedules;
import com.light.niepaper.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.longClick;
import static org.junit.Assert.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
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
public class SchedulesTest {
    @Rule
    public ActivityTestRule<Schedules> mSplashActivityTestRule = new
            ActivityTestRule<Schedules>(Schedules.class);
    @Test
    public void enterScheduler(){
        onView(withId(R.id.fab_menuscheduler)).perform(click());
        onView(withId(R.id.edit_sched_titles)).check(matches(allOf(isDescendantOfA(withId(R.id.layout_scheduler)),isDisplayed())));
    }
    @Test
    public void deleteSchedules(){
        onView(withId(R.id.listViewschedule)).perform(longClick());
        onView(withText(R.string.deleteTextx)).check(matches(isDisplayed()));
    }
}