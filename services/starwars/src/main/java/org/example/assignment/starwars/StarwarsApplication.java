package org.example.assignment.starwars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"org.example.assignment.starwars", "org.example.assignment.rest", "org.example.assignment.mapcache", "org.example.assignment.data"})
public class StarwarsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarwarsApplication.class, args);
    }

}
