package com.example.childsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpPage extends AppCompatActivity {
TextInputEditText pname,pmail,ppassword,mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        pname=findViewById(R.id.pname);
        pmail=findViewById(R.id.pmail);
        ppassword=findViewById(R.id.ppassword);
        mobile=findViewById(R.id.pmobile);

        Button btn=findViewById(R.id.create);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(SignUpPage.this);
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Creating");
                progressDialog.setMessage("\uD83D\uDE42...................");
                final String pname1= Objects.requireNonNull(pname.getText()).toString();
                final String pmail1= Objects.requireNonNull(pmail.getText()).toString();
                final String ppassword1= Objects.requireNonNull(ppassword.getText()).toString();
                final String mobile1= Objects.requireNonNull(mobile.getText()).toString();
                if(pname1.isEmpty()||
                pmail1.isEmpty()||
                        ppassword1.isEmpty()||
                mobile1.isEmpty()){
                    Toast.makeText(SignUpPage.this, "Empty", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Method.POST, "https://wizzie.online/EmployeeTracking/signup.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(SignUpPage.this, response, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            if(Objects.equals(response, "Already Exists")){finish();}
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignUpPage.this, "Error  ->" + error, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("name", pname1);
                            params.put("mail", pmail1);
                            params.put("password", ppassword1);
                            params.put("mobile", mobile1);
                            params.put("type", "parent");
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(SignUpPage.this);
                    requestQueue.add(stringRequest);

                }            }
        });

    }
}