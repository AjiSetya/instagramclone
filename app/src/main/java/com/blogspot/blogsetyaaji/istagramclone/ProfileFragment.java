package com.blogspot.blogsetyaaji.istagramclone;


import android.app.ProgressDialog;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.blogsetyaaji.istagramclone.adapter.AdapterPostUser;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    @BindView(R.id.pp_profile)
    CircleImageView ppProfile;
    @BindView(R.id.txtpost)
    TextView txtpost;
    @BindView(R.id.txtfollower)
    TextView txtfollower;
    @BindView(R.id.txtfollowing)
    TextView txtfollowing;
    @BindView(R.id.btneditprofile)
    MaterialButton btneditprofile;
    @BindView(R.id.p_username)
    TextView pUsername;
    @BindView(R.id.p_bio)
    TextView pBio;
    @BindView(R.id.lvpostuser)
    RecyclerView lvPostUesr;
    private Unbinder unbinder;

    private HashMap<String, String> user;
    private SessionManager sessionManager;
    private ArrayList<HashMap<String, String>> listPost;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails();

        lvPostUesr.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        listPost = new ArrayList<>();

        showDataUser();
        showPhotoUser();
    }

    private void showPhotoUser() {
        // api data
        String URL = Constants.BASEURL + "api_tampilpostuser.php";
        // membuat request mengambil data
        StringRequest requestPostUser = new StringRequest(Request.Method.POST,
                URL,
                response -> {
                    Log.d("log", "onResponse: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String hasil = jsonObject.getString("hasil");
                        String pesan = jsonObject.getString("pesan");
                        if (hasil.equalsIgnoreCase("true")) {
                            JSONArray arrayPost = jsonObject.getJSONArray("post");
                            for (int a = 0; a < arrayPost.length(); a++) {
                                // mengambil tiap objek pada masing2 item sesuai urutannya
                                JSONObject objectPost = arrayPost.getJSONObject(a);
                                HashMap<String, String> map = new HashMap<>();
                                // memasukkan objek ke dalam hashmap dengan memanggil key api
                                map.put("waktu", objectPost.getString("waktu"));
                                map.put("caption", objectPost.getString("caption"));
                                map.put("gambar", objectPost.getString("gambar"));
                                map.put("id_post", objectPost.getString("id_post"));
                                // memasukkan hashmap ke dalam array list
                                listPost.add(map);
                            }

                            // memasukkan data ke dalam adapter
                            AdapterPostUser adapterPostUser = new AdapterPostUser(getActivity(), listPost);
                            // memasang adapter ke recyclerview
                            lvPostUesr.setAdapter(adapterPostUser);
                        } else {
                            Toast.makeText(getActivity(), pesan, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("log", "onResponse: " + e.getMessage());
                    }
                }, error -> {
            Toast.makeText(getActivity(), "Terjadi kesalahan, coba lagi", Toast.LENGTH_LONG).show();
            Log.e("log", "onErrorResponse: " + error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("id", Objects.requireNonNull(user.get(SessionManager.IDUSER_KEY)));

                return parameter;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        // eksekusi request
        requestQueue.add(requestPostUser);
    }

    private void showDataUser() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mengambil Data");
        progressDialog.setMessage("Tunggu sebentar ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String URL = Constants.BASEURL + "api_tampiluser.php";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL,
                response -> {
                    progressDialog.dismiss();
                    Log.d("log", "onResponse: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String hasil = jsonObject.getString("hasil");
                        String pesan = jsonObject.getString("pesan");
                        if (hasil.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("user");
                            pUsername.setText(jsonArray.getJSONObject(0).getString("username"));
                            Glide.with(getActivity())
                                    .load(Constants.BASEURL + "gambar/"
                                            + jsonArray.getJSONObject(0).getString("p_image"))
                                    .into(ppProfile);
                        } else {
                            Toast.makeText(getActivity(), pesan, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("log", "onResponse: " + e.getMessage());
                    }
                }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Terjadi kesalahan, coba lagi", Toast.LENGTH_LONG).show();
            Log.e("log", "onErrorResponse: " + error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("email", Objects.requireNonNull(user.get(SessionManager.EMAIL_KEY)));

                return parameter;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btneditprofile)
    void onViewClicked() {
    }
}
