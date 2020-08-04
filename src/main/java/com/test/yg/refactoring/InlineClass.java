package com.test.yg.refactoring;

import java.sql.Driver;

/**
 * 内联函数
 * 1. 检查方法是否有多态性
 * 2. 找到该函数的所有调用点
 * 3. 将这个函数的调用点替换成函数本体
 * 4. 每次替换都进行测试
 * 5. 删除该函数
 *
 * 但是有一点<B>特别重要<B/>的是 必须是代码十分简洁,不复杂才使用内联
 * 否则其余情况,请考虑别的方式
 *
 * @author sunshine
 */
public class InlineClass {

    public Integer getRating(Driver driver) {
        return moreThanFiveLateDeliveries(driver) ? 2 : 1;
    }

    protected Boolean moreThanFiveLateDeliveries(Driver driver) {
        return driver.number > 5;
    }

    /**
     * 重构后代码
     * 某些函数的内部代码和函数名称一样,都是清晰易读的
     * 可以直接使用其中的代码
     *
     * 过多的非必要间接调用会造成不适
     */
    public Integer getRating_refactoring(Driver driver) {
        return (driver.number > 5) ? 2 : 1;
    }

    class Driver {
        Integer number = 10;
    }
}
