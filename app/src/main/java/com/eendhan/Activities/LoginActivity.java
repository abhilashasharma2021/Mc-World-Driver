package com.eendhan.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView txtAlreadyAccount;
    EditText etEmail, etpassword;
    String strEmail = "", strPassword = "";
    Button btnLogin;
    ProgressBar progress;
    RequestQueue requestQueue;
    TextView forgotPass;

    String regID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtAlreadyAccount = findViewById(R.id.txtAlreadyAccount);
        etEmail = findViewById(R.id.etEmail);
        etpassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        forgotPass = findViewById(R.id.forgotPass);
        progress = findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);

        requestQueue = Volley.newRequestQueue(this);

        regID = SharedHelper.getKey(getApplicationContext(), AppConstats.REG_ID_TOKEN);

        String text = "Don't have an account ? Create";

        clickSignup(text);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strEmail = etEmail.getText().toString().trim();
                strPassword = etpassword.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (TextUtils.isEmpty(strEmail)) {
                    new StyleableToast
                            .Builder(LoginActivity.this)
                            .text("Please enter email address")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();
                } else if (TextUtils.isEmpty(strPassword)) {

                    new StyleableToast
                            .Builder(LoginActivity.this)
                            .text("Please enter password")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();
                } else if (!strEmail.matches(emailPattern)) {

                    new StyleableToast
                            .Builder(LoginActivity.this)
                            .text("Invalid Email Address")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();

                } else {

                    login();

                }

            }
        });


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });


    }


    public void clickSignup(String text) {

        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                startActivity(new Intent(getApplicationContext(), CreateAcctOrLoginActivity.class));
                finish();

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);

                ds.setColor(Color.parseColor("#F69D1D"));
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtAlreadyAccount.setText(ss);
        txtAlreadyAccount.setMovementMethod(LinkMovementMethod.getInstance());

    }


    public void login() {
        progress.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ghjgj", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String strResult = jsonObject.getString("result");

                    if (strResult.equals("Login Successfully.")) {

                        String id = jsonObject.getString("id");
                        String mobile = jsonObject.getString("mobile");
                        String status = jsonObject.getString("status");
                        String name = jsonObject.getString("name");
                        String email = jsonObject.getString("email");
                        String license_no = jsonObject.getString("license_no");
                        String license_expire_date = jsonObject.getString("license_expire_date");
                        String vehicle_rc_no = jsonObject.getString("vehicle_rc_no");
                        String vehicle_type = jsonObject.getString("vehicle_type");
                        String image = jsonObject.getString("image");
                       // String path = jsonObject.getString("[path]");
                        String path = jsonObject.getString("path");
                        String regid = jsonObject.getString("regid");
                        String license_front_image = jsonObject.getString("license_front_image");
                        String license_back_image = jsonObject.getString("license_back_image");
                        String driver_online_status = jsonObject.getString("driver_online_status");

                        Log.e("LoginActivity", "driver_online_status: " +driver_online_status);

                        Log.e("ryyyrry", id);
                        Log.e("ryyyrry", mobile);
                        Log.e("ryyyrry", status);
                        Log.e("ryyyrry", name);
                        Log.e("ryyyrry", email);
                        Log.e("ryyyrry", license_no);
                        Log.e("ryyyrry", license_expire_date);
                        Log.e("ryyyrry", vehicle_rc_no);
                        Log.e("ryyyrry", vehicle_type);
                        Log.e("ryyyrry", image);
                        Log.e("ryyyrry", regid);

                        SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ID, id);
                        SharedHelper.putKey(getApplicationContext(), AppConstats.USER_EMAIL, email);
                        SharedHelper.putKey(getApplicationContext(), AppConstats.USER_FULLNAME, name);
                        SharedHelper.putKey(getApplicationContext(), AppConstats.MOBILE_NUMBER, mobile);
                        SharedHelper.putKey(getApplicationContext(), AppConstats.IMAGE, path + image);
                        SharedHelper.putKey(getApplicationContext(), AppConstats.PATH, path);

                        Log.e("LoginActivity", "OnlineStatus: " +driver_online_status);
                        SharedHelper.putKey(getApplicationContext(), AppConstats.STATUS_ONLINE, driver_online_status);

                        /*driver_online_status= 1 means login nd driver_online_status=0 means offline */

                        SharedHelper.putKey(getApplicationContext(), AppConstats.DRIVER_LICENSE_NUMBER, license_no);
                        SharedHelper.putKey(getApplicationContext(), AppConstats.DRIVER_LICENSE_EXPIRY_DATE, license_expire_date);
                        SharedHelper.putKey(getApplicationContext(), AppConstats.DRIVING_LIC_FRONT_IMAGE, path + license_front_image);
                        SharedHelper.putKey(getApplicationContext(), AppConstats.DRIVING_LIC_BACK_IMAGE, path + license_back_image);


                        new StyleableToast
                                .Builder(LoginActivity.this)
                                .text("Login Successfully")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.parseColor("#F69D1D"))
                                .show();

                        startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                        finish();
                        progress.setVisibility(View.GONE);

                    } else {

                        new StyleableToast
                                .Builder(LoginActivity.this)
                                .text("please check your email or password")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.parseColor("#d50000"))
                                .show();
                        progress.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                    progress.setVisibility(View.GONE);
                }


                progress.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LoginActivity", "error: " +error);
                progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("control", "user_login");
                param.put("email", strEmail);
                param.put("password", strPassword);
                param.put("user_type", "2");
                param.put("os_type", "1");
                param.put("regid", regID);
                return param;
            }
        };

        requestQueue.add(request);

    }


}

