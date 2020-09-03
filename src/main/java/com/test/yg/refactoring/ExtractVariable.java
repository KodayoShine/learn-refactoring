package com.test.yg.refactoring;

/**
 * 提炼变量
 * 1. 确认所要提炼的表达式
 * 2. 先声明一个不可变的变量,将所要替换的表达式复制一份
 * 3. 替换原本的表达式
 * <p>
 * 原本的表达式不太容易理解,将每一块逻辑拆解出来,并根据当前的上下文进行良好的命名
 * 如果所提炼变量可以在更大的上下文中使用,可以提炼函数出来
 * <p>
 * 一步一步对变量进行提取,每次提取完成后,进行相关单元测试,保证提取正确性
 *
 * @author sunshine
 */
public class ExtractVariable {

    public Double getMath(Order order) {
        return order.quantity * order.itemPrice - Math.max(0, order.quantity - 500) * order.itemPrice * 0.05 +
                Math.min(order.quantity * order.itemPrice * 0.1, 100);
    }

    public Double getMath_refactoring(Order order) {
        int basePrice = order.quantity * order.itemPrice;
        Double quantityDiscount = Math.max(0, order.quantity - 500) * order.itemPrice * 0.05;
        Double shipping = Math.min(basePrice * 0.1, 100);
        return basePrice - quantityDiscount + shipping;
    }

    class Order {
        Integer quantity;
        Integer itemPrice;
    }
}

/**
 * 不仅要提取出函数内的变量,还可以根据所代表的含义,提炼成方法
 */
class ExampleExtractToClass {

    class Order {
        Integer quantity;
        Integer itemPrice;

        public Double getPrice() {
            return this.quantity * this.itemPrice - Math.max(0, this.quantity - 500) * this.itemPrice * 0.05 +
                    Math.min(this.quantity * this.itemPrice * 0.1, 100);
        }

        /**
         * 这也是对象带来的好处,方便进行相关逻辑和数据的通用行为进行提炼
         *
         * @return
         */
        public Double getPrice_ref() {
            return basicPrice() - quantityDiscount() + shipping();
        }

        public Integer basicPrice() {
            return this.quantity * this.itemPrice;
        }

        public Double quantityDiscount() {
            return Math.max(0, this.quantity - 500) * this.itemPrice * 0.05;
        }

        public Double shipping() {
            return Math.min(this.quantity * this.itemPrice * 0.1, 100);
        }

    }
}
