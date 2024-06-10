package com.code.do_playlist.api;

import com.code.do_playlist.Models.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CancionApiService {

    @GET("Canciones")
    Call<List<Song>> getSongList();
    @POST("Canciones")
    Call<Void> createSong(@Body Song song);

    @GET("Canciones/{id}")
    Call<List<Song>> getSong(@Path("id") int playlistId);

    @PUT("Canciones/{id}")
    Call<Void> updateSong(@Path("id") int id, @Body Song song);

    @DELETE("Canciones/{id}")
    Call<Void> removeSong(@Path("id") int id);

}
