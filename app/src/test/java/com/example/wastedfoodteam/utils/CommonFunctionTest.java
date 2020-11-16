package com.example.wastedfoodteam.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CommonFunctionTest {

    @Test
    public void testSetImageViewSrc() throws Exception {
        CommonFunction.setImageViewSrc(null, "src", null);
    }

    @Test
    public void testGetCurrency() throws Exception {
        String result = CommonFunction.getCurrency(Double.valueOf(0));
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testGetOpenClose() throws Exception {
        String result = CommonFunction.getOpenClose(new GregorianCalendar(2020, Calendar.NOVEMBER, 14, 11, 35).getTime(), new GregorianCalendar(2020, Calendar.NOVEMBER, 14, 11, 35).getTime());
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testGetDiscount() throws Exception {
        String result = CommonFunction.getDiscount(0d, 0d);
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testGetQuantity() throws Exception {
        String result = CommonFunction.getQuantity(0, 0);
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetQuantityTextView() throws Exception {
        CommonFunction.setQuantityTextView(null, 0, 0);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme