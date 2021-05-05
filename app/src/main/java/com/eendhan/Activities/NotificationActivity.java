package com.eendhan.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.eendhan.Adapter.NotificationAdapter;
import com.eendhan.Model.NotificationData;
import com.eendhan.Others.API;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    ImageView back;
    TextView txt;

    List<NotificationData> notificationDataList;
    NotificationAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView notification_recycler;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        back = findViewById(R.id.back);
        txt = findViewById(R.id.txt);
        notification_recycler = findViewById(R.id.notification_recycler);
        progress = findViewById(R.id.progress);
        Sprite prog = new Circle();
        progress.setIndeterminateDrawable(prog);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        notification_recycler.setLayoutManager(layoutManager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        showNoti();
    }


    public void showNoti() {
        progress.setVisibility(View.VISIBLE);
        final String userID = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);

        AndroidNetworking.post(API.BASE_URL)
                .addBodyParameter("control", "show_notifications")
                .addBodyParameter("user_id", userID)
                .addBodyParameter("user_type", "2")
                .setTag("notifications")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("cjkd", response.toString());

                        notificationDataList = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject object = response.getJSONObject(i);

                                NotificationData data = new NotificationData();

                                String id = object.getString("id");
                                String msg = object.getString("msg");
                                String order_id = object.getString("order_id");
                                String date_time = object.getString("date_time");

                                data.setId(id);
                                data.setMessage(msg);
                                data.setOrderID(order_id);
                                data.setDateAndTime(date_time);

                                notificationDataList.add(data);
                                progress.setVisibility(View.GONE);

                            }

                            adapter = new NotificationAdapter(notificationDataList, getApplicationContext());
                            notification_recycler.setAdapter(adapter);


                        } catch (Exception e) {
                            Log.e("ccccs", e.getMessage());
                            progress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ccccs", anError.getMessage());
                        progress.setVisibility(View.GONE);
                    }
                });
    }
}