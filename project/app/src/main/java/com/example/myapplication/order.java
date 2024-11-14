package com.example.myapplication;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

public class order extends AppCompatActivity {

    TextView date, name, price, qty, total;
    String idProd, namaProd, hargaProd, qtyProd, userEmail, currentDate, totalPrice, encodedImage;
    Button checkout;
    ImageView paymentPic;
    private static final String URL_CHECKOUT = "https://000foodware000.000webhostapp.com/checkout.php";
    private static final int REQUEST_CODE_PERMISSION = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail = sharedPreferences.getString("user_email", "");
        Log.d("SharedPreferences", "user_email retrieved: " + userEmail);

        ImageView back = (ImageView)findViewById(R.id.btnbackpayment);
        date = findViewById(R.id.transactionDate);
        name = findViewById(R.id.productOrder);
        price = findViewById(R.id.priceOrder);
        qty = findViewById(R.id.qtyOrder);
        total = findViewById(R.id.totalOrder);
        checkout = findViewById(R.id.checkOut);
        paymentPic = findViewById(R.id.paymentPic);

        idProd = getIntent().getStringExtra("id");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        currentDate = sdf.format(new Date());
        date.setText(currentDate);
        namaProd = getIntent().getStringExtra("produk");
        hargaProd = getIntent().getStringExtra("harga");
        qtyProd = getIntent().getStringExtra("qty");

        name.setText(namaProd);
        price.setText(hargaProd);
        qty.setText(qtyProd);

        int harga = Integer.parseInt(hargaProd.replaceAll("[^\\d]", ""));
        int qty = Integer.parseInt(qtyProd);
        int totalValue = harga * qty;

        totalPrice = String.valueOf(totalValue);
        total.setText("Rp. "+totalValue);

        checkPermission();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        paymentPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutProduct();
            }
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (EasyPermissions.hasPermissions(this, READ_EXTERNAL_STORAGE)) {
                // Permission already granted
            } else {
                // Request permission
                EasyPermissions.requestPermissions(this, "This app needs access to your storage.", REQUEST_CODE_PERMISSION,     READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                // Convert Bitmap to Base64 here
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                paymentPic.setImageBitmap(bitmap); // Set the bitmap to ImageView
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkOutProduct(){
        if (encodedImage == null || encodedImage.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Mohon masukkan bukti pembayaran", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Server Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            String message = jsonObject.getString("message");

                            if (success == 1) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Log.e("Error: ", error.getMessage());
                            Toast.makeText(getApplicationContext(), "Silahkan cek koneksi internet Anda!", Toast.LENGTH_SHORT).show();

                            // Print the response body (if available)
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                String errorResponse = new String(error.networkResponse.data);
                                Log.e("Error Response: ", errorResponse);
                            }
                        } else {
                            Log.e("Error: ", "Unknown error occurred");
                            Toast.makeText(getApplicationContext(), "Terjadi kesalahan yang tidak diketahui", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID_Product", idProd);
                params.put("Transaction_date", currentDate);
                params.put("Total_Price", totalPrice);
                params.put("Product_qty", qtyProd);
                params.put("email", userEmail);
                params.put("Payment_Image", encodedImage);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}