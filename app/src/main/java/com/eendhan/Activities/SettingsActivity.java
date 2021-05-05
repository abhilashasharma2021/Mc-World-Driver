package com.eendhan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.eendhan.R;

public class SettingsActivity extends AppCompatActivity {

    RelativeLayout rel_contactus,rel_privacyPolicy,rel_tc,rel_updateLicense,rel_changePass,rel_aboutUs;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.back);
        rel_contactus = findViewById(R.id.rel_contactus);
        rel_tc = findViewById(R.id.rel_tc);
        rel_privacyPolicy = findViewById(R.id.rel_privacyPolicy);
        rel_updateLicense = findViewById(R.id.rel_updateLicense);
        rel_changePass = findViewById(R.id.rel_changePass);
        rel_aboutUs = findViewById(R.id.rel_aboutUs);


        rel_changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ChangePasswordActivity.class));

            }
        });

        rel_aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AboutUsActivity.class));

            }
        });



        rel_privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PrivacyPolicyActivity.class));
            }
        });


        rel_updateLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UpdateLicenseActivity.class));

            }
        });


        rel_tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TermAndConditionsActivity.class));
            }
        });


        rel_contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ContactUsActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}