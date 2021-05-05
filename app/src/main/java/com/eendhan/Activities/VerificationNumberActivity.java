
package com.eendhan.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.eendhan.Others.API;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerificationNumberActivity extends AppCompatActivity {

    TextView timerTxt, txt;
    EditText et1, et2, et3, et4;
    String stret1 = "", stret2 = "", stret3 = "", stret4 = "";
    String sumUP = "";
    String getEmailAddress = "";
    ProgressBar progress;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_number);

        timerTxt = findViewById(R.id.timerTxt);
        txt = findViewById(R.id.txt);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        progress = findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);

        requestQueue = Volley.newRequestQueue(this);


        getEmailAddress = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_EMAIL);

        txt.setText("Enter your pin you have recieved via EMAIL on " + getEmailAddress);

        pin();

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {

                timerTxt.setText("Resend Code : " + millisUntilFinished / 1000);

            }

            public void onFinish() {

                timerTxt.setText("Done!!!!");

            }

        }.start();


    }


    public void pin() {

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                stret1 = et1.getText().toString().trim();
                et2.requestFocus();
            }
        });


        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                stret2 = et2.getText().toString().trim();
                et3.requestFocus();


            }
        });

        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                stret3 = et3.getText().toString().trim();
                et4.requestFocus();

            }
        });


        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                stret4 = et4.getText().toString().trim();
                sumUP = stret1 + stret2 + stret3 + stret4;

                verifyNumber();


            }
        });

    }


    public void verification() {
        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.BASE_URL)
                .addBodyParameter("control", "verify_login")
                .addBodyParameter("email", getEmailAddress)
                .addBodyParameter("code", sumUP)
                .addBodyParameter("user_type", "2")
                .setTag("verify")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("cncjkdjc", response.toString());
                        try {
                            if (response.has("result")) {

                                String strResult = response.getString("result");

                                if (strResult.equals("Otp Match Successfully")) {

                                    startActivity(new Intent(getApplicationContext(), YourEmailActivity.class));
                                    finish();
                                    progress.setVisibility(View.GONE);

                                } else {

                                    new StyleableToast
                                            .Builder(VerificationNumberActivity.this)
                                            .text(strResult)
                                            .textColor(Color.WHITE)
                                            .backgroundColor(Color.parseColor("#d50000"))
                                            .show();
                                    progress.setVisibility(View.GONE);
                                }

                            }

                        } catch (Exception e) {
                            Log.e("yryryrye", e.getMessage());
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("yryryrye", anError.getMessage());
                        progress.setVisibility(View.GONE);
                    }
                });
    }


    public void verifyNumber() {

        Log.e("codesumup", sumUP);
        progress.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("sdnskjfn", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String strResult = jsonObject.getString("result");

                    if (strResult.equals("Otp Match Successfully")) {

                        new StyleableToast
                                .Builder(VerificationNumberActivity.this)
                                .text("OTP Matched")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.parseColor("#F69D1D"))
                                .show();

                        startActivity(new Intent(getApplicationContext(), YourEmailActivity.class));
                        finish();
                        progress.setVisibility(View.GONE);


                    } else {
                        new StyleableToast
                                .Builder(VerificationNumberActivity.this)
                                .text(strResult)
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.parseColor("#d50000"))
                                .show();
                        progress.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                    Log.e("error", e.getMessage());

                    progress.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("control", "verify_login");
                param.put("email", getEmailAddress);
                param.put("code", sumUP);
                param.put("user_type", "2");
                return param;
            }
        };

        requestQueue.add(request);

    }

}


