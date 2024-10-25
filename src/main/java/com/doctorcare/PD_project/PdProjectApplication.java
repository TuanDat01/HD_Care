package com.doctorcare.PD_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PdProjectApplication {

	public static void main(String[] args) {
		Runnable task = () -> {
			System.out.println("Task executed by: " + Thread.currentThread().getName());
		};
		Thread thread = new Thread(task);
		thread.start(); // Tạo và khởi chạy một luồng mới

		SpringApplication.run(PdProjectApplication.class, args
		);
	}

}
