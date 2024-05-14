package com.example.childsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void parent(View view) {
        startActivity(new Intent(getApplicationContext(),ParentHome.class));
    }

    public void child(View view) {
        startActivity(new Intent(getApplicationContext(),Child.class));

    }
}