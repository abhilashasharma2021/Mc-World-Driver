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
import android.widget.Toast;

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

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldPass, newPass, confPass;
    String strOldPass = "", strNewPass = "", strConfPass = "";
    Button btnChange;
    ProgressBar progress;
    RequestQueue requestQueue;
    String getUserid = "";
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btnChange = findViewById(R.id.btnChange);
        oldPass = findViewById(R.id.oldPass);
        newPass = findViewById(R.id.newPass);
        confPass = findViewById(R.id.confPass);
        progress = findViewById(R.id.progress);
        back = findViewById(R.id.back);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);

        requestQueue = Volley.newRequestQueue(this);

        getUserid = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strOldPass = oldPass.getText().toString().trim();
                strNewPass = newPass.getText().toString().trim();
                strConfPass = confPass.getText().toString().trim();

                if (TextUtils.isEmpty(strOldPass)) {
                    new StyleableToast
                            .Builder(ChangePasswordActivity.this)
                            .text("Please fill all the fields")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();
                } else if (TextUtils.isEmpty(strNewPass)) {

                    new StyleableToast
                            .Builder(ChangePasswordActivity.this)
                            .text("Please fill all the fields")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();

                } else if (TextUtils.isEmpty(strConfPass)) {

                    new StyleableToast
                            .Builder(ChangePasswordActivity.this)
                            .text("Please fill all the fields")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();

                } else if (!strNewPass.equals(strConfPass)) {

                    new StyleableToast
                            .Builder(ChangePasswordActivity.this)
                            .text("Password not matched")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();

                } else {

                    changePassword();
                }

            }
        });


    }


    public void changePassword() {

        progress.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String strResult = jsonObject.getString("result");

                    if (strResult.equals("Successful")) {

                        new StyleableToast
                                .Builder(ChangePasswordActivity.this)
                                .text("Password Chnages")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.parseColor("#F69D1D"))
                                .show();

                        progress.setVisibility(View.GONE);
                    } else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(ChangePasswordActivity.this, strResult, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("utgyeruit", e.getMessage());
                    progress.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("error", error.toString());
                progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("control", "change_password");
                map.put("user_id", getUserid);
                map.put("old_password", strOldPass);
                map.put("new_password", strNewPass);
                map.put("confirm_password", strConfPass);
                return map;
            }
        };

        requestQueue.add(request);
    }
}