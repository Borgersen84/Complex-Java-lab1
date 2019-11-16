package se.alten.schoolproject.dao;

import javassist.NotFoundException;
import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.exception.DuplicateEmailException;
import se.alten.schoolproject.exception.EmptyFieldException;
import se.alten.schoolproject.exception.StudentNotFoundException;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {

    private Student student = new Student();
    private StudentModel studentModel = new StudentModel();

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Override
    public List listAllStudents(){
        return studentTransactionAccess.listAllStudents();
    }

    @Override
    public StudentModel findStudentByName(String forename, String lastname) throws EmptyFieldException, StudentNotFoundException {
        if(forename != null || !forename.isBlank() || lastname != null || !lastname.isBlank()) {
            try {
                return studentModel.toModel(studentTransactionAccess.findStudentByName(forename, lastname));
            } catch (Exception e) {
                throw new StudentNotFoundException("{\"Student not found!\"}");
            }
        } else {
            throw new EmptyFieldException("{\"No empty fields allowed!\"}");
        }

    }

    @Override
    public StudentModel addStudent(String newStudent) throws EmptyFieldException, DuplicateEmailException {
        Student studentToAdd = student.toEntity(newStudent);
        boolean checkForEmptyVariables = Stream.of(studentToAdd.getForename(), studentToAdd.getLastname(), studentToAdd.getEmail()).anyMatch(String::isBlank);
        if (!checkForEmptyVariables) {
            studentTransactionAccess.addStudent(studentToAdd);
            return studentModel.toModel(studentToAdd);
        } else {
            throw new EmptyFieldException("No Fields Can Be Empty!");
        }
    }

    @Override
    public void removeStudent(String studentEmail) throws NotFoundException {
        if (!studentEmail.isBlank()) {
            studentTransactionAccess.removeStudent(studentEmail);
        } else {
            throw new NotFoundException("This User Does Not Exist!");
        }

    }

    @Override
    public void updateStudent(String forename, String lastname, String email) {
        studentTransactionAccess.updateStudent(forename, lastname, email);
    }

    @Override
    public void updateStudentPartial(String studentModel) {
        Student studentToUpdate = student.toEntity(studentModel);
        studentTransactionAccess.updateStudentPartial(studentToUpdate);
    }
}
