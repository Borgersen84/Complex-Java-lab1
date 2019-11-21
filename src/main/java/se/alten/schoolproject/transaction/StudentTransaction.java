package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.exception.DuplicateEmailException;
import se.alten.schoolproject.exception.EmptyFieldException;
import se.alten.schoolproject.exception.StudentNotFoundException;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("ALL")
@Stateless
@Default
public class StudentTransaction implements StudentTransactionAccess{

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List listAllStudents() throws StudentNotFoundException {
        Query query = entityManager.createQuery("SELECT s from Student s");
        if (query.getResultList().size() < 1 ) {
            throw new StudentNotFoundException("{\"The list is empty!\"}");
        }
        return query.getResultList();
    }

    @Override
    public Student findStudentByName(String forename, String lastname) {
        Query query = entityManager.createQuery("SELECT s FROM Student s WHERE s.forename = :forename AND s.lastname = :lastname");
        query.setParameter("forename", forename).setParameter("lastname", lastname);
        Student student = (Student) query.getSingleResult();
        entityManager.flush();

        return student;
    }

    @Override
    public Student addStudent(Student studentToAdd) throws DuplicateEmailException {
        try {
            entityManager.persist(studentToAdd);
            entityManager.flush();
            return studentToAdd;
        } catch ( PersistenceException pe ) {
            throw new DuplicateEmailException("{\"Email already registered!\"}");
        }
    }

    @Override
    public Student removeStudent(String email) {
        Student studentFound = (Student)entityManager.createQuery("SELECT s FROM Student s WHERE s.email = :email")
                .setParameter("email", email).getSingleResult();
        Query query = entityManager.createNativeQuery("DELETE FROM student WHERE email = :email", Student.class);
        query.setParameter("email", email).executeUpdate();
        entityManager.flush();

        return studentFound;
    }

    @Override
    public Student updateStudent(String forename, String lastname, String email) throws StudentNotFoundException, EmptyFieldException {
        boolean accepted = !forename.isBlank() && !lastname.isBlank();
        if (accepted) {
            Query updateStudent = entityManager.createNativeQuery(
                    "UPDATE student SET forename = :forename, lastname = :lastname WHERE email = :email", Student.class);
            updateStudent.setParameter("forename", forename).setParameter("lastname", lastname)
                    .setParameter("email", email).executeUpdate();
        } else {
            throw new EmptyFieldException("{\"No empty fields allowed!\"}");
        }
        Query getStudent = entityManager.createNativeQuery(
                "SELECT * FROM Student WHERE email = :email", Student.class).setParameter("email", email);
        Student student = null;
        try {
            student = (Student) getStudent.getSingleResult();
        } catch (Exception e) {}
        if (student == null) {
            throw new StudentNotFoundException("{\"This user does not exist!\"}");
        }
        entityManager.flush();

        return student;
    }

    @Override
    public Student updateStudentPartial(Student student) throws EmptyFieldException, StudentNotFoundException {
        boolean accepted = !student.getForename().isBlank();
        if (accepted) {
            Query query = entityManager.createQuery("UPDATE Student SET forename = :studentForename WHERE email = :email");
            query.setParameter("studentForename", student.getForename())
                    .setParameter("email", student.getEmail()).executeUpdate();
        } else {
            throw new EmptyFieldException("{\"Perhaps your updated name should have some letters...?\"}");
        }
        Student studentFound = null;
        try {
            studentFound = (Student)entityManager.createQuery("SELECT s FROM Student s WHERE s.email = :email")
                    .setParameter("email", student.getEmail()).getSingleResult();
        } catch (Exception e) {}
        if (student == null) {
            throw new StudentNotFoundException("{\"This user does not exist!\"}");
        }
        entityManager.flush();

        return studentFound;
    }
}
