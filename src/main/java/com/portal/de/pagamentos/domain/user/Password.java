package com.portal.de.pagamentos.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Embeddable
public class Password {

    @Column(name="password",nullable=false)
    private String value;

    public Password() {}

    public Password(String value) {
        setValue(value);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        if(value == null
                || value.length()<12
                || value.length()>128){
            throw new IllegalArgumentException("A password deve ter pelo menos 12 characters e um maximo de 128");
        }
        if (value.contains(" ")
                || !value.matches(".*[A-Z].*")
                || !value.matches(".*[a-z].*")
                || !value.matches(".*\\d.*")
                || !value.matches(".*[^A-Za-z0-9].*")) {
            throw new IllegalArgumentException("Password não deve ter espaços e deve conter pelo menos uma letra minúscula, uma maiúscula, um número e um caracter especial.");
        }
        if (isBreached(value)) {
            throw new IllegalArgumentException("Esta senha já foi comprometida em vazamentos públicos. Por favor escolha outra.");
        }

        this.value = BCrypt.hashpw(value, BCrypt.gensalt(12));
    }

    public String toString() {
        return this.value;
    }

    public static boolean isBreached(String password) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] hashed = sha1.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashed) {
                hexString.append(String.format("%02X", b));
            }

            String fullHash = hexString.toString();
            String prefix = fullHash.substring(0, 5);
            String suffix = fullHash.substring(5);

            URL url = new URL("https://api.pwnedpasswords.com/range/" + prefix);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(suffix)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
