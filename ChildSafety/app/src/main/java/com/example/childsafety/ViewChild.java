package com.example.childsafety;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arsy.maps_library.MapRipple;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewChild extends AppCompatActivity implements OnMapReadyCallback {

    private static final String URL = "https://wizzie.online/EmployeeTracking/getFrence.php";
    ArrayList<String> lon = new ArrayList<>();
    ArrayList<String> lat = new ArrayList<>();
    LatLng latLng;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    MapRipple mapRipple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_child);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        getData();

    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                       //   Toast.makeText(ViewChild.this, ""+response, Toast.LENGTH_SHORT).show();

                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                lon.add(jsonObject.getString("longitude"));
                                lat.add(jsonObject.getString("latitude"));

                            //    Toast.makeText(ViewChild.this, "" + lon.size(), Toast.LENGTH_SHORT).show();


                                // Toast.makeText(ViewChild.this, ""+jsonObject.getString("longitude")+""+jsonObject.getString("latitude"), Toast.LENGTH_SHORT).show();
                                latLng = new LatLng(Double.parseDouble(lat.get(0).toString()), Double.parseDouble(lon.get(0).toString()));
                                Toast.makeText(ViewChild.this, "" + latLng, Toast.LENGTH_SHORT).show();


                                if(lat!=null)
                                {
                                    double dLatitude = Double.parseDouble(String.valueOf(lat.get(0)));
                                    double dLongitude = Double.parseDouble(String.valueOf(lon.get(0)));
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(dLatitude, dLongitude))
                                            .title("My Location").icon(BitmapDescriptorFactory
                                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 8));


                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Unable to fetch the  location", Toast.LENGTH_SHORT).show();
                                }


                                //Toast.makeText(ViewChild.this, ""+longi.get(i).toString()+""+latti.get(i).toString(), Toast.LENGTH_SHORT).show();




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mail",getSharedPreferences("user",MODE_PRIVATE).getString("mail",""));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(stringRequest);

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        setMap();


       /* for(int i=0;i<lat.size();i++){
            latLng = new LatLng(Double.parseDouble(lat.get(i).toString()),Double.parseDouble(lon.get(i).toString()));
            mMap.addMarker(new MarkerOptions().position(latLng).title("location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
*/


    }

    @SuppressLint("MissingPermission")
    private void setMap() {

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    }
}