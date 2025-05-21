package com.c208.sleephony;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Sleephony API", version = "v1"))
@SpringBootApplication

public class SleephonyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SleephonyApplication.class, args);
	}

}
