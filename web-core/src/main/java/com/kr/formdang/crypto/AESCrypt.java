package com.kr.formdang.crypto;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class AESCrypt {

    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String AES_KEY_SPEC = "AES";
    private static final int IV_SIZE = 16; // 16 bytes for AES-CBC

    public static String encrypt(String plaintext, String key) throws Exception {
        // Generate a random IV (Initialization Vector)
        byte[] iv = new byte[IV_SIZE];
        // You can use a secure random number generator to generate the IV
        // For simplicity, this example uses a fixed IV of all zeros
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Create the AES key from the given key
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_KEY_SPEC);

        // Initialize the cipher in encryption mode with the key and IV
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        // Encrypt the plaintext
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        // Encode the encrypted bytes to Base64 for safe transmission or storage
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }


    public static String decrypt(String ciphertext, String key) throws Exception {
        // Generate a random IV (Initialization Vector)
        byte[] iv = new byte[IV_SIZE];
        // You can use a secure random number generator to generate the IV
        // For simplicity, this example uses a fixed IV of all zeros
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Create the AES key from the given key
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_KEY_SPEC);

        // Initialize the cipher in decryption mode with the key and IV
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        // Decode the Base64-encoded ciphertext to obtain the encrypted bytes
        byte[] encryptedBytes = Base64.getDecoder().decode(ciphertext);

        // Decrypt the ciphertext
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Convert the decrypted bytes to plaintext
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
