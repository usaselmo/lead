package com.allscontracting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Main {

	public static void main(String[] arguments) {
		SpringApplication.run(Main.class, arguments);
	}
	
	/*
	 * @Bean public CorsFilter corsFilter() { UrlBasedCorsConfigurationSource source
	 * = new UrlBasedCorsConfigurationSource(); CorsConfiguration config = new
	 * CorsConfiguration(); config.setAllowCredentials(true);
	 * config.addAllowedOrigin("*"); config.addAllowedHeader("*");
	 * config.addAllowedMethod("OPTIONS"); config.addAllowedMethod("GET");
	 * config.addAllowedMethod("POST"); config.addAllowedMethod("PUT");
	 * config.addAllowedMethod("DELETE"); source.registerCorsConfiguration("/**",
	 * config); return new CorsFilter(source); }
	 */
	
	
  @Bean
  public WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurer() {
          @Override
          public void addCorsMappings(CorsRegistry registry) {
              registry.addMapping("/**");
          }
      };
  }
		
	
}
