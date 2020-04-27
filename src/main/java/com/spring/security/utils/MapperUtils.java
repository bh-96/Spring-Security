package com.spring.security.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class MapperUtils {

    private static ModelMapper modelMapper;

    @Autowired
    public MapperUtils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static <D, E> D convert(E source, Class<? extends D> classLiteral) {
        try {
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(source, classLiteral);
        } catch (Exception e) {
            return null;
        }
    }

    public static <D, E> D convert(E source, Type type) {
        try {
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(source, type);
        } catch (Exception e) {
            return null;
        }
    }

    public static <D, E> Page<D> convert(Page<E> entities, Class<D> dtoClass) {
        return entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
    }

}