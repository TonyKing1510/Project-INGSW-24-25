package com.example.prova2.facade;
import com.example.prova2.dto.AgenteDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.service.AuthService;
import com.example.prova2.service.GoogleService;
import java.io.IOException;

public class AuthServiceFacade {


    public static AgenteDTO login(String email,String password){
        try {
            return AuthService.requestLogin(email, password);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return null;
        }
    }


    public static AgenteDTO authWithGoogle(String code){
        AgenteDTO response=GoogleService.exchangeCodeForToken(code);
        if(response!=null){
            return response;
        }
        AlertFactory.generateFailAlertForErroreInterno();
        return null;
    }







}
