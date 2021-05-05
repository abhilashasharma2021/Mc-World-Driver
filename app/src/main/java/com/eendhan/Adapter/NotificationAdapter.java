package com.eendhan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eendhan.Activities.OrderHistoryActivity;
import com.eendhan.Model.NotificationData;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Viewholder> {

    List<NotificationData> notificationDataList;
    Context context;

    public NotificationAdapter(List<NotificationData> notificationDataList, Context context) {
        this.notificationDataList = notificationDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.noti_recycler, parent, false);
        return new NotificationAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.Viewholder holder, int position) {

        final NotificationData data = notificationDataList.get(position);
        holder.message.setText(data.getMessage());
        holder.timedate.setText(data.getDateAndTime());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedHelper.putKey(context, AppConstats.SAVED_ORDER_ID,data.getOrderID());
                context.startActivity(new Intent(context, OrderHistoryActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationDataList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView timedate;
        public CardView card;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            timedate = itemView.findViewById(R.id.timedate);
            card = itemView.findViewById(R.id.card);
        }
    }
}
