package com.eendhan.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.eendhan.Others.AppConstats;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;
import com.muddzdev.styleabletoast.StyleableToast;

public class YourEmailActivity extends AppCompatActivity {


    Button btn_next;
    ImageView back;
    EditText etMobile;
    String strMobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_email);

        btn_next = findViewById(R.id.btn_next);
        back = findViewById(R.id.back);
        etMobile = findViewById(R.id.etMobile);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strMobile = etMobile.getText().toString().trim();

                if (TextUtils.isEmpty(strMobile)) {
                    new StyleableToast
                            .Builder(YourEmailActivity.this)
                            .text("Enter valid Mobile Number")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();
                } else if (strMobile.length() != 10) {

                    new StyleableToast
                            .Builder(YourEmailActivity.this)
                            .text("Mobile number must have 10 digits")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();
                } else {
                    SharedHelper.putKey(getApplicationContext(), AppConstats.MOBILE_NUMBER, strMobile);
                    startActivity(new Intent(getApplicationContext(), CreatePasswordActivity.class));
                    finish();
                }

            }

});
}
}