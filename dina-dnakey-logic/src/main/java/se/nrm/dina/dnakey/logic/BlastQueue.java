/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.dnakey.logic;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList; 
import java.util.List; 
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future; 
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService; 
import org.slf4j.LoggerFactory;
import se.nrm.dina.dnakey.logic.metadata.BlastMetadata; 

/**
 * BlastQueue uses a ManagedExecutorService to submit task in queue 
 * 
 * @author idali
 */
@Stateless
public class BlastQueue {
    
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass()); 
 
    private boolean isLocal = false;
     
//    @Resource(lookup = "concurrent/__highPriority")                               // glassfish
    @Resource(lookup="java:jboss/ee/concurrency/executor/default")                  // wildfly
    ManagedExecutorService highPriority;

    public BlastQueue() { 
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost(); 
            isLocal = inetAddress.getHostName().toLowerCase().contains("ida");  
        } catch (UnknownHostException ex) {
            logger.error(ex.getMessage());
        }
    }

    public BlastQueue(ManagedExecutorService highPriority) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
            isLocal = inetAddress.getHostName().toLowerCase().contains("ida");
        } catch (UnknownHostException ex) {
            logger.error(ex.getMessage());
        }
        this.highPriority = highPriority;
    }

    /**
     * run task in queue
     * @param filePathList
     * @param dbname
     * @return List of results
     */
    public List<BlastMetadata> run(List<String> filePathList, String dbname) {
        
        logger.info("run");
        
        List<BlastMetadata> resultList = new ArrayList<>();
        
        List<Future> list = new ArrayList<>(); 
        filePathList
                .parallelStream()
                .forEachOrdered(x -> {  
                    list.add(highPriority.submit(new BlastCallableTask(x, dbname, isLocal)));
                });
         
//        for (String filePath : filePathList) {
//            result = highPriority.submit(new BlastCallableTask(filePath, dbname, isLocal));   
//            list.add(result);
//        } 
        
        boolean finished = false;
        while(!finished) {  
            finished = isDone(list); 
        } 
        
        list.stream().forEach((future) -> {
            try {
                resultList.add((BlastMetadata) future.get());
            } catch (InterruptedException | ExecutionException ex) {
                logger.error(ex.getMessage());
            }
        });
        return resultList;  
    }
    
    private boolean isDone(List<Future> list) {
        return list.stream().noneMatch((f) -> (!f.isDone()));
    }
}
