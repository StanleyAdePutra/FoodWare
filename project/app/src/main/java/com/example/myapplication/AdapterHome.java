package com.example.myapplication;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
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

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {
    private ArrayList<HashMap<String, String>> productList;
    private ArrayList<HashMap<String, String>> originalList;
    private Context context;

    public AdapterHome(ArrayList<HashMap<String, String>> productList, Context context) {
        this.productList = productList;
        this.originalList = new ArrayList<>(productList);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_productlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> product = productList.get(position);
        holder.namaTxt.setText(product.get("nama"));
        holder.descTxt.setText(product.get("desc"));
        holder.hargaTxt.setText("Rp. "+product.get("harga"));

        String imageUrl = product.get("img");
        Glide.with(context)
                .load(imageUrl)
                .into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView namaTxt, descTxt, hargaTxt;
        ImageView gambar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            namaTxt=itemView.findViewById(R.id.namaProduklist);
            descTxt=itemView.findViewById(R.id.descProdukList);
            hargaTxt=itemView.findViewById(R.id.hargaProduklist);
            gambar=itemView.findViewById(R.id.imgMakananList);
        }
    }

    public void filter(String query) {
        productList.clear();

        if (TextUtils.isEmpty(query)) {
            productList.addAll(originalList);
        } else {
            query = query.toLowerCase().trim();
            for (HashMap<String, String> item : originalList) {
                if (item.get("nama").toLowerCase().contains(query)) {
                    productList.add(item);
                }
            }
        }

        notifyDataSetChanged();
        Log.d("AdapterHome", "ProductList size after notifyDataSetChanged: " + productList.size());
    }
}
