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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;

public class frag_home extends Fragment {

    RecyclerView rv;
    ArrayList<HashMap<String, String>> list_product;
    String userEmail;
    TextView welcome;
    ProgressBar progressBar;
    String url_get_product = "https://000foodware000.000webhostapp.com/getProduct.php";
    String url_get_customer = "https://000foodware000.000webhostapp.com/getCustomer.php";
    private static final String TAG_ID = "ID_Product";
    private static final String TAG_NAMA = "Name_Product";
    private static final String TAG_IDCAT = "ID_Category";
    private static final String TAG_PRICE = "Price_Product";
    private static final String TAG_DESC = "Description_Product";
    private static final String TAG_IMG = "Product_Image";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_home, container, false);

        Button map = view.findViewById(R.id.btnMap);
        EditText search = view.findViewById(R.id.searchbar);
        welcome = view.findViewById(R.id.namaUser);
        list_product = new ArrayList<>();
        rv = view.findViewById(R.id.recycleView);
        progressBar = view.findViewById(R.id.progressBar);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        rv.setAdapter(new AdapterHome(new ArrayList<>(), getContext()));

        AdapterHome adapter = new AdapterHome(new ArrayList<>(), getContext());
        rv.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userEmail = sharedPreferences.getString("user_email", "");
        Log.d("SharedPreferences", "user_email retrieved: " + userEmail);

        displayName();
        displayProduct();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (rv.getAdapter() instanceof AdapterHome) {
                    ((AdapterHome) rv.getAdapter()).filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), mapActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    private void onItemClickHandler(int position) {
        HashMap<String, String> clickedItem = list_product.get(position);
        String id = clickedItem.get("id");

        Intent intent = new Intent(getContext(), detail_product.class);

        intent.putExtra("id", id);

        startActivity(intent);
    }

    public void displayName(){
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_get_customer + "?id=" + userEmail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {
                                JSONObject jObj = jsonArray.getJSONObject(0);

                                if (jObj.has("Username")) {
                                    String Username = jObj.getString("Username");

                                    welcome.setText("Welcome, " + Username+ " !");
                                } else {
                                    Toast.makeText(getContext(), "Kunci tidak ditemukan dalam objek JSON", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Array JSON kosong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            Log.e("Error", "JSON parsing error: " + ex.toString());
                            Toast.makeText(getContext(), "Terjadi kesalahan saat mengolah data JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(getContext(), "Gagal mendapatkan nama user", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    public void displayProduct(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_get_product,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            list_product.clear(); // Clear the list before adding new items
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject a = jsonArray.getJSONObject(i);

                                String id = a.optString(TAG_ID, "");
                                String img = a.optString(TAG_IMG, "");
                                String nama = a.optString(TAG_NAMA, "");
                                String desc = a.optString(TAG_DESC, "");
                                String harga = a.optString(TAG_PRICE, "");

                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", id);
                                map.put("img", img);
                                map.put("nama", nama);
                                map.put("desc", desc);
                                map.put("harga", harga);
                                list_product.add(map);
                            }

                            progressBar.setVisibility(View.GONE);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Set the adapter and update UI components here
                                    AdapterHome adapter = new AdapterHome(list_product, getContext());
                                    rv.setAdapter(adapter);

                                    rv.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            onItemClickHandler(position);
                                        }
                                    }));
                                }
                            });
                        } catch (JSONException ex) {
                            Log.e("Error", "JSON parsing error: " + ex.toString());
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(getContext(), "Silahkan cek koneksi internet "+
                        "Anda!", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}