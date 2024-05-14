package com.example.childsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChildrenActivity extends AppCompatActivity{
ArrayList<String> name=new ArrayList<>();
ArrayList<String> string=new ArrayList<>();
String condition="";
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        ListView recyclerView=findViewById(R.id.link);
String data=getIntent().getStringExtra("val");
if(data!=null){
    condition="messages";
}else{
    condition="calls";
}
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://www.wizzie.online/EmployeeTracking/getchildrens.php", response -> {
            try {
                int num=1;
                JSONArray jsonArray=new JSONArray(response);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    string.add(jsonObject.getString("mail"));
                    name.add("Child "+num+"\n"+jsonObject.getString("mail"));
                    num++;
                }
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(ChildrenActivity.this,android.R.layout.simple_expandable_list_item_1,name);
                recyclerView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
               // Toast.makeText(this, ""+string, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("parent",getSharedPreferences("user",MODE_PRIVATE).getString("mail","nothing"));
                params.put("condition",condition);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(ChildrenActivity.this);
        requestQueue.add(stringRequest);
recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
  //      Toast.makeText(ChildrenActivity.this, ""+string.get(position), Toast.LENGTH_SHORT).show();
        if(!condition.isEmpty()){
/*
            condition="calls";
*/

            intent=new Intent(ChildrenActivity.this,ViewCalls.class);
            intent.putExtra("name",string.get(position));
            intent.putExtra("condition",condition);

            /*  condition="messages";*/
        }
        startActivity(intent);
    }
});
    }


}