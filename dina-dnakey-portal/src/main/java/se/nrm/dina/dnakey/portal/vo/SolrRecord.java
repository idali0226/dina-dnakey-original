package se.nrm.dina.dnakey.portal.vo;
  
import java.util.ArrayList;
import java.util.Date;
import java.util.List; 
import org.apache.commons.lang.StringUtils; 
import org.apache.solr.client.solrj.beans.Field; 
import se.nrm.dina.dnakey.logic.vo.MorphyBankImage;

/**
 *
 * @author idali
 */ 
public class SolrRecord  {
       
    @Field("id")
    String id;
    
    @Field("geo") 
    String geo;
    
    @Field("geopoint")
    String geopoint;
     
    @Field("cn")
    String catalogNum;
       
    @Field("ftx")
    String fullname; 
           
    @Field("sd")
    Date startDate;
    
    @Field("lc") 
    String locality;
     
    @Field("lat")
    String lat;
     
    @Field("lnt")
    String lnt;
     
    @Field("lat1")
    Double lat1;
     
    @Field("lnt1")
    Double lnt1;
     
    @Field("cm")
    String[] commonNames;
     
    @Field("ht")
    String highTaxon;
     
    @Field("tsn")
    String type;
     
    @Field("cln") 
    String collectionId;
    
    @Field("clnm") 
    String collectionName;
  
    @Field("ctd")
    Date catalogedDate;
 
    @Field("sym")
    String[] synomy;
    
    String sym;
     
    @Field("rm")
    String[] remarks;
    
    @Field("asr")
    String[] accessionRemarks;
    
    @Field("auth")
    String[] author;
    
    @Field("col")
    String[] collectors;
      
    @Field("cy")
    String country;
    
    @Field("ct") 
    String continent; 
     
    @Field("dn")
    String determiner;
    
    @Field("mbid")
    String mbid;
    
    @Field("img")
    String[] imgMbids;
     
    @Field("sfn")
    String stationFieldNumber;
    
    List<MorphyBankImage> images;
    boolean imageExist;
    boolean displayMap;
    boolean displayImage;
     
    
    String institution = "Naturhistoriska riksmuseet"; 
    String contient1;
    boolean selected = false;

    public String getContient1() {
        return contient1;
    }

    public void setContient1(String contient1) {
        this.contient1 = contient1;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(String geopoint) {
        this.geopoint = geopoint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLat1() {
        return lat1;
    }

    public void setLat1(Double lat1) {
        this.lat1 = lat1;
    }

    public Double getLnt1() {
        return lnt1;
    }

    public void setLnt1(Double lnt1) {
        this.lnt1 = lnt1;
    }
     
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
  
    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
 
    public Date getCatalogedDate() {
        return catalogedDate;
    }

    public void setCatalogedDate(Date catalogedDate) {
        this.catalogedDate = catalogedDate;
    }
 
    public String getCatalogNum() {
        return catalogNum;
    }
  
    public String getHighTaxon() {
        return highTaxon;
    }

    public void setHighTaxon(String highTaxon) {
        this.highTaxon = highTaxon;
    }
 
    public String getLocality() {
        return locality;
    }

    public String[] getCommonNames() {
        return commonNames;
    }

    public void setCommonNames(String[] commonNames) {
        this.commonNames = commonNames;
    }
    
    public String getCommonName() {
        return StringUtils.join(commonNames, ", ");
    }
  
 
    public Date getStartDate() {
        return startDate;
    }
  
    public String getStationFieldNumber() {
        return stationFieldNumber;
    }

    public void setStationFieldNumber(String stationFieldNumber) {
        this.stationFieldNumber = stationFieldNumber;
    }
 
    public String getDeterminer() {
        return determiner;
    }

    public void setDeterminer(String determiner) {
        this.determiner = determiner;
    }
  
    public String getFullname() {
        return fullname;
    }
   
    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }
 
    public String[] getImgMbids() {
        return imgMbids;
    }

    public void setImgMbids(String[] imgMbids) {
        this.imgMbids = imgMbids;
    }
  
    public String getCollectionId() {
        return collectionId;
    }
    
    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }
 
