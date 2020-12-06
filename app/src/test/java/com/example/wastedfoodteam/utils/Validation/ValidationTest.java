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
    public void testCheckPhoneNumberNull() throws Exception {
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
    public void testCheckPhoneShorterThan9By84() throws Exception {
        boolean result = Validation.checkPhone("8498460556");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckPhoneLargerThan9s() throws Exception {
        boolean result = Validation.checkPhone("09846055688");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckPhoneLargerThan9By84() throws Exception {
        boolean result = Validation.checkPhone("849846055688");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckPhoneContainSpecialCharacter() throws Exception {
        boolean result = Validation.checkPhone("@#$12456");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckPhoneContainWordCharacter() throws Exception {
        boolean result = Validation.checkPhone("098460556a");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckPhoneNotStart0or84() throws Exception {
        boolean result = Validation.checkPhone("1984605568");
        Assert.assertEquals(false, result);
    }

    //checkMail

    @Test
    public void testCheckEmailNormalCorrect() throws Exception {
        boolean result = Validation.checkEmail("tungphamtp987@gmail.com");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testCheckEmailorganizationCorrect() throws Exception {
        boolean result = Validation.checkEmail("tungptse05613@fpt.edu.vn");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testCheckEmailFailureNulls() throws Exception {
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
        boolean result = Validation.checkEmail("tung phamtp987@gmail.com");
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCheckEmailFailure2() throws Exception {
        boolean result = Validation.checkEmail("tungphamtp987gmail.com");
        Assert.assertEquals(false, result);
    }


    @Test
    public void testCheckEmailFailure3() throws Exception {
        boolean result = Validation.checkEmail("tungphamtp987@@gmail.com");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckEmailFailure4() throws Exception {
        boolean result = Validation.checkEmail("sampleemail@gmail.com.v");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckEmailFailure5() throws Exception {
        boolean result = Validation.checkEmail("tungphamtp987@gmail");
        Assert.assertFalse(result);
    }


    @Test
    public void testCheckEmailContainSpecialCharacter() throws Exception {
        boolean result = Validation.checkEmail("tungphamtp987!@gmail.com");
        Assert.assertFalse(result);
    }

    //check Pass

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
    public void testCheckPasswordCorrect3s() throws Exception {
        boolean result = Validation.checkPassword("test@1234");
        Assert.assertTrue(result);
    }
    @Test
    public void testCheckPasswordCorrect4s() throws Exception {
        boolean result = Validation.checkPassword("Test@1234");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckPasswordFailureNulls() throws Exception {
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


    //checkName
    @Test
    public void testCheckNameCorrect() throws Exception {
        boolean result = Validation.checkName("a");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckNameCorrect2() throws Exception {
        boolean result = Validation.checkName("abc abc");
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckNameLager50() throws Exception {
        boolean result = Validation.checkName("ádasdasda dasdasdasdasdasdasdasdasdasdasdasdasdas");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckNameEmpty() throws Exception {
        boolean result = Validation.checkName("");
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckNameNull() throws Exception {
        boolean result = Validation.checkName(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckNameSpecial() throws Exception {
        boolean result = Validation.checkName("Tung@1101");
        Assert.assertFalse(result);
    }
    //check Dob
    @Test
    public void testValidateDateCorrect() throws Exception {
        Boolean result = Validation.validateDate("1998-11-01");
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testValidateDateCurrentDate() throws Exception {
        Boolean result = Validation.validateDate("2020-12-05");
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testValidateDateLagerCurrentDate() throws Exception {
        Boolean result = Validation.validateDate("2021-11-01");
        Assert.assertEquals(Boolean.FALSE, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme