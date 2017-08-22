package com.light.niepaper;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by sayo on 7/6/2017.
 */
public class UserInterfaceTest  extends InstrumentationTestCase {
    private UiDevice device;
    @Before
    public void setUp() throws Exception {

        device = UiDevice.getInstance(getInstrumentation());
        device.pressHome();
        device.wait(Until.hasObject(By.desc("Apps")), 3000);

    }
    public void testAdd() throws Exception {
        UiObject2 appsButton = device.findObject(By.desc("Apps"));
        appsButton.click();
        device.wait(Until.hasObject(By.text("NIEPAPER")), 3000);
        UiObject2 myapp = device.findObject(By.text("NIEPAPER"));
        myapp.click();
        device.wait(Until.hasObject(By.text("LOGIN")), 3000);
        UiObject2 Loginsplash = device.findObject(By.text("LOGIN"));
        Loginsplash.click();
        device.waitForIdle(3000);
        UiObject2 emailedit= device.findObject(By.clazz("android.widget.EditText"));
        UiObject2 passwordedit = device.findObject(By.clazz("android.widget.EditText"));
        UiObject2 Login = device.findObject(By.text("LOGIN"));
        Login.click();
        device.wait(Until.hasObject(By.text("Scheduler")), 3000);
        UiObject2 Scheduler = device.findObject(By.text("Scheduler"));
        Scheduler.click();
    }
}