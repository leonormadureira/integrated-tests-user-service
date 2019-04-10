package com.celfocus.integratedtestsuserservice;

import com.celfocus.integratedtestsuserservice.dto.Car;
import com.celfocus.integratedtestsuserservice.dto.InputBody;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntegratedTestes {

    @Value(value = "${wiremock-port}")
    public String wiremockPort;

    @Value(value = "${wiremock-host}")
    public String wiremockHost;

    @Value(value = "${user-service-port}")
    public String userServicePort;

    @Value(value = "${user-service-host}")
    public String userServiceHost;

    public Long user_id;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @After
    public void dropTable() {
        String sqlStatements[] = {
                "DELETE FROM User"};

        Arrays.asList(sqlStatements).forEach(sql -> {
            jdbcTemplate.execute(sql);
        });
    }

    @Test
    public void createUserTest() {

        InputBody input = new InputBody();

        input.setFirst_name("Leonor");
        input.setLast_name("Madureira");
        input.setNif((long) 209050772);

        Gson gson = new Gson();
        String jsonInput = gson.toJson(input);

        given().contentType("application/json").body(jsonInput).
                when().post("http://" + userServiceHost + ":" + userServicePort + "/userService").
                then().assertThat().statusCode(200);

        String sqlStatements[] = {
                "SELECT user_id FROM User WHERE nif = '209050772'"};

        Arrays.asList(sqlStatements).forEach(sql -> {
            List<Long> notEmptyList = jdbcTemplate.queryForList(sql, Long.class);
            System.out.println(notEmptyList);
            Assert.assertFalse(notEmptyList.isEmpty());
        });
    }

    @Test
    public void deleteUserTest() {

        String sqlStatements[] = {
                "INSERT INTO User (user_id, first_name, last_name, nif) VALUES ('101', 'Ines', 'Madureira', '254668987')"
        };

        Arrays.asList(sqlStatements).forEach(sql -> {
            jdbcTemplate.execute(sql);
        });

        String sqlStatements1[] = {
                "SELECT user_id FROM User WHERE nif = '254668987'"};
        Arrays.asList(sqlStatements1).forEach(sql -> {
            System.out.println(jdbcTemplate.queryForList(sql, Long.class));
        });


        InputBody input = new InputBody();

        input.setNif((long) 254668987);

        Gson gson = new Gson();
        String jsonInput = gson.toJson(input);

        given().contentType("application/json").body(jsonInput).
                when().delete("http://" + userServiceHost + ":" + userServicePort + "/userService").
                then().assertThat().statusCode(200);


        Arrays.asList(sqlStatements1).forEach(sql -> {
            List<Long> emtyList = new ArrayList<>();
            emtyList = jdbcTemplate.queryForList(sql, Long.class);
            System.out.println(emtyList);
            Assert.assertTrue(emtyList.isEmpty());
        });
    }

    @Test
    public void getCarsCurrentlyRentedByUserTest() {

        user_id = 2L;

        List<Car> expectedListOfCars = new ArrayList<>();
        Car expectedCar = new Car();

        expectedCar.setId(1L);
        expectedCar.setBrand("Seat");
        expectedCar.setModel("Ibiza");
        expectedCar.setIs_available(false);
        expectedCar.setUser_id(2L);
        expectedListOfCars.add(expectedCar);

        Gson gson = new Gson();
        String jsonExpectedResult = gson.toJson(expectedListOfCars);

        when().get("http://" + userServiceHost + ":" + userServicePort + "/userService?user_id=" + user_id).
                then().assertThat().statusCode(200).contentType(ContentType.JSON).body(equalTo(jsonExpectedResult));
    }

    @Test
    public void rentCarTest() {

        InputBody input = new InputBody();

        input.setUser_id(2L);
        input.setCar_id(1L);

        Gson gson = new Gson();
        String jsonInput = gson.toJson(input);

        Car expectedCar = new Car();
        expectedCar.setId(1L);
        expectedCar.setBrand("Seat");
        expectedCar.setModel("Ibiza");
        expectedCar.setIs_available(false);
        expectedCar.setUser_id(2L);

        String jsonExpectedResult = gson.toJson(expectedCar);

        given().contentType("application/json").body(jsonInput).
                when().put("http://" + userServiceHost + ":" + userServicePort + "/userService/rentCar").
                then().assertThat().statusCode(200).contentType(ContentType.JSON).body(equalTo(jsonExpectedResult));
    }

    @Test
    public void releaseCarTest() {
        InputBody input = new InputBody();

        input.setUser_id(2L);
        input.setCar_id(1L);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String jsonInput = gson.toJson(input);

        Car expectedCar = new Car();
        expectedCar.setId(1L);
        expectedCar.setBrand("Seat");
        expectedCar.setModel("Ibiza");
        expectedCar.setIs_available(true);
        expectedCar.setUser_id(null);

        String jsonExpectedResult = gson.toJson(expectedCar);

        given().contentType("application/json").body(jsonInput).
                when().put("http://" + userServiceHost + ":" + userServicePort + "/userService/releaseCar").
                then().assertThat().statusCode(200).contentType(ContentType.JSON).body(equalTo(jsonExpectedResult));
    }
}