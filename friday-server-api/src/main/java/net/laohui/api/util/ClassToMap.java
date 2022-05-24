package net.laohui.api.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClassToMap {
    public static Map<String, Object> asMap(Object c) {
        Class<?> cls = c.getClass();
        Field[] declaredFields = cls.getDeclaredFields();
        Map<String, Object> map = new HashMap<>();
        for (Field field : declaredFields) {
            try {
                Field fieldAsName = cls.getDeclaredField(field.getName());
                fieldAsName.setAccessible(true);
                Object o = fieldAsName.get(c);
                // log.warn("type:{}", fieldAsName.getType().toString());
                if (Objects.isNull(o)) {
                    o = "";
                }
                map.put(field.getName(), o);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                System.out.println("error");
            }
        }
//        System.out.println(map);
        return map;
    }
}
