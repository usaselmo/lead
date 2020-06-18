package com.allscontracting;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Main implements CommandLineRunner {

	public static void main(String[] arguments) {
		SpringApplication.run(Main.class, arguments);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.passwordEncoder().encode("123"));
	}


  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  
}
