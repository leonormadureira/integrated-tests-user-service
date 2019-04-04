package com.celfocus.integratedtestsuserservice.dto;

import lombok.Data;

@Data
public class InputBody {
    public Long user_id;
    public String first_name;
    public String last_name;
    public Long nif;
    public Long car_id;
}
