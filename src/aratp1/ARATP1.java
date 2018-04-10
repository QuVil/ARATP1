/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aratp1;

import java.util.ArrayList;

/**
 *
 * @author p1507338
 */
public class ARATP1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Scan des ports...");
        ArrayList<Integer> portList = Com.scan(0, 65525);
        
    }
    
}
