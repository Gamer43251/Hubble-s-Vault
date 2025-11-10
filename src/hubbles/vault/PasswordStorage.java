/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hubbles.vault;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author canno
 */
public class PasswordStorage extends HubbleDecryptor{
    private static String vaultFileName;
    private static final int shift = 3;

    public static void setVaultFileName(String vaultFileName) {
        PasswordStorage.vaultFileName = vaultFileName;
    }
    
    public static String encryptPassword(String passwords){
        StringBuilder encrypted = new StringBuilder();
        for(int i = 0; i < passwords.length(); i++){
            char ch = passwords.charAt(i);
            if (Character.isLetter(ch)){
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                ch = (char) ((ch - base + shift) % 26 + base);
            }else if(Character.isDigit(ch)){
                ch = (char) ((ch - '0' + shift) % 10 + '0');
            }
            encrypted.append(ch);
        }
        return encrypted.toString();
    }
    
    public static void addPassword(String appName, String username, String passwords){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(vaultFileName, true))){
            System.out.println("Vault file path: " + new File(vaultFileName).getAbsolutePath());
            String encryptedPassword = encryptPassword(passwords);
            String account = appName + "," + username + "," + encryptedPassword;
            writer.write(account);
            writer.newLine();
            System.out.println("Password encrypted and added successfully!");
        }catch(IOException e){
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

       public static void viewEncryptedPasswords(){
        try(BufferedReader reader = new BufferedReader(new FileReader(vaultFileName))){
            String account;
            System.out.println("Encrypted Passwords");
            while((account = reader.readLine()) != null){
                System.out.println(account);
            }
        }catch(FileNotFoundException e) {
            System.out.println("No passwords found.");
        }catch(IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    
    public void deletePassword(String appName){
        File file = new File(vaultFileName);
        File tempFile = new File(file.getParent(), "temp_vault.txt");
        boolean deleted = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))){
            String pass;
            while ((pass = br.readLine()) != null){
                String[] code = pass.split(",");
                if (code.length >= 1 && code[0].equalsIgnoreCase(appName)){
                    deleted = true;
                    continue;
                }
                bw.write(pass);
                bw.newLine();
            }
        }catch(IOException e){
            System.out.println("Error deleting password: " + e.getMessage());
        }
        if(file.delete() && tempFile.renameTo(file)){
            if(deleted){
                System.out.println("Password entry for " + appName + " deleted successfully!!!");
            }else{
                System.out.println("No entry found for " + appName + ".");
            }
        }else{
            System.out.println("Error updating file.");
        }
    }
       
    public void vaultMenu(){
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.print("\nPassword Vault Menu\n1. Add Password\n2. Delete Password\n3. View Encrypted Passwords\n4. View Decrypted Passwords\n5. Logout\nEnter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter Application Name: ");
                    String appName = sc.nextLine();
                    System.out.print("Enter Username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();
                    addPassword(appName, username, password);
                    break;

                case 2:                          
                    System.out.print("Enter Application Name to Delete: ");
                    String delApp = sc.nextLine();
                    deletePassword(delApp);
                    break;

                case 3:
                    viewEncryptedPasswords();
                    break;

                case 4:
                   HubbleDecryptor.printDecryptedFile(vaultFileName);
                    break;
                    
                 case 5:
                    break;

                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 5);

        sc.close();
    }
}
