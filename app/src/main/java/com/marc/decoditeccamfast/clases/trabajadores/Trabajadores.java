package com.marc.decoditeccamfast.clases.trabajadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.marc.decoditeccamfast.clases.Callbacks.trabajadores.CallbacksTrabajadores;
import com.marc.decoditeccamfast.clases.http.MultipartRequest;
import com.marc.decoditeccamfast.clases.http.Rutes;
import com.marc.decoditeccamfast.clases.http.VolleySingleton;
import com.marc.decoditeccamfast.clases.models.User;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Trabajadores {

    public void registrarTrabajador(Context con, String nom, String num, String area, String imagenStr, CallbacksTrabajadores cbt){
        try {
            JSONObject body = new JSONObject();
            body.put("nombre", nom);
            body.put("numero", num);
            body.put("tvl", area);
            body.put("imagen", imagenStr);
            User user = new Gson().fromJson(con.getSharedPreferences("user", Context.MODE_PRIVATE).getString("userJson", ""), User.class);
            body.put("Authorization", user.getToken());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Rutes.REGISTRAR_TRABAJADOR, body,
                    response -> {
                        try {
                            Toast.makeText(con, response.toString(), Toast.LENGTH_SHORT).show();
                            boolean registrado = (boolean) response.get("registrado");
                            cbt.registrarTrabajador(registrado);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> Toast.makeText(con, error.getMessage(), Toast.LENGTH_SHORT).show());

            VolleySingleton.getInstanceVolley(con).addToRequestQueQue(request);
        }catch(JSONException e){
            Toast.makeText(con, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
