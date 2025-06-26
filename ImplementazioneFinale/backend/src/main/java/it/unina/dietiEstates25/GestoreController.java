package it.unina.dietiEstates25;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unina.dietiEstates25.dto.request.AddGestoreRequestDTO;
import it.unina.dietiEstates25.dto.request.CheckPasswordRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdateDatiRequestDTO;
import it.unina.dietiEstates25.dto.response.*;
import it.unina.dietiEstates25.filter.RequireJWTAuthentication;
import it.unina.dietiEstates25.service.*;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

import static it.unina.dietiEstates25.AgenteController.validaCf;
import static it.unina.dietiEstates25.ClienteController.conflictResponse;
import static it.unina.dietiEstates25.ClienteController.validaEmail;
import static it.unina.dietiEstates25.ImmobileController.inputNonValido;

@Path("gestore")
public class GestoreController {

    private static final Logger logger = LoggerFactory.getLogger(GestoreController.class);

    @RequireJWTAuthentication
    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGestore(AddGestoreRequestDTO request) {
        String jsonResponse=validaInput(request);
        if(jsonResponse != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        GestoreDTO dto= AddGestoreService.addGestore(request);
        if (duplicato(dto)) return Response.status(Response.Status.CONFLICT).entity(dto).build();
        if (erroreInterno(dto)) return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.ok(dto).build();
    }


    @RequireJWTAuthentication
    @Path("checkPassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public static Response checkPassword(CheckPasswordRequestDTO requestDTO) {
        String jsonResponse=validaInput(requestDTO);
        if(jsonResponse != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        if(PasswordCheckGestoreService.checkPassword(requestDTO)){
            return Response.status(Response.Status.OK).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    public static <T> String validaInput(T object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        String jsonResponse = null;
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<T> violation : violations) {
                errorMessages.add(violation.getMessage());
            }
            jsonResponse = creaJsonCheContieneErrori(errorMessages,jsonResponse);
        }
        return jsonResponse;
    }


    private static String creaJsonCheContieneErrori(List<String> errorMessages, String jsonResponse) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("errors", errorMessages);
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonResponse = mapper.writeValueAsString(response);
        } catch (Exception e) {
            logger.error("Errore nella verifica dell'input",e);
        }
        return jsonResponse;
    }

    @Path("cf")
    @GET
    @RequireJWTAuthentication
    public List<String> getCf() throws SQLException {
        List<String> cf= GestoreService.getAllCfService();
        if(cf.isEmpty()){
            return new ArrayList<>();
        }else{
            return cf;
        }
    }

    @Path("email")
    @GET
    @RequireJWTAuthentication
    public List<String> getMail() throws SQLException {
        List<String> mails= GestoreService.getAllEmailService();
        if(mails.isEmpty()){
            return new ArrayList<>();
        }else{
            return mails;
        }
    }

    @Path("ricerche/{cf}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRicerhce(@PathParam("cf")String cf){
        if(validaCf(cf)){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<GetAllRicercheResponse> ricerche= GestoreService.getRicerche(cf);
        if(ricerche==null || ricerche.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(ricerche).build();
    }

    @Path("dati")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response fetchDatiOfGestore(@QueryParam("email") String email) {
        if (email == null || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Email is required")
                    .build();
        }
        try {
            GestoreAgenziaImmobiliareDatiDTO dto = GestoreService.getGestoreByEmail(email);
            if (dto == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Gestore not found")
                        .build();
            }
            return Response
                    .status(Response.Status.OK)
                    .entity(dto)
                    .build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching data from database")
                    .build();
        }
    }

    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateGestore(UpdateDatiRequestDTO gestore, @QueryParam("emailAttuale") String emailAttuale) {
        if(validaEmail(emailAttuale)){
            return Response.status(Response.Status.BAD_REQUEST).entity("Devi inserire l'email attuale!").build();
        }
        String jsonResponse=validaInput(gestore);
        if(inputNonValido(jsonResponse)){
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        UpdateDatiResponseDTO response= GestoreService.updateDati(gestore,emailAttuale);
        return getResponse(response);
    }

    @Path("deleteGestore")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteGestoreByEmail(@QueryParam("email") String email){
        boolean response=GestoreService.deleteGestoreByEmail(email);
        if(response){
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("getAccountGestori")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@QueryParam("cf") String cf){
        List<DatiDTO> accountGestori=GestoreService.getAccountGestori(cf);
        if(accountGestori.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).entity(accountGestori).build();
        }
        return Response.status(Response.Status.OK).entity(accountGestori).build();
    }



    static Response getResponse(UpdateDatiResponseDTO response) {
        if(response == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if(response.getEmailDuplicate()){
            return conflictResponse();
        }
        if(response.getError()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
        return Response.status(Response.Status.OK).entity(response).build();
    }

    private static boolean erroreInterno(GestoreDTO dto) {
        return dto.isErroreInterno();
    }

    private static boolean duplicato(GestoreDTO dto) {
        return dto.isDuplicatoCF() || dto.isDuplicatoEmail();
    }
}
