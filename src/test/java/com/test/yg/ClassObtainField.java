package com.test.yg;

import com.google.common.collect.Lists;
import com.test.project.ClassField;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassObtainField {

    Map<String, String> maps = Arrays.asList("perCode", "unitCode", "phone").stream().collect(Collectors.toMap(a -> a, d -> d));

    @Test
    public void 测试_类中包含的指定字段_长度() {
        ClassField classField = new ClassField(TestObj.class, maps);
        Assert.assertThat(classField.getMatchField().size(), Is.is(2));
    }

    @Test
    public void 测试_类中包含的指定字段_忽略基础类型_长度() {
        ClassField classField = new ClassField(BasicTypeTestObj.class, maps);
        Assert.assertThat(classField.getMatchField().size(), Is.is(1));
    }

    @Test
    public void 测试_类中包含的指定字段_忽略时间类型_长度() {
        ClassField classField = new ClassField(DateTestObj.class, maps);
        Assert.assertThat(classField.getMatchField().size(), Is.is(1));
    }

    @Test
    public void 测试_类字段_匹配规则_顺序展示() {
        ClassField classField = new ClassField(TestObj.class, maps);
        Assert.assertThat(classField.getMatchField().get(0).getName(), Is.is("perCode"));
        Assert.assertThat(classField.getMatchField().get(1).getName(), Is.is("phone"));
    }

    @Test
    public void 测试_类中匹配的数值() {
        TestObj testObj = new TestObj();
        testObj.age = "66";
        testObj.perCode = "777";
        ClassField classField = new ClassField(TestObj.class, maps);
        Assert.assertThat(classField.matchValues(testObj), Is.is(Lists.newArrayList("777", "")));
    }

    @Test
    public void 测试_类中匹配的数值_进行重新赋值操作() {
        TestObj testObj = new TestObj();
        testObj.age = "66";
        testObj.perCode = "777";
        ClassField classField = new ClassField(TestObj.class, maps);
        classField.assignmentValue(testObj, Lists.newArrayList("111", "222"));
        Assert.assertThat(testObj.perCode, Is.is("111"));
        Assert.assertThat(testObj.phone, Is.is("222"));
    }


    @AllArgsConstructor
    @NoArgsConstructor
    static class TestObj {
        String perCode;
        String phone;
        String age;
    }

    static class BasicTypeTestObj {
        private String perCode;
        private Integer unitCode;
    }
    static class DateTestObj {
        private String perCode;
        private Date unitCode;
    }


}
