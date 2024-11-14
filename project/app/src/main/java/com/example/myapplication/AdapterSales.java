package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterSales extends RecyclerView.Adapter<AdapterSales.ViewHolder> {
    private ArrayList<HashMap<String, String>> salesList;
    private Context context;

    public AdapterSales(ArrayList<HashMap<String, String>> salesList, Context context) {
        this.salesList = salesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cartlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> product = salesList.get(position);
        holder.namaTxt.setText(product.get("produk"));
        holder.dateTxt.setText(product.get("date"));
        holder.qtyTxt.setText(product.get("qty"));
        holder.totalTxt.setText("Rp. "+product.get("total"));

        String imageUrl = product.get("img");
        Glide.with(context)
                .load(imageUrl)
                .into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView namaTxt, dateTxt, qtyTxt, totalTxt;
        ImageView gambar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            namaTxt=itemView.findViewById(R.id.namaProdukcart);
            dateTxt=itemView.findViewById(R.id.tglCart);
            qtyTxt=itemView.findViewById(R.id.qtyProductCart);
            totalTxt=itemView.findViewById(R.id.hargaProdukcart);
            gambar=itemView.findViewById(R.id.fotomakanancart);
        }
    }
}