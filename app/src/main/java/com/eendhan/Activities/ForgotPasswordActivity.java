package com.eendhan.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail;
    Button btnSubmit;
    String strEmail = "";
    RequestQueue requestQueue;
    ProgressBar progress;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        progress = findViewById(R.id.progress);
        back = findViewById(R.id.back);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);


        requestQueue = Volley.newRequestQueue(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = etEmail.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (TextUtils.isEmpty(strEmail)) {
                    new StyleableToast
                            .Builder(ForgotPasswordActivity.this)
                            .text("Please enter your email")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();

                } else if (!strEmail.matches(emailPattern)) {

                    new StyleableToast
                            .Builder(ForgotPasswordActivity.this)
                            .text("Invalid Email Address")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();

                } else {

                    forgotPass();

                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void forgotPass() {

        progress.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("sdnskjfn", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String strResult = jsonObject.getString("result");

                    if (strResult.equals("Please Check Your Email To Get Your Password")) {


                        new StyleableToast
                                .Builder(ForgotPasswordActivity.this)
                                .text("Successfully Sent Check Your Email")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.parseColor("#F69D1D"))
                                .show();

                        progress.setVisibility(View.GONE);

                    } else {

                        new StyleableToast
                                .Builder(ForgotPasswordActivity.this)
                                .text(strResult)
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.parseColor("#d50000"))
                                .show();
                        progress.setVisibility(View.GONE);

                    }


                } catch (Exception e) {
                    Log.e("urrieuw", e.getMessage());
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
                param.put("control", "forget_password");
                param.put("email", strEmail);
                param.put("user_type", "2");
                return param;
            }
        };

        requestQueue.add(request);

    }

}
