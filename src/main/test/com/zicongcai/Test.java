package com.zicongcai;

public class Test {

    public static void main(String[] args){
        Base base = new Base();
        base.setI(2);

        ClassA a = new ClassA();
        a.setI(1);
        System.out.println(a.getI());

        ClassB b = new ClassB();
        System.out.println(b.getI());
    }
}
