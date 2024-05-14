package com.example.childsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView textView2=findViewById(R.id.textView2);
                TextView textView3=findViewById(R.id.textView3);
        TextView mobilex=findViewById(R.id.mobilex);
        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        String gg=sharedPreferences.getString("name","");
StringBuilder stringBuilder=new StringBuilder();
        for ( int num=0;num<gg.length();num++){
if(num==0){
    String f= String.valueOf(gg.charAt(num)).toUpperCase(Locale.ROOT);
    stringBuilder.append(f);
}else{
    stringBuilder.append(gg.charAt(num));
}
       }
        textView2.setText(stringBuilder);
        textView3.setText(sharedPreferences.getString("mail",""));
        mobilex.setText(sharedPreferences.getString("mobile",""));
    }
}