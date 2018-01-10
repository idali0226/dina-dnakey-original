/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dnakey.portal.beans.geo;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.dnakey.logic.geomap.GeoMapDataSource;
import se.nrm.dina.dnakey.logic.vo.GeoMapData;

/**
 *
 * @author idali
 */
@Named("map")
@ApplicationScoped 
@Slf4j
public class GeoMap implements Serializable {
    
    private List<GeoMapData> geoMap;
    
    @Inject
    private GeoMapDataSource geo;
    
    public GeoMap() { 
    }
    
    @PostConstruct
    public void init() { 
        log.info("init: {}", geoMap == null);
        if(geoMap == null) {
            geoMap = geo.getGeoMapData(); 
        } 
    }
    
    public List<GeoMapData> getGeoMap() { 
        return geoMap;
    }  
}
