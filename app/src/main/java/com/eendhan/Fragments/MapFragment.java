package com.eendhan.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.eendhan.Activities.ShowRouteActivity;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.eendhan.Activities.GPSTracker;
import com.eendhan.Activities.NavigationActivity;
import com.eendhan.Activities.ShowAllVendorsActivity;
import com.eendhan.Others.API;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import hari.bounceview.BounceView;

import static android.content.Context.LOCATION_SERVICE;
import static com.eendhan.Others.AppConstats.CLIENT_ORDER_ID;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.StrictMath.acos;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    ImageView menu;
    GoogleMap mMap;
    GPSTracker gpsTracker;
    double latitude, longitude;
    double lat, lon;
    RelativeLayout goOnline, goOffline;
    ImageView prf, itemImg;
    //String GOOGLE_DIRECTION_KEY = "AIzaSyA1yFj539rsjnQwYPRZpP6cSUcG5NXjg-c";
    String GOOGLE_DIRECTION_KEY = "AIzaSyBVXWPGpxUIMfAn4T-rzWkWNaDzeEWKmTo";
    CardView card2;
    Button btngo, btnstart, btnorderComplete, btnCall;
    RelativeLayout relpickup, reltime, kmLeft;
    TextView txt_yourareInLocation, prdName, pickUpPoint, distanceRemaining, time;
    String SHOP_LAT="",SHOP_LONG="";
    ImageView onlinebtn;


    /////////////////////////////////////////////

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    boolean isLocationPermission = false;

    CameraPosition cameraPosition;
    Location lastLocation;
    //////////////////////////////////////////////

    String request = "", getClientID = "", getClientLat = "", getClientLong = "", getOrderID = "", getClientAddress = "";
    String getUserID = "", getProductName = "", getShopName = "", getShopAddress = "", getPrice = "", getProductImage = "", getImagePath = "";

    LatLng origin, destination;
    LatLng vendorLatLng;
    RequestQueue requestQueue;
    Timer carousalTimer;
   String ClientImage="",getOrderAddress="",ClientNumber="",ClientName="",orderStatus="",CLIENT_DISTANCE="",OnlineStatus="";
    ProgressBar progress;
    ProgressDialog dialog;
    String genrateOrderLong="",genrateOrderLat="";
    String strVendorLng = "";
    String strVendorLat = "";
    ImageView imgVendors;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    LatLng currentPosition;
    String strVendorStatus = "";
    String userId = "";

    MarkerOptions mMarkerOptions;

    BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("title");
            String payloadData = intent.getStringExtra("payload");


            Log.e("ccrfhrtfgjygk", result);
            Log.e("ccrfhrtfgjygk", payloadData);

            if (result.equals("You have new request")) {
                request(payloadData);
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frag_map);
        mapFragment.getMapAsync(this);


        userId = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
        OnlineStatus = SharedHelper.getKey(getActivity(), AppConstats.OnlineStatus);
        Log.e("dshfdfhfnhn", userId);
        Log.e("dshfdfhfnhn", OnlineStatus);

        imgVendors = view.findViewById(R.id.imgVendors);
        menu = view.findViewById(R.id.menu);
        prf = view.findViewById(R.id.prf);
        card2 = view.findViewById(R.id.card2);
        goOnline = view.findViewById(R.id.goOnline);
        goOffline = view.findViewById(R.id.goOffline);
        btngo = view.findViewById(R.id.btngo);
        btnstart = view.findViewById(R.id.btnstart);
        relpickup = view.findViewById(R.id.relpickup);
        reltime = view.findViewById(R.id.reltime);
        txt_yourareInLocation = view.findViewById(R.id.txt_yourareInLocation);
        kmLeft = view.findViewById(R.id.kmLeft);
        btnorderComplete = view.findViewById(R.id.btnorderComplete);
        prdName = view.findViewById(R.id.prdName);
        itemImg = view.findViewById(R.id.itemImg);
        pickUpPoint = view.findViewById(R.id.pickUpPoint);
        distanceRemaining = view.findViewById(R.id.distanceRemaining);
        time = view.findViewById(R.id.time);
        btnCall = view.findViewById(R.id.btnCall);
        onlinebtn = view.findViewById(R.id.onlinebtn);
        progress = view.findViewById(R.id.progress);
        Sprite doubleBounce = new Circle();
        progress.setIndeterminateDrawable(doubleBounce);


        String getImage = SharedHelper.getKey(getActivity(), AppConstats.IMAGE);
        Glide.with(getActivity()).load(getImage).error(R.drawable.prf2).into(prf);

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        time.setText(currentTime);

        displayLocationSettingsRequest(getActivity());
        MapsInitializer.initialize(getActivity());
        BounceView.addAnimTo(goOnline);
        BounceView.addAnimTo(goOffline);
        requestQueue = Volley.newRequestQueue(getActivity());

        /////////////////////////////////////////////////////////////////////////////////

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (savedInstanceState != null) {
            lastLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }


        ///////////////////////////////////////////////////////////////


    /*    String onlineStatus = SharedHelper.getKey(getActivity(), AppConstats.STATUS_ONLINE);
        *//*onlineStatus= 1 means login nd onlineStatus=0 means offline *//*
        Log.e("thtrjuysjky", "onlineStatus: " + onlineStatus);*/


   //////////////////////online ///offline/////////////////////////////////////////////////

        if (userId.equals("")) {
            goOnline.setVisibility(View.GONE);
            goOffline.setVisibility(View.VISIBLE);
        } else {
            if (OnlineStatus.equals("1")){
                goOnline.setVisibility(View.VISIBLE);
                goOffline.setVisibility(View.GONE);
                onlinebtn.setVisibility(View.VISIBLE);
            }else   if (OnlineStatus.equals("0")){
                onlinebtn.setVisibility(View.GONE);

                goOnline.setVisibility(View.GONE);
                goOffline.setVisibility(View.VISIBLE);

            }else {
                goOnline.setVisibility(View.VISIBLE);
                goOffline.setVisibility(View.GONE);
            }



        }

   goOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getUserID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);

                Log.e("MapFragment", "offline: " +getUserID);
                onlineStatus(userId, "1");
            }
        });

           goOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MapFragment", "online: " +getUserID);
               // Toast.makeText(getActivity(), "checkOnline", Toast.LENGTH_SHORT).show();
                getUserID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
                onlineStatus(userId, "0");
            }
        });





