package it.unina.dietiEstates25;

import it.unina.dietiEstates25.dto.request.AddImmobileRequestDTO;
import it.unina.dietiEstates25.dto.request.RicercaImmobileDTORequest;
import it.unina.dietiEstates25.dto.response.DatiImmobileDTO;
import it.unina.dietiEstates25.dto.response.ImmobileDTO;
import it.unina.dietiEstates25.dto.response.ImmobileResponseRicercaDTO;
import it.unina.dietiEstates25.filter.RequireJWTAuthentication;
import it.unina.dietiEstates25.model.Immobile;
import it.unina.dietiEstates25.service.ImmobileService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import static it.unina.dietiEstates25.GestoreController.validaInput;

@Path("immobile")
public class ImmobileController {
    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public static Response addImmobile(AddImmobileRequestDTO immobile){
        String jsonResponse=validaInput(immobile);
        if(inputNonValido(jsonResponse)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        ImmobileDTO dto=ImmobileService.addImmobile(immobile);
        if(isImmobileNonInserito(dto)){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ImmobileDTO(false,true,true))
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(new ImmobileDTO(true,false,false))
                .build();
    }


    public static boolean inputNonValido(String jsonResponse) {
        return jsonResponse != null;
    }

    private static boolean isImmobileNonInserito(ImmobileDTO dto) {
        return !dto.isImmobileInserito();
    }

    @Path("annunciRecenti")
    @GET
    @RequireJWTAuthentication
    public List<Immobile> getAllAnnunci(@QueryParam("email") String email) {
        List<Immobile> immobiles = ImmobileService.getAllAnnunci(email);
        if (immobiles.isEmpty()) {
            return new ArrayList<>();
        }
        return immobiles;
    }

    @Path("searchImmobile")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getImmobili(RicercaImmobileDTORequest ricerca){

        List<ImmobileResponseRicercaDTO> risultati=ImmobileService.getImmobile(ricerca);
        if(risultati.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(risultati).build();
    }


    @Path("getInfoAboutImmobile")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getInfoAbout(@QueryParam("idImmobile") int id){
        DatiImmobileDTO immobile=ImmobileService.getInfoAboutImmobile(id);
        if(immobile==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(immobile).build();
    }

    @Path("AnnunciAgente")
    @GET
    @RequireJWTAuthentication
    public Response getAllAnnunciAgente(@QueryParam("mail") String mail){
        if(mail == null || mail.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<ImmobileResponseRicercaDTO> risultati = ImmobileService.getImmobileForAgente(mail);
        if(risultati.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(risultati).build();
    }

    @Path("getInfoAboutImmobileById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getInfoAboutAnnuncio(@QueryParam("idImmobile") int id){
        ImmobileResponseRicercaDTO immobile=ImmobileService.getInfoAboutImmobileById(id);
        if(immobile==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(immobile).build();
    }

    @Path("deleteImmobile")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteImmobileById(@QueryParam("id") int id){
        boolean response=ImmobileService.deleteImmobileById(id);
        if(!response){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }


}
