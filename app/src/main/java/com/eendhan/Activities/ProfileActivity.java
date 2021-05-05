package com.eendhan.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.eendhan.Others.API;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.ImageUtils;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import iam.thevoid.mediapicker.rxmediapicker.Purpose;
import iam.thevoid.mediapicker.rxmediapicker.RxMediaPicker;

public class ProfileActivity extends AppCompatActivity{

    ImageView back, camera;
    ImageView prf;
    EditText etName, etNumber, etEmail;
    String strName = "", strNumber = "", strEmail = "";
    Button btnUpdate;
    GPSTracker gpsTracker;
    double latitude, longitude;

    String getUserID = "", getUsername = "", getNumber = "", getEmail = "";

    private static final String IMAGE_DIRECTORY = "/McWorld25 Driver";
    private int GALLERY = 1, CAMERA = 2;
    File f;

    RequestQueue requestQueue;
    ProgressBar progress;
    private File gallery_file;
    Uri contentURI;


    Uri picUri;

    File getImage;
    String imageFilePath="";

    private static final int REQUEST_CAPTURE_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        back = findViewById(R.id.back);
        prf = findViewById(R.id.prf);
        camera = findViewById(R.id.camera);
        btnUpdate = findViewById(R.id.btnUpdate);
        etName = findViewById(R.id.etName);
        etNumber = findViewById(R.id.etNumber);
        etEmail = findViewById(R.id.etEmail);
        progress = findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);

        requestQueue = Volley.newRequestQueue(this);

        gpsTracker = new GPSTracker(this);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        Log.e("ksnclks", latitude + "");
        Log.e("ksnclks", longitude + "");

        getEmail = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_EMAIL);
        getUserID = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
        getUsername = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_FULLNAME);
        getNumber = SharedHelper.getKey(getApplicationContext(), AppConstats.MOBILE_NUMBER);
        String getImage = SharedHelper.getKey(getApplicationContext(), AppConstats.IMAGE);

        Log.e("sdbjhgfjvhd", getEmail);
        Log.e("sdbjhgfjvhd", getUserID);
        Log.e("sdbjhgfjvhd", getUsername);
        Log.e("sdbjhgfjvhd", getNumber);

        etName.setText(getUsername);
        etEmail.setText(getEmail);
        etNumber.setText(getNumber);

        Glide.with(getApplicationContext()).load(getImage).error(R.drawable.prf2).into(prf);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = etName.getText().toString().trim();
                strNumber = etNumber.getText().toString().trim();
                strEmail = etEmail.getText().toString().trim();

                profile();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              // showPictureDialog();

                 RxMediaPicker.builder(ProfileActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                 .subscribe(filepath -> {
                    Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(ProfileActivity.this, filepath));
                    gallery_file = ImageUtils.bitmapToFile(bitmap, ProfileActivity.this);
                    Glide.with(ProfileActivity.this).load(gallery_file).into(prf);
                    Log.e("sdfsdfsdfsfd",gallery_file.toString());
                });

            }
        });

    }





    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                prf.setImageBitmap(imageBitmap);
            }
        }
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

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                 contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    prf.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            prf.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
*/
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
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public void profile() {


        progress.setVisibility(View.VISIBLE);

        AndroidNetworking.upload(API.BASE_URL).
                addMultipartParameter("control", "driver_update_profile")
                .addMultipartParameter("driver_id", getUserID)
                .addMultipartParameter("name", strName)
                .addMultipartParameter("email", strEmail)
                .addMultipartParameter("mobile", strNumber)
                .addMultipartFile("image", gallery_file)
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
                                    String mobile = response.getString("mobile");
                                    String name = response.getString("name");
                                    String email = response.getString("email");
                                    String image = response.getString("image");
                                    String path = response.getString("path");

                                     Log.e("sdfsdfsdfsd",path+image);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ID, id);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_EMAIL, email);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USER_FULLNAME, name);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.MOBILE_NUMBER, mobile);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.IMAGE, path + image);
                                    Glide.with(ProfileActivity.this).load(path + image).into(prf);

                                    progress.setVisibility(View.GONE);
                                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                    Animatoo.animateZoom(ProfileActivity.this);
                                    finish();

                                } else {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(ProfileActivity.this, strMessage, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progress.setVisibility(View.GONE);
                                Toast.makeText(ProfileActivity.this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Log.e("djcjhsdc", e.getMessage());
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("djcjhsdc", anError.getMessage());
                        progress.setVisibility(View.GONE);
                    }
                });
    }
}