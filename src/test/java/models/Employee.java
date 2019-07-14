package models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Employee {

    @JsonIgnore
    private String id;

//    @JsonProperty("employee_name")
    private String name;

//    @JsonProperty("employee_age")
    private String age;

//    @JsonProperty("employee_salary")
    private String salary;

    @JsonProperty("profile_image")
    private String profileImage;

    public Employee(String name, String age, String salary, String profileImage) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.profileImage = profileImage;
    }

    public Employee(String id, String name, String age, String salary, String profileImage) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.profileImage = profileImage;
    }


}
