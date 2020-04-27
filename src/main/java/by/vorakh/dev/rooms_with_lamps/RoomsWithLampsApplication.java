package by.vorakh.dev.rooms_with_lamps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ComponentScan
public class RoomsWithLampsApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RoomsWithLampsApplication.class, args);
    }
    
}
