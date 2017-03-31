package gr.uoa.di.digibid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@SpringBootApplication
public class DigiBidApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(DigiBidApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DigiBidApplication.class);
    }
}
