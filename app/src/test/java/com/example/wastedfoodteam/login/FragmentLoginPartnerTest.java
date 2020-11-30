package com.example.wastedfoodteam.login;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FragmentLoginPartnerTest {
    private static final String FAKE_STRING = "12707736894140473154801792860916528374";

    @Test
    public void checkMD5() {

        FragmentLoginPartner myObjectUnderTest = new FragmentLoginPartner();

        // ...when the string is returned from the object under test...
        String result = "12707736894140473154801792860916528374";


        // ...then the result should be the expected one.

        assertThat(FAKE_STRING, is(myObjectUnderTest.md5("test")));
    }

}