package com.test.yg.refactoring;


/**
 * <p><b>引入参数对象</b></p>
 * <p>
 * 一组数据项总是同时出现,使用一个数据结构进行取代
 * 将这组数据项更加明显的展示出来,提升代码的一致性
 * 1. 创建一个合适的数据结构
 * 2. 测试
 * 3. 使用改变函数声明,给原来的函数新增一个参数
 * 4. 调整所有调用者,每一次的修改,都跟随一次测试
 * 5. 然后用新的数据结构取代原本参数列表对应参数项
 *
 * @author sunshine
 */
public class IntroduceParameterObject {

    /**
     * 计算当前now值范围大小是否匹配min和max
     * 作为min和max可以组合成为一个对象进行声明
     *
     */
    public void readingOutsideRange(Integer now, Integer min, Integer max) {
        System.out.println((now < min || now > max));
    }

    class NumberRange {
        Integer min;
        Integer max;

        public boolean contains(Integer now, NumberRange range) {
            return now < range.min || now > range.max;
        }
    }

    public void readingOutsideRange_firstStep(Integer now, Integer min, Integer max, NumberRange range) {
        System.out.println((now < min || now > max));
    }

    /** 测试 */

    public void readingOutsideRange_secondStep(Integer now, Integer min, NumberRange range) {
        System.out.println((now < min || now > range.max));
    }

    /** 测试 */

    public void readingOutsideRange_thirdStep(Integer now, NumberRange range) {
        System.out.println((now < range.min || now > range.max));
    }
    /**
     * 第四步,需要到类里面执行相关操作
     * 将这个方法搬移到类当中
     */
    public void readingOutsideRange_fourthStep(Integer now, NumberRange range) {
        System.out.println(range.contains(now, range));
    }



}
