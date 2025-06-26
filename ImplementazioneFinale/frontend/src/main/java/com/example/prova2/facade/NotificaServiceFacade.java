package com.example.prova2.facade;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Agente;
import com.example.prova2.model.CategoriaNotifica;
import com.example.prova2.model.Cliente;
import com.example.prova2.service.NotificaService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotificaServiceFacade {


    public static List<GetNotificheDTO> getNotificaCliente(Cliente cliente){
        try {
            return NotificaService.getNotificationsForCliente(cliente);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static boolean disattivaCategoriaPerAgente(String categoriaNotifica,String cf,String token){
        categoriaNotifica=categoriaNotifica.replace(" ","");
        categoriaNotifica=categoriaNotifica.toUpperCase();
        boolean res = NotificaService.disattivaCategoriaNotificaPerAgente(categoriaNotifica,cf,token);
        if(!res){
            AlertFactory.generateFailAlertForErroreInterno();
            return false;
        }
        return true;
    }


    public static List<CategoriaNotifica> getCategorieDispCliente(String email,String token){
        return NotificaService.getCategorieDisponibiliCliente(email,token);
    }

    public static List<CategoriaNotifica> getCategorieDispAgente(String cf,String token){
        return NotificaService.getCategorieDisponibiliAgente(cf,token);
    }

    public static boolean disattivaCategoriaPerCliente(String categoriaNotifica,String email,String token){
        categoriaNotifica=categoriaNotifica.replace(" ","");
        categoriaNotifica=categoriaNotifica.toUpperCase();
        return NotificaService.disattivaCategoriaNotificaPerCliente(categoriaNotifica,email,token);
    }

    public static boolean attivaCategoriaPerCliente(String categoriaNotifica,String email,String token){
        categoriaNotifica=categoriaNotifica.replace(" ","");
        categoriaNotifica=categoriaNotifica.toUpperCase();
        return NotificaService.attivaCategoriaNotificaPerCliente(categoriaNotifica,email,token);
    }

    public static boolean attivaCategoriaPerAgente(String categoriaNotifica,String cf,String token){
        categoriaNotifica=categoriaNotifica.replace(" ","");
        categoriaNotifica=categoriaNotifica.toUpperCase();
        boolean res = NotificaService.attivaCategoriaNotificaPerAgente(categoriaNotifica,cf,token);
        if(!res){
            AlertFactory.generateFailAlertForErroreInterno();
            return false;
        }
        return true;
    }

    public static List<CategoriaNotifica> getCategorieDisattivate(Agente agente){
        List<CategoriaNotifica> categorie=NotificaService.getCategorieDisattivate(agente);
        if(!categorie.isEmpty()){
            ordinaInModoDecrescenteAlfabetico(categorie);
            return categorie;
        }
        return Collections.emptyList();
    }

    public static List<CategoriaNotifica> getCategorieDisattivateCliente(Cliente cliente){
        List<CategoriaNotifica> categorie=NotificaService.getCategorieDisattivateCliente(cliente);
        if(!categorie.isEmpty()){
            ordinaInModoDecrescenteAlfabetico(categorie);
            return categorie;
        }
        return Collections.emptyList();
    }

    private static void ordinaInModoDecrescenteAlfabetico(List<CategoriaNotifica> categorie) {
        categorie.sort(Comparator.comparing(CategoriaNotifica::getLabel).reversed());
    }



}
