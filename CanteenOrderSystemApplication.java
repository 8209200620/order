package edu.canteen.order.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.canteen.order.system.mapper")
public class CanteenOrderSystemApplication {

	
	
	public static void main(String[] args) {
		SpringApplication.run(CanteenOrderSystemApplication.class, args);
	}
}
