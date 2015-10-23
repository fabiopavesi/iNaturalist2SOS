package it.cnr.irea.inatsos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
@EnableTransactionManagement
@EnableAsync

public class INatSosApplication {

    public static void main(String[] args) {
        SpringApplication.run(INatSosApplication.class, args);
    } 
}
