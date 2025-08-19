package com.ferrefull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FerrefullBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FerrefullBackendApplication.class, args);
	}

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
          .allowedOrigins("*")
          .allowedMethods("GET","POST","HEAD","PUT","DELETE","OPTIONS","TRACE","PATCH")
          .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization");
      }
    };
  }

}
