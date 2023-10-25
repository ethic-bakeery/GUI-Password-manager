package Keyring;

import GUI.Autenticate;
import javax.swing.UnsupportedLookAndFeelException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nino
 */
public class Main {        
    public static void main(String[] args) {
        
        System.out.println("<----- KEYRING ----->");
        System.out.println("Versione: " + Keyring.version);    
        System.out.println("Author " + Keyring.author + "\n");
         
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            System.out.println("Theme error: " +ex.getMessage());
        }
        
        Autenticate auth = new Autenticate();
        auth.setLocationRelativeTo(null);
        auth.setVisible(true);
    }
}
