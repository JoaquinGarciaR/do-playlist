package com.code.do_playlist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.code.do_playlist.Models.Playlist;
import com.code.do_playlist.api.ApiClient;
import com.code.do_playlist.api.PlaylistApiService;

import com.code.do_playlist.databinding.ActivityVerPlaylistBinding;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerPlaylist extends AppCompatActivity {

    private ActivityVerPlaylistBinding binding;
    private Playlist playlistData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityVerPlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applyWindowInsets();
        initToolbar();
        initComponent();
    }

    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbarTop);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressLint("SetTextI18n")
    private void initComponent() {
        String playlistJson = getIntent().getStringExtra("playlist");
        if (playlistJson != null) {
            playlistData = new Gson().fromJson(playlistJson, Playlist.class);
            Log.e("View : ", playlistJson);

            binding.idText.setText("ID : " + playlistData.getPlaylistid());
            binding.playlistName.setText(playlistData.getNombre());
            binding.playlistDescription.setText(playlistData.getDescripcion());

            binding.buttonSongs.setOnClickListener(v -> verSongs());
            binding.buttonSave.setOnClickListener(v -> actualizarPlaylist());
            binding.buttonDelete.setOnClickListener(v -> deletePlaylist());
        } else {
            Toast.makeText(this, "Error: Playlist data is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void verSongs() {
        Intent intent = new Intent(this, SongPlaylistActivity.class);
        intent.putExtra("id", playlistData.getPlaylistid());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void actualizarPlaylist() {
        PlaylistApiService apiService = ApiClient.getPlaylistApiService();
        Playlist updatedPlaylist = new Playlist(playlistData.getPlaylistid(), binding.playlistName.getText().toString(), binding.playlistDescription.getText().toString());

        Call<Void> call = apiService.updatePlaylist(playlistData.getPlaylistid(), updatedPlaylist);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("Update Successful", response.toString());
                    Toast.makeText(VerPlaylist.this, "Actualizacion Realizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerPlaylist.this, "Actualizacion Fallida", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API_ERROR", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(VerPlaylist.this, "Conexion Fallida", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePlaylist() {
        PlaylistApiService apiService = ApiClient.getPlaylistApiService();

        Call<Void> call = apiService.deletePlaylist(playlistData.getPlaylistid());
        Log.e("API_ERROR CALL", call.request().toString());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("Borrado con Exito", response.toString());
                    Toast.makeText(VerPlaylist.this, "Borrado con Exito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VerPlaylist.this, PlaylistActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(VerPlaylist.this, "Borrado ha fallado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API_ERROR", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(VerPlaylist.this, "Conexion Fallida", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


//public class VerPlaylist extends AppCompatActivity {
//
//    private ActivityVerPlaylistBinding binding;
//    private EditText name;
//    private EditText description;
//    private TextView id;
//
//    private Playlist playlistData;
//    private Button guardar_btn;
//    private Button canciones_btn;
//    private Button delete_btn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_ver_playlist);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        binding = ActivityVerPlaylistBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        initComponent();
//
//    }
//
//
//
//    @SuppressLint("SetTextI18n")
//    private void initComponent() {
//        setSupportActionBar(findViewById(R.id.toolbarTop));
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////            getSupportActionBar().setIcon(R.mipmap.ic_logo_round);
//        }
//        String playlist = getIntent().getStringExtra("playlist");
//        assert playlist != null;
//        Log.e("View : ", playlist);
//        Toast.makeText(this, playlist, Toast.LENGTH_SHORT).show();
//
//        Gson gson = new Gson();
//
//        playlistData = gson.fromJson(playlist, Playlist.class);
//
//        name = binding.playlistName;
//        description = binding.playlistDescription;
//        guardar_btn = binding.buttonSave;
//        canciones_btn = binding.buttonSongs;
//        delete_btn = binding.buttonDelete;
//        id = binding.idText;
//        id.setText("ID : " + playlistData.getPlaylistid());
//        name.setText(playlistData.getNombre());
//        description.setText(playlistData.getDescripcion());
//
//        canciones_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                verSongs();
//            }
//        });
//        guardar_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                actualizar_playlist();
//            }
//        });
//        delete_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                delete_playlist();
//            }
//        });
//    }
//
//    public void verSongs(){
//        Intent intent = new Intent(this, SongPlaylistActivity.class);
//        intent.putExtra("id", playlistData.getPlaylistid());
//        startActivity(intent);
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Maneja los clics en los elementos de la barra de acción/toolbar
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();// Puede ser necesario implementar onBackPressed()
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//    public void actualizar_playlist(){
//        PlaylistApiService apiService = ApiClient.getPlaylistApiService();
//
//        Call<Void> call = apiService.updatePlaylist(playlistData.getPlaylistid(),new Playlist(playlistData.getPlaylistid(),name.getText().toString(),description.getText().toString()));
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                Log.e("Response", response.toString());
//                if (response.isSuccessful()) {
//                    Log.e("Buena Picha Actualizada", response.toString());
////                    Intent intent = new Intent(VerPlaylist.this, PlaylistActivity.class);
////                    intent.putExtra("id", playlistData.getPlaylistid());
////                    startActivity(intent);
//                } else {
//                    Log.e("Picha Mala", response.toString());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e("API_ERROR PICHA ERRRRRORRRRR", t.getMessage());
//            }
//        });
//
//    }
//
//    public void delete_playlist(){
//        PlaylistApiService apiService = ApiClient.getPlaylistApiService();
//
//        Call<Void> call = apiService.deletePlaylist(playlistData.getPlaylistid());
//        Log.e("API_ERROR CALL Picha", call.request().toString());
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                Log.e("Response", response.toString());
//                if (response.isSuccessful()) {
//                    Log.e("Buena Picha Actualizada", response.toString());
//                    Intent intent = new Intent(VerPlaylist.this, PlaylistActivity.class);
//                    startActivity(intent);
//                } else {
//                    Log.e("Picha Mala", response.toString());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e("API_ERROR PICHA ERRRRRORRRRR", t.getMessage());
//            }
//        });
//
//    }
//
//
//}