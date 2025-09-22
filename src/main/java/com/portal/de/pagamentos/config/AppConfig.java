package com.portal.de.pagamentos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@EnableConfigurationProperties
public class AppConfig {
    private Email email = new Email();
    private TwoFA twofa = new TwoFA();

    public static class Email {
        private String deliveryMode = "mailhog";
        private Mailhog mailhog = new Mailhog();
        private Smtp smtp = new Smtp();

        public static class Mailhog {
            private String host = "localhost";
            private int port = 1025;

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public String getHost(){
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }
        }

        public static class Smtp {
            private String host;
            private int port = 587;
            private String username;
            private String password;

            public String getHost(){
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort(){
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public String getUsername(){
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword(){
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }
        }

        public String getDeliveryMode() {
            return deliveryMode;
        }

        public void setDeliveryMode(String deliveryMode) {
            this.deliveryMode = deliveryMode;
        }

        public Mailhog getMailhog() {
            return mailhog;
        }

        public void setMailhog(Mailhog mailhog) {
            this.mailhog = mailhog;
        }

        public Smtp getSmtp() {
            return smtp;
        }

        public void setSmtp(Smtp smtp) {
            this.smtp = smtp;
        }
    }

    public static class TwoFA {
        private int codeExpiryMinutes = 5;
        private int maxAttempts = 3;

        public int getCodeExpiryMinutes() {
            return codeExpiryMinutes;
        }

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setCodeExpiryMinutes(int codeExpiryMinutes) {
            this.codeExpiryMinutes = codeExpiryMinutes;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }
    }

    public Email getEmail() {
        return email;
    }

    public TwoFA getTwofa() {
        return twofa;
    }

    public void setEmail(Email email){
        this.email = email;
    }

    public void setTwofa(TwoFA twofa){
        this.twofa = twofa;
    }
}