package com.code.do_playlist.api;

import com.code.do_playlist.Models.Playlist;
import com.code.do_playlist.Models.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PlaylistApiService {

    @GET("Playlists")
    Call<List<Playlist>> getPlaylist();

    @POST("Playlists")
    Call<Void> createPlaylist(@Body Playlist playlist);

    @GET("Playlists/{id}")
    Call<List<Playlist>> getPlaylistById(@Path("id") int playlistId);

    @PUT("Playlists/{id}")
    Call<Void> updatePlaylist(@Path("id") int playlistId, @Body Playlist playlist);

    @DELETE("Playlists/{id}")
    Call<Void> deletePlaylist(@Path("id") int playlistId);

    @GET("Playlists/{id}/Canciones")
    Call<List<Song>> getSongsInPlaylist(@Path("id") int playlistId);

    @POST("Playlists/{id}/Canciones/{songId}")
    Call<Void> addSongToPlaylist(@Path("id") int playlistId, @Path("songId") int songId);

    @DELETE("Playlists/{id}/Canciones/{songId}")
    Call<Void> removeSongFromPlaylist(@Path("id") int playlistId, @Path("songId") int songId);

}
