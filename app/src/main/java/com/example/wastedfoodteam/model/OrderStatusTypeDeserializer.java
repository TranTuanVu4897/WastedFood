package com.example.wastedfoodteam.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class OrderStatusTypeDeserializer implements JsonDeserializer<Order.Status> {

    @Override
    public Order.Status deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int key = json.getAsInt();
        return Order.Status.fromKey(key);

    }
}
