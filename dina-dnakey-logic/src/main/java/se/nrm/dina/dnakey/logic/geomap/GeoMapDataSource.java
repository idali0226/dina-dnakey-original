/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dnakey.logic.geomap;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.dnakey.logic.config.ConfigProperties;
import se.nrm.dina.dnakey.logic.vo.GeoMapData;

/**
 *
 * @author idali
 */
@Slf4j
public class GeoMapDataSource implements Serializable {
    
//    private final String BASE_COORDINATES_LOCAL_FILE_PATH = "/Users/idali/git-idali/data/dnakey/"; 
//    private final String BASE_COORDINATES_REMOTE_FILE_PATH = "/opt/data/dnakey/data/";
      
    private final String SEPARATOR = "\t";
    
//    private String coordinates_file_path;
    private List<GeoMapData> list;
     
    @Inject
    private ConfigProperties config;
     
    public GeoMapDataSource() { 
    }

    /**
     * Start ejb every time deploy into glassfish or glassfish start
     */
    @PostConstruct
    void init() {  
        buildGeoMapData();
    }
 
    private GeoMapData mapperToGeoMapData(String value) {
        String[] geo = value.split(SEPARATOR);
        return new GeoMapData(Double.parseDouble(geo[2]), Double.parseDouble(geo[1]), Integer.parseInt(geo[0]));
    }
    
    public List<GeoMapData> getGeoMapData() {
        return list;
    }
      
    private void buildGeoMapData() {
        log.info("buildGeoMapData");
         
        list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(config.getGeoDataFilePath()))) { 
            list = stream.map(l -> mapperToGeoMapData(l))
                                .collect(Collectors.toList());    
        } catch (IOException e) {
        } 
    }
    
}
