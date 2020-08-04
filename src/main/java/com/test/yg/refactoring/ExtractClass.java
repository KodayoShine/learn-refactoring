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
     * <p>
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

class BasicMethod {

    public void printOwing(String name) {
        Integer outstanding = 0;

        System.out.println("*************************");
        System.out.println("******Customer Owes******");
        System.out.println("*************************");

        for (int i = 0; i < 10; i++) {
            outstanding += i;
        }

        // record due date
        String mockDate = "2020-08-04";


        System.out.println("name:" + name);
        System.out.println("amount:" + outstanding);
        System.out.println("due:" + mockDate);
    }

}

class ExampleNoLocalVariable extends BasicMethod {

    /**
     * 将打印横幅代码进行抽取,只需要简单的复制和粘贴
     */
    public void printOwing_noLocalVariables_refactoring(String name) {
        Integer outstanding = 0;
        printBanner();
        for (int i = 0; i < 10; i++) {
            outstanding += i;
        }

        // record due date
        String mockDate = "2020-08-04";

        System.out.println("name:" + name);
        System.out.println("amount:" + outstanding);
        System.out.println("due:" + mockDate);
    }

    protected void printBanner() {
        System.out.println("*************************");
        System.out.println("******Customer Owes******");
        System.out.println("*************************");
    }

}

class ExampleHaveLocalVariable extends ExampleNoLocalVariable {

    /**
     * 所提炼的代码只读取值,在不修改的情况下,可以简单将参数传给目标函数printDetails
     */
    public void printOwing_haveLocalVariables_refactoring(String name) {
        Integer outstanding = 0;
        printBanner();
        for (int i = 0; i < 10; i++) {
            outstanding += i;
        }

        // mock record due date
        String mockDate = "2020-08-04";
        printDetails(name, outstanding, mockDate);
    }

    protected void printDetails(String name, Integer outstanding, String mockDate) {
        System.out.println("name:" + name);
        System.out.println("amount:" + outstanding);
        System.out.println("due:" + mockDate);
    }

}

class ExampleReAssignLocalVariables extends ExampleHaveLocalVariable {

    /**
     * 对局部变量再赋值
     * 第一步: 先将变量声明移动到使用处前
     */
    public void printOwing_reAssignLocalVariables_refactoring_firstStep(String name) {
        printBanner();

        Integer outstanding = 0;
        for (int i = 0; i < 10; i++) {
            outstanding += i;
        }

        // mock record due date
        String mockDate = "2020-08-04";
        printDetails(name, outstanding, mockDate);
    }

    /**
     * 第二步: 将所要提炼的代码复制到目标函数中
     * 第四步: 改名字
     */
    public Integer calculateOutstanding(){
        Integer result = 0;
        for (int i = 0; i < 10; i++) {
            result += i;
        }
        return result;
    }

    /**
     * 第三步: 修改原本的代码,令其调用新函数
     * 第五步: 将返回声明更改成为final类型,不能再初始化后再被赋值
     */
    public void printOwing_reAssignLocalVariables_refactoring_thirdStep(String name) {
        printBanner();

        final Integer outstanding = calculateOutstanding();
        // mock record due date
        String mockDate = "2020-08-04";
        printDetails(name, outstanding, mockDate);
    }


}