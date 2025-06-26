package it.unina.dietiEstates25;
import it.unina.dietiEstates25.dto.request.AddVisitaRequestDTO;
import it.unina.dietiEstates25.dto.response.*;
import it.unina.dietiEstates25.filter.RequireJWTAuthentication;
import it.unina.dietiEstates25.service.VisitaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import static it.unina.dietiEstates25.GestoreController.validaInput;
import static it.unina.dietiEstates25.ImmobileController.inputNonValido;

@Path("visita")
public class VisitaController {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Path("addVisita")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response addVisita(AddVisitaRequestDTO visitaDaAggiungere) {
        String jsonResponse=validaInput(visitaDaAggiungere);
        if(inputNonValido(jsonResponse)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        VisitaResponseDTO response=VisitaService.addVisita(visitaDaAggiungere);
        if(response.getFail()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Path("getVisiteOf")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getVisiteOf(@QueryParam("cf")String cf) {
        if(cf.length() != 16){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<GetVisiteResponse> visite=VisitaService.getAllVisiteByCf(cf);
        if(visite.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(visite).build();
    }

    @Path("getInfoAboutVisita")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getInfoAboutVisita(@QueryParam("idNotifica")int id) {
        InformazioniVisitaDTO response=VisitaService.getInfoAboutVisita(id);
        if(response == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Path("dateEOreOccupate")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response dateEOreOccupate(@QueryParam("cf")String cf) {
        if(cf==null||cf.length() != 16){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<DateEOreOccupateAgente> response=VisitaService.getDateEOreOccupateAgente(cf);
        if(response == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Path("checkVisita/{idImmobile}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response checkVisitaPerCliente(@PathParam("idImmobile") Integer idImmobile,
                                          @QueryParam("email") String email) {
        Response validate=validateInputsForCheckVisita(email, idImmobile);
        if(validate!=null){
            return validate;
        }
        CheckVisitaPerClienteDTO response=VisitaService.checkUtenteHaVisitaPerImmobile(email,idImmobile);
        if(response.isVisitGiaPrenotata()){
            return Response.status(Response.Status.NOT_FOUND).entity("Il cliente ha già una visita a carico").build();
        }else if(response.isInternalError()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).entity(true).build();
    }

    private Response validateInputsForCheckVisita(String email, Integer idImmobile) {
        if (email == null || !email.matches(EMAIL_REGEX) || idImmobile == null || idImmobile <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return null;
    }


    @Path("delete/visita")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public static Response deleteVisita(AddVisitaRequestDTO visita){
        String jsonResponse=validaInput(visita);
        if(inputNonValido(jsonResponse)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        boolean response=VisitaService.deleteVisita(visita);
        if(response){
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    /*returna true se il cliente non ha una visita per quell'immobile*/
}
