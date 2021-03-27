package com.test.project;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类字段描述
 *
 * @author sunshine
 */
@EqualsAndHashCode
public class ClassField implements TypeDescription {

    /**
     * 匹配的字段规则
     */
    private final Map<String, String> maps;

    /**
     * 已排除基础类型字段、时间字段
     */
    @Getter
    private List<Field> fields;

    /**
     * 匹配的数值字段
     */
    private List<Field> matchField;

    public ClassField(Class clazz, Map<String, String> maps) {
        this.maps = maps;
        this.fields = Arrays.stream(ReflectUtil.getFields(clazz))
                .filter(field -> !ClassUtil.isBasicType(field.getType()))
                .filter(field -> field.getType() != Date.class)
                .collect(Collectors.toList());
        this.matchField = getMatchField();
    }

    /**
     * 根据已处理好的字段(排除基础类型、日期类型)
     * 获取全部匹配map的字段Field
     *
     * @return 匹配的字段
     */
    public List<Field> getMatchField() {
        return fields.stream().filter(field -> !StrUtil.isEmpty(maps.get(field.getName()))).collect(Collectors.toList());
    }


    /**
     * 获取匹配Field全部数值
     *
     * @param object 操作对象
     * @return 匹配的数值
     */
    @Override
    public List<String> matchValues(Object object) {
        return matchField.stream()
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        Object o = field.get(object);
                        if (o != null) {
                            return (String) o;
                        }
                    } catch (Exception e) {
                        System.out.println("get field value is error, object: " + object + ", field: " + field);
                    }
                    return "";
                })
                .collect(Collectors.toList());
    }

    /**
     * 对操作对象根据匹配字段进行赋值操作
     *
     * @param object 操作对象
     * @param arrs   赋值集合
     */
    @Override
    public void assignmentValue(Object object, List<String> arrs) {
        for (int i = 0; i < matchField.size(); i++) {
            Field field = matchField.get(i);
            field.setAccessible(true);
            try {
                field.set(object, arrs.get(i));
            } catch (Exception e) {
                System.out.println("assignment field obj is error, object: " + object + ", field: " + field);
            }
        }
    }
}
