package com.example.childsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddChildren extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_children);
        final TextInputEditText cname=findViewById(R.id.cname);
        final TextInputEditText cmail=findViewById(R.id.cmail);
        final TextInputEditText cpassword=findViewById(R.id.cpassword);
        final TextInputEditText cmobile=findViewById(R.id.cmobile);
        Button ccreate=findViewById(R.id.ccreate);
       ccreate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final String name= Objects.requireNonNull(cname.getText()).toString();
               final String mail= Objects.requireNonNull(cmail.getText()).toString();
               final String password= Objects.requireNonNull(cpassword.getText()).toString();
               //final String mobile= Objects.requireNonNull(cmobile.getText()).toString();
               final ProgressDialog progressBar=new ProgressDialog(getApplicationContext());
               progressBar.setCancelable(false);
               progressBar.setTitle("Wait While we Add -> \uD83D\uDE42");
               if(name.isEmpty()||
                       mail.isEmpty()||
                       password.isEmpty()
                     ){
                   Toast.makeText(AddChildren.this, "Empty", Toast.LENGTH_SHORT).show();
               }else{
                   //progressBar.show();
                   StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://wizzie.online/EmployeeTracking/signup.php", new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           Toast.makeText(AddChildren.this, "Success ->"+response, Toast.LENGTH_SHORT).show();
                         //  progressBar.dismiss();
                       }
                   }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           Toast.makeText(AddChildren.this, "Error ->"+error, Toast.LENGTH_SHORT).show();
                         // progressBar.dismiss();
                       }
                   }){
                       @Override
                       protected Map<String, String> getParams() throws AuthFailureError {
                           HashMap<String,String> params=new HashMap<>();
                            SharedPreferences fs=getSharedPreferences("user",MODE_PRIVATE);
                           String f=fs.getString("mail","dd");
                            String mod=fs.getString("mobile","");
                           params.put("child","child");
                           params.put("name",name);
                           params.put("mail",mail);
                           params.put("password",password);
                           params.put("mobile",mod);
                           params.put("type","child");
                           params.put("parent",f);
                           return params;
                       }
                   };
                   RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                   requestQueue.add(stringRequest);
               }

           }
       });
    }
}