package hubbles.vault;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HubbleFileReader {

    // Reads an encrypted file and returns all its text as one big string
    public String readEncryptedFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Read the file line by line
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace(); // Shows the error if something goes wrong
        }

        return content.toString();
    }


    // Picks the correct user vault file, reads it, and prepares to decrypt it
    public void decryptForLoggedInUser(String loggedInUser) {
        HubbleFileReader reader = new HubbleFileReader();
        HubbleDecryptor decryptor = new HubbleDecryptor();

        String filePath = "";

        // Match username to its vault file
        if (loggedInUser.equalsIgnoreCase("TheCrimsonShadow")) {
            filePath = "HubblesVaultData\\Vaults\\TheCrimsonShadow_vault.txt";
        } else if (loggedInUser.equalsIgnoreCase("CosmicStorm89")) {
            filePath = "HubblesVaultData\\\\Vaults\\\\CosmicStorm89_vault.txt";
        } else if (loggedInUser.equalsIgnoreCase("ViridianHelmet64")) {
            filePath = "HubblesVaultData\\\\Vaults\\\\ViridianHelmet64_vault.txt";
        } else if (loggedInUser.equalsIgnoreCase("JuniperLanding34")) {
            filePath = "HubblesVaultData\\\\Vaults\\\\JuniperLanding34_vault.txt";
        } else if (loggedInUser.equalsIgnoreCase("PrettyLittleDevil12")) {
            filePath = "HubblesVaultData\\\\Vaults\\\\PrettyLittleDevil12_vault.txt";
        } else {
            System.out.println("Error: There is no user in the system with that username!");
            return;
        }

        // Read the encrypted file
        String encryptedData = reader.readEncryptedFile(filePath);

        // (Decryption call commented out in your version)
        // String decryptedData = decryptor.decrypt(encryptedData, 3);

        System.out.println("\n--- Decrypted data for " + loggedInUser + " ---");
        // System.out.println(decryptedData);  // Would show decrypted results
    }
}
