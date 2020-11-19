package com.example.wastedfoodteam.login;

import android.app.Activity;
import android.widget.LinearLayout;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import com.example.wastedfoodteam.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class FragmentLoginBuyerTest{

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
    private FragmentLoginBuyer fragmentLoginBuyer = null;
    @Before
    public void setUp() throws Exception {
        fragmentLoginBuyer = new FragmentLoginBuyer();

    }
    @Test
    public void testLaunch(){
//        LinearLayout linearLayout = mainActivity.findViewById(androidx.customview.R.id.)
    }

    @After
    public void tearDown() throws Exception {
    }
}