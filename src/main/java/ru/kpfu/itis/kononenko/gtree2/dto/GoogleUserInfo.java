package ru.kpfu.itis.kononenko.gtree2.dto;

import lombok.Data;

@Data
public class GoogleUserInfo {
    private String id;
    private String email;
    private String name;
    private String picture;
}