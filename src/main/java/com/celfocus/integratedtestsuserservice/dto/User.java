package com.celfocus.integratedtestsuserservice.dto;

import lombok.Data;

@Data
public class User {
    public Long user_id;
    public String first_name;
    public String last_name;
    public Long nif;
}
