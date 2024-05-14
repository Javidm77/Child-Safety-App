package com.example.childsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

public class Splash2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        SharedPreferences sh=getSharedPreferences("user",MODE_PRIVATE);
        String type=sh.getString("type","");


        new Handler().postDelayed(() -> {
            if(type.equals("parent")){
                startActivity(new Intent(getApplicationContext(),ParentActivity.class));
                finish();
            }else if(type.equals("child")){
                startActivity(new Intent(getApplicationContext(),Child.class));
                finish();
            }else{
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finish();
            }},2000);

    }
}