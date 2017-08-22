package com.light.niepaper.MainActivityNotes;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.light.niepaper.Activities.Notepad.makeNote;
import com.light.niepaper.Activities.Notepad.textPad;
import com.light.niepaper.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.longClick;
import static org.junit.Assert.*;
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
public class textPadTest {
    @Rule
    public ActivityTestRule<textPad> mSplashActivityTestRule = new
            ActivityTestRule<textPad>(textPad.class);
    @Test
    public void saveNote(){
        String test_note = "This is me ruunung Instrumentation text";
        String test_title = "Instrumentation Test";
        onView(withId(R.id.edit_text_title)).perform(typeText(test_title), closeSoftKeyboard());
        onView(withId(R.id.edit_text_note)).perform(typeText(test_note), closeSoftKeyboard());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getContext());
        onView(withText(R.string.saveTextx)).perform(click());
        onView(withId(R.id.fab_menunotes)).check(matches(allOf(isDescendantOfA(withId(R.id.layout_makenote)),isDisplayed())));
    }

}