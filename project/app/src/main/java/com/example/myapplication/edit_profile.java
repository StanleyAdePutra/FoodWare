package com.example.myapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class edit_profile extends AppCompatActivity {

    ProgressBar progressBar;
    EditText nama, alamat, telp, tglLahir;
    Button save;
    String URL_EDIT = "https://000foodware000.000webhostapp.com/editProfile.php";
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        progressBar = findViewById(R.id.progressBarEdit);
        nama = findViewById(R.id.editnama);
        alamat = findViewById(R.id.editalamat);
        telp = findViewById(R.id.editnotelp);
        tglLahir = findViewById(R.id.edittgllahir);
        ImageView back = (ImageView)findViewById(R.id.btnbackprofile);
        save = findViewById(R.id.submitedit);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userEmail = sharedPreferences.getString("user_email", "");
        Log.d("SharedPreferences", "user_email retrieved: " + userEmail);

        Intent intent = getIntent();
        nama.setText(intent.getStringExtra("nama"));
        alamat.setText(intent.getStringExtra("alamat"));
        telp.setText(intent.getStringExtra("telp"));
        tglLahir.setText(intent.getStringExtra("tglLahir"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker.showDatePickerDialog(edit_profile.this, tglLahir);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = nama.getText().toString().trim();
                final String userAddress = alamat.getText().toString().trim();
                final String userPhone = telp.getText().toString().trim();
                final String userBDate = tglLahir.getText().toString().trim();

                progressBar.setVisibility(View.VISIBLE);

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Setelah parsing response di bagian Response.Listener
                                Log.d("Server Response", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int success = jsonObject.getInt("success");
                                    String message = jsonObject.getString("message");

                                    if (success == 1) {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                        progressBar.setVisibility(View.GONE);

                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                        progressBar.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressBar.setVisibility(View.GONE);
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

                                progressBar.setVisibility(View.GONE);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Email", userEmail);
                        params.put("Username", userName);
                        params.put("Address", userAddress);
                        params.put("Phone", userPhone);
                        params.put("Birth_Date", userBDate);
                        return params;
                    }
                };

                queue.add(stringRequest);
            }
        });
    }
}