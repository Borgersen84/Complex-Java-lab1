package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.exception.DuplicateEmailException;
import se.alten.schoolproject.exception.EmptyFieldException;
import se.alten.schoolproject.exception.StudentNotFoundException;
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
public class StudentController {

    @Inject
    private SchoolAccessLocal sal;

    @GET
    @Produces({"application/JSON"})
    public Response showStudents() {
        try {
            List students = sal.listAllStudents();
            return Response.ok(students).build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("/find")
    @Produces({"application/JSON"})
    public Response findStudentByName( @QueryParam("forename") String forename, @QueryParam("lastname") String lastname) {
        try {
           StudentModel student = sal.findStudentByName(forename, lastname);
            return Response.ok(student).build();
        } catch (StudentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (EmptyFieldException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"Something went wrong\"}").build();
        }
    }

    @POST
    @Path("/add")
    @Produces({"application/JSON"})
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
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"Something went wrong\"}").build();
        }
    }

    @DELETE
    @Path("{email}")
    public Response deleteUser( @PathParam("email") String email) {
        try {
            StudentModel student = sal.removeStudent(email);
            return Response.ok().entity("{\"Student " + student.getForename() + " " + student.getLastname() +
                    "[" + student.getEmail() + "]" +  " removed\"}").build();
        } catch (EmptyFieldException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (StudentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"Something went wrong\"}").build();
        }
    }

    @PUT
    @Produces({"application/JSON"})
    public Response updateStudent( @QueryParam("forename") String forename, @QueryParam("lastname") String lastname, @QueryParam("email") String email) {
        try {
            StudentModel student = sal.updateStudent(forename, lastname, email);
            return Response.ok(student).build();
        } catch (EmptyFieldException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (StudentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"Something went wrong!\"}").build();
        }
    }

    @PATCH
    @Produces({"application/JSON"})
    public Response updatePartialAStudent(String studentModel) {
    	try {
        StudentModel student = sal.updateStudentPartial(studentModel);
        return Response.ok(student).build();
    	} catch (EmptyFieldException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (StudentNotFoundException e) {
    	    return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("{\"Something went wrong\"}").build();
    	}
    }
}
