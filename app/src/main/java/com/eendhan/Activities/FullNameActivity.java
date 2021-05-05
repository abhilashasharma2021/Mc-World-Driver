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

public class FullNameActivity extends AppCompatActivity {

    Button btn_next;
    ImageView back;
    EditText etFullName;
    String strFullName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_name);

        btn_next = findViewById(R.id.btn_next);
        back = findViewById(R.id.back);
        etFullName = findViewById(R.id.etFullName);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strFullName = etFullName.getText().toString().trim();

                if (TextUtils.isEmpty(strFullName)) {

                    new StyleableToast
                            .Builder(FullNameActivity.this)
                            .text("Please enter your fullname")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();

                } else {
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_FULLNAME, strFullName);
                    startActivity(new Intent(getApplicationContext(), DrivingLicenseActivity.class));


                }


            }
        });
    }
}