package com.example.barcodeprice;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "captura_table")
public class Captura {
    private String lat, lon, barcode, note;

    @PrimaryKey(autoGenerate = true)
    private int id;

    public Captura(String lat, String lon, String barcode, String note) {
        this.lat = lat;
        this.lon = lon;
        this.barcode = barcode;
        this.note = note;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }


}