    public String getSym() {  
        if(synomy != null) {
            return StringUtils.join(synomy, ", ");  
        }
        return null; 
    }

    public void setSym(String sym) {
        this.sym = sym;
    }
 
    public String[] getSynomy() { 
        return synomy;
    }

    public void setSynomy(String[] synomy) {
        this.synomy = synomy;
    }
      
    public String getDisciplineIconId() {
        return collectionId;
    }


    public String getName() {
        return commonNames == null ? fullname : fullname + " [" + StringUtils.join(commonNames, ", ") + "] ";
    }
  
    public String[] getRemarks() {
        return remarks;
    }

    public void setRemarks(String[] remarks) {
        this.remarks = remarks;
    }
 
    public String getRemarkList() {
        return StringUtils.join(remarks, ". ");  
    }
   
    public String getLat() {
        return lat;
    }
 
    public String getLnt() {
        return lnt;
    }

    public String[] getAccessionRemarks() {
        return accessionRemarks;
    }

    public void setAccessionRemarks(String[] accessionRemarks) {
        this.accessionRemarks = accessionRemarks;
    }
  
    public String getAuth() { 
        return StringUtils.join(author, ", "); 
    }
        
    public String[] getAuthor() {
        return author;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }
    
    public String getContinent() {
        return continent;
    }
     
    public String getCollector() {   
        return StringUtils.join(collectors, ", ");  
    }
     
    public String[] getCollectors() {
        return collectors;
    }

    public void setCollectors(String[] collectors) {
        this.collectors = collectors;
    }
    

    public void setContinent(String continent) {
        this.continent = continent;
    }
     
    public String getCountry() {
        return country;
    } 
    
    public void setCountry(String country) {
        this.country = country;
    }
 
    public List<MorphyBankImage> getImages() {
        
        images = new ArrayList<>();
        MorphyBankImage img;
        for(String imgMBid : imgMbids) {  
            img = new MorphyBankImage(Integer.parseInt(imgMBid), "http://morphbank.nrm.se/?id=" + imgMBid,
                                        catalogNum, fullname, "", 
                                        "http://images.morphbank.nrm.se/?id=" + imgMBid + "&imgType=JPG");
            
             
            images.add(img);
        } 
        return images;
    }

    public void setImages(List<MorphyBankImage> images) {
        this.images = images;
    } 
     
    public List<String> getThumbs() {
        List<String> thumbs = new ArrayList<>();
        
        for(String imgMBid : imgMbids) { 
            thumbs.add("http://images.morphbank.nrm.se/?id=" + imgMBid + "&imgType=thumb");
        }
        return thumbs;  
    } 
 
    public boolean isImageExist() { 
        return mbid != null;
    }

    public void setImageExist(boolean imageExist) {
        this.imageExist = imageExist;
    }
 
    public boolean isDisplayMap() {
        return displayMap;
    }

    public void setDisplayMap(boolean displayMap) { 
        this.displayMap = displayMap;
    }
 
    public boolean isDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(boolean displayImage) {
        this.displayImage = displayImage;
    }
     
    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public boolean isSelected() {
        return selected;
    }
 
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

 
 
    
    
 
    public int getCoordinate() { 
        
        if(lat != null && lat.length() > 0) {
            if(lnt != null && lnt.length() > 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if(lnt != null && lnt.length() > 0) {
                return 2;
            }
            return 3;
        }
    }
    
 
    
    
    
    public String getNewLine() {
        return "\n";
    }
    
    public String getNewTab() {
        return "\t";
    }
 
    @Override
    public String toString() {
        return "Detail : [ " + catalogNum + " : family : [ " + highTaxon + " ] : "  + locality + " ] : Collection : " + collectionId;
    }
}
