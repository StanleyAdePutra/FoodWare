package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class frag_profile extends Fragment {

    ProgressBar progressBar;
    TextView email, nama, alamat, telp, tglLahir;
    Button editprofile, logout;
    String userEmail, namaUser, alamatUser, telpUser, tglLahirUser;
    String url_get_customer = "https://000foodware000.000webhostapp.com/getCustomer.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_frag_profile,container,false);

        progressBar = view.findViewById(R.id.progressBarProfile);
        email = view.findViewById(R.id.profileemail);
        nama = view.findViewById(R.id.profilenama);
        alamat = view.findViewById(R.id.profilealamat);
        telp = view.findViewById(R.id.profiletelp);
        tglLahir = view.findViewById(R.id.profiletgllahir);
        editprofile = view.findViewById(R.id.btnedit);
        logout = view.findViewById(R.id.btnlogout);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userEmail = sharedPreferences.getString("user_email", "");
        Log.d("SharedPreferences", "user_email retrieved: " + userEmail);

        showProfile();

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), edit_profile.class);
                i.putExtra("nama", namaUser);
                i.putExtra("alamat", alamatUser);
                i.putExtra("telp", telpUser);
                i.putExtra("tglLahir", tglLahirUser);
                startActivityForResult(i, 1);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("user_email");
                editor.apply();

                LoginManager loginManager = new LoginManager(getContext());
                loginManager.open();
                loginManager.clearUserData();
                loginManager.close();

                Intent i = new Intent(getActivity(), landing.class);
                startActivity(i);

                getActivity().finishAffinity();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                showProfile();
            }
        }
    }

    public void showProfile(){
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_get_customer + "?id=" + userEmail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {
                                JSONObject jObj = jsonArray.getJSONObject(0);

                                namaUser = jObj.getString("Username");
                                alamatUser = jObj.getString("Address");
                                telpUser = jObj.getString("Phone");
                                tglLahirUser = jObj.getString("Birth_Date");

                                email.setText(userEmail);
                                nama.setText(namaUser);
                                alamat.setText(alamatUser);
                                telp.setText(telpUser);
                                tglLahir.setText(tglLahirUser);
                            } else {
                                Toast.makeText(getContext(), "Array JSON kosong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            Log.e("Error", "JSON parsing error: " + ex.toString());
                            Toast.makeText(getContext(), "Terjadi kesalahan saat mengolah data JSON", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(getContext(), "Gagal mendapatkan detail Produk", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
            }
        });

        queue.add(stringRequest);
    }
}