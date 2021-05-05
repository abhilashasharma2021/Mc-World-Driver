package com.eendhan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;
import com.pd.chocobar.ChocoBar;

public class CreatePasswordActivity extends AppCompatActivity {

    public static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})";
    TextInputEditText etPassword;
    Button btn_next;
    ImageView back;
    String strPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        etPassword = findViewById(R.id.etPassword);
        btn_next = findViewById(R.id.btn_next);

        back=findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strPassword = etPassword.getText().toString().trim();

                if (!strPassword.matches(PASSWORD_PATTERN)){

                    ChocoBar.builder().setView(v)
                            .setText("Password should contains atleast 8 digit,Upper and Lower case character,and a number")
                            .setDuration(ChocoBar.LENGTH_INDEFINITE)
                            .setActionText(android.R.string.ok)
                            .red()   // in built red ChocoBar
                            .show();
                }else {
                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_PASSWORD,strPassword);
                    startActivity(new Intent(getApplicationContext(),FullNameActivity.class));
                    finish();

                }

            }
        });


    }
}