package com.example.wastedfoodteam.utils.Validation;

import org.junit.Assert;
import org.junit.Test;

public class ValidationTest {

//    @Test
//    public void testCheckPhoneExist() throws Exception {
//        Validation.checkPhoneExist(null, null, "phone");
//    }

    @Test
    public void testCheckPhoneCorrect() throws Exception {
        boolean result = Validation.checkPhone("0984605568");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testCheckPhoneCorrect2() throws Exception {
        boolean result = Validation.checkPhone("84984605568");
        Assert.assertEquals(true, result);
    }
    @Test
    public void testCheckPhoneNull() throws Exception {
        boolean result = Validation.checkPhone(null);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckPhoneEmpty() throws Exception {
        boolean result = Validation.checkPhone("");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckPhoneContainSpace() throws Exception {
        boolean result = Validation.checkPhone("098460 5568");
        Assert.assertEquals(false, result);
    }


    @Test
    public void testCheckPhoneShorterThan9() throws Exception {
        boolean result = Validation.checkPhone("098460556");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckPhoneLargerThan9() throws Exception {
        boolean result = Validation.checkPhone("09846055688");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckPhoneContainSpecialCharacter() throws Exception {
        boolean result = Validation.checkPhone("098460556*");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckPhoneContainWordCharacter() throws Exception {
        boolean result = Validation.checkPhone("098460556a");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckEmailCorrect() throws Exception {
        boolean result = Validation.checkEmail("sampleemail@gmail.com");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testCheckEmailFailureNull() throws Exception {
        boolean result = Validation.checkEmail(null);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckEmailFailureEmpty() throws Exception {
        boolean result = Validation.checkEmail("");
        Assert.assertEquals(false, result);
    }
    @Test
    public void testCheckEmailFailureContainSpace() throws Exception {
        boolean result = Validation.checkEmail("sample email@gmail.com");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckEmailFailure2() throws Exception {
        boolean result = Validation.checkEmail("sampleemailgmail.com");
        Assert.assertEquals(false, result);
    }


    @Test
    public void testCheckEmailFailure3() throws Exception {
        boolean result = Validation.checkEmail("sampleemail@@gmail.com");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckEmailFailure4() throws Exception {
        boolean result = Validation.checkEmail("sampleemail@gmail.com.v");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckEmailFailure5() throws Exception {
        boolean result = Validation.checkEmail("sampleemail@gmail");
        Assert.assertFalse(result);
    }


    @Test
    public void testCheckEmailContainSpecialCharacter() throws Exception {
        boolean result = Validation.checkEmail("email!failure@gmail.com");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPasswordCorrect() throws Exception {
        boolean result = Validation.checkPassword("samplepass1");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckPasswordCorrect1() throws Exception {
        boolean result = Validation.checkPassword("1234test");
        Assert.assertTrue(result);
    }
    @Test
    public void testCheckPasswordCorrect2() throws Exception {
        boolean result = Validation.checkPassword("1234test1234test");
        Assert.assertTrue(result);
    }
    @Test
    public void testCheckPasswordCorrect3() throws Exception {
        boolean result = Validation.checkPassword("test@1234");
        Assert.assertTrue(result);
    }
    @Test
    public void testCheckPasswordCorrect4() throws Exception {
        boolean result = Validation.checkPassword("Test@1234");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckPasswordFailureNull() throws Exception {
        boolean result = Validation.checkPassword(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPasswordFailureEmpty() throws Exception {
        boolean result = Validation.checkPassword("");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPasswordFailureInputSpace() throws Exception {
        boolean result = Validation.checkPassword("test 1234");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPasswordFailureOnlyNum() throws Exception {
        boolean result = Validation.checkPassword("01234567");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPasswordFailureOnlyWord() throws Exception {
        boolean result = Validation.checkPassword("asdfghjk");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckPasswordFailureOnlyLargerThan16() throws Exception {
        boolean result = Validation.checkPassword("asdfghjk012345678");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckNameCorrect() throws Exception {
        boolean result = Validation.checkName("string");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckName() throws Exception {
        boolean result = Validation.checkName("string");
        Assert.assertFalse(result);
    }

    @Test
    public void testValidateDate() throws Exception {
        Boolean result = Validation.validateDate("date");
        Assert.assertEquals(Boolean.TRUE, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme