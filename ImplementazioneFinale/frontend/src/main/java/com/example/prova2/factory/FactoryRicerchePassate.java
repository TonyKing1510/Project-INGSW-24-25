package com.example.prova2.factory;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class FactoryRicerchePassate {
    public static Pane createPane(){
        Pane pane = new Pane();
        pane.setPrefSize(358, 478);
        pane.setStyle("-fx-background-color: #f3f6f4; -fx-border-color: #011638; -fx-background-radius: 45; -fx-border-radius: 45;");
        pane.setMinSize(358, 478);
        pane.setMaxSize(358, 478);
        return pane;
    }

    private static void makeOmbra(Pane pane) {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        shadow.setColor(Color.rgb(0, 0, 0, 0.3)); // Ombra nera con trasparenza

        pane.setOnMouseEntered(event -> {
            pane.setScaleX(1.01);
            pane.setScaleY(1.01);
            pane.setEffect(shadow); // Applica l'ombra
        });

        pane.setOnMouseExited(event -> {
            pane.setScaleX(1.0);
            pane.setScaleY(1.0);
            pane.setEffect(null); // Rimuove l'ombra
        });
    }
}
