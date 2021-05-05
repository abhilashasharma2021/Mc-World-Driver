package com.eendhan.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.eendhan.Others.API;
import com.eendhan.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TermAndConditionsActivity extends AppCompatActivity {

    TextView tcText,title;
    RequestQueue requestQueue;
    ProgressBar progress;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_conditions);

        tcText = findViewById(R.id.tcText);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        progress = findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);
        requestQueue = Volley.newRequestQueue(this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        termsandContitions();


    }


    public void termsandContitions() {
        progress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String text = jsonObject.getString("text");
                        String titlee = jsonObject.getString("title");
                        tcText.setText(text);
                        title.setText(titlee);

                        progress.setVisibility(View.GONE);

                    }

                } catch (Exception e) {
                    Log.e("ewytgdcf", e.getMessage());
                    progress.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("dcsbsdbc",error.toString());

                progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("control", "terms_cond");
                return param;
            }
        };

        requestQueue.add(request);
    }
}