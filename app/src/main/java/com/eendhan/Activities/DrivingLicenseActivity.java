package com.eendhan.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.ImageUtils;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.File;
import java.util.Calendar;

import iam.thevoid.mediapicker.rxmediapicker.Purpose;
import iam.thevoid.mediapicker.rxmediapicker.RxMediaPicker;

public class DrivingLicenseActivity extends AppCompatActivity {

    ImageView back;
    Button btn_next;
    EditText licenseNumber;
    ImageView calender;
    String strLicenseNumber = "", strDate = "";
    DatePickerDialog datePickerDialog;
    TextView etDate;
    ImageView frontLic, backLic;

    private File front_gallery_file;
    private File back_gallery_file;

    String frontp = "", backp = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_license);

        back = findViewById(R.id.back);
        btn_next = findViewById(R.id.btn_next);
        licenseNumber = findViewById(R.id.licenseNumber);
        calender = findViewById(R.id.calender);
        etDate = findViewById(R.id.etDate);
        frontLic = findViewById(R.id.frontLic);
        backLic = findViewById(R.id.backLic);


        frontLic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxMediaPicker.builder(DrivingLicenseActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(DrivingLicenseActivity.this, filepath));
                            front_gallery_file = ImageUtils.bitmapToFile(bitmap, DrivingLicenseActivity.this);
                            Glide.with(DrivingLicenseActivity.this).load(front_gallery_file).into(frontLic);
                            frontp = front_gallery_file.toString();

                        });
            }
        });


        backLic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxMediaPicker.builder(DrivingLicenseActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap2 = ImageUtils.imageCompress(ImageUtils.getRealPath(DrivingLicenseActivity.this, filepath));
                            back_gallery_file = ImageUtils.bitmapToFile(bitmap2, DrivingLicenseActivity.this);
                            Glide.with(DrivingLicenseActivity.this).load(back_gallery_file).into(backLic);
                            backp = back_gallery_file.toString();

                        });
            }
        });


        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(DrivingLicenseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                etDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                                strDate = dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year;
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();

            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strLicenseNumber = licenseNumber.getText().toString().trim();
                strDate = etDate.getText().toString().trim();


                if (TextUtils.isEmpty(strLicenseNumber)) {
                    new StyleableToast
                            .Builder(DrivingLicenseActivity.this)
                            .text("Please enter license number")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();

                } else if (strDate.equals("Driving License Expiry Date") || strDate.equals("")) {

                    new StyleableToast
                            .Builder(DrivingLicenseActivity.this)
                            .text("Select date")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();
                } else if (frontp.equals("")||backp.equals("")){
                    new StyleableToast
                            .Builder(DrivingLicenseActivity.this)
                            .text("Select front/back photos")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();
                }




                else {
                    try {
                        SharedHelper.putKey(getApplicationContext(), AppConstats.DRIVER_LICENSE_NUMBER, strLicenseNumber);
                        SharedHelper.putKey(getApplicationContext(), AppConstats.DRIVER_LICENSE_EXPIRY_DATE, strDate);
                        SharedHelper.putKey(getApplicationContext(), AppConstats.DRIVING_LIC_FRONT_IMAGE_PATH, frontp);
                        SharedHelper.putKey(getApplicationContext(), AppConstats.DRIVING_LIC_BACK_IMAGE_PATH, backp);
                        startActivity(new Intent(getApplicationContext(), RegistrationCertificateActivity.class));
                        finish();

                    } catch (Exception e) {
                        Log.e("ckcdcc", e.getMessage());
                    }

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


}