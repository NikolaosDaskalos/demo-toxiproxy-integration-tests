package org.demo.toxiproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoToxiproxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoToxiproxyApplication.class, args);
	}

}
