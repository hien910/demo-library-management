package com.example.demolibrarymanagement.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static <T, R> List<R> convertList(List<T> list, Function<T, R> func) {
        return list.stream().map(func).collect(Collectors.toList());
    }

    public static <T, R> R convertObject(T t, Function<T, R> func) {
        return func.apply(t);
    }
}
