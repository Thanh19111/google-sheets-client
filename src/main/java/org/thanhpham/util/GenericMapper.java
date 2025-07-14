package org.thanhpham.util;

import org.thanhpham.anotation.Id;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class GenericMapper<T> {
    private static final Map<Class<?>, Function<String, Object>> PARSERS = new HashMap<>();

    static {
        PARSERS.put(Integer.class, Integer::parseInt);
        PARSERS.put(int.class, Integer::parseInt);
        PARSERS.put(Double.class, Double::parseDouble);
        PARSERS.put(double.class, Double::parseDouble);
        PARSERS.put(Boolean.class, Boolean::parseBoolean);
        PARSERS.put(boolean.class, Boolean::parseBoolean);
        PARSERS.put(Long.class, Long::parseLong);
        PARSERS.put(long.class, Long::parseLong);
        PARSERS.put(Float.class, Float::parseFloat);
        PARSERS.put(float.class, Float::parseFloat);
        PARSERS.put(LocalDate.class, LocalDate::parse);
        PARSERS.put(LocalDateTime.class, LocalDateTime::parse);
        PARSERS.put(String.class, s -> s);
        PARSERS.put(BigDecimal.class, BigDecimal::new);
        PARSERS.put(BigInteger.class, BigInteger::new);
    }

    private final Class<T> clazz;

    public GenericMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T mapFromList(List<Object> values) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length && i < values.size(); i++) {
                Field field = fields[i];
                Object value = values.get(i);
                field.setAccessible(true);

                if(value == null){
                    field.set(instance, null);
                    continue;
                }

                field.set(instance, parseValue(value, field.getType()));
            }

            return instance;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Cannot map into object: " + clazz.getSimpleName(), e);
        }
    }

    public List<Object> mapFromEntity(T entity){
        List<Object> values = new ArrayList<>();
        Class<?> clazz = entity.getClass();

        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                values.add(field.get(entity) == null ? " " : field.get(entity).toString());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access the attribute", e);
        }
        
        return values;
    }

    public static <T> Object getIdFromEntity(T entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                try {
                    field.setAccessible(true);
                    return field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot access @Id field", e);
                }
            }
        }
        throw new RuntimeException("No field annotated with @Id found");
    }

    public static <T> int getIndexOfIdField(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(Id.class)) {
                return i;
            }
        }

        throw new RuntimeException("No field annotated with @Id found");
    }

    @SuppressWarnings("unchecked")
    private Object parseValue(Object value, Class<?> targetType) {
        if (value == null) return null;

        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }

        if (value instanceof String stringValue) {
            Function<String, Object> parser = PARSERS.get(targetType);
            if (parser != null) {
                try {
                    return parser.apply(stringValue);
                } catch (Exception e) {
                    return null;
                }
            } else if (targetType.isEnum()) {
                return Enum.valueOf((Class<Enum>) targetType, stringValue);
            }
        }

        throw new IllegalArgumentException(
                "Unexpected data type: expected " + targetType.getSimpleName() +
                        ", but got " + value.getClass().getSimpleName()
        );
    }
}
