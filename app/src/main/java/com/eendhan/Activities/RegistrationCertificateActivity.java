package com.eendhan.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.eendhan.Others.API;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.ImageUtils;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.io.File;

import iam.thevoid.mediapicker.rxmediapicker.Purpose;
import iam.thevoid.mediapicker.rxmediapicker.RxMediaPicker;

public class RegistrationCertificateActivity extends AppCompatActivity {

    ImageView back;
    Button btn_next;
    EditText etRcNumber;
    Spinner vehicleType;
    String strRCNumber = "", strVehicleType = "";
    String[] type = {"Bike", "Car"};
    ProgressBar progress;
    RequestQueue requestQueue;
    File file1, file2;

    String regID = "";

    private File front_gallery_file;
    private File back_gallery_file;

    String frontp = "", backp = "";

    File f, f2;
    ImageView regFront, regBack;

    String getMobile = "", getEmail = "", getPassword = "", getName = "", getLicenseNo = "";
    String getLicenseExp = "", getLicFront = "", getBackLic = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_certificate);


        back = findViewById(R.id.back);
        btn_next = findViewById(R.id.btn_next);
        etRcNumber = findViewById(R.id.etRcNumber);
        vehicleType = findViewById(R.id.vehicleType);
        regFront = findViewById(R.id.regFront);
        regBack = findViewById(R.id.regBack);


        progress = findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);

        requestQueue = Volley.newRequestQueue(this);
        regID = SharedHelper.getKey(getApplicationContext(), AppConstats.REG_ID_TOKEN);


        regFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxMediaPicker.builder(RegistrationCertificateActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(RegistrationCertificateActivity.this, filepath));
                            front_gallery_file = ImageUtils.bitmapToFile(bitmap, RegistrationCertificateActivity.this);
                            Glide.with(RegistrationCertificateActivity.this).load(front_gallery_file).into(regFront);
                            frontp = front_gallery_file.toString();
                            f = new File(frontp);

                        });

            }
        });

        regBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxMediaPicker.builder(RegistrationCertificateActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap2 = ImageUtils.imageCompress(ImageUtils.getRealPath(RegistrationCertificateActivity.this, filepath));
                            back_gallery_file = ImageUtils.bitmapToFile(bitmap2, RegistrationCertificateActivity.this);
                            Glide.with(RegistrationCertificateActivity.this).load(back_gallery_file).into(regBack);
                            backp = back_gallery_file.toString();
                            f2 = new File(backp);

                        });

            }
        });

        getEmail = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_EMAIL);
        getMobile = SharedHelper.getKey(getApplicationContext(), AppConstats.MOBILE_NUMBER);
        getPassword = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_PASSWORD);
        getName = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_FULLNAME);
        getLicenseNo = SharedHelper.getKey(getApplicationContext(), AppConstats.DRIVER_LICENSE_NUMBER);
        getLicenseExp = SharedHelper.getKey(getApplicationContext(), AppConstats.DRIVER_LICENSE_EXPIRY_DATE);
        getLicFront = SharedHelper.getKey(getApplicationContext(), AppConstats.DRIVING_LIC_FRONT_IMAGE_PATH);
        getBackLic = SharedHelper.getKey(getApplicationContext(), AppConstats.DRIVING_LIC_BACK_IMAGE_PATH);

        Log.e("cjdjcjkadbcj", getEmail);
        Log.e("cjdjcjkadbcj", getMobile);
        Log.e("cjdjcjkadbcj", getPassword);
        Log.e("cjdjcjkadbcj", getName);
        Log.e("cjdjcjkadbcj", getLicenseNo);
        Log.e("cjdjcjkadbcj", getLicenseExp);
        Log.e("cjdjcjkadbcj", getLicFront);
        Log.e("cjdjcjkadbcj", getBackLic);

        file1 = new File(getLicFront);
        file2 = new File(getBackLic);


        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, type);
        vehicleType.setAdapter(adapter);

        vehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = adapter.getItem(position);

                if (selected.equals("Bike")) {

                    strVehicleType = "1";
                } else {

                    strVehicleType = "2";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strRCNumber = etRcNumber.getText().toString().trim();

                if (TextUtils.isEmpty(strRCNumber)) {
                    new StyleableToast
                            .Builder(RegistrationCertificateActivity.this)
                            .text("Please fill all the details")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();
                } else if (frontp.equals("") || backp.equals("")) {

                    new StyleableToast
                            .Builder(RegistrationCertificateActivity.this)
                            .text("Select front/back photos")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();

                } else {
                    signup();
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


    public void signup() {

        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.upload(API.BASE_URL)
                .addMultipartParameter("control", "driver_registration")
                .addMultipartParameter("mobile", getMobile)
                .addMultipartParameter("user_type", "2")
                .addMultipartParameter("email", getEmail)
                .addMultipartParameter("password", getPassword)
                .addMultipartParameter("name", getName)
                .addMultipartParameter("regid", regID)
                .addMultipartParameter("license_no", getLicenseNo)
                .addMultipartParameter("license_expire_date", getLicenseExp)
                .addMultipartParameter("vehicle_rc_no", strRCNumber)
                .addMultipartParameter("vehicle_type", strVehicleType)
                .addMultipartFile("license_front_image", file1)
                .addMultipartFile("license_back_image", file2)
                .addMultipartFile("registration_front_image", f)
                .addMultipartFile("registration_back_image", f2)
                .setTag("signup")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dhsfjshbd", response.toString());
                        try {

                            if (response.has("result")) {

                                String strResult = response.getString("result");

                                if (strResult.equals("Registration Successfully.")) {

                                    String id = response.getString("id");
                                    String mobile = response.getString("mobile");
                                    String status = response.getString("status");
                                    String name = response.getString("name");
                                    String email = response.getString("email");
                                    String license_no = response.getString("license_no");
                                    String license_expire_date = response.getString("license_expire_date");
                                    String vehicle_rc_no = response.getString("vehicle_rc_no");
                                    String vehicle_type = response.getString("vehicle_type");

                                    Log.e("reuyry", id);
                                    Log.e("reuyry", mobile);
                                    Log.e("reuyry", status);
                                    Log.e("reuyry", name);
                                    Log.e("reuyry", email);
                                    Log.e("reuyry", license_no);
                                    Log.e("reuyry", license_expire_date);
                                    Log.e("reuyry", vehicle_rc_no);
                                    Log.e("reuyry", vehicle_type);

                                    new StyleableToast
                                            .Builder(RegistrationCertificateActivity.this)
                                            .text("Registration Successfully")
                                            .textColor(Color.WHITE)
                                            .backgroundColor(Color.parseColor("#F69D1D"))
                                            .show();

                                    progress.setVisibility(View.GONE);
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();


                                } else {

                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(RegistrationCertificateActivity.this, strResult, Toast.LENGTH_SHORT).show();
                                }

                            } else {

                                progress.setVisibility(View.GONE);
                                Toast.makeText(RegistrationCertificateActivity.this, "Something went wrong!!!!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                             Log.e("sdfdvcdv", e.getMessage());
                            progress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                         Log.e("bcjdgjsd", anError.getMessage());
                        progress.setVisibility(View.GONE);
                    }
                });
    }
}