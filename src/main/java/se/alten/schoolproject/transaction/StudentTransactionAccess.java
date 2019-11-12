package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.exception.DuplicateEmailException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentTransactionAccess {
    List listAllStudents();
    Student addStudent(Student studentToAdd) throws DuplicateEmailException;
    void removeStudent(String student);
    void updateStudent(String forename, String lastname, String email);
    void updateStudentPartial(Student studentToUpdate);
}
