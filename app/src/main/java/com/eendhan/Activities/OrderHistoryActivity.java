package com.eendhan.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.eendhan.Others.API;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderHistoryActivity extends AppCompatActivity {

    TextView shopName, shopAddress, delievto, prdName;
    ImageView image, back;
    ProgressDialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        prdName = findViewById(R.id.prdName);
        shopName = findViewById(R.id.shopName);
        shopAddress = findViewById(R.id.shopAddress);
        delievto = findViewById(R.id.delievto);
        image = findViewById(R.id.image);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orderInfo();
    }


    public void orderInfo() {

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("McWorld25 Driver");
        dialog.setMessage("please wait.........");
        dialog.show();

        String userId = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
        String orderID = SharedHelper.getKey(getApplicationContext(), AppConstats.SAVED_ORDER_ID);

        AndroidNetworking.post(API.BASE_URL)
                .addBodyParameter("control", "show_notification_detail")
                .addBodyParameter("user_id", userId)
                .addBodyParameter("user_type", "2")
                .addBodyParameter("order_id", orderID)
                .setTag("orderInfo")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        dialog.hide();

                        Log.e("ytuyewr", response.toString());


                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);

                                String shop_data = jsonObject.getString("shop_data");

                                JSONArray jsonArray = new JSONArray(shop_data);
                                for (int j = 0; j < jsonArray.length(); j++) {

                                    JSONObject object = jsonArray.getJSONObject(j);

                                    String img = object.getString("image");
                                    String shop_name = object.getString("shop_name");
                                    String shop_address = object.getString("shop_address");

                                    String path = SharedHelper.getKey(getApplicationContext(), AppConstats.PATH);

                                    Glide.with(getApplicationContext()).load(path + img).error(R.drawable.logo).into(image);

                                    if (shop_name.equals("")){
                                        shopName.setText("");
                                    }else {
                                        shopName.setText(shop_name);
                                    }


                                    if (shop_address.equals("")){
                                        shopAddress.setText("");
                                    }else {
                                        shopAddress.setText(shop_address);
                                    }


                                }


                                String order_data = jsonObject.getString("order_data");

                                JSONArray jsonArray1 = new JSONArray(order_data);

                                for (int k = 0; k < jsonArray1.length(); k++) {

                                    JSONObject obj = jsonArray1.getJSONObject(k);

                                    String address = obj.getString("address");
                                    String product_name = obj.getString("product_name");

                                    delievto.setText(address);
                                    prdName.setText(product_name);


                                    if (address.equals("")){
                                        delievto.setText("");
                                    }else {
                                        delievto.setText(address);
                                    }


                                    if (product_name.equals("")){
                                        prdName.setText("");
                                    }else {
                                        prdName.setText(product_name);
                                    }


                                }

                            }

                        } catch (Exception e) {
                            Log.e("etrwty", e.getMessage());
                            dialog.hide();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("etrwty", anError.getMessage());
                        dialog.hide();
                    }
                });
    }
}