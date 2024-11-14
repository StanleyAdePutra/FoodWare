package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.util.HashMap;
import java.util.Map;

public class frag_login extends Fragment {

    private static final String URL_LOGIN = "https://000foodware000.000webhostapp.com/login.php";
    ProgressBar progressBar;
    EditText email, password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_login, container, false);

        progressBar = view.findViewById(R.id.progressBarLogin);
        email = view.findViewById(R.id.edtemaillogin);
        password = view.findViewById(R.id.edtpasslogin);
        ImageView seepass = view.findViewById(R.id.seepass);
        Button login = view.findViewById(R.id.btnlogin);

        seepass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return showPassword.visiblePassword(password, event);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                userLogin();
            }
        });

        return view;
    }

    private void userLogin() {
        final String userEmail = email.getText().toString().trim();
        final String userPassword = password.getText().toString().trim();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            String message = jsonObject.getString("message");

                            if (success == 1) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                                LoginManager loginManager = new LoginManager(getContext());
                                loginManager.open();
                                loginManager.insertUser(userEmail, userPassword);
                                loginManager.saveLoginStatus(userEmail, true);
                                loginManager.close();

                                saveEmailToSharedPreferences(userEmail);

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }

                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("Error: ", "HTTP Status Code: " + error.networkResponse.statusCode);
                        }
                        Log.e("Error: ", error.toString());
                        Toast.makeText(getContext(), "Terjadi kesalahan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                params.put("password", userPassword);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void saveEmailToSharedPreferences(String userEmail) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_email", userEmail);
        editor.apply();

        Log.d("SharedPreferences", "user_email saved: " + userEmail);
    }
}