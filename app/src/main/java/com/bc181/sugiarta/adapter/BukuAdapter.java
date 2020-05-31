package com.bc181.sugiarta.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.bc181.sugiarta.InputActivity;
import com.bc181.sugiarta.R;
import com.bc181.sugiarta.TampilActivity;
import com.bc181.sugiarta.model.Buku;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BukuAdapter extends RecyclerView.Adapter<BukuAdapter.BukuViewHolder> {

    private List<Buku> dataBuku;
    private Context context;

    public BukuAdapter(List<Buku> dataBuku, Context context) {
        this.dataBuku = dataBuku;
        this.context = context;
    }

    @NonNull
    @Override
    public BukuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_buku, parent, false);
        return new BukuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BukuViewHolder holder, int position) {
        Buku tempBuku = dataBuku.get(position);
        holder.id = tempBuku.getId();
        holder.judul = tempBuku.getJudul();
        holder.penulis = tempBuku.getPenulis();
        holder.penerbit = tempBuku.getPenerbit();
        holder.tahunTerbit = tempBuku.getTahunTerbit();
        holder.jenis = tempBuku.getJenis();
        holder.tvJudul.setText(holder.judul);
        holder.tvPenulis.setText(holder.penulis);
        String imgLocation = tempBuku.getGambar();
        if(!imgLocation.equals(null)) {
            //Picasso.get().load(imgLocation).resize(64, 64).into(holder.imgBuku);
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttp3Downloader(context));
            builder.build().load(imgLocation)
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.imgBuku);
        }
        holder.imgBuku.setContentDescription(tempBuku.getGambar());
    }

    @Override
    public int getItemCount() {
        return dataBuku.size();
    }

    public class BukuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView tvJudul, tvPenulis;
        private CircleImageView imgBuku;
        private int id;
        private String judul, penulis, penerbit, tahunTerbit, jenis;

        public BukuViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJudul = itemView.findViewById(R.id.tv_judul);
            tvPenulis = itemView.findViewById(R.id.tv_penulis);
            imgBuku = itemView.findViewById(R.id.buku_image);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent openDisplay = new Intent(context, TampilActivity.class);
            openDisplay.putExtra("JUDUL", tvJudul.getText());
            openDisplay.putExtra("PENULIS", tvPenulis.getText());
            openDisplay.putExtra("PENERBIT", penerbit);
            openDisplay.putExtra("TAHUN_TERBIT", tahunTerbit);
            openDisplay.putExtra("JENIS", jenis);
            openDisplay.putExtra("GAMBAR", imgBuku.getContentDescription());
            itemView.getContext().startActivity(openDisplay);
        }

        @Override
        public boolean onLongClick(View v) {
            Intent openInput = new Intent(context, InputActivity.class);
            openInput.putExtra("OPERATION", "update");
            openInput.putExtra("ID", id);
            openInput.putExtra("JUDUL", tvJudul.getText());
            openInput.putExtra("PENULIS", tvPenulis.getText());
            openInput.putExtra("PENERBIT", penerbit);
            openInput.putExtra("TAHUN_TERBIT", tahunTerbit);
            openInput.putExtra("JENIS", jenis);
            openInput.putExtra("GAMBAR", imgBuku.getContentDescription());
            itemView.getContext().startActivity(openInput);
            return true;
        }
    }
}