package com.blogspot.blogsetyaaji.istagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.blogsetyaaji.istagramclone.Constants;
import com.blogspot.blogsetyaaji.istagramclone.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPostUser extends RecyclerView.Adapter<AdapterPostUser.PostHolder> {

    // deklarasi variable data dari home fragment
    private ArrayList<HashMap<String, String>> listData;
    private Context context;

    // penangkap data dari home fragment
    public AdapterPostUser(FragmentActivity activity, ArrayList<HashMap<String, String>> listPost) {
        // data dari home fragment di masukkan ke dalam variable adapter
        listData = listPost;
        context = activity;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // memasang layout item ke dalam adapter
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_profil, viewGroup, false);
        // memasukkan  layout ke dalam viewhlder
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder postHolder, int i) {
        // menampilkan data ke masing2 komponen layout

        String URL_GAMBAR = Constants.BASEURL + "gambar/";
        Glide.with(context)
                .load(URL_GAMBAR + listData.get(i).get("gambar"))
                .into(postHolder.imgPPost);
        postHolder.imgPPost.setOnClickListener(view -> Toast.makeText(context
                , "Kamu memilih foto post dengan id = " + listData.get(i).get("id_post")
                , Toast.LENGTH_LONG).show());
    }

    @Override
    public int getItemCount() {
        return listData.size();
        // menentukan jumlah data yang ingin ditampilkan
    }

    class PostHolder extends RecyclerView.ViewHolder {
        ImageView imgPPost;

        PostHolder(@NonNull View itemView) {
            super(itemView);

            // inisialisasi komponen layout
            imgPPost = itemView.findViewById(R.id.imgppost);
        }
    }
}
