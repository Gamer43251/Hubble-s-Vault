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
            filePath = "users/TheCrimsonShadow.txt";
        } else if (loggedInUser.equalsIgnoreCase("CosmicStorm89")) {
            filePath = "users/CosmicStorm89.txt";
        } else if (loggedInUser.equalsIgnoreCase("ViridianHelmet64")) {
            filePath = "users/ViridianHelmet64.txt";
        } else if (loggedInUser.equalsIgnoreCase("JuniperLanding34")) {
            filePath = "users/JuniperLanding34.txt";
        } else if (loggedInUser.equalsIgnoreCase("PrettyLittleDevil12")) {
            filePath = "users/PrettyLittleDevil12.txt";
        } else {
            System.out.println("Error: There is no user in the system with that username!");
            return;
        }

        // Now read and decrypt
        String encryptedData = reader.readEncryptedFile(filePath);
        String decryptedData = decryptor.decrypt(encryptedData, 3); // example shift

        System.out.println("\n--- Decrypted data for " + loggedInUser + " ---");
        System.out.println(decryptedData);
    }
}
