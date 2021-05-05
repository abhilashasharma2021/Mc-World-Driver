package com.eendhan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eendhan.Activities.NavigationActivity;
import com.eendhan.Model.VendorModal;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowVendorsAdapter extends RecyclerView.Adapter<ShowVendorsAdapter.ViewHolder> {

    Context context;

    List<VendorModal> dataAdapters;


    public ShowVendorsAdapter(List<VendorModal> getDataAdapter, Context context) {

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_adapter, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        final VendorModal dataAdapterOBJ = dataAdapters.get(position);
        Viewholder.txtOffer.setText(dataAdapterOBJ.getName());
        Viewholder.txtType.setText(dataAdapterOBJ.getShop_address());
        Viewholder.txtTime.setText(dataAdapterOBJ.getDistance());
        try {
            Picasso.get().load(dataAdapterOBJ.getImage()).placeholder(R.drawable.placeholder).into(Viewholder.imgSubCat);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Viewholder.relSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedHelper.putKey(context, AppConstats.VendorLat, dataAdapterOBJ.getLat());
                SharedHelper.putKey(context, AppConstats.VendorLng, dataAdapterOBJ.getLng());
                SharedHelper.putKey(context, AppConstats.VendorRouteStatus, "1");
                context.startActivity(new Intent(context, NavigationActivity.class));


            }
        });

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtOffer,txtType,txtTime;
        public RelativeLayout relSubCategory;
        public RelativeLayout relMain;
        public ImageView imgSubCat;

        public ViewHolder(View itemView) {

            super(itemView);

            //txtQuestion =itemView.findViewById(R.id.txtQuestion);
            relSubCategory = itemView.findViewById(R.id.relSubCategory);
            relMain = itemView.findViewById(R.id.relSubCategory);
            imgSubCat = itemView.findViewById(R.id.imgSubCat);
            txtOffer = itemView.findViewById(R.id.txtOffer);
            txtType = itemView.findViewById(R.id.txtType);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}