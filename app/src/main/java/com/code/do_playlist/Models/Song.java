package com.code.do_playlist.Models;

import androidx.annotation.NonNull;

public class Song {
    private int cancionid;
    private String titulo;
    private String artista;
    private String album;
    private String genero;
    private String duracion;
//    private boolean isChecked;

    // Constructor, getters y setters


    public Song(int cancionid, String titulo, String artista, String album, String genero, String duracion) {
        this.cancionid = cancionid;
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.genero = genero;
        this.duracion = duracion;
//        this.isChecked = isChecked;
    }

    public int getCancionid() {
        return cancionid;
    }

    public void setCancionid(int cancionid) {
        this.cancionid = cancionid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public SongCheck getSongCheck(boolean check){
        return new SongCheck(this.cancionid,this.titulo,this.artista,this.album,this.genero,this.duracion, check);
    }

    @NonNull
    @Override
    public String toString() {
        return "Song{" +
                "cancionid=" + cancionid +
                ", titulo='" + titulo + '\'' +
                ", artista='" + artista + '\'' +
                ", album='" + album + '\'' +
                ", genero='" + genero + '\'' +
                ", duracion='" + duracion + '\'' +
                '}';
    }
}

