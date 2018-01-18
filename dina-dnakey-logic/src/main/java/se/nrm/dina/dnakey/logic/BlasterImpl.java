package se.nrm.dina.dnakey.logic;

//import java.util.ArrayList;
//import java.util.List;  
import javax.ejb.Stateless;
//import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import se.nrm.dina.dnakey.logic.metadata.BlastMetadata;
//import se.nrm.dina.dnakey.logic.metadata.DataFactory;
//import se.nrm.dina.dnakey.logic.metadata.MetadataDataFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.biojava3.ws.alignment.qblast.*;
import se.nrm.dina.dnakey.logic.util.HelpClass;

/**
 *
 * @author idali
 */
@Stateless
public class BlasterImpl implements Blaster, Serializable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    private final String outputStyle2 = " -task blastn -dust no -outfmt 5 -num_alignments 10 -num_descriptions 10 -parse_deflines";
    private static final String LOCAL_BLAST = "/usr/local/blast/";
    private static final String REMOTE_BLAST = "/home/admin/wildfly-8.1.0-0/dnakey/";

    private static final String LOCAL_BLAST_DB = "/usr/local/blast/db/";
    private static final String REMOTE_BLAST_DB = "/home/admin/wildfly-8.1.0-0/dnakey/db/";

    private static String BLAST_DB_PATH;
    private static String BLAST_PATH;
    private boolean isLocal = false;

    public BlasterImpl() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();

            isLocal = inetAddress.getHostName().toLowerCase().contains("ida");
            if (isLocal) {
                BLAST_PATH = LOCAL_BLAST;
                BLAST_DB_PATH = LOCAL_BLAST_DB;
            } else {
                BLAST_PATH = REMOTE_BLAST;
                BLAST_DB_PATH = REMOTE_BLAST_DB;
            }
        } catch (UnknownHostException ex) {
            logger.error(ex.getMessage());
        }
    }

