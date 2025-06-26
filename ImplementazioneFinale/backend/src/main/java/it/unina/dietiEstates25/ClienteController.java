package it.unina.dietiEstates25;
import it.unina.dietiEstates25.dto.request.AddClienteRequestDTO;
import it.unina.dietiEstates25.dto.request.LasciaRecensioneRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdateDatiRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdatePasswordRequestDTO;
import it.unina.dietiEstates25.dto.response.ClienteDTO;
import it.unina.dietiEstates25.dto.response.ClienteDatiDTO;
import it.unina.dietiEstates25.dto.response.GetAllRicercheResponse;
import it.unina.dietiEstates25.dto.response.UpdateDatiResponseDTO;
import it.unina.dietiEstates25.filter.RequireJWTAuthentication;
import it.unina.dietiEstates25.service.ClienteService;
import it.unina.dietiEstates25.service.UpdatePasswordService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static it.unina.dietiEstates25.GestoreController.getResponse;
import static it.unina.dietiEstates25.GestoreController.validaInput;

@Path("cliente")
public class ClienteController {

    private ClienteService clienteService = new ClienteService();

    public ClienteController(){}

    public ClienteController(ClienteService clienteService) {
        this.clienteService=clienteService;
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCliente(AddClienteRequestDTO cliente) {
        String jsonResponse=validaInput(cliente);
        if(inputNonValido(jsonResponse)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        ClienteDTO dto = ClienteService.addCliente(cliente);
        if(isVerificatoErroreInterno(dto)){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if(isClienteEsiste(dto)){
            return conflictResponse();
        }else{
            String token = AuthController.createJWT(cliente.getEmail(), TimeUnit.DAYS.toMillis(365));
            return responseWithToken(token);
        }
    }

    private static boolean inputNonValido(String jsonResponse) {
        return jsonResponse != null;
    }

    private static Response responseWithToken(String token) {
        return Response
                .status(Response.Status.OK)
                .entity(new ClienteDTO(token, false))
                .build();
    }

    static Response conflictResponse() {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(new ClienteDTO(null, true))
                .build();
    }


    private static boolean isVerificatoErroreInterno(ClienteDTO dto) {
        return dto.isErroreInterno();
    }

    private static boolean isClienteEsiste(ClienteDTO dto) {
        return dto.isDuplicato();
    }

    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCliente(UpdateDatiRequestDTO cliente,@QueryParam("emailAttuale") String emailAttuale) {
        if(validaEmail(emailAttuale)){
            return Response.status(Response.Status.BAD_REQUEST).entity("Devi inserire l'email attuale!").build();
        }
        String jsonResponse=validaInput(cliente);
        if(inputNonValido(jsonResponse)){
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        UpdateDatiResponseDTO response=ClienteService.updateDati(cliente,emailAttuale);
        return getResponse(response);
    }

    @Path("getUtente")
    @GET
    @RequireJWTAuthentication
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUtenteByEmail(@QueryParam("email") String email){
        if(validaEmail(email)){
            return Response.status(Response.Status.BAD_REQUEST).entity("Inserisci una email valida!").build();
        }
        ClienteDatiDTO response=ClienteService.getCliente(email);
        if(response == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).entity(response).build();
    }

    static boolean validaEmail(String emailAttuale) {
        return emailAttuale == null || emailAttuale.isEmpty();
    }

    @Path("lasciaRecensione")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response lasciaRecensione(LasciaRecensioneRequestDTO recensione){
        String jsonResponse=validaInput(recensione);
        if(inputNonValido(jsonResponse)){
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        boolean esito=clienteService.lasciaRecensione(recensione);
        if(!esito){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @Path("getRicerche")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getAllRicerche(@QueryParam("email")String email){
        if(validaEmail(email)){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<GetAllRicercheResponse> ricerche=ClienteService.getAllRicerche(email);
        if(ricerche == null){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if(ricerche.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(ricerche).build();
    }

    @Path("updatePassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean updatePassword(UpdatePasswordRequestDTO request){
        return UpdatePasswordService.updatePasswordCliente(request);
    }
}
