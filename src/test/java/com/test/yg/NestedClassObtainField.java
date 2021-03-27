package com.test.yg;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.test.project.NestedClassField;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NestedClassObtainField {

    Map<String, String> maps = Arrays.asList("perCode", "unitCode", "phone").stream().collect(Collectors.toMap(a -> a, d -> d));

    @Test
    public void 测试_嵌套类_当前类的属性() {
        NestedClassField classField = new NestedClassField(RecursionTestObj.class, maps);
        // 普通类型:1,  list: 2,  map: 3
        Assert.assertThat(classField.getType(), Is.is(1));
    }

    @Test
    public void 测试_嵌套类普通类_递归_自定义字段_自定义类型_子类的父属性名称是否匹配() {
        NestedClassField classField = new NestedClassField(RecursionTestObj.class, maps);
        Assert.assertThat(classField.childClass().size(), Is.is(1));
        Assert.assertThat(classField.childClass().get(0).getType(), Is.is(1));
        //Assert.assertThat(classField.childClass().get(0), Is.is(new NestedClassField(ClassObtainField.TestObj.class, maps, ReflectUtil.getField(RecursionTestObj.class, "testObj"), 1, new AggregationArray())));
        Assert.assertEquals(classField.childClass().get(0).getClazz(), ClassObtainField.TestObj.class);
        Assert.assertEquals(classField.childClass().get(0).getObjParentField(), ReflectUtil.getField(RecursionTestObj.class, "testObj"));
    }


    @Test
    public void 测试_嵌套类集合中_递归_列表类型的类_自定义字段_自定义类型_子类的父属性名称是否匹配() {
        NestedClassField classField = new NestedClassField(ListRecursionTestObj.class, maps);
        Assert.assertThat(classField.childClass().size(), Is.is(1));
        Assert.assertThat(classField.childClass().get(0).getType(), Is.is(2));
        //Assert.assertThat(classField.childClass().get(0), Is.is(new NestedClassField(ClassObtainField.TestObj.class, maps, ReflectUtil.getField(ListRecursionTestObj.class, "testObjs"), 2, new AggregationArray())));
        Assert.assertEquals(classField.childClass().get(0).getClazz(), ClassObtainField.TestObj.class);
        Assert.assertEquals(classField.childClass().get(0).getObjParentField(), ReflectUtil.getField(ListRecursionTestObj.class, "testObjs"));
    }

    @Test
    public void 测试_递归类_普通类中_全部匹配字段数值() {
        NestedClassField classField = new NestedClassField(RecursionTestObj.class, maps);
        Assert.assertThat(classField.childClass().size(), Is.is(1));
        Assert.assertThat(classField.childClass().get(0).getType(), Is.is(1));

        RecursionTestObj recursionTestObj = new RecursionTestObj();
        recursionTestObj.age = "20";
        recursionTestObj.unitCode = "666";
        recursionTestObj.testObj = new ClassObtainField.TestObj("1", "2", "3");
        Assert.assertThat(classField.getValue(recursionTestObj).size(), Is.is(3));
        Assert.assertThat(classField.getValue(recursionTestObj).get(0), Is.is("666"));
        Assert.assertThat(classField.getValue(recursionTestObj).get(1), Is.is("1"));
        Assert.assertThat(classField.getValue(recursionTestObj).get(2), Is.is("2"));
    }


    @Test
    public void 测试_递归类_递归类中_全部匹配字段数值() {
        NestedClassField classField = new NestedClassField(ListRecursionTestObj.class, maps);
        Assert.assertThat(classField.childClass().size(), Is.is(1));
        Assert.assertThat(classField.childClass().get(0).getType(), Is.is(2));

        ListRecursionTestObj listRecursionTestObj = new ListRecursionTestObj();
        listRecursionTestObj.age = "20";
        listRecursionTestObj.unitCode = "666";
        listRecursionTestObj.testObjs = Lists.newArrayList(new ClassObtainField.TestObj("1", "2", "3"), new ClassObtainField.TestObj("11", "22", "33"));
        Assert.assertThat(classField.getValue(listRecursionTestObj).size(), Is.is(5));
        Assert.assertThat(classField.getValue(listRecursionTestObj).get(0), Is.is("666"));
        Assert.assertThat(classField.getValue(listRecursionTestObj).get(1), Is.is("1"));
        Assert.assertThat(classField.getValue(listRecursionTestObj).get(2), Is.is("2"));
        Assert.assertThat(classField.getValue(listRecursionTestObj).get(3), Is.is("11"));
        Assert.assertThat(classField.getValue(listRecursionTestObj).get(4), Is.is("22"));
    }


    static class RecursionTestObj {
        String unitCode;
        String age;
        ClassObtainField.TestObj testObj;
    }

    static class ListRecursionTestObj {
        String unitCode;
        String age;
        List<ClassObtainField.TestObj> testObjs;
    }

}
