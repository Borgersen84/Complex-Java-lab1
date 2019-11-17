package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentTransactionAccess {
    List listAllStudents();
    Student findStudentByName(String forename, String lastname);
    Student addStudent(Student studentToAdd) throws Exception;
    Student removeStudent(String student);
    Student updateStudent(String forename, String lastname, String email);
    Student updateStudentPartial(Student studentToUpdate);
}
