package com.bc181.sugiarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.bc181.sugiarta.model.ResponseData;
import com.bc181.sugiarta.services.ApiClient;
import com.bc181.sugiarta.services.ApiBuku;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private EditText editJudul, editPenulis, editPenerbit, editTahunTerbit;
    private Button btnSave;
    private RadioButton rbFiksi, rbNonfiksi;
    private CircleImageView imgGambar;
    private String imgLocation;
    private boolean updateOperation = false;
    private String jenis;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        progressDialog = new ProgressDialog(InputActivity.this);

        editJudul = findViewById(R.id.edit_judul);
        editPenulis = findViewById(R.id.edit_penulis);
        editPenerbit = findViewById(R.id.edit_penerbit);
        editTahunTerbit = findViewById(R.id.edit_tahun_terbit);
        btnSave = findViewById(R.id.btn_simpan);
        imgGambar = findViewById(R.id.buku_image);
        rbFiksi = findViewById(R.id.jns_fiksi);
        rbNonfiksi = findViewById(R.id.jns_nonfiksi);

        Intent receiveData = getIntent();
        Bundle data = receiveData.getExtras();
        if (data.getString("OPERATION").equals("insert")) {
            updateOperation = false;
        } else {
            updateOperation = true;
            id = data.getInt("ID");
            editJudul.setText(data.getString("JUDUL"));
            editPenulis.setText(data.getString("PENULIS"));
            editPenerbit.setText(data.getString("PENERBIT"));
            editTahunTerbit.setText(data.getString("TAHUN_TERBIT"));
            imgLocation = data.getString("GAMBAR");
            jenis = data.getString("JENIS");
            if (jenis.equals(getString(R.string.jenis_fiksi)))
                rbFiksi.setChecked(true);
            else
                rbNonfiksi.setChecked(true);
            if (imgLocation.equals(null)) {
                Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
                builder.downloader(new OkHttp3Downloader(getApplicationContext()));
                builder.build().load(imgLocation)
                        .placeholder((R.drawable.ic_launcher_background))
                        .error(R.drawable.ic_launcher_background)
                        .into(imgGambar);
            }
            imgGambar.setContentDescription(imgLocation);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_input, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item_menu_delete) {
            deleteData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.item_menu_delete);

        if (updateOperation == true) {
            item.setEnabled(true);
            item.getIcon().setAlpha(255);
        } else {
            item.setEnabled(false);
            item.getIcon().setAlpha(130);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();


        switch (view.getId()) {
            case R.id.jns_fiksi:
                if (checked) {
                    jenis = getString(R.string.jenis_fiksi);
                    imgLocation = ApiClient.IMAGE_URL + "fiksi.png";
                    break;
                }
            case R.id.jns_nonfiksi:
                if (checked) {
                    jenis = getString(R.string.jenis_nonfiksi);
                    imgLocation = ApiClient.IMAGE_URL + "non_fiksi.png";
                    break;
                }
        }
    }

    private void saveData() {
        progressDialog.setMessage("Menyimpan...");
        progressDialog.show();

        String judul = editJudul.getText().toString();
        String penulis = editPenulis.getText().toString();
        String penerbit = editPenerbit.getText().toString();
        String tahunTerbit = editTahunTerbit.getText().toString();
        if (!(judul.equals("") && penulis.equals("") && penerbit.equals("") && tahunTerbit.equals(""))) {
            ApiBuku api = ApiClient.getRetrofitInstance().create(ApiBuku.class);
            Call<ResponseData> call;
            if (updateOperation == false) {
                call = api.addData(judul, penulis, penerbit, tahunTerbit, jenis, imgLocation);
            } else {
                call = api.editData(String.valueOf(id), judul, penulis, penerbit, tahunTerbit, jenis, imgLocation);
                updateOperation = false;
            }
            call.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    String value = response.body().getValue();
                    String message = response.body().getMessage();
                    progressDialog.dismiss();
                    if (value.equals("1")) {
                        Toast.makeText(InputActivity.this, "SUKSES " + message, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(InputActivity.this, "GAGAL " + message, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(InputActivity.this, "Gagal menghubungi server...", Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                    Log.d("Input Data Error", t.toString());

                }
            });
        } else {
            Toast.makeText(this, "Judul buku, penulis, penerbit dan tahun terbit harus diisi", Toast.LENGTH_LONG).show();
        }

    }

    private void deleteData() {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Hapus Data");
        builder.setMessage("Apakah anda yakin ingin menghapus data?")
                .setCancelable(false)
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressDialog.setMessage("Menghapus...");
                        progressDialog.show();
                        ApiBuku api = ApiClient.getRetrofitInstance().create(ApiBuku.class);
                        Call<ResponseData> call = api.deleteData(String.valueOf(id));
                        call.enqueue(new Callback<ResponseData>() {
                            @Override
                            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                                String value = response.body().getValue();
                                String message = response.body().getMessage();
                                progressDialog.dismiss();

                                if(value.equals("1")) {
                                    Toast.makeText(InputActivity.this, "SUKSES: " + message, Toast.LENGTH_LONG).show();
                                } else{
                                    Toast.makeText(InputActivity.this, "GAGAL: " + message, Toast.LENGTH_LONG).show();
                                }

                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseData> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(InputActivity.this, "Gagal menghubungi server...", Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                                Log.d("Detele Data Error", t.toString());
                            }
                        });
                    }
                }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}