/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hubbles.vault;
//Imports
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
public class PasswordStorage extends HubbleDecryptor{//PasswordStorage Class
    private static String vaultFileName;
    private static final int shift = 3;

    public static void setVaultFileName(String vaultFileName) {//Setter for VaultFileName
        PasswordStorage.vaultFileName = vaultFileName;
    }
    
    public static String encryptPassword(String passwords){//Encrypt Password Method using Caesar Cipher
        StringBuilder encrypted = new StringBuilder();
        for(int i = 0; i < passwords.length(); i++){
            char ch = passwords.charAt(i);
            if (Character.isLetter(ch)){//Shifts letters around keeping case sensitivity
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                ch = (char) ((ch - base + shift) % 26 + base);
            }else if(Character.isDigit(ch)){//Shifts Digits around
                ch = (char) ((ch - '0' + shift) % 10 + '0');
            }
            encrypted.append(ch);//Appends Shifted Characters together
        }
        return encrypted.toString();//Returns Encrypted Password
    }
    
    public static void addPassword(String appName, String username, String passwords){//Add Password Method
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(vaultFileName, true))){
            System.out.println("Vault file path: " + new File(vaultFileName).getAbsolutePath());
            String encryptedPassword = encryptPassword(passwords);//Calls the encryptPassword() method to Encrpt Password
            String account = appName + "," + username + "," + encryptedPassword;
            writer.write(account);//Data added to the Text File
            writer.newLine();
            System.out.println("Password encrypted and added successfully!");
        }catch(IOException e){//Called if Error with the Text File
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

       public static void viewEncryptedPasswords(){//View Encryted Versions of Passwords Method
        try(BufferedReader reader = new BufferedReader(new FileReader(vaultFileName))){//Reads the Account's Password Vault Text File
            String account;
            System.out.println("Encrypted Passwords");
            while((account = reader.readLine()) != null){//Called if there is Passwords Stored in The Password Vault
                System.out.println(account);
            }
        }catch(FileNotFoundException e) {//Called if Password Valut is Empty
            System.out.println("No passwords found.");
        }catch(IOException e) {//Called if Error with the Text File
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    
    public void deletePassword(String appName){//Delete Password Method
        File file = new File(vaultFileName);
        File tempFile = new File(file.getParent(), "temp_vault.txt");//Temp File for Updates
        boolean deleted = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))){
            String pass;
            while ((pass = br.readLine()) != null){
                String[] code = pass.split(",");
                if (code.length >=                                                                      1 && code[0].equalsIgnoreCase(appName)){//Checks if App Name inputted matches
                    deleted = true;
                    continue;
                }
                bw.write(pass);
                bw.newLine();
            }
        }catch(IOException e){//Caled if Error Deleting Password
            System.out.println("Error deleting password: " + e.getMessage());
        }
        if(file.delete() && tempFile.renameTo(file)){//Replace Original File with Upadated File
            if(deleted){// App Name Password Data Deleted Succcessfully
                System.out.println("Password entry for " + appName + " deleted successfully!!!");
            }else{//App Name to be Deleted Does not Exixt
                System.out.println("No entry found for " + appName + ".");
            }
        }else{//If Errorr occuredd
            System.out.println("Error updating file.");
        }
    }
       
    public void vaultMenu(){//Vault Menu Option Method
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.print("\nPassword Vault Menu\n1. Add Password\n2. Delete Password\n3. View Encrypted Passwords\n4. View Decrypted Passwords\n5. Exit\nEnter choice: ");
            choice = sc.nextInt();//Choice input
            sc.nextLine();//clear buffer

            switch (choice) {
                case 1://Links to Add Password Option
                    System.out.print("Enter Application Name: ");
                    String appName = sc.nextLine();//App Name input
                    System.out.print("Enter Username: ");
                    String username = sc.nextLine();//Username input
                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();//Password input
                    addPassword(appName, username, password);//Calls addPassword()
                    break;

                case 2://Links to Delete Password Option                   
                    System.out.print("Enter Application Name to Delete: ");
                    String delApp = sc.nextLine();//App Name to be Deleted input
                    deletePassword(delApp);//Calls deletePassword()
                    break;

                case 3://Links to View Encrypted Passwords Option
                    viewEncryptedPasswords();//Calls viewEncryptedPasswords()
                    break;

                case 4:
                   HubbleDecryptor.printDecryptedFile(vaultFileName);
                    break;
                    
                 case 5://Links to Logout Option
                     login();//Calls login()
                    break;

                default://Called if invalid choice is Entered
                    System.out.println("Invalid choice");
            }
        } while (choice != 5);

        sc.close();
    }
}
