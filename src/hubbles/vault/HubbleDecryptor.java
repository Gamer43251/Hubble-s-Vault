package hubbles.vault;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class HubbleDecryptor extends HubbleSVault {

    int index, count=3;
    private static final int SHIFT = 3;

    // Stores the email of the user who is currently logged in
    private static String loggedInEmail;

    // Sets the logged-in user's email (converted to lowercase)
    public static void setLoggedInEmail(String email){
        loggedInEmail = email.toLowerCase();
    }

    // Decrypts a string using a reverse Caesar shift of 3
    public static String decrypt(String encrypted) {
        if (encrypted == null) return null;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < encrypted.length(); i++) {
            char ch = encrypted.charAt(i);

            // If it's a letter, shift it back by 3
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                char dec = (char) ((ch - base - SHIFT + 26) % 26 + base);
                sb.append(dec);

            // If it's a number, shift it back by 3
            } else if (Character.isDigit(ch)) {
                char dec = (char) ((ch - '0' - SHIFT + 10) % 10 + '0');
                sb.append(dec);

            // Leave symbols unchanged
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    // Asks the user to re-enter their password before showing decrypted data
    public static void EnterPassword(){
        Scanner sc = new Scanner(System.in);
        int attempts = 0;

        // Allow up to 3 attempts
        while(attempts < 3) {
            System.out.print("Re-enter your account password to continue: ");
            String rePass = sc.nextLine();

            // Check password using the file manager
            if(HubbleSVault.fileManager.login(loggedInEmail, rePass)){
                System.out.println("Verified.");
                return;
            } else {
                attempts++;
                System.out.println("Incorrect Password. Attempts left: " + (3 - attempts));
            }
        }

        // If 3 failures, force logout
        System.out.println("Session Timed Out! Please Log In Again!");
        login();
    }

    // Reads a saved password file, decrypts each password, and prints them
    public static void printDecryptedFile(String filePath) {

        // Require re-authentication before showing passwords
        EnterPassword();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            System.out.println("Decrypted Passwords:");

            // Read each line from the file
            while ((line = br.readLine()) != null) {

                // Format: appName, username, encryptedPassword
                String[] parts = line.split(",", 3);
                if (parts.length < 3) continue;

                String appName = parts[0].trim();
                String username = parts[1].trim();
                String encryptedPassword = parts[2].trim();

                // Decrypt the password
                String decryptedPassword = decrypt(encryptedPassword);

                // Print in a readable format
                System.out.println(appName + " | user: " + username + " | password: " + decryptedPassword);
            }

        } catch (FileNotFoundException e) {
            System.out.println("No passwords found.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

}
