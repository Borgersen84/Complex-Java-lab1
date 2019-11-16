package se.alten.schoolproject.dao;

import javassist.NotFoundException;
import se.alten.schoolproject.exception.DuplicateEmailException;
import se.alten.schoolproject.exception.EmptyFieldException;
import se.alten.schoolproject.exception.StudentNotFoundException;
import se.alten.schoolproject.model.StudentModel;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SchoolAccessLocal {

    List listAllStudents();

    StudentModel findStudentByName(String forename, String lastName) throws StudentNotFoundException, EmptyFieldException;

    StudentModel addStudent(String studentModel) throws EmptyFieldException, DuplicateEmailException;

    void removeStudent(String student) throws NotFoundException;

    void updateStudent(String forename, String lastname, String email);

    void updateStudentPartial(String studentModel);
}
