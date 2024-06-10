package com.code.do_playlist.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.code.do_playlist.Models.Playlist;
import com.code.do_playlist.Models.Song;
import com.code.do_playlist.Models.SongCheck;
import com.code.do_playlist.PlaylistActivity;
import com.code.do_playlist.R;
import com.code.do_playlist.SongPlaylistActivity;
import com.code.do_playlist.api.ApiClient;
import com.code.do_playlist.api.PlaylistApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongAdapter extends ArrayAdapter<SongCheck> {
    private final LayoutInflater inflater;
    private final int idPlay;

    public SongAdapter(Context context, List<SongCheck> songs, int idPlay) {
        super(context, 0, songs);
        this.inflater = LayoutInflater.from(context);
        this.idPlay = idPlay;
    }

    private static class ViewHolder {
        TextView songTitle;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch songSwitch;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_song_swichable, parent, false);
            holder = new ViewHolder();
            holder.songTitle = convertView.findViewById(R.id.songTitle);
            holder.songSwitch = convertView.findViewById(R.id.songSwitch);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SongCheck song = getItem(position);
        if (song != null) {
            holder.songTitle.setText(song.getTitulo());
            // Guardar la posición actual del elemento para evitar errores de reciclaje
            holder.songSwitch.setTag(position);
            holder.songSwitch.setChecked(song.isChecked());

            holder.songSwitch.setOnCheckedChangeListener(null); // Eliminar el listener anterior

            holder.songSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Obtener la posición del elemento afectado por el cambio
                int pos = (int) buttonView.getTag();
                SongCheck currentSong = getItem(pos);

                if (isChecked) {
                    assert currentSong != null;
                    addSongToPlaylist(currentSong);
                } else {
                    assert currentSong != null;
                    removeSongFromPlaylist(currentSong);
                }
            });
        }

        return convertView;
    }

    private void addSongToPlaylist(SongCheck song) {
        PlaylistApiService apiService = ApiClient.getPlaylistApiService();
        Call<Void> call = apiService.addSongToPlaylist(idPlay, song.getCancionid());
        Log.e("API_CALL", call.request().toString());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("SUCCESS", response.toString());
                } else {
                    Log.e("ERROR", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void removeSongFromPlaylist(SongCheck song) {
        PlaylistApiService apiService = ApiClient.getPlaylistApiService();
        Call<Void> call = apiService.removeSongFromPlaylist(idPlay, song.getCancionid());
        Log.e("API_CALL", call.request().toString());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("SUCCESS", response.toString());
                } else {
                    Log.e("ERROR", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }
//    private List<SongCheck> songs;
//    private int idPlay;
//    private LayoutInflater inflater;
//
//    public SongAdapter(Context context, List<SongCheck> songs, int idPlay) {
//        super(context, 0, songs);
//        this.songs = songs;
//        this.idPlay = idPlay;
//        this.inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = convertView != null ? convertView : inflater.inflate(R.layout.item_song_swichable, parent, false);
//
//        SongCheck song = getItem(position);
//
//        TextView songTitle = view.findViewById(R.id.songTitle);
//        assert song != null;
//        songTitle.setText(song.getTitulo());
//
//        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch songSwitch = view.findViewById(R.id.songSwitch);
//        songSwitch.setChecked(song.isChecked());
//
//        songSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
//
//            PlaylistApiService apiService = ApiClient.getPlaylistApiService();
//            if(isChecked){
//                Call<Void> call = apiService.addSongToPlaylist(idPlay, song.getCancionid());
//                Log.e("API_ERROR CALL Picha", call.request().toString());
//
//                call.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        Log.e("Response", response.toString());
//                        if (response.isSuccessful()) {
//                            Log.e("Buena Picha", response.toString());
//                        } else {
//                            Log.e("Picha Mala", response.toString());
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Log.e("API_ERROR PICHA ERRRRRORRRRR", t.getMessage());
//                    }
//                });
//            }
//            else {
//                Call<Void> call = apiService.removeSongFromPlaylist(idPlay, song.getCancionid());
//                Log.e("API_ERROR CALL Picha", call.request().toString());
//
//                call.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        Log.e("Response", response.toString());
//                        if (response.isSuccessful()) {
//                            Log.e("Buena Picha Removida", response.toString());
//                        } else {
//                            Log.e("Picha Mala", response.toString());
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Log.e("API_ERROR PICHA ERRRRRORRRRR", t.getMessage());
//                    }
//                });
//            }
//            // Aquí puedes manejar el cambio de estado, por ejemplo, actualizar la base de datos
//        });
//
//        return view;
//    }
}
