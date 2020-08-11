package com.example.oxygentogo.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.oxygentogo.Util.Constant;
import com.example.oxygentogo.Util.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class Test extends Service {

    public Test() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        VolleyHelper volley = new VolleyHelper(getApplicationContext(), Constant.test);
        volley.get("test.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("test")) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                } catch (JSONException e) {
                    Log.e("Error", e.toString());
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }
}
