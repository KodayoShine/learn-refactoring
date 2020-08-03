package com.test.yg.refactoring;


/**
 * 提炼函数
 * 1.创建一个新的函数，根据所要做的事情进行相关命名
 * 2。将提炼的代码，<b>复制<b/>到新建函数中
 * 3.检查复制后的代码无法访问的变量，采用参数的形式进行传递
 * 4。对代码进行编译，编译完成后，将被提炼的代码替换为对目标函数的调用
 * 5.<B>测试<B/>
 * 6.检查是否还有重复代码，有则以后再说
 *
 * @author sunshine
 * @date 2020-8-3 23:28:40
 */
public class ExtractClass {

    /**
     * 原方法
     *
     * @param name
     */
    public void printOwing(String name) {
        printBanner();
        int outstanding = calculateOutstanding();

        System.out.println("name:" + name);
        System.out.println("amount:" + outstanding);
    }

    /**
     * 重构后代码:
     *
     * 将意图和实现分开
     * 如果需要花时间浏览一段代码弄清所做事情,那么就需要将其提炼到一个函数当中
     * 并根据他所做的事情为其命名
     *
     * @param name
     */
    public void printOwing_refactoring(String name) {
        printBanner();
        int outstanding = calculateOutstanding();
        printDetails(name, outstanding);
    }

    private void printDetails(String name, int outstanding) {
        System.out.println("name:" + name);
        System.out.println("amount:" + outstanding);
    }


    private int calculateOutstanding() {
        return 10;
    }

    private void printBanner() {
        System.out.println("banner");
    }

}

class ExampleNoLocalVariable {

    public void printOwing() {
        Integer outstanding = 0;

        printBanner();
    }

    private void printBanner() {
        System.out.println("*************************");
        System.out.println("******Customer Owes******");
        System.out.println("*************************");
    }

}