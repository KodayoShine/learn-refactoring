package com.test.yg.refactoring;

/**
 * <b><p>改变函数声明<p/></b>
 *
 * 虽然这个手法是对 函数改名、添加参数、移除参数
 * 但是,其中所提出的重构一系列的手段,都是需要逐渐领悟的
 *
 * 单说改名的好办法: 先写一句注释描述这个函数的用途,然后再把这句注释变成函数的名称
 * 参数的添加和移除都是根据所对应的点 进行特定时期的重构,没有一个正确的答案
 *
 *
 * @author sunshine
 */
public class ChangeFunctionDeclaration {

    /**
     * 函数改名简单做法:
     * 找到所有调用函数的方法,对方法名称进行修改;
     * 可以使用工具,全自动化完成名称更改
     * 这种的缺点也是极其明显的，一次性的修改所有调用者和函数声明，容易造成混乱
     *
     */
    public void circum(Double radius){
        System.out.println(2 * Math.PI * radius);
    }

    public void circumference(Double radius){
        System.out.println(2 * Math.PI * radius);
    }


}
class ExampleAddArgs {

    /**
     * 使用迁移法的方式,对当前方法添加一个新的参数
     *
     * @param customerName
     */
    public void addReservation(String customerName) {
        aaa_addReservation_2(customerName, false);
    }

    /**
     * 第一步,使用提炼函数的方式,将方法提炼出来,放置到新的方法中
     *  可以起一个容易搜索的名字
     */
    private void aaa_addReservation(String customerName) {
        System.out.println(customerName);
    }

    /**
     * 第二步,在新的函数中添加新的参数,同时修改旧的方法中调用新方法的地方
     *
     * 然后, 可以引入断言或者if判断,用于防止调用方修改时出错
     */
    private void aaa_addReservation_2(String customerName, Boolean isPriority) {
        assert isPriority == true || isPriority == false;
        System.out.println(customerName);
    }

    /**
     * 最后一步,可以对原函数使用内联函数,让调用者使用新的函数
     * 然后新的函数的名称更改成原本名称
     *
     */
    private void addReservation(String customerName, Boolean isPriority) {
        assert isPriority == true || isPriority == false;
        System.out.println(customerName);
    }
}
