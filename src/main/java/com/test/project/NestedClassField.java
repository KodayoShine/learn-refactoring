package com.test.project;

import cn.hutool.core.util.TypeUtil;
import lombok.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * 嵌套类描述信息
 * 递归处理嵌套对象
 * 如:
 * obj -> obj -> list<obj>
 * 通过List<NestedClassField>接收当前类的所包含的全部对象
 *
 * @author sunshine
 */
@EqualsAndHashCode
@ToString
public class NestedClassField {

    /**
     * 当前类
     */
    @Getter
    private Class clazz;

    /**
     * 主要用于嵌套childClass使用
     * 表明当前对象的哪个字段进行构建的
     * <p>
     * 这个字段用途,一般是父类的obj进行使用的
     */
    @Getter
    private final Field objParentField;

    /**
     * 类描述对象
     */
    private TypeDescription classField;

    private final Map<String, String> maps;

    /**
     * 嵌套class
     */
    private List<NestedClassField> childClass;

    /**
     * 当前类的类型
     * 1: 普通obj
     * 2: list
     * 3: map
     */
    private Integer type;

    /**
     * 聚合数据, 只会在根节点中存在的对象
     * 将当前节点一下的全部对象的数值都进行记录
     */
    @Getter
    private AggregationArray aggregationArray;

    /**
     * 根对象构建对象使用的构造方法
     *
     * @param clazz
     * @param maps
     */
    public NestedClassField(Class clazz, Map<String, String> maps) {
        this(clazz, maps, null, 1, new AggregationArray());
    }

    /**
     * 递归对象使用的构造方法
     *
     * @param clazz
     * @param maps
     * @param objParentField
     * @param type
     * @param aggregationArray
     */
    public NestedClassField(Class clazz, Map<String, String> maps, Field objParentField, Integer type, AggregationArray aggregationArray) {
        if (isMap(clazz)) {
            type = 3;
        }
        this.maps = maps;
        this.type = type;
        this.clazz = clazz;
        this.objParentField = objParentField;
        this.aggregationArray = aggregationArray;
        if (type != 3) {
            this.classField = new ClassField(clazz, maps);
            this.childClass = childClass();
        }
        if (type == 3) {
            //this.classField = new MapKeys();
            //this.childClass = childMap();
        }

    }


    /**
     * 根据类描述类型,循环全部字段
     *
     * @return
     */
    public List<NestedClassField> childClass() {
        return ((ClassField) classField).getFields().stream()
                .filter(field -> field.getType() != String.class)
                .map(field -> {
                    Class<?> fieldTypeClass = field.getType();
                    if (fieldTypeClass == List.class) {
                        return new NestedClassField((Class) TypeUtil.getTypeArgument(field.getGenericType()), maps, field, 2, aggregationArray);
                    } else if (isMap(fieldTypeClass)) {
                        return new NestedClassField(null, maps, field, 3, aggregationArray);
                    } else {
                        return new NestedClassField(fieldTypeClass, maps, field, 1, aggregationArray);
                    }
                }).collect(Collectors.toList());
    }


    private boolean isMap(Class clazz) {
        return clazz == Map.class;
    }


    public Integer getType() {
        return type;
    }

    public List<String> getValue(Object obj) {
        List<String> strings = classField.matchValues(obj);
        aggregationArray.join(strings);
        commonChildClass(obj, NestedClassField::getValue);
        return aggregationArray.get();
    }

    public void assignmentValues(Object obj) {
        classField.assignmentValue(obj, aggregationArray.push());
        commonChildClass(obj, NestedClassField::assignmentValues);
    }

    private void commonChildClass(Object obj, BiConsumer<NestedClassField, Object> consumer) {
        for (NestedClassField nestedClassField : childClass) {
            Field field = nestedClassField.getObjParentField();
            try {
                field.setAccessible(true);
                Object childObject = field.get(obj);
                Integer type = nestedClassField.getType();
                if (type == 2) {
                    List childLists = (List) childObject;
                    childLists.forEach(childList -> consumer.accept(nestedClassField, childList));
                } else {
                    consumer.accept(nestedClassField, childObject);
                }
            } catch (Exception e) {

            }
        }
    }

    public void clearAggregationArray() {
        aggregationArray.clear();
    }

}
