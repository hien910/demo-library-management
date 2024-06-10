package com.example.demolibrarymanagement.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeMail {
    ABOUT_TO_EXPIRE("ABOUT_TO_EXPIRE"),
            EXPIRED("EXPIRED");

    public final String value;
}
