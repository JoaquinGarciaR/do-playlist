package com.code.do_playlist;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;

import android.widget.TextView;
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
import com.code.do_playlist.databinding.ActivityPlaylistBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class PlaylistActivity extends AppCompatActivity {

    private ActivityPlaylistBinding binding;
    private List<Playlist> playlists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityPlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applyWindowInsets();
        initToolbar();
        initComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPlaylists();
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

    private void initComponent() {
        binding.buttonCreate.setOnClickListener(v -> goToCreatePlaylist());
        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
            Playlist playlist = playlists.get(position);
            Intent intent = new Intent(PlaylistActivity.this, VerPlaylist.class);
            String playlistJson = new Gson().toJson(playlist);
            intent.putExtra("playlist", playlistJson);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            Intent intent = new Intent(PlaylistActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListView(List<Playlist> playlistList) {
        ArrayAdapter<Playlist> adapter = new ArrayAdapter<Playlist>(this, R.layout.item_playlist, playlistList) {
            @SuppressLint("SetTextI18n")
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_playlist, parent, false);
                Playlist playlist = playlistList.get(position);

                ((TextView) view.findViewById(R.id.playlistId)).setText("ID : " + playlist.getPlaylistid());
                ((TextView) view.findViewById(R.id.nombre)).setText("Nombre : " + playlist.getNombre());
                ((TextView) view.findViewById(R.id.descripcion)).setText("Descripcion : " + playlist.getDescripcion());

                return view;
            }
        };

        binding.listView.setAdapter(adapter);
    }

    private void getPlaylists() {
        PlaylistApiService apiService = ApiClient.getPlaylistApiService();
        Call<List<Playlist>> call = apiService.getPlaylist();

        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(@NonNull Call<List<Playlist>> call, @NonNull Response<List<Playlist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    playlists = response.body();
                    setupListView(playlists);
                } else {
                    showErrorToast("Error al obtener playlists");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Playlist>> call, @NonNull Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
                showErrorToast("Fallo en la conexión");
            }
        });
    }

    private void goToCreatePlaylist() {
        Intent intent = new Intent(this, CreatePlaylistActivity.class);
        startActivity(intent);
    }

    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

//public class PlaylistActivity extends AppCompatActivity {
//
//    private ListView listView;
//    private List<Playlist> playlists = new ArrayList<>(); // Inicializar con una lista vacía
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_playlist);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        listView = findViewById(R.id.listView);
//        initComponent();
//        clicklers();
//    }
//    @Override
//    protected void onResume(){
//        super.onResume();
//        listView = findViewById(R.id.listView);
//        initComponent();
//        clicklers();
//    }
//
//    private void initComponent() {
//        setSupportActionBar(findViewById(R.id.toolbarTop));
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//        ImageButton add = findViewById(R.id.button_create);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToCreatePlaylist();
//            }
//        });
//        getPlaylist(); // Llamar al método para obtener las canciones
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Maneja los clics en los elementos de la barra de acción/toolbar
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();// Puede ser necesario implementar onBackPressed()
//            Intent intent = new Intent(PlaylistActivity.this, MainActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void setupListView(List<Playlist> playlistList) {
//        ArrayAdapter<Playlist> adapter = new ArrayAdapter<Playlist>(this, R.layout.item_playlist, playlistList) {
//
//            @SuppressLint("SetTextI18n")
//            @NonNull
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_playlist, parent, false);
//                Playlist playlist = playlistList.get(position);
//
//
//
//                TextView playlistId = view.findViewById(R.id.playlistId);
//                playlistId.setText("ID : " + String.valueOf(playlist.getPlaylistid()));
//
//                TextView nombre = view.findViewById(R.id.nombre);
//                nombre.setText("Nombre : " + playlist.getNombre());
//
//                TextView descripcion = view.findViewById(R.id.descripcion);
//                descripcion.setText("Descripcion : " + playlist.getDescripcion());
//
//
//                return view;
//            }
//        };
//
//        listView.setAdapter(adapter);
//    }
//
//    private void getPlaylist() {
//        PlaylistApiService apiService = ApiClient.getPlaylistApiService();
//
//        Call<List<Playlist>> call = apiService.getPlaylist();
//
//        Log.e("API_ERROR CALL", call.request().toString());
//
//        call.enqueue(new Callback<List<Playlist>>() {
//            @Override
//            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
//                Log.e("Response", response.toString());
//                if (response.isSuccessful()) {
//                    playlists = response.body();
//                    Log.e("Songs", playlists.toString());
//                    Toast.makeText(PlaylistActivity.this, "Canciones obtenidas correctamente", Toast.LENGTH_SHORT).show();
//                    setupListView(playlists); // Configurar el ListView con las canciones obtenidas
//                } else {
//                    Toast.makeText(PlaylistActivity.this, "Error al obtener canciones", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Playlist>> call, Throwable t) {
//                Log.e("API_ERROR", t.getMessage());
//                Toast.makeText(PlaylistActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void goToCreatePlaylist() {
//        Intent intent = new Intent(this, CreatePlaylistActivity.class);
////        intent.putExtra("playlist", playlist);
//        startActivity(intent);
//    }
//    public void clicklers() {
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Playlist playlist = playlists.get(position);
//                Log.e("API_ERROR", playlist.toString());
//                Toast.makeText(PlaylistActivity.this, playlist.toString(), Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(PlaylistActivity.this, VerPlaylist.class);
//                String playlistJson = new Gson().toJson(playlist);
//                intent.putExtra("playlist", playlistJson);
//                startActivity(intent);
//
//
//            }
//        });
//    }
//
//}