package com.seb.msusuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MsUsuarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsUsuarioApplication.class, args);
	}

}
