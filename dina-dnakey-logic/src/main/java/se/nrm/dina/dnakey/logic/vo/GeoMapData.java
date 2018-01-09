/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dnakey.logic.vo;

/**
 *
 * @author idali
 */
public final class GeoMapData {
    
    private final double latitude;
    private final double longitude;
    private final int count;
    
    public GeoMapData(double latitude, double longitude, int count) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.count = count;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getCount() {
        return count;
    }
    
    
}
