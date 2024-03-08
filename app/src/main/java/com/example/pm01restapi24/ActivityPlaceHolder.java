package com.example.pm01restapi24;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pm01restapi24.Config.Posts;
import com.example.pm01restapi24.Config.RestApiMethods;

import java.util.ArrayList;
import java.util.List;

public class ActivityPlaceHolder extends AppCompatActivity {

    private RequestQueue requestQueue;

    List<Posts> listplaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_holder);

        listplaces = new ArrayList<>();

        SendData();

    }

    private void SendData()
    {
        requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET,
                RestApiMethods.Endpointplace, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                String mensaje = response.toString();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);
    }
}