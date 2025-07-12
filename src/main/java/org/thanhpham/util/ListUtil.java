package org.thanhpham.util;

import org.thanhpham.entity.GoogleSheetPage;
import org.thanhpham.entity.Page;
import org.thanhpham.entity.Pageable;
import org.thanhpham.entity.Sort;

import java.lang.reflect.Field;
import java.util.List;

public class ListUtil {
    public static <T> void sortList(List<T> list, Sort sort) {
        if (sort == null || sort.getProperty() == null){return;}

        list.sort((o1, o2) -> {
            try {
                Field field = o1.getClass().getDeclaredField(sort.getProperty());
                field.setAccessible(true);

                Comparable val1 = (Comparable) field.get(o1);
                Comparable val2 = (Comparable) field.get(o2);

                int result = val1.compareTo(val2);
                return sort.isDescending() ? -result : result;
            } catch (Exception e) {
                throw new RuntimeException("Sort error", e);
            }
        });
    }

    public static <T> Page<T> paginate(List<T> fullList, Pageable pageable) {
        int fromIndex = (int) pageable.getOffset();
        int toIndex = Math.min(fromIndex + pageable.getPageSize(), fullList.size());

        if (fromIndex > fullList.size()) {
            return new GoogleSheetPage<>(List.of(), pageable, fullList.size());
        }
        
        List<T> pageContent = fullList.subList(fromIndex, toIndex);
        return new GoogleSheetPage<>(pageContent, pageable, fullList.size());
    }
}
