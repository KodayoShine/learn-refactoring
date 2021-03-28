package com.test.yg;

import com.google.common.collect.Lists;
import com.test.project.AggregationArray;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AggregationArrayTest {

    @Test
    public void 测试_多集合数据_转换成一个集合() {
        List<String> list1 = Lists.newArrayList("200");
        List<String> list2 = Lists.newArrayList("2010", "2020");
        List<String> list3 = Lists.newArrayList("1990", "1999");
        AggregationArray aggregationArray = new AggregationArray();
        aggregationArray.join(list1);
        aggregationArray.join(list2);
        aggregationArray.join(list3);
        Assert.assertThat(aggregationArray.get(), Is.is(Lists.newArrayList("200", "2010", "2020", "1990", "1999")));
    }

    @Test
    public void 测试_多集合数据_转换成一个集合_赋值新的集合数据() {
        List<String> list1 = Lists.newArrayList("200");
        List<String> list2 = Lists.newArrayList("2010", "2020");
        List<String> list3 = Lists.newArrayList("1990", "1999");
        AggregationArray aggregationArray = new AggregationArray();
        aggregationArray.join(list1);
        aggregationArray.join(list2);
        aggregationArray.join(list3);
        Assert.assertThat(aggregationArray.get(), Is.is(Lists.newArrayList("200", "2010", "2020", "1990", "1999")));
        aggregationArray.pull(Lists.newArrayList("200x", "2010x", "2020x", "1990x", "1999x"));
        Assert.assertThat(aggregationArray.get(), Is.is(Lists.newArrayList("200x", "2010x", "2020x", "1990x", "1999x")));
    }

    @Test(expected = RuntimeException.class)
    public void 测试_多集合数据_转换成一个集合_赋值新的集合数据_赋值长度不匹配_抛出异常() {
        List<String> list1 = Lists.newArrayList("200");
        List<String> list2 = Lists.newArrayList("2010", "2020");
        List<String> list3 = Lists.newArrayList("1990", "1999");
        AggregationArray aggregationArray = new AggregationArray();
        aggregationArray.join(list1);
        aggregationArray.join(list2);
        aggregationArray.join(list3);
        aggregationArray.pull(Lists.newArrayList("200x", "2010x", "2020x", "1990x"));
    }


    @Test
    public void 测试_多集合数据_依次推出每一段的数据test_mutilate_list_to_pushList() {
        List<String> list1 = Lists.newArrayList("200");
        List<String> list2 = Lists.newArrayList("2010", "2020");
        List<String> list3 = Lists.newArrayList("1990", "1999");
        AggregationArray aggregationArray = new AggregationArray();
        aggregationArray.join(list1);
        aggregationArray.join(list2);
        aggregationArray.join(list3);
        aggregationArray.pull(Lists.newArrayList("200x", "2010x", "2020x", "1990x", "1999x"));
        Assert.assertThat(aggregationArray.push(), Is.is(Lists.newArrayList("200x")));
        Assert.assertThat(aggregationArray.push(), Is.is(Lists.newArrayList("2010x", "2020x")));
        Assert.assertThat(aggregationArray.push(), Is.is(Lists.newArrayList("1990x", "1999x")));
        Assert.assertNull(aggregationArray.push());
    }

    @Test
    public void 测试_初始化全部数据_对数据进行清空() {
        List<String> list1 = Lists.newArrayList("200");
        List<String> list2 = Lists.newArrayList("2010", "2020");
        List<String> list3 = Lists.newArrayList("1990", "1999");
        AggregationArray aggregationArray = new AggregationArray();
        aggregationArray.join(list1);
        aggregationArray.join(list2);
        aggregationArray.join(list3);
        aggregationArray.pull(Lists.newArrayList("200x", "2010x", "2020x", "1990x", "1999x"));
        Assert.assertThat(aggregationArray.push(), Is.is(Lists.newArrayList("200x")));

        aggregationArray.clear();
        Assert.assertThat(aggregationArray.get(), Is.is(Lists.newArrayList()));

    }

}
