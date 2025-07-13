package org.thanhpham.util;

import org.thanhpham.anotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GenericMapper<T> {
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
                values.add(field.get(entity) == null ? " " : field.get(entity));
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

    private Object parseValue(Object value, Class<?> targetType) {
        if (value != null && targetType.isAssignableFrom(value.getClass())) {
            return value;
        }

        if (value instanceof String stringValue) {
            try {
                if (targetType == int.class || targetType == Integer.class) {
                    return Integer.parseInt(stringValue);
                } else if (targetType == double.class || targetType == Double.class) {
                    return Double.parseDouble(stringValue);
                } else if (targetType == boolean.class || targetType == Boolean.class) {
                    return Boolean.parseBoolean(stringValue);
                } else if (targetType == long.class || targetType == Long.class) {
                    return Long.parseLong(stringValue);
                } else if (targetType == float.class || targetType == Float.class) {
                    return Float.parseFloat(stringValue);
                } else if (targetType == String.class) {
                    return stringValue;
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }

        if (value != null && !targetType.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException(
                    "Unexpected data type: expected " + targetType.getSimpleName() +
                            ", but got " + value.getClass().getSimpleName()
            );
        }

        return value;
    }
}
