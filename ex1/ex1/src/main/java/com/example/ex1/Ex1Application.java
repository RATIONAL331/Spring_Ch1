package com.example.ex1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ex1Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex1Application.class, args);
	}
	// gradle 탭에서 Tasks -> build -> bootJar를 더블 클릭하면 jar파일이 만들어지고 java -jar로 실행할 수 있다.
}
