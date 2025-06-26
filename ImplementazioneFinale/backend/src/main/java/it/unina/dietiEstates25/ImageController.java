package it.unina.dietiEstates25;
import it.unina.dietiEstates25.dto.request.ImageUploadRequest;
import it.unina.dietiEstates25.dto.response.FotoDTO;
import it.unina.dietiEstates25.service.ImageService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("image")
public class ImageController {
    @Path("upload")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uploadImageofUtente(@QueryParam("codicefiscale")String cf, ImageUploadRequest key) {
        if(isNotValidCF(cf)) {
            return badRequest();
        }
        FotoDTO response=ImageService.addImageAlDB(key.getKey(), cf);
        if(response.isErrore()){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
        return Response.ok().build();
    }

    private static boolean isNotValidCF(String cf) {
        return cf.length() != 16;
    }


    private static Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @GET
    @Path("fetchByCf")
    public Response getImageByCf(@QueryParam("codicefiscale") String cf) {
        if(isNotValidCF(cf)) {
            return badRequest();
        }
        List<String> ids=ImageService.getPublicIdByCF(cf);
        if(ids.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ids).build();
    }

    @GET
    @Path("getImageByIdImmobile")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getImageByIdImmobile(@QueryParam("idImmobile") int idImmobile) {
        List<String> ids=ImageService.getUriOfImmobile(idImmobile);
        if(ids.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ids).build();
    }

}
