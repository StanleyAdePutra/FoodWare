package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class frag_cart extends Fragment {

    RecyclerView rv;
    String userEmail;
    ProgressBar progressBar;
    ArrayList<HashMap<String, String>> list_sales;
    String url_get_sales = "https://000foodware000.000webhostapp.com/getSalesbyUser.php";
    String deleteUrl = "https://000foodware000.000webhostapp.com/deleteSalesbyUser.php";

    private static final String TAG_ID = "ID_Sales";
    private static final String TAG_PROD = "Name_Product";
    private static final String TAG_DATE = "Transaction_date";
    private static final String TAG_PRICE = "Total_price";
    private static final String TAG_QTY = "Product_qty";
    private static final String TAG_IMG = "Product_Image";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_cart, container, false);

        list_sales = new ArrayList<>();
        rv = view.findViewById(R.id.recycleViewSales);
        progressBar = view.findViewById(R.id.progressBarSales);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        rv.setAdapter(new AdapterSales(new ArrayList<>(), getContext()));
        Button delete = view.findViewById(R.id.btnclearcart);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userEmail = sharedPreferences.getString("user_email", "");
        Log.d("SharedPreferences", "user_email retrieved: " + userEmail);

        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_get_sales + "?user_email=" + userEmail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            list_sales.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject a = jsonArray.getJSONObject(i);

                                String id = a.optString(TAG_ID, "");
                                String prod = a.optString(TAG_PROD, "");
                                String date = a.optString(TAG_DATE,"");
                                String price = a.optString(TAG_PRICE, "");
                                String qty = a.optString(TAG_QTY, "");
                                String img = a.optString(TAG_IMG, "");

                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", id);
                                map.put("produk", prod);
                                map.put("date",date);
                                map.put("qty", qty);
                                map.put("total", price);
                                map.put("img", img);
                                list_sales.add(map);
                            }

                            progressBar.setVisibility(View.GONE);

                            AdapterSales adapter = new AdapterSales(list_sales, getContext());
                            rv.setAdapter(adapter);

                        } catch (JSONException ex) {
                            Log.e("Error", "JSON parsing error: " + ex.toString());
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(getContext(), "Silahkan cek koneksi internet Anda!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        queue.add(stringRequest);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                RequestQueue queue = Volley.newRequestQueue(getContext());
                StringRequest deleteRequest = new StringRequest(Request.Method.POST, deleteUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle the response, e.g., show a message
                                Log.d("Delete Response", response);
                                Toast.makeText(getContext(), "All sales data have been cleared!", Toast.LENGTH_SHORT).show();

                                list_sales.clear();
                                AdapterSales adapter = new AdapterSales(list_sales, getContext());
                                rv.setAdapter(adapter);

                                progressBar.setVisibility(View.GONE);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error: ", error.getMessage());
                        Toast.makeText(getContext(), "Failed to delete sales data!", Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_email", userEmail);
                        return params;
                    }
                };

                queue.add(deleteRequest);
            }
        });

        return view;
    }
}