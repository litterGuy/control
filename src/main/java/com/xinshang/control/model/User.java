package com.xinshang.control.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private String accountId;
    private String nickName;
    private String userName;
    private String mobile;
    private String email;
    private String certNo;
    private Integer age;
}
