package com.eendhan.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eendhan.Fragments.MapFragment;
import com.eendhan.Others.API;
import com.eendhan.Others.AppConstats;
import com.eendhan.Others.DirectionsJSONParser;
import com.eendhan.Others.SharedHelper;
import com.eendhan.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.eendhan.Others.AppConstats.CLIENT_ORDER_ID;

public class ShowRouteActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ArrayList<LatLng> points = null;
    private Polyline polyline;
    private GoogleMap googleMap;
    String getUserID="",getOrderID="";
    LatLng s_latLng,d_latLng;
    ImageView iv_back;
    TextView time;
    String strDestinationLatitude="",strDestinationLongitude="",strSourceLatitude="",strSourceLongitude="";
    String url="";
   Button btngo,btnstart,btnorderComplete;
   TextView txt_yourareInLocation;
    RelativeLayout relpickup,reltime,kmLeft;
    ProgressBar progress;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);

        strDestinationLatitude = SharedHelper.getKey(ShowRouteActivity.this, AppConstats.CLIENT_LAT);
        strDestinationLongitude = SharedHelper.getKey(ShowRouteActivity.this, AppConstats.CLIENT_LONG);
        strSourceLatitude = SharedHelper.getKey(ShowRouteActivity.this, AppConstats.VendorLat);
        strSourceLongitude = SharedHelper.getKey(ShowRouteActivity.this, AppConstats.VendorLng);

        time = findViewById(R.id.time);
        kmLeft = findViewById(R.id.kmLeft);
        reltime = findViewById(R.id.reltime);
        relpickup = findViewById(R.id.relpickup);
        progress = findViewById(R.id.progress);
        btngo = findViewById(R.id.btngo);
        btnstart = findViewById(R.id.btnstart);
        btnorderComplete = findViewById(R.id.btnorderComplete);
        txt_yourareInLocation = findViewById(R.id.txt_yourareInLocation);

      /*  String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        time.setText(currentTime);*/
        Log.e("ShowRouteActivity", "vendorLat: " +strDestinationLatitude);
        Log.e("ShowRouteActivity", "vendorLong: " +strDestinationLongitude);
        Log.e("ShowRouteActivity", "clientLat: " +strSourceLatitude);
        Log.e("ShowRouteActivity", "clientLong: " +strSourceLongitude);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);

        s_latLng=new LatLng(Double.parseDouble(String.valueOf(strSourceLatitude)),Double.parseDouble(String.valueOf(strSourceLongitude)));
        d_latLng=new LatLng(Double.parseDouble(String.valueOf(strDestinationLatitude)),Double.parseDouble(String.valueOf(strDestinationLongitude)));
        Log.e("aduhcjdcnad", s_latLng+"" );
        Log.e("aduhcjdcnad", d_latLng+"" );
        requestQueue = Volley.newRequestQueue(ShowRouteActivity.this);
        String url = getDirectionsUrl(s_latLng, d_latLng);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);


      /*  btngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btngo.setText("START");
                reltime.setVisibility(View.GONE);
                relpickup.setVisibility(View.GONE);
                txt_yourareInLocation.setVisibility(View.VISIBLE);
                btnstart.setVisibility(View.VISIBLE);

            }
        });*/

      /*  btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_yourareInLocation.setText("CONGRATULATION");
                txt_yourareInLocation.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                btnorderComplete.setVisibility(View.VISIBLE);
                btnstart.setVisibility(View.GONE);
                kmLeft.setVisibility(View.GONE);
            }
        });*/

        btnorderComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderComplete();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
      /*  MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(ShowRouteActivity.this, R.raw.uber_style_map);
        googleMap.setMapStyle(style);*/

        int height = 80;
        int width = 60;
        BitmapDrawable bitmapdraw1=(BitmapDrawable)getResources().getDrawable(R.drawable.sourceicon);
        Bitmap b1=bitmapdraw1.getBitmap();
        Bitmap smallMarker1 = Bitmap.createScaledBitmap(b1, width, height, false);
        BitmapDrawable bitmapdraw2=(BitmapDrawable)getResources().getDrawable(R.drawable.destination);
        Bitmap b2=bitmapdraw2.getBitmap();
        Bitmap smallMarker2 = Bitmap.createScaledBitmap(b2, width, height, false);

        map.addMarker(new MarkerOptions()
                .position(s_latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker1))
        );
        map.addMarker(new MarkerOptions()
                .position(d_latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker2))
        );
    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom((float) 14.5).build();
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String alternative = "alternatives=true";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        Log.e("fgkjdyhtiy7reugre",parameters);
        //  url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyAURsUjEy5yIa7CTgW_vyn8f5L7iKXz5DI";
        url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" +
                "AIzaSyCDEADQSNC3EMJD31OU7Z7xKHkuFR7DzBc";

        return url;
    }

    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }



    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            PolylineOptions lineOptions = null;
            for (int i = 0; i < result.size(); i++) {
                Log.e("sfgsdhfsdfsq",result.toString());
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                if (polyline != null) {
                    polyline.remove();
                }
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.getJointType();
                lineOptions.color(getResources().getColor(R.color.colorPrimary));
                googleMap.addPolyline(lineOptions);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (LatLng point : points) {
                    builder.include(point);
                }

                LatLngBounds bounds = builder.build();
                int padding = 20; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
                        padding);
                googleMap.moveCamera(cu);
                googleMap.animateCamera(cu, 1800, null);
            }
        }
    }

    public void orderComplete() {
        progress.setVisibility(View.VISIBLE);
        getUserID = SharedHelper.getKey(ShowRouteActivity.this, AppConstats.USER_ID);
        getOrderID = SharedHelper.getKey(ShowRouteActivity.this, CLIENT_ORDER_ID);

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

                       Dialog dialog = new Dialog(ShowRouteActivity.this);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.completed_dialog);

                        Button dialogButton = (Button) dialog.findViewById(R.id.btDone);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                startActivity(new Intent(ShowRouteActivity.this,NavigationActivity.class));
                            }
                        });

                        dialog.show();



                        Toast.makeText(ShowRouteActivity.this, "Order Delivered Successfully", Toast.LENGTH_SHORT).show();

                      //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
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
}