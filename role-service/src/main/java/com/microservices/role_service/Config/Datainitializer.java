package com.microservices.role_service.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.microservices.role_service.repository.Rolerepository;

@Component
public class Datainitializer implements CommandLineRunner{

    @Autowired
    Rolerepository rolerepository;

    @Override
    public void run(String... args) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}
