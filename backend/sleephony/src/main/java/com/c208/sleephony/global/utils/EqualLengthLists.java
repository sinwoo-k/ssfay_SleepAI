package com.c208.sleephony.global.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EqualLengthListsValidator.class)
public @interface EqualLengthLists {
    String message() default "accX, accY, accZ, temp, hr 길이가 서로 달라요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}