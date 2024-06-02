package client.demo;

import client.demo.utils.GlobalBeanUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2024/6/2 12:45
 * @describe 循环引用测试
 */
public class CircleReferenceTest {

    @Test
    public void test() {
        Person person = Person.builder().age(18).name("yichen").build();
        person.setHomeLeader(person);
        System.out.println(GlobalBeanUtils.gson.toJson(person));;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class Person {
        private Person homeLeader;
        private String name;
        private Integer age;
    }

}
