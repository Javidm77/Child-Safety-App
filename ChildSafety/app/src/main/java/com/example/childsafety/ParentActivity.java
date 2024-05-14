package com.example.childsafety;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

public class ParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);


    }

    public void click(View view) {
        startActivity(new Intent(ParentActivity.this, ParentHome.class));
    }

    public void the(View view) {
        startActivity(new Intent(ParentActivity.this,Profile.class));
    }

    public void thed(View view) {
        new AlertDialog.Builder(ParentActivity.this)
                .setTitle("Do you Want Log out!!")
                .setIcon(R.drawable.shutdown)
                .setMessage("Press ->'Yes' to Log out \n(or) \nPress ->'No' to stay")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ParentActivity.this,LoginPage.class));
                        getSharedPreferences("user",MODE_PRIVATE).edit().clear().apply();
                        finishAffinity();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }
}