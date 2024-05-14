package com.example.childsafety;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    ArrayList<String> name;
    ArrayList<String>   mobile;
    Context context;
    public Adapter(Context context,ArrayList<String>name,ArrayList<String>mobile){
        this.context=context;
        this.name=name;
        this.mobile=mobile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.phone,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.names.setText(name.get(position));
                holder.phones.setText(mobile.get(position));
                holder.imageView.setOnClickListener(v->{



if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
    ActivityCompat.requestPermissions((Activity) context,new String[]{ Manifest.permission.CALL_PHONE},1);

}else {
    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile.get(position))));
}
                });
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView names,
        phones;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        names=itemView.findViewById(R.id.names);
         phones=itemView.findViewById(R.id.phones);
            imageView=itemView.findViewById(R.id.images);
        }
    }
     }