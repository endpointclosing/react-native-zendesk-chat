package com.taskrabbit.zendesk;

import android.app.Activity;
import android.content.Intent;

import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.prechat.PreChatForm;
import com.zopim.android.sdk.prechat.ZopimChatActivity;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.prechat.PreChatForm;
import com.zopim.android.sdk.api.ZopimChatApi;

import com.zopim.android.sdk.model.VisitorInfo;

import java.lang.String;
import java.util.ArrayList;


public class RNZendeskChatModule extends ReactContextBaseJavaModule {
    private ReactContext mReactContext;

    public RNZendeskChatModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNZendeskChatModule";
    }

    @ReactMethod
    public void setVisitorInfo(ReadableMap options) {
        VisitorInfo.Builder builder = new 
        VisitorInfo.Builder();

        if (options.hasKey("name")) {
            builder.name(options.getString("name"));
        }
        if (options.hasKey("email")) {
            builder.email(options.getString("email"));
        }
        if (options.hasKey("phone")) {
            builder.phoneNumber(options.getString("phone"));
        }

        VisitorInfo visitorData = builder.build();

        ZopimChat.setVisitorInfo(visitorData);

        
    }

    @ReactMethod
    public void init(String key) {
        ZopimChat.init(key);
    }

    @ReactMethod
    public void startChat(ReadableMap options) {
        setVisitorInfo(options);
        String[] tags = getTags(options);

        ZopimChat.SessionConfig context = new ZopimChat.SessionConfig()
            .tags(tags);
        
        if (options.hasKey("department")) {
            context.department(options.getString("department"));
        }

         Activity activity = getCurrentActivity();
         if (activity != null) {
            ZopimChatActivity.startActivity(activity, context);
        }
    }

    private static String[] getTags(ReadableMap options) {
        ArrayList<String> result = new ArrayList();

        if (!options.hasKey("tags")) {
            return new String[0];
        }
        ReadableArray arr = options.getArray("tags");
        for (int i = 0; i < arr.size(); i++) {
            if (arr.isNull(i)) {
                continue;
            }
            result.add(arr.getString(i));
        }
        return result.toArray(new String[0]);
    }
}
