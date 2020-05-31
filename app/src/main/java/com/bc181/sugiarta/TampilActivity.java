package com.bc181.sugiarta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TampilActivity extends AppCompatActivity {

    private TextView tvJudul, tvPenulis, tvPenerbit, tvTahunTerbit, tvJenis;
    private CircleImageView imgBuku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil);

        tvJudul = findViewById(R.id.tv_judul_buku);
        tvPenulis = findViewById(R.id.tv_penulis_buku);
        tvPenerbit = findViewById(R.id.tv_penerbit);
        tvTahunTerbit = findViewById(R.id.tv_tahun_terbit);
        tvJenis = findViewById(R.id.tv_jenis);
        imgBuku = findViewById(R.id.buku_image_display);

        Intent recieveData = getIntent();
        Bundle data = recieveData.getExtras();
        tvJudul.setText(data.getString("JUDUL"));
        tvPenulis.setText(data.getString("PENULIS"));
        tvPenerbit.setText(data.getString("PENERBIT"));
        tvTahunTerbit.setText(data.getString("TAHUN_TERBIT"));
        tvJenis.setText(data.getString("JENIS"));
        String imgLocation = data.getString("GAMBAR");
        if (!imgLocation.equals(null)) {
            Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
            builder.downloader(new OkHttp3Downloader(getApplicationContext()));
            builder.build().load(imgLocation)
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(imgBuku);
        }
        imgBuku.setContentDescription(imgLocation);
    }
}