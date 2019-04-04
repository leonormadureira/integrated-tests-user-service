package com.celfocus.integratedtestsuserservice.dto;

import lombok.Data;

@Data
public class Car {
    public Long id;
    public String brand;
    public String model;
    public Boolean is_available;
    public Long user_id;
}
