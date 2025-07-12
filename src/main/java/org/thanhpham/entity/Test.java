package org.thanhpham.entity;

import org.thanhpham.anotation.Id;

public class Test {
    @Id
    private String id;
    private String name;
    private String gender;
    private String address;

    @Override
    public String toString() {
        return "Test{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
