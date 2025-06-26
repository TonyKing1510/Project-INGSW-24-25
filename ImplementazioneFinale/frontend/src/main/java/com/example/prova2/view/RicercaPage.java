package com.example.prova2.view;
import com.example.prova2.controller.SchermataRicercaController;
import com.example.prova2.model.ClasseEnergetica;
import com.example.prova2.model.TipoVendita;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.math.BigDecimal;

public class RicercaPage {
    private RicercaPage() {}

    public static void caricamento(Stage primaryStage, String comune, TipoVendita tv, BigDecimal prezzoMin, BigDecimal prezzoMax, Integer numStanze, ClasseEnergetica ce) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/schermataRicerca.fxml"));
        Scene mainScene = new Scene(loader.load(), 1540, 790);
        SchermataRicercaController controller = loader.getController();
        new Thread(() -> {
            controller.initialize(comune,tv,prezzoMin,prezzoMax,numStanze,ce);
            controller.loadAnnunci(comune, tv, prezzoMin, prezzoMax, numStanze, ce);
            controller.setTextIniziali(tv,prezzoMin,prezzoMax,numStanze,ce);
        }).start();
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("I tuo immobili");
    }
}
