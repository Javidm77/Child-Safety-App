package com.example.childsafety;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.childsafety.Child.loca;
import static com.example.childsafety.Child.mobs;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    static  double longitude,latitude;
    double lat = 13.626256, lon = 79.431062;
    String msg;
    double dist,d;
    private static  final String URL="https://wizzie.online/EmployeeTracking/addlocations.php";


    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(final Context context, Intent intent) {
        TrackGPS gps = new TrackGPS(context);
        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
            latitude = gps.getLatitude();

            Toast.makeText(context,"Your Location Accessed", Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, ""+longitude+""+latitude, Toast.LENGTH_SHORT).show();

           // Toast.makeText(context, ""+d, Toast.LENGTH_SHORT).show();

String fg="";
for (int i2=0;i2<loca.size();i2++) {
    fg = loca.get(i2);


    try {
        Address ds= new Geocoder(context).getFromLocationName(fg,1).get(0);
Location locationA=new Location("A");
locationA.setLatitude(ds.getLatitude());
locationA.setLongitude(ds.getLongitude());
Location locationB=new Location("B");
locationB.setLongitude(longitude);
locationB.setLatitude(latitude);
        float ff = locationB.distanceTo(locationA)/1000;

        if ( ff< 50) {

            try {
                String mob = "";
                NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_baseline_message_24)
                        .setContentTitle("Alert Notifications ")
                        .setContentText("Notification From Child Safety..");

                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, builder.build());


                for (int i = 0; i < mobs.size(); i++) {
                    mob = (String) mobs.get(i);

                    msg = "U r child in out of Fencing..!" + " " + "https://www.google.com/maps?saddr=&daddr=" + latitude + "," + longitude;

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(mob, null, msg, null, null);

                }
                Toast.makeText(context, "Sent", Toast.LENGTH_LONG).show();

            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }


        }
    } catch (IOException e) {
        e.printStackTrace();
    }

}

        }
        else {
            gps.showSettingsAlert();
        }

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "s ->"+response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"e ->"+error,Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("lo",String.valueOf(longitude).trim());
                params.put("la",String.valueOf(latitude).trim());
                params.put("mail",context.getSharedPreferences("user",Context.MODE_PRIVATE).getString("parent",""));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


       WakeLocker.release();
    }



/*
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return dist;
    }
*/

    /*private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
*/
    /*private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }*/


}
