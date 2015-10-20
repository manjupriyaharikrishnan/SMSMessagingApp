package com.asap.messenger.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.asap.messenger.ViewAllMessagesActivity;
import com.asap.messenger.helper.MessageHelper;

/**
 * Created by Umadevi on 10/20/2015.
 */
public class ViewAllMessagesActivityTest extends ActivityInstrumentationTestCase2<ViewAllMessagesActivity> {

    private ViewAllMessagesActivity viewAllMessagesActivity;
    MessageHelper messageHelper;
    Intent mLaunchIntent;

    public ViewAllMessagesActivityTest(String name) {
        super("com.asap.messenger", ViewAllMessagesActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ViewAllMessagesActivity activity = getActivity();
    }

    @MediumTest
    public void testActivity() {

    }

}
