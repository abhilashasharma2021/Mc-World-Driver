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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.eendhan.Others.API;
import com.eendhan.R;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContactUsActivity extends AppCompatActivity {

    ImageView back;
    EditText etName, etEmail, etNumber, etMessage;
    String strName = "", strEmail = "", strNumber = "", strMessage = "";
    Button btnSubmit;
    RequestQueue requestQueue;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        back = findViewById(R.id.back);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etNumber = findViewById(R.id.etNumber);
        etMessage = findViewById(R.id.etMessage);
        progress = findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);
        btnSubmit = findViewById(R.id.btnSubmit);


        requestQueue = Volley.newRequestQueue(this);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = etName.getText().toString().trim();
                strEmail = etEmail.getText().toString().trim();
                strNumber = etNumber.getText().toString().trim();
                strMessage = etMessage.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


                if (TextUtils.isEmpty(strName)) {

                    YoYo.with(Techniques.Bounce)
                            .duration(700)
                            .repeat(1)
                            .playOn(findViewById(R.id.relName));
                    showToast("Please fill the details");

                } else if (TextUtils.isEmpty(strEmail)) {
                    YoYo.with(Techniques.Bounce)
                            .duration(700)
                            .repeat(1)
                            .playOn(findViewById(R.id.etEmail));
                    showToast("Please fill the details");
                } else if (!strEmail.matches(emailPattern)) {
                    showToast("Invalid email Address");
                } else if (TextUtils.isEmpty(strNumber)) {
                    YoYo.with(Techniques.Bounce)
                            .duration(700)
                            .repeat(1)
                            .playOn(findViewById(R.id.etNumber));
                    showToast("Please fill the details");
                } else if (TextUtils.isEmpty(strMessage)) {
                    YoYo.with(Techniques.Bounce)
                            .duration(700)
                            .repeat(1)
                            .playOn(findViewById(R.id.etMessage));
                    showToast("Please fill the details");
                } else {
                    contactUs();

                }


            }
        });
        ;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void contactUs() {
        progress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("djnvcjnsdv", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String result = jsonObject.getString("result");

                    if (result.equals("An email has been sent")) {

                        new StyleableToast
                                .Builder(ContactUsActivity.this)
                                .text("Sent")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.parseColor("#F69D1D"))
                                .show();

                        progress.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    Log.e("sdchjbdj", e.getMessage());
                    progress.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("ksdnjkdnfjkv", error.toString());
                progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("control", "contact_us");
                param.put("name", strName);
                param.put("phone", strNumber);
                param.put("email", strEmail);
                param.put("msg", strMessage);
                return param;
            }
        };

        requestQueue.add(request);
    }


    public void showToast(String text) {
        new StyleableToast
                .Builder(ContactUsActivity.this)
                .text(text)
                .textColor(Color.WHITE)
                .backgroundColor(Color.parseColor("#d50000"))
                .show();
    }
}