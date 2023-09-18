package frgp.utn.edu.ar.entidades;

import android.location.Location;

public class Ubicacion {
    private double lat;
    private double lng;
    private double alt;

    public Ubicacion(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        alt = location.getAltitude();
    }
    public Ubicacion(double lat, double lng, double alt) {
        this.lat = lat;
        this.lng = lng;
        this.alt = alt;
    }

    public Ubicacion() {
    }

    @Override
    public String toString() {
        return lat + " - " + lng + " | " + alt + "Mts";
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }
}
