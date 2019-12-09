package kristian9577.cardealer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CardealerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardealerApplication.class, args);
    }

}
