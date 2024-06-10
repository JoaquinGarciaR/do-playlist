package com.code.do_playlist;

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
import com.code.do_playlist.databinding.ActivityCreatePlaylistBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePlaylistActivity extends AppCompatActivity {

    private ActivityCreatePlaylistBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreatePlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applyWindowInsets();
        setupToolbar();
        setupUIComponents();
    }

    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbarTop);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }

    private void setupUIComponents() {
        binding.buttonSave.setOnClickListener(v -> {
            if (validateFields()) {
                savePlaylist();
            }
        });
    }

    private boolean validateFields() {
        String name = binding.playlistName.getText().toString().trim();
        String description = binding.playlistDescription.getText().toString().trim();

        if (name.isEmpty()) {
            binding.playlistName.setError("El nombre no puede estar vacío");
            binding.playlistName.requestFocus();
            return false;
        }

        if (description.isEmpty()) {
            binding.playlistDescription.setError("La descripción no puede estar vacía");
            binding.playlistDescription.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePlaylist() {
        PlaylistApiService apiService = ApiClient.getPlaylistApiService();
        String name = binding.playlistName.getText().toString().trim();
        String description = binding.playlistDescription.getText().toString().trim();
        Playlist playlist = new Playlist(0, name, description);

        Call<Void> call = apiService.createPlaylist(playlist);
        Log.d("API_CALL", call.request().toString());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Log.d("API_RESPONSE", response.toString());
                if (response.isSuccessful()) {
                    Toast.makeText(CreatePlaylistActivity.this, "Playlist creada correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreatePlaylistActivity.this, PlaylistActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreatePlaylistActivity.this, "Error al crear la playlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API_ERROR", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(CreatePlaylistActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


//public class CreatePlaylistActivity extends AppCompatActivity {
//
//    private ActivityCreatePlaylistBinding binding;
//    private EditText name;
//    private EditText description;
//
//    private Button guardar_btn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_create_playlist);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        binding = ActivityCreatePlaylistBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        initComponent();
//    }
//
//    private void initComponent() {
//        setSupportActionBar(findViewById(R.id.toolbarTop));
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////            getSupportActionBar().setIcon(R.mipmap.ic_logo_round);
//        }
//        name = binding.playlistName;
//        description = binding.playlistDescription;
//        guardar_btn = binding.buttonSave;
//
//        guardar_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                savePlaylist();
//            }
//        });
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Maneja los clics en los elementos de la barra de acción/toolbar
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();// Puede ser necesario implementar onBackPressed()
//
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//    private void savePlaylist() {
//        PlaylistApiService apiService = ApiClient.getPlaylistApiService();
//
//        Call<Void> call = apiService.createPlaylist(new Playlist(0,name.getText().toString(),description.getText().toString()));
//
//        Log.e("API_ERROR CALL", call.request().toString());
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                Log.e("Response", response.toString());
//                if (response.isSuccessful()) {
//                    Toast.makeText(CreatePlaylistActivity.this, "Cancion Insertada Correctamente", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(CreatePlaylistActivity.this, "Error al insertar cancion", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e("API_ERROR", t.getMessage());
//                Toast.makeText(CreatePlaylistActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}