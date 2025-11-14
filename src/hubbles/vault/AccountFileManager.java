/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hubbles.vault;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author dreel
 */
public class AccountFileManager {
    private final Path baseDir;
    private final Path credentialsFile;
    private final Path vaultsDir;
    
    private static final int PBKDF_ITER = 520_000;  
    private static final int SALT_LEN = 16;           
    private static final int DK_BITS  = 256;          
    
    public AccountFileManager(){
        this(Paths.get(System.getProperty("user.home"), "HubblesVaultData"));
    }
    
    public AccountFileManager(Path baseDir){
        this.baseDir = baseDir;
        this.credentialsFile = baseDir.resolve("credentials.txt");
        this.vaultsDir = baseDir.resolve("Vaults");
    }
    
    public void init() throws IOException{
        if(!Files.exists(baseDir)){
            Files.createDirectories(vaultsDir);

            boolean created = false;
            if(!Files.exists(credentialsFile)){
                Files.createFile(credentialsFile);
                created = true;
            }

            if (created || isFileEmpty(credentialsFile)) {
                try {
                    seedInitialAccounts();
                } catch (Exception e) {
                    throw new IOException("Failed to seed demo accounts", e);
                }
            }

            System.out.println("== Hubbles Vault Initialized ==");
            System.out.println("Base Directory : " + baseDir.toAbsolutePath());
            System.out.println("Credentials File : " + credentialsFile.toAbsolutePath());
            System.out.println("Vaults Directory : " + vaultsDir.toAbsolutePath());
            System.out.println("===========================");
        }else{
            System.out.println("Files Already Exist");
        }
    }
    
    private boolean isFileEmpty(Path p) throws IOException {
        return Files.size(p) == 0;
    }
    
    public Path getBaseDir() {return baseDir;}
    public Path getCredentialsFile() {return credentialsFile;}
    public Path getVaultDir() {return vaultsDir;}
    
    private void seedInitialAccounts() throws Exception{ // method that hardcodes initial accounts
        // Map of demo accounts: email → plain-text password
        Map<String,String> demo = new LinkedHashMap<>();
        demo.put("TheCrimsonShadow@gmail.com", "Password1");
        demo.put("CosmicStorm89@gmail.com", "SuperNova98");
        demo.put("ViridianHelmet64@gmail.com", "GreenGardens22");
        demo.put("JuniperLanding34@gmail.com", "Planetfall");
        demo.put("PrettyLittleDevil12@gmail.com", "TwoCanKeepASecret!");
        
        // List to hold formatted credential records for writing
        List<String> lines = new ArrayList<>();
        
        // Process each demo account
        for(Map.Entry<String,String> e : demo.entrySet()){
            
            //Normalise the Username
            String username = e.getKey().toLowerCase(); 
            String password = e.getValue();
            
            //Generate a random salt for this account
            byte[] salt = SecurityUtils.randomBytes(SALT_LEN);
            
            //Hash the password using PBKDF2 with the random salt
            byte[] dk = SecurityUtils.pbkdf(password.toCharArray(), salt, PBKDF_ITER, DK_BITS);
            
            // Format: iterations : base64(salt) : base64(hash)
            String kdfSpec = PBKDF_ITER + ":" + SecurityUtils.b64(salt) + ":" + SecurityUtils.b64(dk); 
            
            // Construct the final stored record:
            // username | kdfSpec | failedLoginCount | lastLoginTimestamp
            String record  = username + "|" + kdfSpec + "|0|null";
            
            //Add this record to the output list
            lines.add(record);           
        }
        
        //Write to a temporary file to prevent partial writes
        Path tmp = credentialsFile.resolveSibling("credentials.txt.tmp");
        String content = String.join("\n", lines) + "\n";
        Files.writeString(tmp, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        
        //Atomicially replace the real credentials.txt file with the new one
        Files.move(tmp, credentialsFile, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        
        //print message to confuirm accounts have been seeded
        System.out.println("Seeded demo accounts (" + lines.size() + ") into credentials.txt");
    }
    
    private static class ParsedRecord {
        String username;   // The username/email from the record
        int iterations;    // PBKDF2 iteration count used for hashing
        byte[] salt;       // Salt used for hashing (decoded from Base64)
        byte[] dk;         // The stored PBKDF2-derived key (decoded from Base64)
    }
    
    private static ParsedRecord parseRecordLine(String line) {

        // Reject empty or blank lines
        if (line == null || line.isBlank())
            throw new IllegalArgumentException("Empty Record Line");

        // Split by '|' to extract the username and KDF spec
        String[] parts = line.split("\\|");
        if (parts.length < 2)
            throw new IllegalArgumentException("Bad record line: " + line);

        // Split the KDF spec into iterations, salt, and derived key
        String[] kdf = parts[1].split(":");
        if (kdf.length != 3)
            throw new IllegalArgumentException("Bad kdf spec: " + parts[1]);

        // Build and populate the ParsedRecord object
        ParsedRecord r = new ParsedRecord();
        r.username   = parts[0];                    // username as stored
        r.iterations = Integer.parseInt(kdf[0]);    // PBKDF2 iterations
        r.salt       = SecurityUtils.deb64(kdf[1]); // decode Base64 salt
        r.dk         = SecurityUtils.deb64(kdf[2]); // decode Base64 derived key

        return r;
    }
    
    public static String formatRecord(ParsedRecord r) {

        // Normalize username for storage (emails are case-insensitive)
        String uname = r.username.toLowerCase();

        // Rebuild the PBKDF2 specification: iterations : base64(salt) : base64(hash)
        String kdfSpec = r.iterations + ":" 
                       + SecurityUtils.b64(r.salt) + ":" 
                       + SecurityUtils.b64(r.dk);

        // Complete record format:
        //     username | kdfSpec
        return uname + "|" + kdfSpec;
    }
    
    private Map<String, String> loadRawLinesMap() throws IOException {
        if (!java.nio.file.Files.exists(credentialsFile)) return new java.util.LinkedHashMap<>(); // If credentials file doesnt exist return empty map
        var lines = java.nio.file.Files.readAllLines(credentialsFile, java.nio.charset.StandardCharsets.UTF_8); //Read all lines from the file UTF-8
        var map = new java.util.LinkedHashMap<String,String>(); // Map: username → raw record line (preserves insertion order)
        for (String L : lines) {
            if (L == null || L.isBlank()) continue; //Skip blank or null lines
            int sep = L.indexOf('|'); //Find the posititon of the first | seperator
            if (sep <= 0) continue; //Skip malformed lines
            map.put(L.substring(0, sep).toLowerCase(), L); //Store the full record line
        }
        return map;
    }
    
    private ParsedRecord findUser(String username) throws java.io.IOException {
        var map = loadRawLinesMap(); //load all lines into a map for quick lookup
        String line = map.get(username.toLowerCase()); //normalise username for lookup
        return (line == null) ? null : parseRecordLine(line); //if user not found return null otherwise parse the line
    }
    
    public boolean login(String username, String password) {
    try {
        ParsedRecord rec = findUser(username); //find the stored record of this user
        if (rec == null) return false; //no user found

        byte[] computed = SecurityUtils.pbkdf(password.toCharArray(), rec.salt, rec.iterations, rec.dk.length * 8); //hash entered password using same paramaters as originalls
        return SecurityUtils.constantTimeEquals(computed, rec.dk); //compare computed hash with stored hash
    } catch (Exception e) {
        // For debugging; in production you might log and return false
        e.printStackTrace();
        return false;
    }
}


}
