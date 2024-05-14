package com.example.childsafety;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParentHome extends AppCompatActivity {

    private static final String URL="https://wizzie.online/EmployeeTracking/addloc.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);

        CardView cardView=findViewById(R.id.chilce);
cardView.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),AddChildren.class)));

    }

    public boolean fence(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ParentHome.this);
        builder.setTitle("Fencing");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
        builder.setView(customLayout);
        final EditText timer=customLayout.findViewById(R.id.location);
        final EditText status=customLayout.findViewById(R.id.mobile);
        status.setClickable(false);
        status.setFocusable(false);
        status.setText(getSharedPreferences("user",MODE_PRIVATE).getString("mobile",""));
       // final Button button=customLayout.findViewById(R.id.ok);

        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addFence(timer.getText().toString().trim(),status.getText().toString().trim());

            }
        });

        // create and show
        // the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;

    }

    private void addFence(final String trim, final String trim1) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
         Toast.makeText(ParentHome.this, ""+response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject=new JSONObject(response);
/*
                    if (jsonObject.getString("result").equalsIgnoreCase("success")){

                        Snackbar.make(ParentHome.this.getWindow().getDecorView().findViewById(android.R.id.content), "Register", Snackbar.LENGTH_SHORT).show();

                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ParentHome.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();
                params.put("loc",trim);
                params.put("mob",trim1);
                params.put("mail",getSharedPreferences("user",MODE_PRIVATE).getString("mail",""));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void view(View view) {

        startActivity(new Intent(getApplicationContext(),ViewChild.class));
    }


    public void next(View view) {
        Intent intent=new Intent(ParentHome.this,ChildrenActivity.class);
        if(view.getId()==R.id.r1){
            intent.putExtra("val","the");
        }
        startActivity(intent);
    }


}