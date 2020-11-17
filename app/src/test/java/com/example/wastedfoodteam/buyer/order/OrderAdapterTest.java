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

import static org.mockito.Mockito.*;

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
    public void testGetCount() throws Exception {
        int result = orderAdapter.getCount();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testGetItem() throws Exception {
        Object result = orderAdapter.getItem(0);
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testGetItemId() throws Exception {
        long result = orderAdapter.getItemId(0);
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testGetView() throws Exception {
        View result = orderAdapter.getView(0, null, null);
        Assert.assertEquals(null, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme