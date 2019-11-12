package se.alten.schoolproject.dao;

import se.alten.schoolproject.exception.DuplicateEmailException;
import se.alten.schoolproject.exception.EmptyFieldException;
import se.alten.schoolproject.model.StudentModel;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SchoolAccessLocal {

    List listAllStudents();

    StudentModel findStudentByName(String forename, String lastName);

    StudentModel addStudent(String studentModel) throws EmptyFieldException, DuplicateEmailException;

    void removeStudent(String student);

    void updateStudent(String forename, String lastname, String email);

    void updateStudentPartial(String studentModel);
}
