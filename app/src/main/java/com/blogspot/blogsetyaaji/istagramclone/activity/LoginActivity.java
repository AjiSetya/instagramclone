package com.blogspot.blogsetyaaji.istagramclone.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.blogsetyaaji.istagramclone.Constants;
import com.blogspot.blogsetyaaji.istagramclone.R;
import com.blogspot.blogsetyaaji.istagramclone.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    AnimationDrawable animationDrawable;
    @BindView(R.id.txtemaillogin)
    TextInputEditText txtemaillogin;
    @BindView(R.id.txtpasslogin)
    TextInputEditText txtpasslogin;
    @BindView(R.id.btnlogin)
    MaterialButton btnlogin;
    @BindView(R.id.txthelplogin)
    TextView txthelplogin;
    @BindView(R.id.txtregisterlogin)
    TextView txtregisterlogin;
    @BindView(R.id.parentrootlogin)
    RelativeLayout parentrootlogin;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(LoginActivity.this);

        // onCreate
        animationDrawable = (AnimationDrawable) parentrootlogin.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        txtregisterlogin.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // onResume
        animationDrawable.start();
    }

    @OnClick(R.id.btnlogin)
    public void onViewClicked() {
        insertData();
    }

    private void insertData() {
        if (TextUtils.isEmpty(Objects.requireNonNull(txtemaillogin.getText()).toString())) {
            txtemaillogin.setError("Email harus diisi");
            txtemaillogin.requestFocus();
        } else if (TextUtils.isEmpty(Objects.requireNonNull(txtpasslogin.getText()).toString())) {
            txtpasslogin.setError("Password harus diisi");
            txtpasslogin.requestFocus();
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Memproses");
            progressDialog.setMessage("Tunggu sebentar ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                    Constants.BASEURL + "api_loginuser.php",
                    response -> {
                        progressDialog.dismiss();
                        Log.d("log", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String hasil = jsonObject.getString("hasil");
                            String pesan = jsonObject.getString("pesan");
                            if (hasil.equalsIgnoreCase("true")) {
                                JSONArray arrayPost = jsonObject.getJSONArray("user");
                                for (int a = 0; a < arrayPost.length(); a++) {
                                    // mengambil tiap objek pada masing2 item sesuai urutannya
                                    JSONObject objectPost = arrayPost.getJSONObject(a);
                                    HashMap<String, String> map = new HashMap<>();
                                    // memasukkan objek ke dalam hashmap dengan memanggil key api
                                    map.put("iduser", objectPost.getString("id_user"));
                                    map.put("emailuser", objectPost.getString("email"));
                                    sessionManager.createSession(map.get("emailuser"), map.get("iduser"));
                                }

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("log", "onResponse: " + e.getMessage());
                        }
                    }, error -> {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Terjadi kesalahan, coba lagi", Toast.LENGTH_LONG).show();
                Log.e("log", "onErrorResponse: " + error.getMessage());
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameter = new HashMap<>();
                    parameter.put("email", txtemaillogin.getText().toString());
                    parameter.put("password", txtpasslogin.getText().toString());

                    return parameter;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(LoginActivity.this));
            requestQueue.add(jsonObjectRequest);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
