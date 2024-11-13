package corp.pjh.kiki.jwt.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

@Component
public class AESUtil {

    private final String cipherAlgorithm = "AES/CBC/PKCS5Padding";

    private final SecretKeySpec key;

    private final IvParameterSpec iv;

    public AESUtil(@Value("${security.aes.secret-key}") String secretKey, @Value("${security.aes.iv-secret-key}") String ivKey) {
        this.key = new SecretKeySpec(secretKey.getBytes(), "AES");
        this.iv = new IvParameterSpec(ivKey.getBytes());
    }

    public String encrypt(String input) throws Exception {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] encrypted = cipher.doFinal(input.getBytes());

        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String encryptedInput) throws Exception {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedInput));

        return new String(decrypted);
    }

}

