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
    public static void main(String[] args) {
        try{
            AccountFileManager fileManager = new AccountFileManager();
            fileManager.init();
            System.out.println("Hubbles Vault is Ready To Store Account Details");
        }catch(Exception e){
            System.err.println("Initialization Failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        boolean valid = false;
        System.out.println("===============================");
        System.out.println("Welcome To Hubble's Vault");
        System.out.println("[1] Login");
        System.out.println("[2] Create Account");
        System.out.println("[3] Exit");
        System.out.println("How Would You Like To Proceed (1/2/3):");
        
        while(valid == false){
           try{
               int choice = sc.nextInt();
        
                switch(choice){
                    case 1:
                        login();
                        valid = true;
                    case 2:
                        createAccount();
                        valid = true;
                    case 3:
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
        System.out.println("Enter Your Username: ");
        String username = sc.next();
    }
    
    public static void createAccount(){
        
    }
    
}
