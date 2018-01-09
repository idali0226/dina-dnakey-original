/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.dnakey.logic;

import org.junit.After; 
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import se.nrm.dina.dnakey.logic.metadata.BlastMetadata;

/**
 *
 * @author idali
 */
public class BlastCallableTaskTest {
    
    public BlastCallableTaskTest() {
    }
 
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of call method, of class BlastCallableTask.
     * @throws java.lang.Exception
     */
    @Test
    public void testCall() throws Exception {
        System.out.println("call");
        
        BlastCallableTask testInstance = Mockito.mock(BlastCallableTask.class); 
     
        BlastMetadata result = testInstance.call();
        Mockito.verify(testInstance).call();
       
    }
    
}
