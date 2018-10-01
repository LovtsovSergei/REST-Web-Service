package rest.clinic;

import dao.Workday;
import exceptions.doctorException.DoctorNotFoundException;
import exceptions.timeSlotException.TimeSlotIsNotFreeException;
import exceptions.timeSlotException.TimeSlotNotFoundException;
import exceptions.workdayException.WorkdayNotFoundException;
import exceptions.workdayException.WorkdayNotFreeException;
import service.DoctorService;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/clinic")
public class PetClinicServer {

    private static DoctorService doctorService = new DoctorService();

    @GET
    @Path("/doctors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDoctorLastName(@QueryParam("lastname") String lastName) {
        List<String> list;
        if (lastName == null) {
            list = doctorService.getAllDoctorsLastNames();
            GenericEntity<List<String>> docsWrapper = new GenericEntity<List<String>>(list) {
            };
            return Response.ok(docsWrapper).build();
        } else {
            try {
                list = doctorService.getDoctorLastName(lastName);

                GenericEntity<List<String>> docsWrapper = new GenericEntity<List<String>>(list) {
                };
                return Response.ok(docsWrapper).build();
            } catch (DoctorNotFoundException e) {

                e.printStackTrace();
                return Response.status(404)
                        .entity(e.getMessage())
                        .build();
            }
        }
    }

    @GET
    @Path("/schedule/{lastname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDoctorScheduleByLastName(@PathParam("lastname") String lastName) {
        List<Workday> list;
        try {
            list = doctorService.getDoctorScheduleByLastName(lastName);
            GenericEntity<List<Workday>> docsWrapper = new GenericEntity<List<Workday>>(list) {
            };
            return Response.ok(docsWrapper).build();
        } catch (DoctorNotFoundException e) {
            e.printStackTrace();
            return Response.status(404)
                    .entity(e.getMessage())
                    .build();
        }

    }

    @GET
    @Path("/schedule/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDoctors() {
        return Response.ok(doctorService.getDoctors()).build();
    }


    @POST
    @Path("/registration")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Response registerNewPatient(@FormParam("patient") String patient,
                                       @FormParam("date") String date,
                                       @FormParam("time") String time,
                                       @FormParam("doctor") String doctor) {

        try {
            doctorService.registerNewPatient(patient, date, time, doctor);
            return Response.status(201).entity("The patient was successfully registered").build();
        } catch (DoctorNotFoundException | WorkdayNotFoundException | TimeSlotNotFoundException e) {
            e.printStackTrace();
            return Response.status(404)
                    .entity(e.getMessage())
                    .build();
        } catch (TimeSlotIsNotFreeException e) {
            e.printStackTrace();
            return Response.status(400)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/workday/{lastname}")
    public Response addWorkday(@PathParam("lastname") String lastName, Workday workday) {
        try {
            doctorService.addWorkday(lastName, workday);
            return Response.status(201).entity("The workday was successfully added").build();
        } catch (DoctorNotFoundException e) {
            e.printStackTrace();
            return Response.status(404)
                    .entity(e.getMessage())
                    .build();
        } catch (WorkdayNotFreeException e) {
            e.printStackTrace();
            return Response.status(400)
                    .entity(e.getMessage())
                    .build();
        }
    }

//    @GET
//    @Path("/alldocs/xml")
//    @Produces(MediaType.APPLICATION_XML)
//    public Response getAllDoctorsXML() {
//        List<Doctor> list = doctorService.getDoctors();
//        GenericEntity<List<Doctor>> docsWrapper = new GenericEntity<List<Doctor>>(list) {
//        };
//        return Response.ok(docsWrapper).build();
//    }
//
//
//    @GET
//    @Path("/alldocs/json")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getAllDoctorsJSON() {
//        return Response.ok(doctorService.getDoctors()).build();
//    }
//
//    @GET
//    @Path("/json/registration/date/{date}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getRegistrationDateJSON(@PathParam("date") String date) {
//        return Response.ok(doctorService.getFreeTimeSlotsOnCertainDate(date)).build();
//    }
//
//    @GET
//    @Path("/surname/json/{lastName}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getDoctorByLastNameJSON(@PathParam("lastName") String lastName) throws DoctorNotFoundException {
//        return Response.ok(doctorService.getDoctorByLastName(lastName)).build();
//    }
//
//    @GET
//    @Path("/surname/xml/{lastName}")
//    @Produces(MediaType.APPLICATION_XML)
//    public Response getDoctorByLastNameXML(@PathParam("lastName") String lastName) throws DoctorNotFoundException {
//        Doctor doc = doctorService.getDoctorByLastName(lastName);
//        System.out.println(doctorService.getDoctorByLastName(lastName));
//        GenericEntity<Doctor> docsWrapper = new GenericEntity<Doctor>(doc) {
//        };
//        return Response.ok(docsWrapper).build();
//    }
}
