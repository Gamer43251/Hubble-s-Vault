package hubbles.vault;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class HubbleDecryptor extends HubbleSVault {

    int index, count=3;
    private static final int SHIFT = 3;

    // store who is logged in WITHOUT touching AccountFileManager
    private static String loggedInEmail;

    public static void setLoggedInEmail(String email){
        loggedInEmail = email.toLowerCase();
    }

    public static String decrypt(String encrypted) {
        if (encrypted == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encrypted.length(); i++) {
            char ch = encrypted.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                char dec = (char) ((ch - base - SHIFT + 26) % 26 + base);
                sb.append(dec);
            } else if (Character.isDigit(ch)) {
                char dec = (char) ((ch - '0' - SHIFT + 10) % 10 + '0');
                sb.append(dec);
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static void EnterPassword(){
        Scanner sc = new Scanner(System.in);
        int attempts = 0;

        while(attempts < 3) {
            System.out.print("Re-enter your account password to continue: ");
            String rePass = sc.nextLine();

            if(HubbleSVault.fileManager.login(loggedInEmail, rePass)){
                System.out.println("Verified.");
                return; 
            } else {
                attempts++;
                System.out.println("Incorrect Password. Attempts left: " + (3 - attempts));
            }
        }

        System.out.println("Session Timed Out! Please Log In Again!");
        login();
    }

    public static void printDecryptedFile(String filePath) {

        // require re-auth before exposing decrypted passwords
        EnterPassword();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            System.out.println("Decrypted Passwords:");
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length < 3) continue;
                String appName = parts[0].trim();
                String username = parts[1].trim();
                String encryptedPassword = parts[2].trim();
                String decryptedPassword = decrypt(encryptedPassword);
                System.out.println(appName + " | user: " + username + " | password: " + decryptedPassword);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No passwords found.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    
}

