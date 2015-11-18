package com.cuong.futurenav.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Cuong on 11/17/2015.
 */
public class RestClient {
    private static final String BASE_URL = "http://futurenav-env.elasticbeanstalk.com";

    private static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
//            GsonBuilder builder = new GsonBuilder();
//            // Register an adapter to manage the date types as long values
//            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                    return new Date(json.getAsJsonPrimitive().getAsLong());
//                }
//            });
//            yyyy-MM-dd'T'HH:mm:ss.SSS
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                            //.setLog(new AndroidLog("RETROFIT_LOG_TAG"))
                    .setEndpoint(BASE_URL)
                    .setConverter(new GsonConverter(gson))
                    .build();

            apiService = restAdapter.create(ApiService.class);

        }

        return apiService;
    }
}