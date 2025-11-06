/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hubbles.vault;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author dreel
 */
public class HubblesVault {
    static Scanner sc = new Scanner(System.in);
    /**
     * @param args the command line arguments
     */
    
    static AccountFileManager fileManager = null;
    
    public static void main(String[] args) {
        
        try{
            fileManager = new AccountFileManager();
            fileManager.init();
            //System.out.println("Hubbles Vault is Ready To Store Account Details"); Utilised for Testing
        }catch(Exception e){
            System.err.println("Initialization Failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        startupMenu();
        
    }
    
    public static void startupMenu(){
        boolean valid = false;
        System.out.println("===============================");
        System.out.println("Welcome To Hubble's Vault");
        System.out.println("[1] Login");
        System.out.println("[2] Exit");
        System.out.println("How Would You Like To Proceed (1|2):");
        
        while(valid == false){
           try{
               int choice = sc.nextInt();
        
                switch(choice){
                    case 1:
                        login();
                        valid = true;
                    case 2:
                        System.exit(0);
                        valid = true;
                }
                
           }catch(InputMismatchException e){
               System.out.println("Please Enter A Valid Integer");
               valid = false;
               
           }
        }
    }
    
    public static void login(){
        System.out.println("Enter Your Email: ");
        String email = sc.next();
        System.out.println("Enter Your Password");
        String password = sc.next();
        
        if(fileManager.login(email, password)){
            System.out.println("Access Granted");
        }else{
            System.out.println("Invalid Credentials Please Try Again");
        }
    }
    
    
}
