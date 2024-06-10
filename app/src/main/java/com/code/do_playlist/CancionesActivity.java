package com.code.do_playlist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.code.do_playlist.Models.Song;
import com.code.do_playlist.api.ApiClient;
import com.code.do_playlist.api.CancionApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancionesActivity extends AppCompatActivity {

    private ListView listView;
    private List<Song> songs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_canciones);
        applyWindowInsets(findViewById(R.id.main));
        listView = findViewById(R.id.listView);
        initComponent();
        setListViewClickListener();
    }

    private void applyWindowInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initComponent() {
        setSupportActionBar(findViewById(R.id.toolbarTop));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        fetchSongs();
    }

    private void setListViewClickListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Song song = songs.get(position);
            Log.d("Song Selected", song.toString());
            Toast.makeText(CancionesActivity.this, song.getTitulo(), Toast.LENGTH_SHORT).show();
            // Implementar lógica adicional si es necesario
        });
    }

    private void setupListView(List<Song> songList) {
        ArrayAdapter<Song> adapter = new ArrayAdapter<Song>(this, R.layout.item_song, songList) {
            @SuppressLint("SetTextI18n")
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                ViewHolder holder;

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_song, parent, false);
                    holder = new ViewHolder();
                    holder.cancionId = convertView.findViewById(R.id.cancionId);
                    holder.titulo = convertView.findViewById(R.id.titulo);
                    holder.artista = convertView.findViewById(R.id.artista);
                    holder.album = convertView.findViewById(R.id.album);
                    holder.genero = convertView.findViewById(R.id.genero);
                    holder.duracion = convertView.findViewById(R.id.duracion);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                Song song = songList.get(position);

                holder.cancionId.setText("ID : " + song.getCancionid());
                holder.titulo.setText("Titulo : " + song.getTitulo());
                holder.artista.setText("Artista : " + song.getArtista());
                holder.album.setText("Album : " + song.getAlbum());
                holder.genero.setText("Genero : " + song.getGenero());
                holder.duracion.setText("Duracion : " + song.getDuracion());

                return convertView;
            }
        };

        listView.setAdapter(adapter);
    }

    private static class ViewHolder {
        TextView cancionId;
        TextView titulo;
        TextView artista;
        TextView album;
        TextView genero;
        TextView duracion;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchSongs() {
        CancionApiService apiService = ApiClient.getCancionApiService();
        Call<List<Song>> call = apiService.getSongList();

        Log.d("API_CALL", call.request().toString());

        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    songs = response.body();
                    Log.d("Canciones", songs.toString());
                    Toast.makeText(CancionesActivity.this, "Canciones obtenidas correctamente", Toast.LENGTH_SHORT).show();
                    setupListView(songs);
                } else {
                    Log.e("API_ERROR", "Error al obtener canciones: " + response.errorBody());
                    Toast.makeText(CancionesActivity.this, "Error al obtener canciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                Log.e("API_ERROR", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(CancionesActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

//public class CancionesActivity extends AppCompatActivity {
//
//    private ListView listView;
//    private List<Song> songs = new ArrayList<>(); // Inicializar con una lista vacía
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_canciones);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
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
////            getSupportActionBar().setIcon(R.mipmap.ic_logo_round);
//        }
//        getSongs(); // Llamar al método para obtener las canciones
//    }
//
//    private void setupListView(List<Song> songList) {
//        ArrayAdapter<Song> adapter = new ArrayAdapter<Song>(this, R.layout.item_song, songList) {
//
//            @SuppressLint("SetTextI18n")
//            @NonNull
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_song, parent, false);
//                Song song = songList.get(position);
//
//                Log.v("Cancion",song.toString());
//
//                TextView cancionId = view.findViewById(R.id.cancionId);
//                cancionId.setText("ID : " + song.getCancionid());
//
//                TextView titulo = view.findViewById(R.id.titulo);
//                titulo.setText("Titulo : " + song.getTitulo());
//
//                TextView artista = view.findViewById(R.id.artista);
//                artista.setText("Artista : " + song.getArtista());
//
//                TextView album = view.findViewById(R.id.album);
//                album.setText("Album : " + song.getAlbum());
//
//                TextView genero = view.findViewById(R.id.genero);
//                genero.setText("Genero : " + song.getGenero());
//
//                TextView duracion = view.findViewById(R.id.duracion);
//                duracion.setText("Duracion : " + song.getDuracion());
//
//                return view;
//            }
//        };
//
//        listView.setAdapter(adapter);
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
//    private void getSongs() {
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
//                    songs = response.body();
//                    Log.e("Songs", songs.toString());
//                    Toast.makeText(CancionesActivity.this, "Canciones obtenidas correctamente", Toast.LENGTH_SHORT).show();
//                    setupListView(songs); // Configurar el ListView con las canciones obtenidas
//                } else {
//                    Toast.makeText(CancionesActivity.this, "Error al obtener canciones", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Song>> call, Throwable t) {
//                Log.e("API_ERROR", t.getMessage());
//                Toast.makeText(CancionesActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    public void clicklers() {
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Song song = songs.get(position);
//                Log.e("API_ERROR", song.toString());
//                Toast.makeText(CancionesActivity.this, song.toString(), Toast.LENGTH_SHORT).show();
//
////                Intent intent = new Intent(this, SpecificPlaylist.class);
////                intent.putExtra("playlist", playlist);
////                startActivity(intent);
//            }
//        });
//    }
//}