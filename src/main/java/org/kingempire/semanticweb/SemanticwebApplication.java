package org.kingempire.semanticweb;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SemanticwebApplication implements CommandLineRunner {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SemanticwebApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("SemanticwebApplication.CommandLineRunner start..");
//        new NewClass().main2();
    }
}
