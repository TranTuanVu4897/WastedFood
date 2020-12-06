package com.example.wastedfoodteam.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.widget.ImageView;

import com.example.wastedfoodteam.model.Seller;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CommonFunctionTest {
    @Mock
    private Context mContext;
    @Mock
    private ImageView mImageView;

    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCurrencyCorrect() throws Exception {
        String result = CommonFunction.getCurrency(Double.valueOf(0));
        Assert.assertEquals("0VND", result);
    }

    @Test
    public void testGetCurrencyCorrect2() throws Exception {
        String result = CommonFunction.getCurrency(Double.valueOf(0));
        Assert.assertEquals("1,000VND", result);
    }

    @Test
    public void testGetOpenClose() throws Exception {
        String result = CommonFunction.getOpenClose(new GregorianCalendar(2020, Calendar.DECEMBER, 5, 15, 43).getTime(), new GregorianCalendar(2020, Calendar.DECEMBER, 5, 15, 43).getTime());
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testGetOpenCloseInputNull() throws Exception {
        String result = CommonFunction.getOpenClose(null,null);
        Assert.assertEquals("00.00 - 23.59", result);
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

    @Test
    public void testGetCurrentDate() throws Exception {
        String result = CommonFunction.getCurrentDate();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testCheckEmptyEditText() throws Exception {
        boolean result = CommonFunction.checkEmptyEditText(null);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testGetStringDistance() throws Exception {
        Seller seller = new Seller();
        Location gps = new Location("");
        String result = CommonFunction.getStringDistance(seller,gps);
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme