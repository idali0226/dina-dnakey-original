package se.nrm.dina.dnakey.logic.util;
  
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.math.NumberUtils;


/**
 *
 * @author idali
 */
public class HelpClass {
    
    private static final String BLAST_DB_INFO_PATH = "bin/blastdbcmd -db  ";
    private static final String BLASTN = "bin/blastn";

    private static final String MB_BASE_URL = "http://morphbank.nrm.se/";
    private static final String MB_THUMB_URL = "http://images.morphbank.nrm.se";
    private static final String MB_IMAGE_URL = "http://morphbank.nrm.se/Browse/ByImage/";
    private static final String QUERY_THUMB = "&imgType=thumb";
    private static final String QUERY_ID = "?id=";

    private static final DateFormat DFT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSS'Z'");
    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

    public static String getBlastDBInfo(String basepath, String blastdbpath, String dbname) {
        StringBuilder sb = new StringBuilder();
        sb.append(basepath);
        sb.append(BLAST_DB_INFO_PATH);
        sb.append(blastdbpath);
        sb.append(dbname);
        sb.append(" -info");
        return sb.toString().trim();
    }

    /**
     * Build blastn command
     * 
     * @param fastfilepath
     * @param dbname
     * @param outputOption - blast result output option
     * @param basepath
     * @param dbpath
     * 
     * @return blastn command
     */
    public static String buildBlastCommand(String fastfilepath, String dbname, 
                                    String outputOption, String basepath, String dbpath) {
        StringBuilder command = new StringBuilder();
        command.append(getBlastnPath(basepath));
        command.append(" -query ");
        command.append(fastfilepath);
        command.append(" -db "); 
        command.append(dbpath); 
        command.append(dbname); 
        command.append(outputOption);

        System.out.println(command.toString());
        return command.toString();
    }
    
    public static String getBlastnPath(String basepath) {
        
        StringBuilder sb = new StringBuilder();
        sb.append(basepath);
        sb.append(BLASTN);
        return sb.toString().trim();
    }

    public static String getMorphybankImageURLById(String imageid) {
        StringBuilder sb = new StringBuilder();
        sb.append(MB_IMAGE_URL);
        sb.append(imageid);
        return sb.toString();
    }
    
    public static String getMorphybankThumbURLById(String imageid) {
        StringBuilder sb = new StringBuilder();
        sb.append(MB_THUMB_URL);
        sb.append(QUERY_ID);
        sb.append(imageid);
        sb.append(QUERY_THUMB);
        return sb.toString();
    }
     

    public static double intToDouble(int value) {
        return (double)value;
    }



    
    public static int stringToInteger(String strNumber) {
        return isNumeric(strNumber) ? Integer.parseInt(strNumber) : 0;
    }

    public static String stringToPercentageString(String strNumber) {
        if (isNumeric(strNumber)) {
            Double d = Double.valueOf(strNumber);
            d = Math.round(d * 100) / 100.0d;
            return String.valueOf(d);
        } else {
            return strNumber;
        }
    }

    public static boolean isNumeric(String arg) {
        return NumberUtils.isNumber(arg);
    }

    public static String dateToStringWithTime(Date date) {
        if (date == null) {
            return null;
        } else {
            return DFT.format(date);
        }
    }
    
        
    /**
     * Convert Date to String with "yyyy-MM-dd" format
     * 
     * @param date - Date
     * @return String
     */
    public static String dateToString(Date date) { 
        if(date == null) {
            return null;
        } else {
            return DF.format(date); 
        } 
    } 
 }
