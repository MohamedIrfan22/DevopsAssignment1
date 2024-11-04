package org.fileencrypt.fileencryptdecrypt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FileEncryptorDecryptor {
    private String originalFilePath;
    private String encryptedFilePath;
    private String decryptedFilePath;
    private String password;

    @BeforeEach
    public void setUp() {
        password = "123";
        originalFilePath = "E:\\text\\testfile.txt";
        encryptedFilePath = "E:\\text\\testfile.txt.enc";
        decryptedFilePath = "E:\\text\\testfile.txt.dec";

        // Clear previous test files
        clearTestFiles();
        System.out.println("Setup completed. Previous test files cleared.");
    }

    private void clearTestFiles() {
        deleteFile(originalFilePath);
        deleteFile(encryptedFilePath);
        deleteFile(decryptedFilePath);
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.delete()) {
            System.out.println("Deleted file: " + filePath);
        }
    }

    @Test
    public void testEncryptAndDecrypt() {
        // Step 1: Create a sample text file
        createSampleFile(originalFilePath, "This is a test file for encryption and decryption.");

        // Step 2: Encrypt the file
        try {
            file.encrypt(originalFilePath, password); // Call to the actual encrypt method
            assertTrue(new File(encryptedFilePath).exists(), "Encryption failed: Output file does not exist.");
            System.out.println("Encryption completed: " + encryptedFilePath);
        } catch (Exception e) {
            System.out.println("Encryption failed: " + e.getMessage());
            return; // Exit the test early
        }

        // Step 3: Decrypt the file
        try {
            file.decrypt(encryptedFilePath, password); // Call to the actual decrypt method
            assertTrue(new File(decryptedFilePath).exists(), "Decryption failed: Output file does not exist.");
            System.out.println("Decryption completed: " + decryptedFilePath);
        } catch (Exception e) {
            System.out.println("Decryption failed: " + e.getMessage());
            return; // Exit the test early
        }

        // Step 4: Verify the content
        boolean isContentSame = verifyFileContent(originalFilePath, decryptedFilePath);
        assertTrue(isContentSame, "Test failed: The decrypted content does not match the original content.");
        System.out.println("Test passed: The decrypted content matches the original content.");
    }

    private void createSampleFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(new File(filePath))) {
            writer.write(content);
            System.out.println("Sample file created: " + filePath);
        } catch (IOException e) {
            fail("Error creating sample file: " + e.getMessage());
        }
    }

    private boolean verifyFileContent(String originalFilePath, String decryptedFilePath) {
        try {
            byte[] originalBytes = Files.readAllBytes(Paths.get(originalFilePath));
            byte[] decryptedBytes = Files.readAllBytes(Paths.get(decryptedFilePath));
            return Arrays.equals(originalBytes, decryptedBytes);
        } catch (IOException e) {
            fail("Error reading files: " + e.getMessage());
            return false; // This line will never be reached due to the fail call above
        }
    }
}