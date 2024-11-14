package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class frag_register extends Fragment {

    private static final String URL_REGISTER = "https://000foodware000.000webhostapp.com/register.php";
    EditText email, username, password, address, phone, tglLahir;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_register, container, false);

        progressBar = view.findViewById(R.id.progressBarRegister);
        email = view.findViewById(R.id.edtemailregister);
        password = view.findViewById(R.id.edtpassregister);
        username = view.findViewById(R.id.edtnama);
        address = view.findViewById(R.id.edtalamat);
        phone = view.findViewById(R.id.edttelp);
        tglLahir = view.findViewById(R.id.edttgllahir);
        ImageView seepass = view.findViewById(R.id.seepass);
        Button register = view.findViewById(R.id.btnregister);

        tglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker.showDatePickerDialog(requireContext(), tglLahir);
            }
        });

        seepass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return showPassword.visiblePassword(password, event);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (validateInputs()) {
                    userRegister();
                }
            }
        });

        return view;
    }

    private boolean validateInputs() {
        String userEmail = email.getText().toString().trim();
        String userName = username.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userAddress = address.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();
        String userBDate = tglLahir.getText().toString().trim();

        if (userEmail.isEmpty() || userName.isEmpty() || userPassword.isEmpty()  || userAddress.isEmpty() || userPhone.isEmpty() ||
                userBDate.isEmpty()) {
            Toast.makeText(getContext(), "Isi semua kolom registrasi.", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);
            return false;
        }
        return true;
    }

    private void userRegister() {
        final String userEmail = email.getText().toString().trim();
        final String userName = username.getText().toString().trim();
        final String userPassword = password.getText().toString().trim();
        final String userAddress = address.getText().toString().trim();
        final String userPhone = phone.getText().toString().trim();
        final String userBDate = tglLahir.getText().toString().trim();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            String message = jsonObject.getString("message");

                            if (success == 1) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                                if (getActivity() != null && getActivity() instanceof landing) {
                                    landing landingActivity = (landing) getActivity();
                                    landingActivity.switchToLoginFragment();
                                }
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Log.e("Error: ", error.getMessage());
                            Toast.makeText(getContext(), "Silahkan cek koneksi internet Anda!", Toast.LENGTH_SHORT).show();

                            // Print the response body (if available)
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                String errorResponse = new String(error.networkResponse.data);
                                Log.e("Error Response: ", errorResponse);
                            }
                        } else {
                            Log.e("Error: ", "Unknown error occurred");
                            Toast.makeText(getContext(), "Terjadi kesalahan yang tidak diketahui", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Email", userEmail);
                params.put("Username", userName);
                params.put("Password", userPassword);
                params.put("Address", userAddress);
                params.put("Phone", userPhone);
                params.put("Birth_Date", userBDate);

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    Log.d("Request Parameters", entry.getKey() + ": " + entry.getValue());
                }

                return params;
            }
        };

        queue.add(stringRequest);
    }
}
