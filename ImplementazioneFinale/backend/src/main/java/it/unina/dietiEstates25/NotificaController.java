package it.unina.dietiEstates25;
import it.unina.dietiEstates25.dto.request.UpdateCategoriaNotificaDTO;
import it.unina.dietiEstates25.dto.response.NotificaDTO;
import it.unina.dietiEstates25.dto.response.UpdateCategoriaResponse;
import it.unina.dietiEstates25.filter.RequireJWTAuthentication;
import it.unina.dietiEstates25.model.CategoriaNotifica;
import it.unina.dietiEstates25.service.CategoriaService;
import it.unina.dietiEstates25.service.NotificationsService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import static it.unina.dietiEstates25.GestoreController.validaInput;
import static it.unina.dietiEstates25.ImmobileController.inputNonValido;


@Path("not")
public class NotificaController {
    @GET
    @RequireJWTAuthentication
    public Response getAllNotificationsOfGestore(@QueryParam("cf") String cf) {
        List<it.unina.dietiEstates25.model.Notifica> notifications = NotificationsService.getNotificationOfAdminService(cf);
        if (notifications.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(notifications).build();
    }

    @GET
    @Path("/agente")
    @RequireJWTAuthentication
    public Response getAllNotificationsAgente(@QueryParam("cf") String cf) {
        List<it.unina.dietiEstates25.model.Notifica> notifications = NotificationsService.getNotificaOfAgenteService(cf);
        if (notifications.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(notifications).build();
    }

    @GET
    @Path("/cliente")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getAllNotificationsCliente(@QueryParam("email") String email) {
        if(email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<it.unina.dietiEstates25.model.Notifica> notifications = NotificationsService.getNotificaOfClienteService(email);
        if (notifications.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(notifications).build();
    }


    @PUT
    @Path("/{id}/accetta")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public NotificaDTO accettaNotifica(@PathParam("id") int idNotifica) {
        return NotificationsService.setNotificationAccepted(idNotifica);
    }


    @DELETE
    @Path("/{id}/elimina")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public NotificaDTO eliminaNotifica(@PathParam("id") int idNotifica) {
        return NotificationsService.setNotificationRejected(idNotifica);
    }

    @PUT
    @Path("/{id}/annullaInvioNotifica")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response annullaInvioNotifica(@PathParam("id") int idNotifica) {
        boolean ris=NotificationsService.annullaInvioNotifica(idNotifica);
        if(!ris){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/disattivaCategoria/agente")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response disattivaCategoriaAgente(@QueryParam("cf")String cf, UpdateCategoriaNotificaDTO dto) {
        if (validaCf(cf)) return Response.status(Response.Status.BAD_REQUEST).build();
        String jsonResponse=validaInput(dto);
        if(inputNonValido(jsonResponse)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        UpdateCategoriaResponse response=CategoriaService.setNonInviareCategoriaAdAgente(cf,dto);
        return getResponse(response);
    }

    private static boolean validaCf(String cf) {
        return (cf == null || cf.length() < 16);
    }


    @Path("categorieDisattivateAgente")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getCategorieDisattivateAgente(@QueryParam("cf")String cf){
        if (validaCf(cf)) return Response.status(Response.Status.BAD_REQUEST).build();
        List<CategoriaNotifica> categorie= CategoriaService.getCategorieDisattivateByCfAgente(cf);
        if (categorie.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(categorie).build();
    }

    @Path("categorieCliente")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getAllCategorieCliente(@QueryParam("email")String email){
        if(email == null||email.isEmpty()) return Response.status(Response.Status.BAD_REQUEST).build();
        List<CategoriaNotifica> categorie= CategoriaService.getAllCategorieByEmailCliente(email);
        if (categorie.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(categorie).build();
    }

    @Path("categorieAgente")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getAllCategorieAgente(@QueryParam("cf")String cf){
        if(validaCf(cf)){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<CategoriaNotifica> categorie= CategoriaService.getAllCategorieByCFAgente(cf);
        if (categorie.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(categorie).build();
    }

    @Path("categorieDisattivateCliente")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getCategorieDisattivateCliente(@QueryParam("email")String email){
        if(email == null||email.isEmpty()) return Response.status(Response.Status.BAD_REQUEST).build();
        List<CategoriaNotifica> categorie= CategoriaService.getCategorieDisattivateByEmailCliente(email);
        if (categorie.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(categorie).build();
    }


    @POST
    @Path("/attivaCategoria/agente")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response attivaCategoriaAgente(@QueryParam("cf")String cf, UpdateCategoriaNotificaDTO dto) {
        if (validaCf(cf)) return Response.status(Response.Status.BAD_REQUEST).build();
        String jsonResponse=validaInput(dto);
        if(inputNonValido(jsonResponse)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        UpdateCategoriaResponse response=CategoriaService.setInviareCategoriaAdAgente(cf,dto);
        return getResponse(response);
    }

    @POST
    @Path("/attivaCategoria/cliente")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response attivaCategoriaCliente(@QueryParam("email")String email, UpdateCategoriaNotificaDTO dto) {
        if (validaEmail(email)) return Response.status(Response.Status.BAD_REQUEST).build();
        String jsonResponse=validaInput(dto);
        if(inputNonValido(jsonResponse)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        UpdateCategoriaResponse response=CategoriaService.setInviaCategoriaAdCliente(email,dto);
        return getResponse(response);
    }

    private static boolean validaEmail(String email) {
        return (email == null || email.isEmpty());
    }

    @POST
    @Path("/disattivaCategoria/cliente")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response disattivaCategoriaCliente(@QueryParam("email")String email, UpdateCategoriaNotificaDTO dto) {
        if (validaEmail(email)) return Response.status(Response.Status.BAD_REQUEST).build();
        String jsonResponse=validaInput(dto);
        if(inputNonValido(jsonResponse)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        UpdateCategoriaResponse response=CategoriaService.setNonInviareCategoriaAdCliente(email,dto);
        return getResponse(response);
    }

    private Response getResponse(UpdateCategoriaResponse response) {
        if(response.isSuccess()){
            return Response.ok(response).build();
        }else {
            if (response.isNessunaNotificaTrovata()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (response.isCfExists()) {
                return Response.status(Response.Status.CONFLICT).build();
            }
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

}
