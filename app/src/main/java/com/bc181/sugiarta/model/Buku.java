package com.bc181.sugiarta.model;

import com.google.gson.annotations.SerializedName;

public class Buku {

    @SerializedName("id")
    private int id;
    @SerializedName("judul")
    private String judul;
    @SerializedName("penulis")
    private String penulis;
    @SerializedName("penerbit")
    private String penerbit;
    @SerializedName("thn_terbit")
    private String tahunTerbit;
    @SerializedName("jenis")
    private String jenis;
    @SerializedName("gambar")
    private String gambar;

    public Buku(int id, String judul, String penulis, String penerbit, String tahunTerbit, String jenis, String gambar) {
        this.id = id;
        this.judul = judul;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.jenis = jenis;
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public String getTahunTerbit() {
        return tahunTerbit;
    }

    public String getJenis() {
        return jenis;
    }

    public String getGambar() {
        return gambar;
    }

}