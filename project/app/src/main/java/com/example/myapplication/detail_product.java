package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class detail_product extends AppCompatActivity {

    ProgressBar progressBar;
    TextView nama, harga, desc, plus, minus, qty;
    ImageView img, back;
    Button toCart;
    String idProduct;
    String url_get_product = "https://000foodware000.000webhostapp.com/getProductbyId.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        progressBar = findViewById(R.id.progressBarDet);
        nama = findViewById(R.id.namaProduk);
        harga = findViewById(R.id.hargaProduk);
        desc = findViewById(R.id.deskripsiProduk);
        img = findViewById(R.id.ftomakanan);
        back = findViewById(R.id.btnbackproduct);
        toCart = findViewById(R.id.buyItem);
        plus = findViewById(R.id.btnplus);
        minus = findViewById(R.id.btnminus);
        qty = findViewById(R.id.qtyitem);

        idProduct = getIntent().getStringExtra("id");
        Log.d("Detail Product", "ID Product: " + idProduct);

        RequestQueue queue = Volley.newRequestQueue(detail_product.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_get_product + "?id=" + idProduct,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {
                                JSONObject jObj = jsonArray.getJSONObject(0);

                                if (jObj.has("Name_Product") && jObj.has("Price_Product") && jObj.has("Description_Product")) {
                                    String namaProduk = jObj.getString("Name_Product");
                                    String hargaProduk = jObj.getString("Price_Product");
                                    String deskripsiProduk = jObj.getString("Description_Product");
                                    String gambarProdukUrl = jObj.getString("Product_Image").replace("%20", " ");

                                    // Tampilkan data di TextView
                                    nama.setText(namaProduk);
                                    harga.setText("Rp. "+hargaProduk);
                                    desc.setText(deskripsiProduk);

                                    // Load gambar menggunakan Glide
                                    Glide.with(detail_product.this)
                                            .load(gambarProdukUrl)
                                            .into(img);
                                } else {
                                    Toast.makeText(detail_product.this, "Kunci tidak ditemukan dalam objek JSON", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(detail_product.this, "Array JSON kosong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            Log.e("Error", "JSON parsing error: " + ex.toString());
                            Toast.makeText(detail_product.this, "Terjadi kesalahan saat mengolah data JSON", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(detail_product.this, "Gagal mendapatkan detail Produk", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
            }
        });

        queue.add(stringRequest);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("toCartClickListener", "Nama Produk: " + nama.getText().toString());
                Log.d("toCartClickListener", "Harga Produk: " + harga.getText().toString());
                Log.d("toCartClickListener", "Qty: " + qty.getText().toString());

                Intent i = new Intent(getApplicationContext(), order.class);
                i.putExtra("id",idProduct);
                i.putExtra("produk", nama.getText().toString());
                i.putExtra("harga", harga.getText().toString());
                i.putExtra("qty", qty.getText().toString());
                startActivity(i);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityEditor.increaseQuantity(qty);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityEditor.decreaseQuantity(qty);
            }
        });
    }
}