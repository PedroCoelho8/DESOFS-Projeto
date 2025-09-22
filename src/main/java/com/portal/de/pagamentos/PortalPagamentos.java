package com.portal.de.pagamentos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class PortalPagamentos {
    private static final Logger logger = LoggerFactory.getLogger(PortalPagamentos.class);

    public static void main(String[] args) {
        SpringApplication.run(PortalPagamentos.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady() {
        logger.info("\n" + "=".repeat(60));
        logger.info("🚀 Portal de Pagamentos is ready!");
        logger.info("=".repeat(60) + "\n");
    }
}