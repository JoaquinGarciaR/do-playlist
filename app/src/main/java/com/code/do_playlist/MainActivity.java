package com.code.do_playlist;

import android.content.Intent;
import android.os.Bundle;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.code.do_playlist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
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
        }
    }

    private void setupUIComponents() {
        binding.songBtn.setOnClickListener(v -> goToSongs());
        binding.playlistBtn.setOnClickListener(v -> goToPlaylist());
    }

    private void goToSongs() {
        Intent intent = new Intent(this, CancionesActivity.class);
        startActivity(intent);
    }

    private void goToPlaylist() {
        Intent intent = new Intent(this, PlaylistActivity.class);
        startActivity(intent);
    }
}
//public class MainActivity extends AppCompatActivity {
//
//    private ActivityMainBinding binding;
//    private Button playlistsBtn;
//    private Button cancionesBtn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        toolbarSetUp();
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//
//    protected void toolbarSetUp(){
//
//        setSupportActionBar(findViewById(R.id.toolbarTop));
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
////            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////            getSupportActionBar().setIcon(R.mipmap.ic_logo_round);
//        }
//        cancionesBtn = binding.songBtn;
//        cancionesBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToSongs();
//            }
//        });
//
//        playlistsBtn = binding.playlistBtn;
//        playlistsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToPlaylist();
//            }
//        });
//    }
//
//
//    protected void goToSongs(){
//        Intent intent = new Intent(this, CancionesActivity.class);
//        startActivity(intent);
//    }
//
//    protected void goToPlaylist(){
//        Intent intent = new Intent(this, PlaylistActivity.class);
//        startActivity(intent);
//    }
//
//}