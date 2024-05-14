package com.example.childsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewCalls extends AppCompatActivity {
ArrayList<String>data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calls);
        ListView listView=findViewById(R.id.list_items);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://www.wizzie.online/EmployeeTracking/getdata.php", response -> {

            try {
                JSONArray jsonArray=new JSONArray(response);
             for (int i=0;i<jsonArray.length();i++){
                 JSONObject jsonObject=jsonArray.getJSONObject(i);
                 String string="";
                if(getIntent().getStringExtra("condition").equals("calls")){
                data.add("Number : "+jsonObject.getString("number")+"\n"+"Duration :"+jsonObject.getString("duration")
                    +"\n"+"Date :"+jsonObject.getString("date"));
                }else{
data.add("Date :"+jsonObject.getString("date")+"\n"+"Number :"+jsonObject.getString("number")+"\n"+"Message :"+jsonObject.getString("body"));
                }

                 ArrayAdapter<String> adapt=new ArrayAdapter<>(ViewCalls.this, android.R.layout.simple_list_item_1,data);
                 listView.setAdapter(adapt);
                 adapt.notifyDataSetChanged();
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {


        }){
            @Override
            public HashMap<String, String> getParams() {
                HashMap<String,String> params=new HashMap<>();
                params.put("parent",getSharedPreferences("user",MODE_PRIVATE).getString("mail",""));
                params.put("conidition",getIntent().getStringExtra("condition"));
                params.put("mail",getIntent().getStringExtra("name"));
                return params;
            }

        };
        RequestQueue requestQueue= Volley.newRequestQueue(ViewCalls.this);
        requestQueue.add(stringRequest);
    }
}