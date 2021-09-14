package com.marc.decoditeccamfast.clases.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton instanciaVolley;
    private static Context con;
    private RequestQueue request;

    private VolleySingleton(Context context) {
        con = context;
        request = getRequestQueQue();
    }

    public static synchronized VolleySingleton getInstanceVolley(Context context) {
        if (instanciaVolley == null) {
            instanciaVolley = new VolleySingleton(context);

        }

        return instanciaVolley;
    }

    public RequestQueue getRequestQueQue() {
        if (request == null) {
            request = Volley.newRequestQueue(con.getApplicationContext());
        }
        return request;
    }

    public <T> void addToRequestQueQue(Request<T> req) {
        getRequestQueQue().add(req);
    }

}
