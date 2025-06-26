package com.example.prova2.view;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import com.example.prova2.controller.CalendarioAgenteController;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;

public class CalendarioAgente {
    public static void initCalendario(Stage primaryStage) {

        CalendarView calendarView = new CalendarView();
        Calendar appuntamenti = new Calendar("Appuntamenti"); // (2)
        appuntamenti.setStyle(Calendar.Style.STYLE1); // (3)
        CalendarSource myCalendarSource = new CalendarSource("My Calendars"); // (4)
        myCalendarSource.getCalendars().addAll(appuntamenti);
        calendarView.getCalendarSources().addAll(myCalendarSource); // (5)

        calendarView.setRequestedTime(LocalTime.now());

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });
                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
        Stage calendarStage = new Stage();
        Scene scene = new Scene(calendarView, 1540, 800);
        calendarStage.setScene(scene);
        calendarStage.setTitle("I miei appuntamenti");

        calendarStage.setOnCloseRequest(event -> {
            primaryStage.show();
        });
        calendarStage.show();
        primaryStage.hide();
        CalendarioAgenteController.initEventiAgente(appuntamenti);
    }
}
