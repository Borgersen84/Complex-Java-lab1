package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.exception.EmptyFieldException;
import se.alten.schoolproject.exception.StudentNotFoundException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentTransactionAccess {
    List listAllStudents() throws StudentNotFoundException;
    Student findStudentByName(String forename, String lastname);
    Student addStudent(Student studentToAdd) throws Exception;
    Student removeStudent(String student);
    Student updateStudent(String forename, String lastname, String email) throws StudentNotFoundException, EmptyFieldException;
    Student updateStudentPartial(Student studentToUpdate) throws EmptyFieldException, StudentNotFoundException;
}
