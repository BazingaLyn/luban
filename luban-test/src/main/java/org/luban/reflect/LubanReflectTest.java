package com.luban.reflect;

import org.junit.Test;

import java.lang.reflect.Method;

public class LubanReflectTest {

    @Test
    public void test01(){

        Method[] methods = com.luban.reflect.LubanReflect.class.getMethods();
        for (int i = 0; i < methods.length; i++) {
        }
    }
}
