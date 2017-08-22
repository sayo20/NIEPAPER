package com.light.niepaper.MainActivityNotes;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.light.niepaper.Activities.Authentication.profileActivity;
import com.light.niepaper.Activities.Notepad.makeNote;
import com.light.niepaper.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
public class profileActivityTextNotesTest {
    @Rule
    public ActivityTestRule<makeNote> mSplashActivityTestRule = new
            ActivityTestRule<makeNote>(makeNote.class);
    @Test
    public void enterNotesScreen(){
        String test_note = "This is me ruunung Instrumentation text";
        String test_title = "Instrumentation Test";

        onView(withId(R.id.fab_menunotes)).perform(click());
        onView(withId(R.id.fab_note)).perform(click());
        onView(withId(R.id.edit_text_title)).check(matches(allOf(isDescendantOfA(withId(R.id.layout_textpad)),isDisplayed())));

    }
    @Test
    public void testSketchPad(){
        onView(withId(R.id.fab_menunotes)).perform(click());
        onView(withId(R.id.fab_sketch)).perform(click());
//        onView(withText(R.string.saveTextx)).perform(click());
//        onView(withId(R.id.fab_menu)).check(matches(allOf(isDescendantOfA(withId(R.id.layout_mainscreen)),isDisplayed())));
    }
    @Test
    public void deleteNote(){
        onView(withId(R.id.listViewa)).perform(longClick());
        onView(withText(R.string.deleteTextx)).check(matches(isDisplayed()));
    }
}