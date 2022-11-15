package com.orm.Utils;

import com.orm.Annotation.AutoIncrement;
import com.orm.Annotation.PrimaryKey;
import com.orm.Annotation.Unique;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class EntityAnnotationsValidator {

    public static LinkedHashMap<Field, List<String>> validateAnnotations(Field[] declaredFields) {
        // <PrimaryKey, [id, name]>
        HashMap<String, List<Field>> annotationListHashMap = new HashMap<>();

        //<id, [PrimaryKey, AutoIncrement]>
        LinkedHashMap<Field, List<String>> fieldListHashMap = new LinkedHashMap<>();

        for (Field field : declaredFields) {
            List<String> annotations = Arrays.stream(field.getAnnotations())
                    .map(annotation -> annotation.annotationType().getName())
                    .collect(Collectors.toList());
            fieldListHashMap.put(field, annotations);
            for (String annotation : annotations) {
                if (!annotationListHashMap.containsKey(annotation)) {
                    annotationListHashMap.put(annotation, List.of(field));
                } else {
                    List<Field> fields = annotationListHashMap.get(annotation);
                    List<Field> copy = new ArrayList<>(fields);
                    copy.add(field);
                    annotationListHashMap.put(annotation, copy);
                }
            }
        }

        validateAnnotationsMostNumber(annotationListHashMap);
        validateAutoIncrementToPrimary(annotationListHashMap);

        return fieldListHashMap;
    }

    private static void validateAnnotationsMostNumber(HashMap<String, List<Field>> annotationListHashMap) {
        /**
         * Check if annotation @AutoIncrement appears more than one
         */
        if (annotationListHashMap.containsKey(AutoIncrement.class.getName())) {
            if (annotationListHashMap.get(AutoIncrement.class.getName()).size() > 1) {
                throw new IllegalArgumentException("AutoIncrement annotation not allowed more than one");
            }
        }

        /**
         * Check if annotation @PrimaryKey appears more than one
         */
        if (annotationListHashMap.containsKey(PrimaryKey.class.getName())) {
            if (annotationListHashMap.get(PrimaryKey.class.getName()).size() > 1) {
                throw new IllegalArgumentException("PrimaryKey annotation not allowed more than one");
            }
        }

        /**
         * Check if annotation @Unique appears more than one
         */
        if (annotationListHashMap.containsKey(Unique.class.getName())) {
            if (annotationListHashMap.get(Unique.class.getName()).size() > 1) {
                throw new IllegalArgumentException("Unique annotation not allowed more than one");
            }
        }
    }

    private static void validateAutoIncrementToPrimary(HashMap<String, List<Field>> annotationListHashMap) {
        /**
         * Check if PrimaryKey AutoIncrement fields are different
         */
        if (annotationListHashMap.containsKey(AutoIncrement.class.getName())) {
            Field autoIncrementedField = annotationListHashMap.get(AutoIncrement.class.getName()).get(0);
            if (annotationListHashMap.containsKey(PrimaryKey.class.getName())) {
                Field primaryKeyField = annotationListHashMap.get(PrimaryKey.class.getName()).get(0);
                if (!autoIncrementedField.getName().equals(primaryKeyField.getName())) {
                    throw new IllegalArgumentException("AutoIncremented field should be PrimaryKey field");
                }
            }
        }
    }

}
