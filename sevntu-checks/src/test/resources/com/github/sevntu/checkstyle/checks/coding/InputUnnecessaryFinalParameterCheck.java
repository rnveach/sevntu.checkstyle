package com.github.sevntu.checkstyle.checks.coding;

public class InputUnnecessaryFinalParameterCheck {
    private InputUnnecessaryFinalParameterCheck(String s, final String t) {}

    public void method1() {}
    public void method2(String s) {}
    public void method3(final String s) {}
    public String method4(final String s) {
        String temp = s;
        return temp;
    }
    public void method5(final String parameter) {
        new Anonymous() {
            @Override
            public void anonymousMethod(String s) {
                method(parameter);
            }
        };
    }

    private static abstract class Anonymous {
        public abstract void anonymousMethod(final String s);

        public void method(String s) {
        }
    }
}
