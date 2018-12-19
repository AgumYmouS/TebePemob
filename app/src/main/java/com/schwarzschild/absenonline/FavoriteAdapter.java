package com.schwarzschild.absenonline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Url;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>{
    ArrayList<Mahasiswa> listMahasiswa = new ArrayList<>();

    public void setListMahasiswa(ArrayList<Mahasiswa> mahas){
        listMahasiswa = mahas;
        notifyDataSetChanged();
    }

    public void setListMahasiswa(List<Mahasiswa> mahas){
        listMahasiswa = new ArrayList<>(mahas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.layout_favorit, viewGroup, false);
        FavoriteHolder holder = new FavoriteHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder favoriteHolder, int i){
        final Mahasiswa daftar = listMahasiswa.get(i);
        ImageView imageView = favoriteHolder.foto;
        String url = daftar.getFotoLink();

        Glide.with(favoriteHolder.parentLayout1).load(url).into(imageView);

    }

    @Override
    public int getItemCount() {
        if(listMahasiswa != null){
            return listMahasiswa.size();
        }
        return 0;
    }

    public class FavoriteHolder extends RecyclerView.ViewHolder{
        ImageView foto;
        RelativeLayout parentLayout1;

        public FavoriteHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.f_foto);
            parentLayout1 = itemView.findViewById(R.id.parent_layout1);

        }
    }

}
