package com.code.do_playlist;


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

import com.code.do_playlist.Adapter.SongAdapter;

import com.code.do_playlist.Models.Song;
import com.code.do_playlist.Models.SongCheck;
import com.code.do_playlist.api.ApiClient;
import com.code.do_playlist.api.CancionApiService;
import com.code.do_playlist.api.PlaylistApiService;
import com.code.do_playlist.databinding.ActivitySongPlaylistBinding;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongPlaylistActivity extends AppCompatActivity {

    private ActivitySongPlaylistBinding binding;
    private int idPlaylist;
    private List<Song> songs = new ArrayList<>();
    private List<SongCheck> songsCheck = new ArrayList<>();
    private List<Song> allSongs = new ArrayList<>();
    private List<SongCheck> allSongsCheck = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySongPlaylistBinding.inflate(getLayoutInflater());
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

    private void initComponent() {
        idPlaylist = getIntent().getIntExtra("id", 0);
        getSongsByIdPlaylist(idPlaylist);
    }

    private void getSongsByIdPlaylist(int id) {
        PlaylistApiService apiService = ApiClient.getPlaylistApiService();
        Call<List<Song>> call = apiService.getSongsInPlaylist(id);

        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    songs = response.body();
                    songsCheck = songs.stream().map(song -> song.getSongCheck(true)).collect(Collectors.toList());
                    setupListView(songsCheck);
                    getAllSongs();
                    Toast.makeText(SongPlaylistActivity.this, "Canciones obtenidas correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    showErrorToast("Error al obtener canciones");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
                showErrorToast("Fallo en la conexión");
            }
        });
    }

    private void getAllSongs() {
        CancionApiService apiService = ApiClient.getCancionApiService();
        Call<List<Song>> call = apiService.getSongList();

        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allSongs = response.body();
                    allSongsCheck = allSongs.stream().map(song -> song.getSongCheck(false)).collect(Collectors.toList());
                    mergeList();
                    Toast.makeText(SongPlaylistActivity.this, "Canciones obtenidas correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    showErrorToast("Error al obtener canciones");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
                showErrorToast("Fallo en la conexión");
            }
        });
    }

    private void mergeList() {
        Map<Integer, SongCheck> interseccionMap = songsCheck.stream()
                .collect(Collectors.toMap(SongCheck::getCancionid, song -> song));

        List<SongCheck> updatedSongs = allSongsCheck.stream()
                .map(song -> interseccionMap.getOrDefault(song.getCancionid(), song))
                .collect(Collectors.toList());

        allSongsCheck = updatedSongs;
        setupListView(allSongsCheck);
    }

    private void setupListView(List<SongCheck> songCheckList) {
        SongAdapter adapter = new SongAdapter(this, songCheckList, idPlaylist);
        binding.listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

//public class SongPlaylistActivity extends AppCompatActivity {
//
//    private ListView listView;
//    private int idPlaylist;
//    private List<Song> songs = new ArrayList<>();;
//    private List<SongCheck> songsCheck = new ArrayList<>();;
//    private List<Song> AllSongs = new ArrayList<>();
//    private List<SongCheck> AllSongsCheck = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_song_playlist);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        initComponent();
//
//
//
//    }
//
//
//    private void initComponent() {
//        setSupportActionBar(findViewById(R.id.toolbarTop));
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//        int id = getIntent().getIntExtra("id",0);
//        idPlaylist = id;
//        getSongsByIdPlaylist(id);
//        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
//        Log.e("Ya Tenemos la lista", songs.toString());
//
//
////        ImageButton add = findViewById(R.id.button_create);
////        add.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                goToCreatePlaylist();
////            }
////        });
////        getPlaylist(); // Llamar al método para obtener las canciones
//    }
//
//
//    private void getSongsByIdPlaylist(int id) {
//        PlaylistApiService apiService = ApiClient.getPlaylistApiService();
//
//        Call<List<Song>> call = apiService.getSongsInPlaylist(id);
//
//        Log.e("API_ERROR CALL", call.request().toString());
//
//        call.enqueue(new Callback<List<Song>>() {
//            @Override
//            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
//                if (response.isSuccessful()) {
//                    songs = response.body();
//                    Log.e("Songs Canciones", response.body().toString());
//                    listView = findViewById(R.id.listView);
//                    songsCheck = songs.stream().map(song -> song.getSongCheck(true)).collect(Collectors.toList());
//                    SongAdapter adapter = new SongAdapter(SongPlaylistActivity.this, songsCheck,idPlaylist);
//                    listView.setAdapter(adapter);
//                    Log.e("Songs Canciones22", songs.toString());
//                    getAllSongs();
//                    Toast.makeText(SongPlaylistActivity.this , "Canciones obtenidas correctamente", Toast.LENGTH_SHORT).show();
//                     // Configurar el ListView con las canciones obtenidas
//                } else {
//                    Toast.makeText(SongPlaylistActivity.this , "Error al obtener canciones", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Song>> call, Throwable t) {
//                Log.e("API_ERROR", t.getMessage());
//                Toast.makeText(SongPlaylistActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
//            }
//
//        });
//
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
//    private void getAllSongs() {
//        CancionApiService apiService = ApiClient.getCancionApiService();
//
//        Call<List<Song>> call = apiService.getSongList();
//
//        Log.e("API_ERROR CALL", call.request().toString());
//
//        call.enqueue(new Callback<List<Song>>() {
//            @Override
//            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
//                if (response.isSuccessful()) {
//                    AllSongs = response.body();
//
//
//                    listView = findViewById(R.id.listView);
//                    AllSongsCheck = AllSongs.stream().map(song -> song.getSongCheck(false)).collect(Collectors.toList());
//                    SongAdapter adapter = new SongAdapter(SongPlaylistActivity.this, AllSongsCheck,idPlaylist);
//                    listView.setAdapter(adapter);
//                    mergeList();
//                    Log.e("Songs Canciones Salida", AllSongsCheck.toString());
//                    Toast.makeText(SongPlaylistActivity.this , "Canciones obtenidas correctamente", Toast.LENGTH_SHORT).show();
//                    } else {
//                    Toast.makeText(SongPlaylistActivity.this , "Error al obtener canciones", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Song>> call, Throwable t) {
//                Log.e("API_ERROR", t.getMessage());
//                Toast.makeText(SongPlaylistActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }
//    public void mergeList(){
//        listView = findViewById(R.id.listView);
//        List<SongCheck> intersection = songsCheck.stream().filter(AllSongsCheck::contains).collect(Collectors.toList());
//        // Encontrar la intersección usando iteradores
////        for (SongCheck song : songsCheck) {
////            if (AllSongsCheck.contains(song)) {
////                intersection.add(song);
////            }
////        }
//        Log.e("Interseccion", intersection.toString());
//        // Convertir la lista de intersección en un mapa para un acceso más rápido
//        Map<Integer, SongCheck> interseccionMap = new HashMap<>();
//        for (SongCheck song : intersection) {
//            interseccionMap.put(song.getCancionid(), song);
//        }
//
//        // Reemplazo en la lista de canciones
//        List<SongCheck> updatedSongs = new ArrayList<>();
//        for (SongCheck song : AllSongsCheck) {
//            if (interseccionMap.containsKey(song.getCancionid())) {
//                updatedSongs.add(interseccionMap.get(song.getCancionid()));
//            } else {
//                updatedSongs.add(song);
//            }
//        }
//        AllSongsCheck = updatedSongs;
//        SongAdapter adapter = new SongAdapter(SongPlaylistActivity.this, AllSongsCheck,idPlaylist);
//        listView.setAdapter(adapter);
//
//    }
//}