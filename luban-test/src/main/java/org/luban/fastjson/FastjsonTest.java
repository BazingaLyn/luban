package org.luban.fastjson;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class FastjsonTest {



    @Test
    public void test(){

        FastjsonObj fastjsonObj = new FastjsonObj();
        fastjsonObj.setClazz(new BaseClz().getClass());
        fastjsonObj.setName("testFastjson");

        String result = JSON.toJSONString(fastjsonObj);
        System.out.println(result);

        FastjsonObj fastjsonObj1 = JSON.parseObject(result, FastjsonObj.class);

        Class<?> clazz = fastjsonObj1.getClazz();
        System.out.println(clazz);


    }


    private static class BaseClz {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class FastjsonObj {

        private Class<?> clazz;

        private String name;

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }



}
