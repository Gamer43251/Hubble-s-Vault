package hubbles.vault;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HubbleFileReader {

    // Reads the encrypted file and returns its contents
    public String readEncryptedFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

   
    public void decryptForLoggedInUser(String loggedInUser) {
        HubbleFileReader reader = new HubbleFileReader();
        HubbleDecryptor decryptor = new HubbleDecryptor();

        String filePath = "";

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

        // Now read and decrypt
        String encryptedData = reader.readEncryptedFile(filePath);
        //String decryptedData = decryptor.decrypt(encryptedData, 3); // example shift

        System.out.println("\n--- Decrypted data for " + loggedInUser + " ---");
        //System.out.println(decryptedData);
    }
}
