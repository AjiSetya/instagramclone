package com.blogspot.blogsetyaaji.istagramclone;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    // deklarasi komponen recyclerview
    private RecyclerView lvPost;
    // deklarasi variable untuk data dari api
    private ArrayList<HashMap<String, String>> listPost;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // inisialisasi recyclerview
        lvPost = view.findViewById(R.id.lv_post);
        // mengatur layout utk recyclerview
        lvPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        // inisialisasi variable array list
        listPost = new ArrayList<>();
        // memanggil fungsi showData() untuk menampilkan data
        showDataPost();
    }

    private void showDataPost() {
        // menampilkan progres dialog ketika mengambil data
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        // api data
        String URL = "https://ajisetyaserver.000webhostapp.com/SMPIDN/webdatabase/api_tampilpost.php";
        // membuat request mengambil data
        JsonObjectRequest requestPost = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // menghilangkan progressdialog ketika request selesai
                        progressDialog.dismiss();
                        // menampilkan data yg telah diambil di logcat
                        Log.d("log", "onResponse: " + response.toString());
                        try {
                            // mengambil data dari json array dengan index post
                            JSONArray arrayPost = response.getJSONArray("post");
                            for (int a = 0; a < arrayPost.length(); a++) {
                                // mengambil tiap objek pada masing2 item sesuai urutannya
                                JSONObject objectPost = arrayPost.getJSONObject(a);
                                HashMap<String, String> map = new HashMap<>();
                                // memasukkan objek ke dalam hashmap dengan memanggil key api
                                map.put("waktu", objectPost.getString("waktu"));
                                map.put("username", objectPost.getString("username"));
                                map.put("caption", objectPost.getString("caption"));
                                map.put("gambar", objectPost.getString("gambar"));
                                map.put("id_user", objectPost.getString("id_user"));
                                map.put("p_image", objectPost.getString("p_image"));
                                map.put("id_post", objectPost.getString("id_post"));
                                // memasukkan hashmap ke dalam array list
                                listPost.add(map);
                            }

                            // memasukkan data ke dalam adapter
                            AdapterPost adapterPost = new AdapterPost(getActivity(), listPost);
                            // memasang adapter ke recyclerview
                            lvPost.setAdapter(adapterPost);
                        } catch (JSONException e) {
                            // menampilkan error di logcat
                            Log.d("log", "JSONException: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // menghilangkan progressdialog ketika terjadi error
                progressDialog.dismiss();
                // menampilkan error di logcat
                Log.d("log", "onErrorResponse: " + error.getMessage());
                // menampilkan error ke user
                Toast.makeText(getActivity(), "Gagal menampilkan data.\nCoba Lagi", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        // eksekusi request
        requestQueue.add(requestPost);
    }
}
