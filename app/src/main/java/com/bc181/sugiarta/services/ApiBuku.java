package com.bc181.sugiarta.services;

import com.bc181.sugiarta.model.ResponseData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBuku {

    @FormUrlEncoded
    @POST("insert.php")
    Call<ResponseData> addData(
            @Field("judul") String judul,
            @Field("penulis") String penulis,
            @Field("penerbit") String penerbit,
            @Field("thn_terbit") String tahunTerbit,
            @Field("jenis") String jenis,
            @Field("gambar") String gambar
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseData> editData(
            @Field("id") String id,
            @Field("judul") String judul,
            @Field("penulis") String penulis,
            @Field("penerbit") String penerbit,
            @Field("thn_terbit") String tahunTerbit,
            @Field("jenis") String jenis,
            @Field("gambar") String gambar
    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<ResponseData> deleteData(
            @Field("id") String id
    );

    @GET("getdata.php")
    Call<ResponseData> getData();

}