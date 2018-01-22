package se.nrm.dina.dnakey.logic;
 
//import javax.ejb.Stateless; 
import org.apache.commons.lang.StringUtils; 
import java.io.*; 
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.ws.alignment.qblast.BlastProgramEnum;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastService;
 

/**
 *
 * @author idali
 */
//@Stateless
@Slf4j
public class GenbankBlaster implements Serializable {
 
 
    public GenbankBlaster() { 
    }
  
    public String remoteGenbankBlast(String fastSequence) {
        log.info("remoteGenbankBlast");
        try {
            NCBIQBlastService service = new NCBIQBlastService();

            // set alignment options
            NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
            props.setBlastProgram(BlastProgramEnum.blastn);
            props.setBlastDatabase("nr");

            fastSequence = StringUtils.replace(fastSequence, "\n", "%0A");
            return service.sendAlignmentRequest(fastSequence, props);
        } catch (Exception ex) { 
            return null;
        } 
    } 
}
