package com.mubaracktahir.jpos.demo;

import com.mubaracktahir.jpos.demo.connection.ConnectionHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JposDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JposDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ConnectionHelper connectionHelper) {
        return args -> connectionHelper.startServer(8583);
    }
}
