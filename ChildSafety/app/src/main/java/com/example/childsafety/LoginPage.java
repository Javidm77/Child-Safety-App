package com.example.childsafety;

import
        androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginPage extends AppCompatActivity {
EditText semail,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        semail=findViewById(R.id.semail);
        pass=findViewById(R.id.pass);
    }


    public void login(View view) {
        ProgressDialog progressDialog=new ProgressDialog(LoginPage.this);
        progressDialog.setTitle("\uD83D\uDE42");
        progressDialog.setMessage("Wait a Moment!!!");
        progressDialog.setCancelable(false);
        String email=semail.getText().toString();

        String password=pass.getText().toString();
        if(email.isEmpty()||
                password.isEmpty()
        ){
            Toast.makeText(LoginPage.this, "Empty", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.show();
            StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://wizzie.online/EmployeeTracking/parentlogin.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

                    //    Toast.makeText(LoginPage.this, ""+response, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonArray=new JSONObject(response);
                        SharedPreferences.Editor shared=getSharedPreferences("user",MODE_PRIVATE).edit();
                        shared.putString("name",jsonArray.getString("name"));
                        shared.putString("mail",jsonArray.getString("mail"));
                        shared.putString("password",jsonArray.getString("password"));
                        shared.putString("mobile",jsonArray.getString("mobile"));
                        String ff=jsonArray.getString("type");
                        shared.putString("type",ff);

                        if(Objects.equals(ff,"parent")){
                            startActivity(new Intent(LoginPage.this,ParentActivity.class));
                            finishAffinity();
                        }else if(Objects.equals(ff,"child")){
                            shared.putString("parent",jsonArray.getString("parent"));
                            shared.putString("mobile",jsonArray.getString("mobile"));
                            startActivity(new Intent(getApplicationContext(),Child.class));
                            finishAffinity();
                            Toast.makeText(LoginPage.this, ""+jsonArray.getString("mobile"), Toast.LENGTH_SHORT).show();
                        }
                        shared.apply();

                    } catch (JSONException e) {
                        Toast.makeText(LoginPage.this, "Errorr ->"+e, Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginPage.this, "Error ->"+error, Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap <String,String> params=new HashMap<>();
                    params.put("mail",email);
                    params.put("pass",password);
                    return params;}
            };
            RequestQueue requestQueue= Volley.newRequestQueue(LoginPage.this);
            requestQueue.add(stringRequest);
        }
    }

    public void method(View view) {
        TextView button=findViewById(view.getId());
        view.setAlpha(0f);
        view.animate().alpha(1f).setDuration(500).withEndAction(() -> {
            button.setTextColor(Color.WHITE);
            Intent intent=new Intent(LoginPage.this,SignUpPage.class);
            startActivity(intent);
        }).withStartAction(() -> button.setTextColor(Color.BLUE));

    }
}