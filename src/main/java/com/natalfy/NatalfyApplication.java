package com.natalfy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // 👇 Esta é a chave que liga o processamento em segundo plano!
public class NatalfyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NatalfyApplication.class, args);
    }

}
