package com.schwarzschild.absenonline;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    private ShareActionProvider mShare;

    TextView dNama;
    TextView dBp;
    ImageView dFoto;
    Mahasiswa dMahasiswa;
    ToggleButton dtoggleButton;
    ApiClient client;
    int fav;
    boolean isChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_mahasiswa);

        dNama = findViewById(R.id.h_nama);
        dBp = findViewById(R.id.h_bp);
        dFoto = findViewById(R.id.h_foto);
        dtoggleButton = findViewById(R.id.toggleButton);

        Intent detailIntent = getIntent();
        if(null != detailIntent) {
            dMahasiswa = detailIntent.getParcelableExtra("key_maha_parcelable");
        }

        if(dMahasiswa != null){
            getSupportActionBar().setTitle(dMahasiswa.getNama().toUpperCase());
            dNama.setText(dMahasiswa.getNama());
            dBp.setText(String.valueOf(dMahasiswa.getNim()));
            fav = dMahasiswa.getFav();
            if(fav == 0){
                isChecked = false;
                dtoggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_star_gray));
            }else if(fav == 1){
                isChecked = true;
                dtoggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_star_yellow));
            }

            if(isConnected()){
                String url = dMahasiswa.getFotoLink();
                Glide.with(this)
                        .load(url)
                        .into(dFoto);
            }else{
                Toast.makeText(DetailActivity.this, "Failed To Load Photo!", Toast.LENGTH_SHORT).show();
            }
        }

        dtoggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isConnected()) {
                    String API_BASE_URL = "https://api.tigalaskarbeton.com";

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    client = retrofit.create(ApiClient.class);
                    int id = dMahasiswa.getId();
                    if (isChecked) {
                        Call<Mahasiswa> call = client.updateMahasiswa(id, 1);
                        Log.d(TAG, "onCheckedChanged: ok");
                        call.enqueue(new Callback<Mahasiswa>() {
                            @Override
                            public void onResponse(Call<Mahasiswa> call, Response<Mahasiswa> response) {
                                Toast.makeText(getApplicationContext(), "Ditambahkan Ke Favorit", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Mahasiswa> call, Throwable t) {
                                Log.d(TAG, "onFailure: " + t);
                                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dtoggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_star_yellow));
                    } else {
                        Call<Mahasiswa> call = client.updateMahasiswa(id, 0);
                        call.enqueue(new Callback<Mahasiswa>() {
                            @Override
                            public void onResponse(Call<Mahasiswa> call, Response<Mahasiswa> response) {
                                Toast.makeText(getApplicationContext(), "DiHapus Dari Favorit", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Mahasiswa> call, Throwable t) {
                                Log.d(TAG, "onFailure: " + t);
                                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dtoggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_star_gray));
                    }
                }else{
                    Toast.makeText(DetailActivity.this, "Not Connected To Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    public void onBackPressed() {
        Intent detailIntent =new Intent(this, AbsenActivity.class);
        detailIntent.putExtra("key_maha_parcelable", dMahasiswa);
        detailIntent.putExtra("matkul", dMahasiswa.matakuliah);
        startActivityForResult(detailIntent, 1);
        super.onBackPressed();
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.share, menu);
//        MenuItem item = menu.findItem(R.id.action_share);
//        mShare = (ShareActionProvider) item.getActionProvider();
//        return true;
//    }

//    {
//        Uri uri = Uri.parse(dMahasiswa.getFotoLink());
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, dNama.getText());
//        shareIntent.putExtra(Intent.EXTRA_TEXT, dBp.getText());
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        shareIntent.setType("image/*");
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        startActivity(Intent.createChooser(shareIntent, "Share images..."));
//    }

//    private void setShareIntent(Intent shareIntent) {
//        if (mShare != null) {
//            mShare.setShareIntent(shareIntent);
//        }
//    }
}
