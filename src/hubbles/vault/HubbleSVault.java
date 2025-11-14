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
public class HubbleSVault {
    static Scanner sc = new Scanner(System.in);
    /**
     * @param args the command line arguments
     */
    
    static AccountFileManager fileManager = null;
    
    public static void main(String[] args) {
        
        try{
            fileManager = new AccountFileManager(); 
            fileManager.init(); // initialises fileManager instantiation
        }catch(Exception e){ // if filemanager cant be initialised prints a error message
            System.err.println("Initialization Failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        while (true) {
            int choice = startupMenu(); //handles users choice in startup menu
            switch (choice) {
                case 1:
                    login(); //calls login method if user entered 1 
                    break; // return to menu after login attempt
                case 2:
                    System.out.println("Goodbye!"); // exits application if user entered 2
                    return;
                default:
                    // shouldn’t happen because we validate input
                    break;
            }
        }
    }
    
    private static int startupMenu() { // Prints startup menu prompting user to enter credentials
        System.out.println("===============================");
        System.out.println("Welcome To Hubble's Vault");
        System.out.println("[1] Login");
        System.out.println("[2] Exit");
        System.out.print("How would you like to proceed (1|2): ");

        while (true) { // while loop to get users choice
            String line = sc.nextLine().trim();
            try {
                int choice = Integer.parseInt(line);
                if (choice == 1 || choice == 2) {
                    return choice;
                }
                System.out.print("Please enter 1 or 2: ");
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer (1 or 2): ");
            }
        }
    }
    
    public static void login(){ // method that handles logging in 
        System.out.println("Enter Your Email: ");
        String email = sc.next();
        System.out.println("Enter Your Password");
        String password = sc.next();
        
         if(fileManager.login(email, password)){ 
             
            // *** THIS IS THE IMPORTANT LINE ***
            HubbleDecryptor.setLoggedInEmail(email);

            System.out.println("Access Granted"); 
            
            email = email.split("@")[0];//Ignore characters @ and after
            String vaultPath = fileManager.getVaultDir().resolve(email + "_vault.txt").toString();//Make the Vault Files in the Vault Directory
            PasswordStorage.setVaultFileName(vaultPath);//Set the Vault File Name in Password Storage Class         
            PasswordStorage ps = new PasswordStorage();//Create an object 
            ps.vaultMenu();//Links to the vaultMenu() in PasswordStorage Class
        }else{
            System.out.println("Invalid Credentials Please Try Again");
        }
        
    }
    
    
}
