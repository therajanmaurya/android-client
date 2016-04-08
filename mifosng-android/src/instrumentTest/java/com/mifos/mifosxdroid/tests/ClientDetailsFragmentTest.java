package com.mifos.mifosxdroid.tests;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.mifos.mifosxdroid.R;
import com.mifos.mifosxdroid.online.ClientActivity;
import com.mifos.utils.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Gabriel Esteban on 07/12/14.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ClientDetailsFragmentTest {


    private static String displayName = "Smith R";
    private static String accountNo = "000000001";
    private static String officeName = "Head Office";


    @Rule
    public ActivityTestRule<ClientActivity> mClientDetailsFragmentTest =
            new ActivityTestRule<>(ClientActivity.class, true,false);

    @Before
    public void intentWithStubbedClientId(){
       // registerIdlingResource();
        Intent clientActivityIntent = new Intent();
        clientActivityIntent.putExtra(Constants.CLIENT_ID, "000000001");
        mClientDetailsFragmentTest.launchActivity(clientActivityIntent);


    }


    @Test
    public void clientDetailsDisplayedInUi() {

        onView(withId(R.id.tv_fullName)).check(matches(withText(displayName)));
        onView(withId(R.id.tv_office)).check(matches(withText(officeName)));
        onView(withId(R.id.tv_accountNumber)).check(matches(withText(accountNo)));

    }


    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    public void unregisterIdlingResource() {
       /* Espresso.unregisterIdlingResources(
                mClientDetailsFragmentTest.getActivity().getCountingIdlingResource());*/
    }

    /**
     * Convenience method to register an IdlingResources with Espresso. IdlingResource resource is
     * a great way to tell Espresso when your app is in an idle state. This helps Espresso to
     * synchronize your test actions, which makes tests significantly more reliable.
     */
    private void registerIdlingResource() {
       /* Espresso.registerIdlingResources(
                mClientDetailsFragmentTest.getActivity().getCountingIdlingResource());*/
    }
}
