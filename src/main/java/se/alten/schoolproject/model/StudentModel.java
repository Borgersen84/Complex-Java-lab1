package se.alten.schoolproject.model;

import lombok.*;
import se.alten.schoolproject.entity.Student;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentModel implements Serializable {

    private String forename;
    private String lastname;
    private String email;

    public StudentModel toModel(Student student) {
        StudentModel studentModel = new StudentModel();

        studentModel.setForename(student.getForename());
        studentModel.setLastname(student.getLastname());
        studentModel.setEmail(student.getEmail());

        return studentModel;

    }
}