/////////////////////////////////////////////////////////////////////////////////////////////////////////
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationActivity.drawer.openDrawer(GravityCompat.START);
            }
        });

      /*  imgVendors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShowAllVendorsActivity.class));
            }
        });
*/
        btngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btngo.setText("START");
                reltime.setVisibility(View.GONE);
                relpickup.setVisibility(View.GONE);
                txt_yourareInLocation.setVisibility(View.VISIBLE);
                btnstart.setVisibility(View.VISIBLE);

            }
        });

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_yourareInLocation.setText("CONGRATULATION");
                txt_yourareInLocation.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                btnorderComplete.setVisibility(View.VISIBLE);
                btnstart.setVisibility(View.GONE);
                kmLeft.setVisibility(View.GONE);
            }
        });

        btnorderComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // orderComplete();
            }
        });


        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clientNumber = SharedHelper.getKey(getActivity(), AppConstats.CLIENT_MOBILE);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + clientNumber));
                startActivity(intent);
            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        String getImage = SharedHelper.getKey(getActivity(), AppConstats.IMAGE);
        request = SharedHelper.getKey(getActivity(), AppConstats.REQUESTED_TITLE);
        getClientID = SharedHelper.getKey(getActivity(), AppConstats.CLIENT_ID);
        getClientLat = SharedHelper.getKey(getActivity(), AppConstats.CLIENT_LAT);
        getClientLong = SharedHelper.getKey(getActivity(), AppConstats.CLIENT_LONG);
        strVendorStatus = SharedHelper.getKey(getActivity(), AppConstats.VendorRouteStatus);


        Log.e("MapFragment", "strVendorStatus: " + strVendorStatus);

        Glide.with(getActivity()).load(getImage).error(R.drawable.prf2).into(prf);
        getActivity().registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter("Check"));


       /* if (strVendorStatus.equals("1")) {

            acceptRequest();
        }*/

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       /* gpsTracker = new GPSTracker(getActivity());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        // origin = new LatLng(latitude, longitude);
        Log.e("Dfgdfgdfgdf", latitude + "");
        Log.e("Dfgdfgdfgdf", longitude + "");
        currentPosition = new LatLng(latitude, longitude);*/


        getLocationPermission();
        getDeviceLocation();
        updateLocationUI();

       /* carousalTimer = new Timer(); // At this line a new Thread will be created
        carousalTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                Log.e("onReady", "check: " +latitude);
                Log.e("onReady", "check: " +longitude);

             //   updateLatLong(String.valueOf(latitude),String.valueOf(longitude));
            }
        }, 0, 5 * 1000); // delay
*/
        //  getMyLocation();


        try {
            lat = Double.parseDouble(getClientLat);
            lon = Double.parseDouble(getClientLong);
            destination = new LatLng(lat, lon);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sjxchsjc", e.getMessage());
        }


    }


  /* @Override
    public void onDestroy() {
        super.onDestroy();
       SharedHelper.putKey(getActivity(), AppConstats.USER_ID, getDriverUserId);
      //  getUserID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
       // onlineStatus(getUserID, "0");
    }*/



 /*  private void getMyLocation() {


        // Getting LocationManager object from System Service LOCATION_SERVICE
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));
        mMap.moveCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(currentPosition)));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("Dfgdfgdfgdf", "OnLocationChange" + location.getLatitude() + "");
                Log.e("Dfgdfgdfgdf", "OnLocationChange" + location.getLongitude() + "");
                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                mMarkerOptions = new MarkerOptions().position(currentPosition).title("current location");
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(currentPosition)));

                if (vendorLatLng != null && destination != null) {

                    rootDirection(vendorLatLng, destination);
                }


            }
        };

    }*/

    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(10).build();
    }


    public void request(String payload) {

        String getPayload = payload;
        Log.e("MapFragment", "getPayload: " + getPayload);
        try {
            JSONObject json = new JSONObject(getPayload);
            String data2 = json.getString("data2");
            JSONArray array = new JSONArray(data2);
            for (int i = 0; i <array.length() ; i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                getProductName = jsonObject.getString("product_name");
                getImagePath = jsonObject.getString("path");
                getOrderAddress = jsonObject.getString("address");
                genrateOrderLong = jsonObject.getString("longitude");
               genrateOrderLat = jsonObject.getString("latitude");
                getPrice = jsonObject.getString("total_amount");
                String CLIENT_ID = jsonObject.getString("user_id");
                CLIENT_ORDER_ID = jsonObject.getString("order_id");
                String CLIENT_ORDER_TYPE = jsonObject.getString("order_type");
                String user_deatails = jsonObject.getString("user_deatails");
                JSONObject object2=new JSONObject(user_deatails);
                ClientName=object2.getString("name");
                ClientImage=object2.getString("image");
                ClientNumber=object2.getString("mobile");

            }
            /*SHOP DETAILS PICK UP DETAILS */
            String data1 = json.getString("data1");
            JSONArray array1 = new JSONArray(data1);
            for (int j = 0; j < array1.length(); j++) {
                JSONObject jsonObject1 = array1.getJSONObject(j);
                Log.e("checkdata", "data2: " + data2);
                Log.e("checkdata", "data1: " + data1);
                String CLIENT_IMAGE = jsonObject1.getString("image");
                String CLIENT_MOBILE = jsonObject1.getString("mobile");
                getShopName = jsonObject1.getString("shop_name");
                getShopAddress = jsonObject1.getString("shop_address");
                String name = jsonObject1.getString("name");
               SHOP_LAT = jsonObject1.getString("latitude");
               SHOP_LONG = jsonObject1.getString("longitude");
                CLIENT_DISTANCE = jsonObject1.getString("distance");
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.request_dialog);
        dialog.setCanceledOnTouchOutside(false);
        RelativeLayout relAccept = dialog.findViewById(R.id.relAccept);
        RelativeLayout relCancel = dialog.findViewById(R.id.relCancel);
        TextView txt_droppoint = dialog.findViewById(R.id.txt_droppoint);
        TextView txtClientNo = dialog.findViewById(R.id.txtClientNo);
        TextView txtClientName = dialog.findViewById(R.id.txtClientName);
        TextView distance = dialog.findViewById(R.id.distance);
        TextView txtPrice = dialog.findViewById(R.id.txtPrice);
        TextView productName = dialog.findViewById(R.id.productName);
        TextView shopName = dialog.findViewById(R.id.shopName);
        TextView shopAddress = dialog.findViewById(R.id.shopAddress);
        ImageView prImg = dialog.findViewById(R.id.prImg);

        SharedHelper.putKey(getActivity(), AppConstats.VendorLat, SHOP_LAT);
        SharedHelper.putKey(getActivity(), AppConstats.VendorLng, SHOP_LONG);
       /* SharedHelper.putKey(getActivity(), AppConstats.CLIENT_LAT, genrateOrderLat);
        SharedHelper.putKey(getActivity(), AppConstats.CLIENT_LONG, genrateOrderLong);*/
       /* getProductName = SharedHelper.getKey(getActivity(), AppConstats.PRODUCT_NAME);
        getShopName = SharedHelper.getKey(getActivity(), AppConstats.SHOP_NAME);
        getShopAddress = SharedHelper.getKey(getActivity(), AppConstats.SHOP_ADDRESS);
        getProductImage = SharedHelper.getKey(getActivity(), AppConstats.PRODUCT_IMAGE);
        getImagePath = SharedHelper.getKey(getActivity(), AppConstats.PATH);
        getClientAddress = SharedHelper.getKey(getActivity(), AppConstats.CLIENT_ADDRESS);*/

        productName.setText(getProductName);
        // prdName.setText(getProductName);
        shopName.setText(getShopName);
        txtPrice.setText("Rs." + getPrice);
        shopAddress.setText("Pick up : "+ getShopAddress);  /*Pickup Address*/
        txt_droppoint.setText("Drop off :"+  getOrderAddress);/*dropoff Address*/
        txtClientNo.setText(ClientNumber);
        distance.setText("Route Distance : " + CLIENT_DISTANCE + " " + "km");
        txtClientName.setText(ClientName);
      //  pickUpPoint.setText("Pick up : " + getShopAddress);


        Log.e("fvdsgdf", "request: " +getImagePath + ClientImage);
        Log.e("fvdsgdf", "request: " +CLIENT_DISTANCE);

        try {
            Glide.with(getActivity()).load(getImagePath + ClientImage).error(R.drawable.prf2).into(prImg);
        } catch (Exception e) {
            e.printStackTrace();
        }


        double dis = getKilometers(latitude, longitude, lat, lon);
        double h = Math.round(dis);
        Log.e("sdhcjcd", String.valueOf(h));
        String dist = String.valueOf(h);

       /* txt_droppoint.setText(getClientAddress);
        distance.setText("Route Distance : " + dist + " " + "km");
        distanceRemaining.setText(dist + " " + "km");*/

        relAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(getActivity(), ShowAllVendorsActivity.class));
                orderStatus="1";

                acceptRequest(orderStatus,CLIENT_ORDER_ID);
                dialog.dismiss();


            }
        });


        relCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderStatus="3";
                cancelRequest(orderStatus,CLIENT_ORDER_ID);
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public double getKilometers(double lat1, double long1, double lat2, double long2) {
        double PI_RAD = Math.PI / 180.0;
        double phi1 = lat1 * PI_RAD;
        double phi2 = lat2 * PI_RAD;
        double lam1 = long1 * PI_RAD;
        double lam2 = long2 * PI_RAD;
        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));
    }


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("fkdvkf", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("fkdvkf", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            status.startResolutionForResult(getActivity(), 1);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("fkdvkf", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("fkdvkf", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    public void rootDirection(final LatLng sourceLatLng, final LatLng destiLatLng) {
        Log.e("stateOfActivity", "rootDirection");

        GoogleDirection.withServerKey(GOOGLE_DIRECTION_KEY)
                .from(sourceLatLng)
                .to(destiLatLng)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .alternativeRoute(true)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);


                            int heights = 200;
                            int widths = 200;
                            BitmapDrawable bitmapdraws = (BitmapDrawable) getResources().getDrawable(R.drawable.l_200);
                            //   BitmapDrawable bitmapdraws = (BitmapDrawable) getResources().getDrawable(R.drawable.location);
                            Bitmap bs = bitmapdraws.getBitmap();


                            Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, widths, heights, false);
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(sourceLatLng)
                                    // .icon(BitmapDescriptorFactory.fromBitmap(smallMarkers))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))

                            );

                           /* MarkerOptions markerOptions1 = new MarkerOptions().position(sourceLatLng)
                                    .title("Source")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                            mMap.addMarker(marker);*/

                            MarkerOptions markerOptions3 = new MarkerOptions().position(destiLatLng)
                                    .title("Destination").draggable(true);

                            mMap.addMarker(markerOptions3);

                            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                            mMap.addPolyline(DirectionConverter.createPolyline(getActivity(), directionPositionList, 5, (getResources().getColor(R.color.colorPrimaryDark))));

                            setCameraWithCoordinationBounds(route);

                        } else {

                            //Toast.makeText(getActivity(), "something went wrong! ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                        Log.e("lsdjksa", String.valueOf(t));
                        Toast.makeText(getActivity(), "onDirectionFailure", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void setCameraWithCoordinationBounds(Route route) {

        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

    }


    public void acceptRequest(String orderStatus, String clientOrderId) {

        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(true);
        dialog.setTitle("Eendhan Driver");
        dialog.setMessage("Waiting for data to be fetched");
        dialog.show();
        getUserID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
      /*  strVendorLat = SharedHelper.getKey(getActivity(), AppConstats.VendorLat);
        strVendorLng = SharedHelper.getKey(getActivity(), AppConstats.VendorLng);*/
        Log.e("MapFragment", "acceptRequest: " +getUserID);
        Log.e("MapFragment", "acceptRequest: " +strVendorLat);
        Log.e("MapFragment", "acceptRequest: " +strVendorLng);


       /* getUserID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
        getOrderID = SharedHelper.getKey(getActivity(), AppConstats.CLIENT_ORDER_ID);
        strVendorLat = SharedHelper.getKey(getActivity(), AppConstats.VendorLat);
        strVendorLng = SharedHelper.getKey(getActivity(), AppConstats.VendorLng);*/
        /*   Log.e("dsmkkvnjk", getOrderID);
        Log.e("dsmkkvnjk", getUserID);
*/
        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("dsggbdfbvcb", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String strResult = jsonObject.getString("result");

                    if (strResult.equals("Successfully.")) {

                        String id = jsonObject.getString("id");
                        String user_id = jsonObject.getString("user_id");
                        String order_id = jsonObject.getString("order_id");
                        String address = jsonObject.getString("address");
                        String lat = jsonObject.getString("latitude");
                        String longi = jsonObject.getString("longitude");
                        String addre = jsonObject.getString("address");

                        Log.e("cxnxjkcsjk", id);
                        Log.e("cxnxjkcsjk", user_id);
                        Log.e("cxnxjkcsjk", order_id);
                        Log.e("cxnxjkcsjk", address);
                        Log.e("cxnxjkcsjk", lat);
                        Log.e("cxnxjkcsjk", longi);

                        Toast.makeText(getActivity(), jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
                        dialog.hide();





                        SharedHelper.putKey(getActivity(), AppConstats.CLIENT_LAT, lat);
                        SharedHelper.putKey(getActivity(), AppConstats.CLIENT_LONG, longi);
                        SharedHelper.putKey(getActivity(), AppConstats.CLIENT_ORDER_ID, order_id);

                        startActivity(new Intent(getActivity(), ShowRouteActivity.class));


                   /*    if (!strVendorLat.equals("")) {

                           vendorLatLng = new LatLng(23.1616,77.4694);
                           destination = new LatLng(23.1830,  77.4562);
                           rootDirection(vendorLatLng, destination);
                           card2.setVisibility(View.VISIBLE);
                           *//* vendorLatLng = new LatLng(Double.parseDouble(strVendorLat), Double.parseDouble(strVendorLng));
                            destination = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
                            rootDirection(vendorLatLng, destination);
                            card2.setVisibility(View.VISIBLE);*//*

                        } else {
                            Toast.makeText(getActivity(), "Vendor Location not found", Toast.LENGTH_SHORT).show();
                        }
*/

                    }
                    else {
                        Toast.makeText(getActivity(), jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
                        dialog.hide();
                    }

                } catch (Exception e) {
                    Log.e("MCerror", e.getMessage());
                    dialog.hide();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Log.e("MCerror", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("control", "driver_acc_order");
                param.put("driver_id", getUserID);
                param.put("order_id", clientOrderId);
                param.put("order_status", orderStatus);
                Log.e("MapFragment", "acceptRequest: " +orderStatus);
                Log.e("MapFragment", "acceptRequest: " +clientOrderId);
                return param;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);


    }

    public void cancelRequest(String orderStatus, String clientOrderId) {

        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(true);
        dialog.setTitle("Eendhan Driver");
        dialog.setMessage("Waiting for data to be fetched");
        dialog.show();
        getUserID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);

        Log.e("MapFragment", "cancelRequest: " +getUserID);
        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("sdhfrjhnfgnhgfn", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String strResult = jsonObject.getString("result");

                    if (strResult.equals("Successfully.")) {

                        String id = jsonObject.getString("id");
                        String user_id = jsonObject.getString("user_id");
                        String order_id = jsonObject.getString("order_id");
                        String address = jsonObject.getString("address");
                        String lat = jsonObject.getString("latitude");
                        String longi = jsonObject.getString("longitude");
                        String addre = jsonObject.getString("address");

                        Log.e("dgsgdsv", id);
                        Log.e("dgsgdsv", user_id);
                        Log.e("dgsgdsv", order_id);
                        Log.e("dgsgdsv", address);
                        Log.e("dgsgdsv", lat);
                        Log.e("dgsgdsv", longi);
                        Toast.makeText(getActivity(), jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
                        dialog.hide();

                    }
                    else {
                        Toast.makeText(getActivity(),jsonObject.getString("result") , Toast.LENGTH_SHORT).show();
                        dialog.hide();
                    }


                } catch (Exception e) {
                    Log.e("gfdbfdb", e.getMessage());
                    dialog.hide();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Log.e("gfdbfdb", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("control", "driver_acc_order");
                param.put("driver_id", getUserID);
                param.put("order_id", clientOrderId);
                param.put("order_status", orderStatus);
                Log.e("MapFragment", "cancelRequest: " +orderStatus);
                Log.e("MapFragment", "cancelRequest: " +clientOrderId);
                return param;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);


    }



    public void updateLatLong(String lat, String lng) {

        Log.e("mgfhnfgjnfg,", "lat: " + lat);
        Log.e("mgfhnfgjnfg", "lng: " + lng);
        try {
            getUserID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
        } catch (Exception e) {
            Log.e("sjhcc", e.getMessage());
        }

        AndroidNetworking.post(API.BASE_URL)
                .addBodyParameter("control", "driver_update_profile")
                .addBodyParameter("driver_id", getUserID)
                .addBodyParameter("latitude", lat)
                .addBodyParameter("longitude", lng)
                .setTag("updateLatLng")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("qeewerr", response.toString());

                        try {
                            if (response.has("message")) {
                                String strMessage = response.getString("message");

                                if (strMessage.equals("update Successfully")) {

                                } else {

                                    Toast.makeText(getActivity(), strMessage, Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toast.makeText(getActivity(), "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {

                            Log.e("dssbxcs", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dssbxcs", anError.getMessage());
                    }
                });
    }


    public void orderComplete() {
        progress.setVisibility(View.VISIBLE);
        getUserID = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);
        getOrderID = SharedHelper.getKey(getActivity(), CLIENT_ORDER_ID);

        Log.e("tytytytyty", getOrderID);
        Log.e("tytytytyty", getUserID);

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("dcjkdv", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String strResult = jsonObject.getString("result");
                    if (strResult.equals("Successfully.")) {

                        progress.setVisibility(View.GONE);

                        Toast.makeText(getActivity(), "Order Delivered Successfully", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
                    }

                } catch (Exception e) {
                    progress.setVisibility(View.GONE);
                    Log.e("error", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("control", "driver_acc_order");
                param.put("driver_id", getUserID);
                param.put("order_id", getOrderID);
                param.put("order_status", "2");
                return param;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }


    public void onlineStatus(String userID, String status) {
        Log.e("shvcjacvjadvc", status);

        progress.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("bjfdhfdhjfdhvn", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String result = jsonObject.getString("result");

                    if (result.equals("Successfully.")) {

                        String driver_online_status = jsonObject.getString("driver_online_status");

                        Log.e("hdfhd", driver_online_status);

                        SharedHelper.putKey(getActivity(), AppConstats.OnlineStatus, driver_online_status);

                        if (driver_online_status.equals("0")) {

                            new StyleableToast
                                    .Builder(getActivity())
                                    .text("Offline")
                                    .textColor(Color.WHITE)
                                    .backgroundColor(Color.parseColor("#d50000"))
                                    .show();

                            goOffline.setVisibility(View.VISIBLE);
                            goOnline.setVisibility(View.GONE);
                            onlinebtn.setVisibility(View.VISIBLE);

                        } else {
                            new StyleableToast
                                    .Builder(getActivity())
                                    .text("Online")
                                    .textColor(Color.WHITE)
                                    .backgroundColor(Color.parseColor("#00c853"))
                                    .show();

                            goOnline.setVisibility(View.VISIBLE);
                            goOffline.setVisibility(View.GONE);
                            onlinebtn.setVisibility(View.GONE);



                        }


                        progress.setVisibility(View.GONE);

                    }

                } catch (Exception e) {
                    progress.setVisibility(View.GONE);
                    Log.e("dsgvfdbfdb ", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("dsgvfdbfdb", error.toString());
                progress.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("control", "change_drver_onOff_status");
                param.put("driver_id", userId);
                param.put("status", status);

                Log.e("MapFragment", "userID: " + userID);
                Log.e("MapFragment", "status: " + status);
                return param;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    public void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            isLocationPermission = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isLocationPermission = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isLocationPermission = true;
            }
        }

        updateLocationUI();
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (isLocationPermission) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    public void getDeviceLocation() {

        if (isLocationPermission) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            final Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    if (task.isSuccessful()) {
                        lastLocation = task.getResult();

                        if (lastLocation != null) {
                            lastLocation.getLatitude();
                            lastLocation.getLongitude();


                            carousalTimer = new Timer(); // At this line a new Thread will be created
                            carousalTimer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {


                                    Log.e("sdavsdvds", "check: " + lastLocation.getLatitude());
                                    Log.e("sdavsdvds", "check: " + lastLocation.getLongitude());

                                    updateLatLong(String.valueOf(lastLocation.getLatitude()), String.valueOf(lastLocation.getLongitude()));
                                }
                            }, 0, 5 * 1000); // delay


                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 12));


                        } else {
                            LatLng latLng = new LatLng(33.8688, 151.2093);
                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(latLng, 12));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);

                        }
                    }
                }
            });


        }
    }

}

