package com.bc181.sugiarta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.bc181.sugiarta.adapter.BukuAdapter;
import com.bc181.sugiarta.model.ResponseData;
import com.bc181.sugiarta.model.Buku;
import com.bc181.sugiarta.services.ApiClient;
import com.bc181.sugiarta.services.ApiBuku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private BukuAdapter bukuAdapter;
    private RecyclerView rvShowBuku;
    private ProgressDialog progressDialog;
    private List<Buku> dataBuku = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openInput = new Intent(getApplicationContext(), InputActivity.class);
                openInput.putExtra("OPERATION", "insert");
                startActivity(openInput);
            }
        });

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Memuat data...");

        rvShowBuku = findViewById(R.id.rv_tampil);

        showBukuData();
    }

    private void showBukuData() {
        progressDialog.show();
        ApiBuku api = ApiClient.getRetrofitInstance().create(ApiBuku.class);
        Call<ResponseData> call = api.getData();
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                String value = response.body().getValue();

                if(value.equals("1")) {
                    dataBuku = response.body().getResult();
                    bukuAdapter = new BukuAdapter(dataBuku, getApplicationContext());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    rvShowBuku.setLayoutManager(layoutManager);
                    rvShowBuku.setAdapter(bukuAdapter);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
}
