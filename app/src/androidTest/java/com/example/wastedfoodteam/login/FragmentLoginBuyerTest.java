package com.example.wastedfoodteam.login;

import androidx.test.rule.ActivityTestRule;

import com.example.wastedfoodteam.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FragmentLoginBuyerTest{

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);
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