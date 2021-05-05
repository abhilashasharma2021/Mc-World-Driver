package com.eendhan.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
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
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.eendhan.Others.API;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAcctOrLoginActivity extends AppCompatActivity {

    EditText etEmail;
    Button btn_next;
    String strEmail = "";
    ProgressBar progress;
    RequestQueue requestQueue;
    TextView alreadyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acct_or_login);

        btn_next = findViewById(R.id.btn_next);
        etEmail = findViewById(R.id.etEmail);
        progress = findViewById(R.id.progress);
        alreadyAccount = findViewById(R.id.alreadyAccount);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);

        String text = "Already have an account ? Login";
        clickLogin(text);

        requestQueue = Volley.newRequestQueue(this);

        displayLocationSettingsRequest(this);


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strEmail = etEmail.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (TextUtils.isEmpty(strEmail)) {
                    new StyleableToast
                            .Builder(CreateAcctOrLoginActivity.this)
                            .text("Please enter your Email")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();

                } else if (!strEmail.matches(emailPattern)) {

                    new StyleableToast
                            .Builder(CreateAcctOrLoginActivity.this)
                            .text("Invalid Email Address")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();

                } else {
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_EMAIL,strEmail);
                    register();


                }
            }
        });



 /*       btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strEmail = etEmail.getText().toString().trim();

                if (TextUtils.isEmpty(strEmail)) {
                    new StyleableToast
                            .Builder(CreateAcctOrLoginActivity.this)
                            .text("Enter valid Email address")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();
                } *//*else if (strMobile.length() != 10) {

                    new StyleableToast
                            .Builder(CreateAcctOrLoginActivity.this)
                            .text("Mobile number must have 10 digits")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();
                }*//* else {

                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_EMAIL,strEmail);
                    register();

                }


            }
        });*/

}


    public void register() {

        progress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, response -> {
            Log.e("sdnskjfn", response);

            try {

                JSONObject jsonObject = new JSONObject(response);

                String strResult = jsonObject.getString("result");

                if (strResult.equals("Otp Sent Successfully")) {

                    new StyleableToast
                            .Builder(CreateAcctOrLoginActivity.this)
                            .text("OTP sent successfully")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#F69D1D"))
                            .show();

                    progress.setVisibility(View.GONE);

                    startActivity(new Intent(getApplicationContext(), VerificationNumberActivity.class));
                    finish();


                } else {

                    new StyleableToast
                            .Builder(CreateAcctOrLoginActivity.this)
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

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("dcjnxcjkdn",error.toString());

                progress.setVisibility(View.GONE);
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("control", "generate_otp");
                param.put("email", strEmail);
                param.put("user_type", "2");
                return param;
            }
        };
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
      /*  request.setRetryPolicy(new DefaultRetryPolicy(
                        6000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
*/
        requestQueue.add(request);

    }


    public void clickLogin(String text) {

        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);

                ds.setColor(Color.parseColor("#F69D1D"));
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan, 26, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        alreadyAccount.setText(ss);
        alreadyAccount.setMovementMethod(LinkMovementMethod.getInstance());

    }


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {

                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("fkdvkf", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("fkdvkf", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(CreateAcctOrLoginActivity.this, 1);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("fkdvkf", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("fkdvkf", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


}
