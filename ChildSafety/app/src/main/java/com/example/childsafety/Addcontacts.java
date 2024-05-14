package com.example.childsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Addcontacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontacts);

        final TextInputEditText mob=findViewById(R.id.mobliec);
        final TextInputEditText nam=findViewById(R.id.namec);

        Button add=findViewById(R.id.addcontact);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name= Objects.requireNonNull(nam.getText()).toString();
                final String mail= Objects.requireNonNull(mob.getText()).toString();
                final ProgressDialog progressBar=new ProgressDialog(getApplicationContext());
                progressBar.setCancelable(false);
                progressBar.setTitle("Wait While we Add -> \uD83D\uDE42");
                if(name.isEmpty()||mail.isEmpty()
                ){
                    Toast.makeText(Addcontacts.this, "Empty", Toast.LENGTH_SHORT).show();
                }else{
                    //progressBar.show();
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://wizzie.online/EmployeeTracking/addcontact.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(Addcontacts.this, "Success ->"+response, Toast.LENGTH_SHORT).show();
                            //  progressBar.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Addcontacts.this, "Error ->"+error, Toast.LENGTH_SHORT).show();
                            // progressBar.dismiss();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> params=new HashMap<>();
                            SharedPreferences fs=getSharedPreferences("user",MODE_PRIVATE);
                            params.put("name",nam.getText().toString().trim());
                            params.put("mobile",mob.getText().toString().trim());
                            params.put("email",fs.getString("mail","dd"));
                            params.put("parent",fs.getString("parent",""));
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

