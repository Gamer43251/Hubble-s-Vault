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
    
    private void seedInitialAccounts() throws Exception{
        Map<String,String> demo = new LinkedHashMap<>();
        demo.put("TheCrimsonShadow@gmail.com", "Password1");
        demo.put("CosmicStorm89@gmail.com", "SuperNova98");
        demo.put("ViridianHelmet64@gmail.com", "GreenGardens22");
        demo.put("JuniperLanding34@gmail.com", "Planetfall");
        demo.put("PrettyLittleDevil12@gmail.com", "TwoCanKeepASecret!");
        
        List<String> lines = new ArrayList<>();
        for(Map.Entry<String,String> e : demo.entrySet()){
            String username = e.getKey().toLowerCase();
            String password = e.getValue();
            
            byte[] salt = SecurityUtils.randomBytes(SALT_LEN);
            byte[] dk = SecurityUtils.pbkdf(password.toCharArray(), salt, PBKDF_ITER, DK_BITS);
            
            String kdfSpec = PBKDF_ITER + ":" + SecurityUtils.b64(salt) + ":" + SecurityUtils.b64(dk);
            String record  = username + "|" + kdfSpec + "|0|null";
            lines.add(record);           
        }
        
        Path tmp = credentialsFile.resolveSibling("credentials.txt.tmp");
        String content = String.join("\n", lines) + "\n";
        Files.writeString(tmp, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.move(tmp, credentialsFile, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("Seeded demo accounts (" + lines.size() + ") into credentials.txt");
    }
    
    private static class ParsedRecord{
        String username;
        int iterations;
        byte[] salt;
        byte[] dk;
    }
    
    private static ParsedRecord parseRecordLine(String line){
        if(line == null || line.isBlank()) throw new IllegalArgumentException("Empty Record Line");
        String[] parts = line.split("\\|");
        if(parts.length < 2) throw new IllegalArgumentException("Bad record line: " + line);
        
        String[] kdf = parts[1].split(":");
        if (kdf.length != 3) throw new IllegalArgumentException("Bad kdf spec: " + parts[1]);

        ParsedRecord r = new ParsedRecord();
        r.username = parts[0];
        r.iterations = Integer.parseInt(kdf[0]);
        r.salt = SecurityUtils.deb64(kdf[1]);
        r.dk = SecurityUtils.deb64(kdf[2]);
        return r;
    }
    
    public static String formatRecord(ParsedRecord r){
        String uname = r.username.toLowerCase();
        String kdfSpec = r.iterations + ":" + SecurityUtils.b64(r.salt) + ":" + SecurityUtils.b64(r.dk);
        return uname + "|" + kdfSpec;
    }
    
    private Map<String, String> loadRawLinesMap() throws IOException {
        if (!java.nio.file.Files.exists(credentialsFile)) return new java.util.LinkedHashMap<>();
        var lines = java.nio.file.Files.readAllLines(credentialsFile, java.nio.charset.StandardCharsets.UTF_8);
        var map = new java.util.LinkedHashMap<String,String>();
        for (String L : lines) {
            if (L == null || L.isBlank()) continue;
            int sep = L.indexOf('|');
            if (sep <= 0) continue;
            map.put(L.substring(0, sep).toLowerCase(), L);
        }
        return map;
    }
    
    private ParsedRecord findUser(String username) throws java.io.IOException {
        var map = loadRawLinesMap();
        String line = map.get(username.toLowerCase());
        return (line == null) ? null : parseRecordLine(line);
    }
    
    public boolean login(String username, String password) {
    try {
        ParsedRecord rec = findUser(username);
        if (rec == null) return false;

        // IMPORTANT: if you later add a PEPPER, you must also seed with it.
        byte[] computed = SecurityUtils.pbkdf(password.toCharArray(), rec.salt, rec.iterations, rec.dk.length * 8);
        return SecurityUtils.constantTimeEquals(computed, rec.dk);
    } catch (Exception e) {
        // For debugging; in production you might log and return false
        e.printStackTrace();
        return false;
    }
}


}
