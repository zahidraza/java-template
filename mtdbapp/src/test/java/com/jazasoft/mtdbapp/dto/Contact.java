package com.jazasoft.mtdbapp.dto;

/**
 * Created by mdzahidraza on 25/07/17.
 */
public class Contact {
    private String name;
    private int age;

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
