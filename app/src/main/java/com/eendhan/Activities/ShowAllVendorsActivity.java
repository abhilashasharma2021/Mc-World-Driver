package com.eendhan.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.eendhan.Adapter.ShowVendorsAdapter;
import com.eendhan.Model.VendorModal;
import com.eendhan.Others.API;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowAllVendorsActivity extends AppCompatActivity {

    RecyclerView rec_menu;
    LinearLayoutManager layoutManager;

    ShowVendorsAdapter menuAdapter;
    ArrayList<VendorModal> menuArrayList;
    String strUserId = "";
    ImageView imgBack;
    String strOrderID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_vendors);

        strUserId = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
        strOrderID = SharedHelper.getKey(getApplicationContext(), AppConstats.CLIENT_ORDER_ID);
        Log.e("FDghdfgdfgdf", strOrderID);
        rec_menu = findViewById(R.id.rec_menu);
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rec_menu.setLayoutManager(layoutManager);
        rec_menu.setHasFixedSize(true);
        ShowAllVendors();
    }


    public void ShowAllVendors() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();


        AndroidNetworking.post(API.BASE_URL)
                .addBodyParameter("control", "show_notification_detail")
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("user_type", "2")
                /*   .addBodyParameter("order_id","ORD212124")*/
                .addBodyParameter("order_id", strOrderID)
                .setTag("Vendor")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("dslkl", response.toString());
                        menuArrayList = new ArrayList<>();
                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject1 = response.getJSONObject(i);
                                String strShopData = jsonObject1.getString("shop_data");
                                JSONArray jsonArray = new JSONArray(strShopData);

                                for (int j = 0; j < jsonArray.length(); j++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    VendorModal menuModal = new VendorModal();
                                    menuModal.setId(jsonObject.getString("id"));
                                    menuModal.setName(jsonObject.getString("shop_name"));
                                    menuModal.setSubcategory_name(jsonObject.getString("subcategory_id"));
                                    menuModal.setCatId(jsonObject.getString("category_id"));
                                    menuModal.setMobile(jsonObject.getString("mobile"));
                                    menuModal.setLat(jsonObject.getString("latitude"));
                                    menuModal.setLng(jsonObject.getString("longitude"));
                                    menuModal.setShop_address(jsonObject.getString("shop_address"));
                                    menuModal.setImage(jsonObject.getString("image"));
                                    //  menuModal.setImg_menu(jsonObject.getString("menu_image")); ;
                                    //  menuModal.setPath(jsonObject.getString("path"));
                                    menuArrayList.add(menuModal);
                                }
                            }

                            menuAdapter = new ShowVendorsAdapter(menuArrayList, ShowAllVendorsActivity.this);
                            rec_menu.setAdapter(menuAdapter);
                            dialog.dismiss();
                        }
                        catch (Exception ex) {
                            dialog.dismiss();
                            Log.e("trtry", ex.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.e("kukjhk", anError.getMessage());

                    }
                });
    }

}