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

                if (value != null && !field.getType().isAssignableFrom(value.getClass())) {
                    throw new IllegalArgumentException(
                            "Sai kiểu dữ liệu ở field: " + field.getName() +
                                    " - cần: " + field.getType().getSimpleName() +
                                    ", nhưng nhận: " + value.getClass().getSimpleName()
                    );
                }

                field.set(instance, value);
            }

            return instance;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi ánh xạ dữ liệu vào object: " + clazz.getSimpleName(), e);
        }
    }

    public List<Object> mapFromEntity(T entity){
        List<Object> values = new ArrayList<>();
        Class<?> clazz = entity.getClass();

        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                values.add(field.get(entity));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Không thể truy cập thuộc tính", e);
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

}
