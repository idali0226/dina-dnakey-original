package se.nrm.dina.dnakey.portal.vo;

import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Slf4j
public class ResultBean implements Serializable {
    
    private final String query;
    private final String catalogNumber;
    private final String scientificName;
    private final String genbankAcc;
    private final String boldId;
    private final String identity;
    private final String mark;
    private final double score;
    private final double evalue;
    private final String collection;
    private final String swedishName;
    private final String locality;
    private final String coordinates;
    private final String date;
    private final String collector;
    
    public ResultBean(final String query, final String catalogNumber, final String scientificName, 
                      final String genbankAcc, final String boldId, final String identity, 
                      final String mark, final double score, final double evalue, final String collection,
                      final String swedishName, final String locality, final String coordinates, 
                      final String date, final String collector) {
  
        this.query = query;
        this.catalogNumber = catalogNumber;
        this.scientificName = scientificName;
        this.genbankAcc = genbankAcc;
        this.boldId = boldId;
        this.identity = identity;
        this.mark = mark;
        this.score = score;
        this.evalue = evalue;
        this.collection = collection;
        this.swedishName = swedishName;
        this.locality = locality;
        this.coordinates = coordinates;
        this.date = date;
        this.collector = collector;
    }

    public String getBoldId() {
        return boldId;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public String getCollection() {
        return collection;
    }

    public String getCollector() {
        return collector;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getDate() {
        return date;
    }

    public double getEvalue() {
        return evalue;
    }

    public String getGenbankAcc() {
        return genbankAcc;
    }

    public String getIdentity() {
        return identity;
    }

    public String getLocality() {
        return locality;
    }

    public String getQuery() {
        return query;
    }

    public String getScientificName() {
        return scientificName;
    }

    public double getScore() {
        return score;
    }

    public String getSwedishName() {
        return swedishName;
    }

    public String getMark() {
        return mark;
    }  
}
