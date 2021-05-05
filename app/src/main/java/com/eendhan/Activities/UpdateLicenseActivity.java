package com.eendhan.Activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.eendhan.Others.API;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class UpdateLicenseActivity extends AppCompatActivity {

    private static final String IMAGE_DIRECTORY = "/McWorld25 Driver";
    private int GALLERY = 1, CAMERA = 2;
    private int GALLERY2 = 3, CAMERA2 = 4;
    File f, f2;

    String strLicenseNumber = "", strDate = "";

    DatePickerDialog datePickerDialog;
    TextView etDate;
    ImageView frontLic, backLic;
    ImageView back;
    Button btn_update;
    EditText licenseNumber;
    ImageView calender;

    String getLicFrontImage = "", getLicBackImage = "", getLicNumber = "", getLicExpiryDate = "";

    String getUserID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_license);

        back = findViewById(R.id.back);
        btn_update = findViewById(R.id.btn_update);
        licenseNumber = findViewById(R.id.licenseNumber);
        calender = findViewById(R.id.calender);
        etDate = findViewById(R.id.etDate);
        frontLic = findViewById(R.id.frontLic);
        backLic = findViewById(R.id.backLic);

        getUserID = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
        getLicFrontImage = SharedHelper.getKey(getApplicationContext(), AppConstats.DRIVING_LIC_FRONT_IMAGE);
        getLicBackImage = SharedHelper.getKey(getApplicationContext(), AppConstats.DRIVING_LIC_BACK_IMAGE);

        getLicNumber = SharedHelper.getKey(getApplicationContext(), AppConstats.DRIVER_LICENSE_NUMBER);
        getLicExpiryDate = SharedHelper.getKey(getApplicationContext(), AppConstats.DRIVER_LICENSE_EXPIRY_DATE);

        Log.e("cdnskcjndsj", getLicBackImage);
        Log.e("cdnskcjndsj", getLicFrontImage);
        Log.e("cdnskcjndsj", getLicNumber);
        Log.e("cdnskcjndsj", getLicExpiryDate);

        if (getLicFrontImage.equals("")) {
            Glide.with(this).load(R.drawable.licfront).into(frontLic);
        } else {
            Glide.with(this).load(getLicFrontImage).into(frontLic);
        }


        if (getLicBackImage.equals("")) {
            Glide.with(this).load(R.drawable.licback).into(backLic);
        } else {
            Glide.with(this).load(getLicBackImage).into(backLic);
        }


        if (getLicNumber.equals("")) {
            licenseNumber.setText("");
        } else {
            licenseNumber.setText(getLicNumber);
        }


        if (getLicExpiryDate.equals("")) {
            etDate.setText("");
        } else {
            etDate.setText(getLicExpiryDate);
        }

        frontLic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPictureDialog();
            }
        });


        backLic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog2();
            }
        });


        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(UpdateLicenseActivity.this,
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


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strLicenseNumber = licenseNumber.getText().toString().trim();
                strDate = etDate.getText().toString().trim();

                if (TextUtils.isEmpty(strLicenseNumber)) {
                    new StyleableToast
                            .Builder(UpdateLicenseActivity.this)
                            .text("Please enter license number")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();

                } else if (strDate.equals("Driving License Expiry Date") || strDate.equals("")) {

                    new StyleableToast
                            .Builder(UpdateLicenseActivity.this)
                            .text("Select date")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.parseColor("#d50000"))
                            .show();
                } else {


                    updateLicense();

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

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    private void showPictureDialog2() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary2();
                                break;
                            case 1:
                                takePhotoFromCamera2();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }


    public void choosePhotoFromGallary2() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY2);
    }

    private void takePhotoFromCamera2() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    frontLic.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            frontLic.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }


        if (requestCode == GALLERY2) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage2(bitmap);
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    backLic.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA2) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            backLic.setImageBitmap(thumbnail);
            saveImage2(thumbnail);
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }


    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.e("sjncjknc", f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public String saveImage2(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            f2 = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f2.createNewFile();
            FileOutputStream fo = new FileOutputStream(f2);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f2.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.e("sjncjknc", f2.getAbsolutePath());

            return f2.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public void updateLicense() {

        AndroidNetworking.upload(API.BASE_URL).
                addMultipartParameter("control", "driver_update_profile")
                .addMultipartParameter("driver_id", getUserID)
                .addMultipartParameter("license_no", strLicenseNumber)
                .addMultipartParameter("license_expire_date", strDate)
                .addMultipartFile("license_front_image", f)
                .addMultipartFile("license_back_image", f2)
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("rytryew", response.toString());

                        try {

                            if (response.has("message")) {

                                String strMessage = response.getString("message");

                                if (strMessage.equals("update Successfully")) {

                                    String id = response.getString("id");
                                    String license_no = response.getString("license_no");
                                    String license_expire_date = response.getString("license_expire_date");
                                    String path = response.getString("path");
                                    String license_front_image = response.getString("license_front_image");
                                    String license_back_image = response.getString("license_back_image");


                                    licenseNumber.setText(license_no);
                                    etDate.setText(license_expire_date);

                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ID, id);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.DRIVER_LICENSE_NUMBER, license_no);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.DRIVER_LICENSE_EXPIRY_DATE, license_expire_date);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.DRIVING_LIC_FRONT_IMAGE, path + license_front_image);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.DRIVING_LIC_BACK_IMAGE, path + license_back_image);
                                    Glide.with(UpdateLicenseActivity.this).load(path + license_front_image).into(frontLic);
                                    Glide.with(UpdateLicenseActivity.this).load(path + license_back_image).into(backLic);


                                    startActivity(new Intent(getApplicationContext(), UpdateLicenseActivity.class));
                                    Animatoo.animateSpin(UpdateLicenseActivity.this);
                                    finish();

                                }
                            }

                        } catch (Exception e) {

                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


}