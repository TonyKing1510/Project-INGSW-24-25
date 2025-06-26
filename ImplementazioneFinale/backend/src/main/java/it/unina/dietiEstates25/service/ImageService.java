package it.unina.dietiEstates25.service;
import it.unina.dietiEstates25.dao.ImageDao;
import it.unina.dietiEstates25.dao.ImageDaoImpl;
import it.unina.dietiEstates25.dto.response.FotoDTO;
import java.sql.SQLException;
import java.util.*;

public class ImageService {

    private ImageService(){}

    public static FotoDTO addImageAlDB(String path, String cf) {
        try {
            ImageDao dao = new ImageDaoImpl();
            return dao.addImage(path,cf);
        } catch (SQLException e) {
            return errorDto();
        }
    }

    public static List<String> getPublicIdByCF(String cf) {
        try {
            ImageDao dao = new ImageDaoImpl();
            return dao.getPublicIdByCf(cf);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    public static List<String> getUriOfImmobile(int idImmobile) {
        try {
            ImageDao dao = new ImageDaoImpl();
            return dao.getImageOfImmobile(idImmobile);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }


    private static FotoDTO errorDto() {
        return new FotoDTO(false,true);
    }






}
