package com.grash.dto.fastSpring;

import lombok.Data;

@Data
public class Customer {
    public String first;
    public String last;
    public String email;
    public Object company;
    public String phone;
    public boolean subscribed;
}
