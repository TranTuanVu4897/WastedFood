package com.example.wastedfoodteam.buyer.order;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.example.wastedfoodteam.model.Order;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class OrderAdapterTest {
    @Mock
    Context context;
    @Mock
    List<Order> orderList;
    @Mock
    Resources resources;
    @InjectMocks
    OrderAdapter orderAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCount() {
        int result = orderAdapter.getCount();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testGetItem() {
        Object result = orderAdapter.getItem(0);
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testGetItemId() {
        long result = orderAdapter.getItemId(0);
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testGetView() {
        View result = orderAdapter.getView(0, null, null);
        Assert.assertNull(result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme