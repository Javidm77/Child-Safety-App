package com.example.childsafety;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Telephony;

import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Child extends AppCompatActivity   {

    PendingIntent pendingIntent;
    SwitchCompat switchCompat;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    static ArrayList<String> loca=new ArrayList<>();
    static ArrayList<String> mobs=new ArrayList<>();
    static ArrayList<String> mail=new ArrayList<>();
    private static final String URL1="https://wizzie.online/EmployeeTracking/getFrence.php";
    String phoneNo;
    String message;
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;
    RecyclerView rcvvc;
    ArrayList<String>name=new ArrayList<>();
    ArrayList<String> mobile=new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        getFence();
       switchCompat=findViewById(R.id.seitvh);

        rcvvc = (RecyclerView)findViewById(R.id.listphones);
        final LinearLayout layout=findViewById(R.id.layout1);
        final ProgressBar progressBar=findViewById(R.id.viisb);
        layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        @SuppressLint("Recycle")
        Cursor cursor=getContentResolver().query(Telephony.Sms.CONTENT_URI,null,null,null);
  if (cursor.moveToFirst()){
            for (int j=0;j<cursor.getCount();j++){
            String smsDate= cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE));
            String number=cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
            String body=cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
            Date date=new Date(Long.parseLong(smsDate));
            sendmessages(number,date,body);
            cursor.moveToNext();
            if(j==6){
                break;
            }
    }
}
String[] projection=new String[]{
 CallLog.Calls.CACHED_NAME,
        CallLog.Calls.NUMBER,
        CallLog.Calls.TYPE,
        CallLog.Calls.DATE,
        CallLog.Calls.DURATION

};
         Cursor cursor1=getContentResolver().query(CallLog.Calls.CONTENT_URI,projection,null,null);
         int num=0;
       while (cursor1.moveToNext()){
            String pnumber=cursor1.getString(1);
            String ctype=cursor1.getString(2);
            String namea= cursor1.getString(0);
            String cdate=cursor1.getString(3);
            Date date=new Date(Long.parseLong(cdate));
             String duration=cursor1.getString(4);
             String types="";
             int code=Integer.parseInt(ctype);
             switch (code){
                 case CallLog.Calls.OUTGOING_TYPE:
                     types="OUT GOING";
                     break;
                     case  CallLog.Calls.INCOMING_TYPE:
                     types="INCOMING";
                     break;
                 case CallLog.Calls.MISSED_TYPE:
                     types="MISSED CALL";
                     break;
             }

if(num==6){
    break
            ;
}
           sendCalls(namea,pnumber,duration,date,types);

           num++;
         /*    string.add(""+name+pnumber+ctype+cdate);*/
        }
        cursor1.close();
      //  Toast.makeText(this, ""+cursor1.getCount(), Toast.LENGTH_SHORT).show();;


        float batteryPct = level / (float)scale;
        float i=  batteryPct*100;
      if(i<=20){
            message="your children phone having less battery"+i+"%";
            phoneNo= getSharedPreferences("user",MODE_PRIVATE) .getString("mobile","");
            sendSMSMessage();
        }

        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://wizzie.online/EmployeeTracking/getcontacts.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray=new JSONArray(response);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    name.add(jsonObject.getString("name"));
                    mobile.add(jsonObject.getString("mobile"));
                }
//                    Toast.makeText(Child.this, ""+name+mobile, Toast.LENGTH_SHORT).show();
                rcvvc.setLayoutManager(new LinearLayoutManager(Child.this));
                rcvvc.setAdapter(new Adapter(Child.this,name,mobile));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Child.this, ""+error, Toast.LENGTH_SHORT).show();
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("mail",getSharedPreferences("user",MODE_PRIVATE).getString("mail",""));
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(Child.this);
        requestQueue.add(stringRequest);

    //    Toast.makeText(this, ""+i+phoneNo, Toast.LENGTH_SHORT).show();

        String ff=getSharedPreferences("check",MODE_PRIVATE).getString("check","");
        if(ff.equals("true")){
            switchCompat.setChecked(true);
        }else{
            switchCompat.setChecked(false);

        }
switchCompat.setOnClickListener(v -> {
    if(switchCompat.isChecked()){
        start(switchCompat);
    }else{
        stop(switchCompat);
    }
});



    }

    private void sendCalls(String namea, String pnumber, String duration, Date date, String types) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://www.wizzie.online/EmployeeTracking/addcalls.php", response -> {

        }, error -> {

        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                SharedPreferences g=getSharedPreferences("user",MODE_PRIVATE);
                String d=namea;
                if(d==null){
                    d="NON";
                }
                params.put("mail",g.getString("mail",""));
                params.put("parent",g.getString("parent",""));
                params.put("date",date.toString());
                params.put("number",pnumber);
                params.put("name",d);
                params.put("duration",duration);
                params.put("type",types);

                return params;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(Child.this);

        requestQueue.add(stringRequest);


    }

    private void sendmessages(String number, Date date, String body) {
StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://www.wizzie.online/EmployeeTracking/addmessage.php", response -> {}, error -> {/* *//*Toast.makeText(Child.this, "error->"+error.getMessage()*//*, Toast.LENGTH_SHORT).show()*/}){
    @Override
    protected Map<String, String> getParams() {
        HashMap<String,String> params=new HashMap<>();
        SharedPreferences g=getSharedPreferences("user",MODE_PRIVATE);
        params.put("mail",g.getString("mail",""));
        params.put("parent",g.getString("parent",""));
        params.put("date",date.toString());
        params.put("number",number);
        params.put("body",body);

        return params;
    }
};
RequestQueue requestQueue=Volley.newRequestQueue(Child.this);

requestQueue.add(stringRequest);
    }

    private void sendSMSMessage() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    private void getFence() {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL1,
                response -> {

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            loca.add(jsonObject.getString("loc"));
                            mobs.add(jsonObject.getString("mob"));
                            mail.add(jsonObject.getString("mail"));
                        }

                     // Toast.makeText(Child.this, ""+mobs, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                       // Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mail",getSharedPreferences("user",MODE_PRIVATE).getString("parent",""));
                params.put("con","con");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(stringRequest);

    }


    public void start(View view) {
        switchCompat.setChecked(true);
        getSharedPreferences("check",MODE_PRIVATE).edit().putString("check","true").apply();
        Intent alarmIntent = new Intent(Child.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(Child.this, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 60 * 1000;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_SHORT).show();

    }


    public void stop(View view) {
        switchCompat.setChecked(false);
        getSharedPreferences("check",MODE_PRIVATE).edit().putString("check","false").apply();
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getApplicationContext(), "Stop", Toast.LENGTH_LONG).show();
       //  getApplicationContext().startActivity(new Intent(getApplicationContext(), MainActivity.class));
      }



    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.grantResults = grantResults;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    public void contact(View view) {
        startActivity(new Intent(getApplicationContext(),Addcontacts.class));;
    }
}