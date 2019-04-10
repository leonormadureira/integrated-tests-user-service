package com.celfocus.integratedtestsuserservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import org.springframework.jdbc.core.JdbcTemplate;


@SpringBootApplication
public class IntegratedTestsUserServiceApplication {

	public IntegratedTestsUserServiceApplication(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(IntegratedTestsUserServiceApplication.class, args);
	}

	final
	JdbcTemplate jdbcTemplate;

	/*@PostConstruct
	private void initDb() {
		String sqlStatements[] = {
				"INSERT INTO User (user_id, first_name, last_name, nif) VALUES ('101', 'Ines', 'Madureira', '254668987')"
		};

		Arrays.asList(sqlStatements).forEach(sql -> {
			jdbcTemplate.execute(sql);
		});
	}*/
}