//    @Override
//    public String getBlastDbInfo(String db) {
//        logger.info("getBlastDbInfo - db: {} dbpath : {}", db, BLAST_PATH);
//
//        InputStream is = null;
//        String command = HelpClass.getBlastDBInfo(BLAST_PATH, BLAST_DB_PATH, db);
//        try {
//            Process process = Runtime.getRuntime().exec(command);
//            is = process.getInputStream();
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(is));
//
//            String line;
//            while ((line = in.readLine()) != null) {
//                if (line.contains("sequences")) {
//                    return StringUtils.substringBefore(line, " sequences").trim();
//                }
//            }
//        } catch (IOException ex) {
//            logger.error(ex.getMessage());
//        } finally {
//            try {
//                if (is != null) {
//                    is.close();
//                }
//            } catch (IOException ex) {
//                logger.info(ex.getMessage());
//            }
//        }
//        return null;
//    }

    @Override
    public String remoteGenbankBlast(String fastSequence) {
        logger.info("remoteBlast : {}", fastSequence);
        try {
            NCBIQBlastService service = new NCBIQBlastService();

            // set alignment options
            NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
            props.setBlastProgram(BlastProgramEnum.blastn);
            props.setBlastDatabase("nr");

            fastSequence = StringUtils.replace(fastSequence, "\n", "%0A");
            return service.sendAlignmentRequest(fastSequence, props);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

//    @Override
//    public String blast(String fastafilepath, String dbname) {
//
//        logger.info("blastString - sequence: {} database: {}", fastafilepath, dbname);
//
//        InputStream is = null;
//        try {
//            Process process = Runtime.getRuntime().
//                        exec(HelpClass.buildBlastCommand(fastafilepath, dbname, outputStyle2, BLAST_PATH, BLAST_DB_PATH));
//            is = process.getInputStream();
// 
//            return IOUtils.toString(is, "UTF-8");
//        } catch (IOException ex) {
//            logger.error(ex.getMessage());
//        } finally {
//            File file = new File(fastafilepath);
//            file.delete();
//            try {
//                if(is != null) {
//                    is.close();
//                }
//            } catch (IOException ex) {
//                logger.info(ex.getMessage());
//            }
//        }
//        return "";
//    }
//    @Override
//    public BlastMetadata blastXML(String fastafilepath, String dbname) {
//
//        logger.info("blastXML - sequence: {} database: {}", fastafilepath, dbname);
//
//
//        InputStream is = null;
//        try {
//            Process process = Runtime.getRuntime().
//                    exec(HelpClass.buildBlastCommand(fastafilepath, dbname, outputStyle2, BLAST_PATH, BLAST_DB_PATH));
//            is = process.getInputStream();
//
////            String s = IOUtils.toString(is, "UTF-8");
////            System.out.println("s : " + s);
////            
//            logger.info("start");
////            BlastMetadata metadata = MetadataDataFactory.getInstance().buildBlastMetadataByString(s);
//            BlastMetadata metadata = DataFactory.getInstance().getBlastMetadataByInputStream(is);
//            logger.info("end");
//            return metadata;
//        } catch (IOException ex) {
//            logger.error(ex.getMessage());
//        } finally {
//            File file = new File(fastafilepath);
//            file.delete(); 
//            try {
//                if(is != null) {
//                    is.close();
//                } 
//            } catch (IOException ex) {
//                logger.info(ex.getMessage());
//            }
//        }
//        return null;
//    }
//    @Override
//    public List<BlastMetadata> multipleBlast(List<String> filePathList, String dbname) {
//        logger.info("multipleBlast");
//
//        List<BlastMetadata> resultList = new ArrayList<>();
//
//        InputStream is = null;
//        for (String filePath : filePathList) {
//            try {
//                Process process = Runtime.getRuntime().exec(HelpClass.buildBlastCommand(filePath, dbname, outputStyle2, BLAST_PATH, BLAST_DB_PATH));
//                is = process.getInputStream();
//
//                String s = IOUtils.toString(is, "UTF-8");
//                //            
//                logger.info("start");
//                BlastMetadata metadata = MetadataDataFactory.getInstance().buildBlastMetadataByString(s);
////                BlastMetadata metadata = DataFactory.getInstance().getBlastMetadataByInputStream(is);
//                logger.info("end");
//                resultList.add(metadata);
//            } catch (IOException ex) {
//                logger.error(ex.getMessage());
//            } finally {
//                File file = new File(filePath);
//                file.delete();
//                try {
//                    if(is != null) {
//                        is.close();
//                    } 
//                } catch (IOException ex) {
//                    logger.info(ex.getMessage());
//                }
//            }
//        }
//        return resultList;
//    }
//    @Override
//    public BlastMetadata remoteBlast(String fastSequence) {
//        
//        logger.info("remoteBlast : {}", fastSequence);
//        NCBIQBlastService service = new NCBIQBlastService();
//
//        // set alignment options
//        NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
//        props.setBlastProgram(BlastProgramEnum.blastn); 
//        props.setBlastDatabase("nr");  
////        props.setAlignmentOption(ENTREZ_QUERY, ">prok_genome test data from gi|15829254:10000-10400 Escherichia coli O157:H7, complete genome");
// 
//    
//        
//        // set output options
//        NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties(); 
//        outputProps.setDescriptionNumber(10);
//        outputProps.setAlignmentNumber(10);  
//         
//        InputStream is = null;
//        String rid = null;   
//        try {
//            // send blast request and save request id
//            rid = service.sendAlignmentRequest(fastSequence, props); 
//         System.out.println("rid : " + rid);
//            boolean isReady = false;
//            int count = 0;
//            // wait until results become available. Alternatively, one can do other computations/send other alignment requests
//            while (!isReady) { 
//                System.out.println("Waiting for results. Sleeping for 5 seconds.  " + count);
//                isReady = service.isReady(rid);
//                Thread.sleep(5000);
//                count++; 
//                if(count > 16) {
//                    break;
//                }
//            } 
//              
//            if(isReady) {
//                // read results when they are ready
//                is = service.getAlignmentResults(rid, outputProps); 
//                String s = IOUtils.toString(is, "UTF-8");
//                
//                System.out.println("s : " + s);
//                
//                
//                
////                BlastMetadata metadata = DataFactory.getInstance().getBlastMetadataByInputStream(is);
////                return metadata;
//                return MetadataDataFactory.getInstance().buildBlastMetadataByString(s); 
//            }
//            
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        } finally {  
//            try {
//                if(is != null) {
//                    is.close();
//                } 
//                // delete given alignment results from blast server (optional operation)
//                service.sendDeleteRequest(rid);
//            } catch (IOException ex) {
//                logger.error(ex.getMessage());
//            }
//        }
//        return null;
//    }
}
