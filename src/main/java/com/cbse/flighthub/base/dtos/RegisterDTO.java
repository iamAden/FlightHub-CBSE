package com.cbse.flighthub.base.dtos;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String name;
    private String password;

    public RegisterDTO(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
