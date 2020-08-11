package com.example.oxygentogo.Util;

import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VolleyHelper {

    private final Context mContext;
    private final RequestQueue mRequestQueue;
    private final String mBaseUrl;

    public VolleyHelper(Context c, String baseUrl){
        mContext = c;
        mRequestQueue = Volley.newRequestQueue(mContext);
        mBaseUrl = baseUrl;
    }

    private String contructUrl(String method){
        return mBaseUrl + "/" + method;
    }

    public void get(String method, JSONObject jsonRequest,
                    Listener<JSONObject> listener, ErrorListener errorListener){

        JsonObjectRequest objRequest = new JsonObjectRequest(Method.GET, contructUrl(method), jsonRequest, listener, errorListener);
        mRequestQueue.add(objRequest);
    }

    public void put(String method, JSONObject jsonRequest,
                    Listener<JSONObject> listener, ErrorListener errorListener){

        JsonObjectRequest objRequest = new JsonObjectRequest(Method.PUT, contructUrl(method), jsonRequest, listener, errorListener);
        mRequestQueue.add(objRequest);
    }

    public void post(String method, JSONObject jsonRequest,
                     Listener<JSONObject> listener, ErrorListener errorListener){

        JsonObjectRequest objRequest = new JsonObjectRequest(Method.POST, contructUrl(method), jsonRequest, listener, errorListener);
        mRequestQueue.add(objRequest);
    }

    public void delete(String method, JSONObject jsonRequest,
                       Listener<JSONObject> listener, ErrorListener errorListener){

        JsonObjectRequest objRequest = new JsonObjectRequest(Method.DELETE, contructUrl(method), jsonRequest, listener, errorListener);
        mRequestQueue.add(objRequest);
    }

}