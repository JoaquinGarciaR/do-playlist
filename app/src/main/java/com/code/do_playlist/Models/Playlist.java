package com.code.do_playlist.Models;

import androidx.annotation.NonNull;

public class Playlist {
    private int playlistid;
    private String nombre;
    private String descripcion;

    // Constructor, getters y setters


    public Playlist(int playlistid, String nombre, String descripcion) {
        this.playlistid = playlistid;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getPlaylistid() {
        return playlistid;
    }

    public void setPlaylistid(int playlistid) {
        this.playlistid = playlistid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @NonNull
    @Override
    public String toString() {
        return "Playlist{" +
                "playlistid=" + playlistid +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}

