package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.exception.DuplicateEmailException;
import se.alten.schoolproject.exception.EmptyFieldException;
import se.alten.schoolproject.model.StudentModel;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@NoArgsConstructor
@Path("/student")
@Produces({"application/JSON"})
public class StudentController {

    @Inject
    private SchoolAccessLocal sal;

    @GET
    public Response showStudents() {
        try {
            List students = sal.listAllStudents();
            return Response.ok(students).build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
    
    @GET
    @Path("/find")
    public Response findStudentByName( @QueryParam("forename") String forename, @QueryParam("lastname") String lastname) {
        try {
            StudentModel student = sal.findStudentByName(forename, lastname);
            if (student != null) {
                return Response.ok(student).build();
            }

            return Response.status(Response.Status.NOT_FOUND).entity("{\"Person not found\"}").build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(String studentModel) {
        try {

            StudentModel answer = sal.addStudent(studentModel);
            return Response.ok(answer).build();

        } catch (DuplicateEmailException e) {
            return Response.status((Response.Status.CONFLICT)).entity(e.getMessage()).build();
        } catch (EmptyFieldException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("{email}")
    public Response deleteUser( @PathParam("email") String email) {
        try {
            sal.removeStudent(email);
            return Response.ok().build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    public void updateStudent( @QueryParam("forename") String forename, @QueryParam("lastname") String lastname, @QueryParam("email") String email) {
        sal.updateStudent(forename, lastname, email);
    }

    @PATCH
    public Response updatePartialAStudent(String studentModel) {
    	try {
        sal.updateStudentPartial(studentModel);
        return Response.ok().build();
    	} catch (Exception e) {
    		return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"Fill in all details please, Exception\"}").build();
    	}
    }
}
