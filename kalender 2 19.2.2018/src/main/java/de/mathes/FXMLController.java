/**
 * Title: FXMLController.
 * Description:
 *
 * @author nm
 */
package de.mathes;

import de.mathes.backend.Speicher;
import de.mathes.backend.SpeicherH2;
import de.mathes.backend.Speicherdatei;
import de.mathes.klassen.Benutzer;
import de.mathes.klassen.Benutzer_Projekt;
import de.mathes.klassen.Projekte;
import de.mathes.klassen.Urlaub;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FXMLController {

    private Speicher speicherart = null;
    List<Projekte> projektListe = null;
    List<Benutzer> benutzerList = null;
    List<Benutzer_Projekt> benutzer_projektListInZeitraum = null;
    LocalDate beginDatum = DatumsRechner.getStartDay();


    @FXML
    private Label label;

    @FXML
    private VBox pnl_benutzer;

    @FXML
    private VBox pnl_projekte;

    @FXML
    private VBox pnl_left;

    @FXML
    private VBox pnl_right;

    @FXML
    private Button btn_leftPaneClose;

    @FXML
    private Button btn_leftPaneOpen;

    @FXML
    private Button btn_rightPaneClose;

    @FXML
    private Button btn_rightPaneOpen;

    @FXML
    private Button btn_WeekBevor;

    @FXML
    private Button btn_MonthBevor;

    @FXML
    private Button btn_DayToday;

    @FXML
    private Button btn_WeekAfter;

    @FXML
    private Button btn_MonthAfter;

    @FXML
    private GridPane pnl_grid01;

    //Button um Kalenderwochen zu verschieben
    @FXML
    private void handleWeekBefore(ActionEvent event) {
        beginDatum = beginDatum.plusDays(7);
        zeigeKalender();
    }

    @FXML
    private void handleMonthBefore(ActionEvent event) {
        beginDatum = beginDatum.plusDays(28);
        zeigeKalender();
    }

    @FXML
    private void handleWeekAfter(ActionEvent event) {
        beginDatum = beginDatum.minusDays(7);
        zeigeKalender();
    }

    @FXML
    private void handleMonthAfter(ActionEvent event) {
        beginDatum = beginDatum.minusDays(28);
        zeigeKalender();
    }

    @FXML
    private void handleDayToday(ActionEvent event) {
        beginDatum = DatumsRechner.getStartDay();
        zeigeKalender();
    }

    //Button um linke und rechte Seite zu öffnen/schließen
    @FXML
    private void handleLeftPaneClose(ActionEvent event) {
        handlePane(pnl_left, 0, btn_leftPaneOpen, btn_leftPaneClose, true, false, 36);
    }

    @FXML
    private void handleLeftPaneOpen(ActionEvent event) {
        handlePane(pnl_left, 340, btn_leftPaneOpen, btn_leftPaneClose, false, true, 0);
    }

    @FXML
    private void handleRightPaneClose(ActionEvent event) {
        handlePane(pnl_right, 0, btn_rightPaneOpen, btn_rightPaneClose, true, false, 36);
    }

    @FXML
    private void handleRightPaneOpen(ActionEvent event) {
        handlePane(pnl_right, 320, btn_rightPaneOpen, btn_rightPaneClose, false, true, 0);
    }

    private void handlePane(VBox vbox, int width, Button button1, Button button2, Boolean visibleBtn1, Boolean visibleBtn2, int btnSize) {
        vbox.setMaxWidth(width);
        vbox.setMinWidth(width);
        vbox.setPrefWidth(width);
        button1.setVisible(visibleBtn1);
        button2.setVisible(visibleBtn2);
        button1.setMinSize(btnSize, btnSize);
        button1.setMaxSize(btnSize, btnSize);
        button1.setPrefSize(btnSize, btnSize);
    }


    //Alle Projekte abwählen
    @FXML
    private void handleAlleProjekteAbwaehlen(ActionEvent event) {
        for (Projekte projekt : projektListe) {
            projekt.setProjektAnzeigem(false);
        }
        zeigeProjekte();
        zeigeKalender();
    }

    //Alle Projekte auswählen
    @FXML
    private void handleAlleProjekteAuswaehlen(ActionEvent event) {
        for (Projekte projekt : projektListe) {
            projekt.setProjektAnzeigem(true);
        }
        zeigeProjekte();
        zeigeKalender();
    }

    //Alle Benutzer abwählen
    @FXML
    private void handleAlleBenutzerAbwaehlen(ActionEvent event) {
        for (Benutzer benutzer : benutzerList) {
            benutzer.setBenutzerAnzeigen(false);
        }
        zeigeBenutzer();
        zeigeKalender();
    }

    //Alle Benutzer auswählen
    @FXML
    private void handleAlleBenutzerAuswaehlen(ActionEvent event) {
        for (Benutzer benutzer : benutzerList) {
            benutzer.setBenutzerAnzeigen(true);
        }
        zeigeBenutzer();
        zeigeKalender();
    }

    //Allen Urlaub abwählen
    @FXML
    private void handleAllenUrlaubAbwaehlen(ActionEvent event) {
        for (Benutzer benutzer : benutzerList) {
            benutzer.setBenutzerUrlaubAnzeigen(false);
        }
        zeigeBenutzer();
        zeigeKalender();
    }

    //Allen Urlaub auswählen
    @FXML
    private void handleAllenUrlaubAuswaehlen(ActionEvent event) {
        for (Benutzer benutzer : benutzerList) {
            benutzer.setBenutzerUrlaubAnzeigen(true);
        }
        zeigeBenutzer();
        zeigeKalender();
    }

    @FXML
    private void handleBenutzerHinzufuegen(ActionEvent event) {
        Dialog<Pair<Color, String>> dialog = new Dialog<>();
        dialog.setTitle("Benutzer erstellen");

        // Set the button types.
        ButtonType benutzer_anlegen = new ButtonType("Benutzer anlegen", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(benutzer_anlegen, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ColorPicker benutzerFarbe = new ColorPicker();
        TextField benutzerName = new TextField();
        benutzerName.setPromptText("Benutzername");


        grid.add(new Label("Benutzerfarbe"), 0, 0);
        grid.add(benutzerFarbe, 1, 0);
        grid.add(new Label("Benutzername:"), 0, 1);
        grid.add(benutzerName, 1, 1);

        // Aktiviere/Deaktivieren Button wenn ein Benutzername angelegt ist
        Node anlegen_Button = dialog.getDialogPane().lookupButton(benutzer_anlegen);
        anlegen_Button.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        benutzerName.textProperty().addListener((observable, oldValue, newValue) -> {
            anlegen_Button.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> benutzerFarbe.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == benutzer_anlegen) {
                return new Pair<>(benutzerFarbe.getValue(), benutzerName.getText());
            }
            return null;
        });

        Optional<Pair<Color, String>> result = dialog.showAndWait();

        result.ifPresent(benutzerFarbeName -> {
            java.awt.Color awtColor = new java.awt.Color((float) benutzerFarbeName.getKey().getRed(),
                    (float) benutzerFarbeName.getKey().getGreen(),
                    (float) benutzerFarbeName.getKey().getBlue(),
                    (float) benutzerFarbeName.getKey().getOpacity());
            speicherart.erstelleBenutzer(benutzerFarbeName.getValue(), awtColor.getRGB());
            aktualisiereBenutzer();
        });

    }

    @FXML
    private void handleProjektHinzufuegen(ActionEvent event) {
        Dialog<Pair<Color, String>> dialog = new Dialog<>();
        dialog.setTitle("Projekt erstellen");

        // Set the button types.
        ButtonType projekt_anlegen = new ButtonType("Projekt anlegen", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(projekt_anlegen, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ColorPicker projektFarbe = new ColorPicker();
        TextField projektName = new TextField();
        projektName.setPromptText("Projektname");


        grid.add(new Label("Projektfarbe"), 0, 0);
        grid.add(projektFarbe, 1, 0);
        grid.add(new Label("Projektname:"), 0, 1);
        grid.add(projektName, 1, 1);

        // Aktiviere/Deaktivieren Button wenn ein Benutzername angelegt ist
        Node anlegen_Button = dialog.getDialogPane().lookupButton(projekt_anlegen);
        anlegen_Button.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        projektName.textProperty().addListener((observable, oldValue, newValue) -> {
            anlegen_Button.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> projektFarbe.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == projekt_anlegen) {
                return new Pair<>(projektFarbe.getValue(), projektName.getText());
            }
            return null;
        });

        Optional<Pair<Color, String>> result = dialog.showAndWait();

        result.ifPresent(projektFarbeName -> {
            java.awt.Color awtColor = new java.awt.Color((float) projektFarbeName.getKey().getRed(),
                    (float) projektFarbeName.getKey().getGreen(),
                    (float) projektFarbeName.getKey().getBlue(),
                    (float) projektFarbeName.getKey().getOpacity());
            speicherart.erstelleProjekt(projektFarbeName.getValue(), awtColor.getRGB());
            aktualisiereProjekte();
        });

    }

    @FXML
    private void handleUrlaubHinzufuegen(ActionEvent event) {
        Dialog<Urlaub> dialog = new Dialog<>();
        dialog.setTitle("Urlaub hinzufügen");
        dialog.setHeaderText("Urlaub hinzufügen");

        // Set the button types.
        ButtonType benutzer_anlegen = new ButtonType("Urlaub hinzufügen", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(benutzer_anlegen, ButtonType.CANCEL);

        //Beginn/Enddatum auswählen
        DatePicker anfangsDatum = new DatePicker(LocalDate.now());
        DatePicker endDatum = new DatePicker(LocalDate.now());

        // Benutzer ComboBox
        ObservableList<Benutzer> benutzer = FXCollections.observableArrayList();
        benutzer.addAll(benutzerList);

        ComboBox<Benutzer> comboBenutzer = new ComboBox<>();
        comboBenutzer.setItems(benutzer);
        comboBenutzer.setConverter(new StringConverter<Benutzer>() {

            @Override
            public String toString(Benutzer object) {
                if (object == null) {
                    return "Bitte Urlauber auswählen";
                }
                return object.getBenutzerName();
            }

            @Override
            public Benutzer fromString(String s) {
                return comboBenutzer.getValue();
            }
        });


        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Urlaubsbeginn"), 0, 0);
        grid.add(anfangsDatum, 1, 0);
        grid.add(new Label("Urlaubsende:"), 0, 1);
        grid.add(endDatum, 1, 1);
        grid.add(new Label("Urlauber:"), 0, 2);
        grid.add(comboBenutzer, 1, 2);

        //Grid zum Dialog hinzufügen
        dialog.getDialogPane().setContent(grid);

        // Aktiviere/Deaktivieren Button wenn ein Benutzername angelegt ist
        Node anlegen_Button = dialog.getDialogPane().lookupButton(benutzer_anlegen);
        anlegen_Button.setDisable(true);

        comboBenutzer.valueProperty().addListener((observable, oldValue, newValue) -> {
            anlegen_Button.setDisable(comboBenutzer.getSelectionModel().isEmpty());
        });

        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == benutzer_anlegen) {
                return new Urlaub(1, comboBenutzer.getValue().getBenutzerId(), anfangsDatum.getValue(), endDatum.getValue());
            }
            return null;
        });

        Optional<Urlaub> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Urlaub results) -> {
            speicherart.erstelleUrlaub(results.getBenutzerId(), results.getUrlaubBeginn(), results.getUrlaubEnde());
        });


    }

    @FXML
    private void handleBenutzerZuProjektHinzufuegen(ActionEvent event) {
        Dialog<Benutzer_Projekt> dialog = new Dialog<>();
        dialog.setTitle("Benutzer zu Projekt hinzufügen");
        dialog.setHeaderText("Benutzer zu Projekt hinzufügen");

        // Set the button types.
        ButtonType benutzer_anlegen = new ButtonType("Benutzer hinzufügen", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(benutzer_anlegen, ButtonType.CANCEL);

        //Beginn/Enddatum auswählen
        DatePicker anfangsDatum = new DatePicker(LocalDate.now());
        DatePicker endDatum = new DatePicker(LocalDate.now());

        // Benutzer ComboBox
        ObservableList<Benutzer> benutzer = FXCollections.observableArrayList();
        benutzer.addAll(benutzerList);

        ComboBox<Benutzer> comboBenutzer = new ComboBox<>();
        comboBenutzer.setItems(benutzer);
        comboBenutzer.setConverter(new StringConverter<Benutzer>() {

            @Override
            public String toString(Benutzer object) {
                if (object == null) {
                    return "Bitte Benutzer auswählen";
                }
                return object.getBenutzerName();
            }

            @Override
            public Benutzer fromString(String s) {
                return comboBenutzer.getValue();
            }
        });

        // Projekt ComboBox
        ObservableList<Projekte> projekte = FXCollections.observableArrayList();
        projekte.addAll(projektListe);

        ComboBox<Projekte> comboProjekte = new ComboBox<>();
        comboProjekte.setItems(projekte);
        comboProjekte.setConverter(new StringConverter<Projekte>() {
            @Override
            public String toString(Projekte object) {
                if (object == null) {
                    return "Bitte Projekt auswählen";
                }
                return object.getProjektName();
            }

            @Override
            public Projekte fromString(String s) {
                return comboProjekte.getValue();
            }
        });

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Projektbeginn"), 0, 0);
        grid.add(anfangsDatum, 1, 0);
        grid.add(new Label("Projektende:"), 0, 1);
        grid.add(endDatum, 1, 1);
        grid.add(new Label("Projekt:"), 0, 2);
        grid.add(comboProjekte, 1, 2);
        grid.add(new Label("Benutzer:"), 0, 3);
        grid.add(comboBenutzer, 1, 3);

        //Grid zum Dialog hinzufügen
        dialog.getDialogPane().setContent(grid);

        // Aktiviere/Deaktivieren Button wenn ein Benutzername angelegt ist
        Node anlegen_Button = dialog.getDialogPane().lookupButton(benutzer_anlegen);
        anlegen_Button.setDisable(true);

        comboBenutzer.valueProperty().addListener((observable, oldValue, newValue) -> {
            anlegen_Button.setDisable(comboBenutzer.getSelectionModel().isEmpty() || comboProjekte.getSelectionModel().isEmpty());
        });

        comboProjekte.valueProperty().addListener((observable, oldValue, newValue) -> {
            anlegen_Button.setDisable(comboBenutzer.getSelectionModel().isEmpty() || comboProjekte.getSelectionModel().isEmpty());
        });

        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == benutzer_anlegen) {
                return new Benutzer_Projekt(comboProjekte.getValue().getProjektId(), comboBenutzer.getValue().getBenutzerId(), anfangsDatum.getValue(), endDatum.getValue());
            }
            return null;
        });

        Optional<Benutzer_Projekt> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Benutzer_Projekt results) -> {
            speicherart.erstelleBenutzerProjekt(results.getProjektId(), results.getBenutzerId(), results.getTeilnahmeBeginn(), results.getTeilnahmeEnde());
            zeigeKalender();
        });


    }

    private void alert(String title, String message, Consumer<Boolean> callback) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);

        Optional<ButtonType> option = alert.showAndWait();

        option.ifPresent((ButtonType o) -> {
            if (option.get() == ButtonType.OK) {
                callback.accept(true);
            }
        });
    }


    public void aktualisiereBenutzer() {
        benutzerList = speicherart.gebeAlleBenutzer();
        zeigeBenutzer();
    }

    private void zeigeBenutzer() {
        pnl_benutzer.getChildren().clear();

        for (Benutzer benutzer : benutzerList) {
            //Rechteck mit Benutzerfarbe
            Color color = intToColor(benutzer.getBenutzerFarbe());
            Rectangle rectangle = new Rectangle(20, 30, 40, 30);
            rectangle.setFill(color);
            //Benutzername
            Label label1 = new Label(benutzer.getBenutzerName());

            //Sichtbar Button
            Button buttonVisibility = new Button();
            ImageView visibilityImageView = null;
            Image visibilityImage = null;
            buttonVisibility.setOnAction((e) -> {
                benutzer.setBenutzerAnzeigen(!benutzer.isBenutzerAnzeigen());
                zeigeBenutzer();
                zeigeKalender();
            });
            if (benutzer.isBenutzerAnzeigen() == true) {
                label1.setStyle("-fx-text-fill: black");
                rectangle.setFill(color);
                visibilityImage = new Image(getClass().getResourceAsStream("bilder/visibility.png"));
            } else {
                label1.setStyle("-fx-text-fill: silver ");
                rectangle.setFill(Color.gray(0.9));
                visibilityImage = new Image(getClass().getResourceAsStream("bilder/visibility_off.png"));
            }
            buttonVisibility.setGraphic(new ImageView(visibilityImage));

            //Urlaub Button
            Button buttonPlane = new Button();
            ImageView planeImageView = null;
            Image planeImage = null;
            buttonPlane.setOnAction((e) -> {
                benutzer.setBenutzerUrlaubAnzeigen(!benutzer.isBenutzerUrlaubAnzeigen());
                zeigeBenutzer();
            });
            if (benutzer.isBenutzerUrlaubAnzeigen() == true) {
                planeImage = new Image(getClass().getResourceAsStream("bilder/plane.png"));
            } else {
                planeImage = new Image(getClass().getResourceAsStream("bilder/plane_inactive.png"));
            }
            buttonPlane.setGraphic(new ImageView(planeImage));

            //Beabeiten Button
            Button buttonEdit = new Button();
            buttonEdit.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("bilder/edit.png"))));
            buttonEdit.setOnAction(event -> {
                Dialog<Pair<Color, String>> dialog = new Dialog<>();
                dialog.setTitle("Benutzer bearbeiten");

                // Set the button types.
                ButtonType benutzer_bearbeiten = new ButtonType("Benutzer bearbeiten", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(benutzer_bearbeiten, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                ColorPicker benutzerFarbe = new ColorPicker();
                benutzerFarbe.setValue(intToColor(benutzer.getBenutzerFarbe()));
                TextField benutzerName = new TextField();
                benutzerName.setPromptText("Benutzername");
                benutzerName.setText(benutzer.getBenutzerName());


                grid.add(new Label("Benutzerfarbe"), 0, 0);
                grid.add(benutzerFarbe, 1, 0);
                grid.add(new Label("Benutzername:"), 0, 1);
                grid.add(benutzerName, 1, 1);

                dialog.getDialogPane().setContent(grid);

                // Request focus on the username field by default.
                Platform.runLater(() -> benutzerFarbe.requestFocus());

                // Convert the result to a username-password-pair when the login button is clicked.
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == benutzer_bearbeiten) {
                        return new Pair<>(benutzerFarbe.getValue(), benutzerName.getText());
                    }
                    return null;
                });

                Optional<Pair<Color, String>> result = dialog.showAndWait();

                result.ifPresent(benutzerFarbeName -> {
                    java.awt.Color awtColor = new java.awt.Color((float) benutzerFarbeName.getKey().getRed(),
                            (float) benutzerFarbeName.getKey().getGreen(),
                            (float) benutzerFarbeName.getKey().getBlue(),
                            (float) benutzerFarbeName.getKey().getOpacity());
                    speicherart.aendereBenutzer(benutzer.getBenutzerId(), benutzerFarbeName.getValue(), awtColor.getRGB(), benutzer.isBenutzerAktiv());
                    aktualisiereBenutzer();
                    zeigeKalender();
                });
            });


            //Lösch Button
            Button buttonDelete = new Button();
            buttonDelete.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("bilder/delete.png"))));
            buttonDelete.setOnAction(event -> {
                alert("Benutzer löschen", "Wollen Sie wirklich " + benutzer.getBenutzerName() + " löschen?", (b) -> {
                    ;
                    speicherart.loescheBenutzer(benutzer.getBenutzerId());
                    aktualisiereBenutzer();
                });
            });

            //Platz zwischen den Elementen
            Region region = new Region();
            region.setMaxWidth(10);
            region.setMinWidth(10);
            region.setPrefWidth(10);

            Region region2 = new Region();
            HBox.setHgrow(region2, Priority.ALWAYS);

            //Elemente in  Vbox
            HBox hbox = new HBox(rectangle, region, label1, region2, buttonVisibility, buttonPlane, buttonEdit, buttonDelete);

            hbox.setPadding(new Insets(5, 0, 0, 0));
            hbox.setMinWidth(320);
            pnl_benutzer.getChildren().add(hbox);
        }
    }

    public void zeigeKalender() {
        pnl_grid01.getChildren().clear();
        benutzer_projektListInZeitraum = null;

        int weeks = 8;
        int counter = 1;
        int tag = 1;

        DatumsRechner datumsRechner = new DatumsRechner(beginDatum);
        benutzer_projektListInZeitraum = speicherart.gebeBenutzerProjekteInZeitraum(datumsRechner.getStartDatum(), datumsRechner.getStartDatum().plusDays(56));

        pnl_grid01.addRow(0, new Label("KW"), new Label("Montag"), new Label("Dienstag"), new Label("Mittwoch"), new Label("Donnerstag"), new Label("Freitag"), new Label("Samstag"), new Label("Sonntag"));

        for (int week = 0; week < weeks; week++) {

            int subRows = speicherart.gebeZeilenAnzahl(datumsRechner.getStartDatum(), datumsRechner.getStartDatum().plusDays(7));
            LocalDate datum1 = datumsRechner.next();
            LocalDate datum2 = datumsRechner.next();
            LocalDate datum3 = datumsRechner.next();
            LocalDate datum4 = datumsRechner.next();
            LocalDate datum5 = datumsRechner.next();
            LocalDate datum6 = datumsRechner.next();
            LocalDate datum7 = datumsRechner.next();

            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            String weekNumber = String.valueOf(datum1.get(weekFields.weekOfWeekBasedYear()));
            pnl_grid01.addRow(counter++, color(weekNumber, "silver"),
                    color(String.format("%tF", datum1), "silver"),
                    color(String.format("%tF", datum2), "silver"),
                    color(String.format("%tF", datum3), "silver"),
                    color(String.format("%tF", datum4), "silver"),
                    color(String.format("%tF", datum5), "silver"),
                    color(String.format("%tF", datum6), "silver"),
                    color(String.format("%tF", datum7), "silver")
            );
            int index = 0;
            int row = 0;

            for (Benutzer_Projekt benutzer_projekt : benutzer_projektListInZeitraum) {
                List<LocalDate> datumsSpanne = null;
                datumsSpanne = benutzer_projekt.getTeilnahmeBeginn().datesUntil(benutzer_projekt.getTeilnahmeEnde().plusDays(1)).collect(Collectors.toList());
                boolean datumInSpanne = (datumsSpanne.contains(datum1) | datumsSpanne.contains(datum2) | datumsSpanne.contains(datum3) | datumsSpanne.contains(datum4) | datumsSpanne.contains(datum5) | datumsSpanne.contains(datum6) | datumsSpanne.contains(datum7));
                boolean projektAnzeigen = projektListe.get(benutzer_projektListInZeitraum.get(index).getProjektId()).isProjektAnzeigem();
                boolean benutzerAnzeigen = benutzerList.get(benutzer_projektListInZeitraum.get(index).getBenutzerId()).isBenutzerAnzeigen();
                if (benutzerAnzeigen && projektAnzeigen && datumInSpanne) {
                    Node nodeDatum1 = projektColor(datumsSpanne, datum1, index);
                    Node nodeDatum2 = projektColor(datumsSpanne, datum2, index);
                    Node nodeDatum3 = projektColor(datumsSpanne, datum3, index);
                    Node nodeDatum4 = projektColor(datumsSpanne, datum4, index);
                    Node nodeDatum5 = projektColor(datumsSpanne, datum5, index);
                    Node nodeDatum6 = projektColor(datumsSpanne, datum6, index);
                    Node nodeDatum7 = projektColor(datumsSpanne, datum7, index);
                    pnl_grid01.addRow(counter++, color("", "white"), nodeDatum1, nodeDatum2, nodeDatum3, nodeDatum4, nodeDatum5, nodeDatum6, nodeDatum7);
                    row++;
                }
                index++;
            }

            for (int emptyRow = row; emptyRow < 4; emptyRow++) {
                pnl_grid01.addRow(counter++, color("", "white"),color("", "white"), color("", "white"), color("", "white"), color("", "white"), color("", "white"), color("", "white"), color("", "white"));
            }
        }
    }

    private Node projektColor(List<LocalDate> datumsSpanne, LocalDate datum, int index) {
        if (datumsSpanne.contains(datum)) {
            Color benutzerFarbe = intToColor(benutzerList.get(benutzer_projektListInZeitraum.get(index).getBenutzerId()).getBenutzerFarbe());
            String benutzerName = benutzerList.get(benutzer_projektListInZeitraum.get(index).getBenutzerId()).getBenutzerName();
            Color projektFarbe = intToColor(projektListe.get(benutzer_projektListInZeitraum.get(index).getProjektId()).getProjektFarbe());
            String projektName = projektListe.get(benutzer_projektListInZeitraum.get(index).getProjektId()).getProjektName();
            return color(benutzerName + " " + projektName, "linear-gradient(to right," + benutzerFarbe + " 50%, " + projektFarbe + " 50%)");
        } else {
            return color("", "white");
        }
    }

    private Node color(String label, String color) {
        Pane pnl = new Pane();
        Label lbl = new Label(label);
        pnl.getChildren().add(lbl);
        pnl.backgroundProperty().setValue(new Background(new BackgroundFill(Paint.valueOf(color), new CornerRadii(4d), new Insets(1))));
        pnl.getStyleClass().addAll("border_leftright", "border_black");
        lbl.getStyleClass().addAll(/*"color_white",*/ "padding-3");

        //if (borderBottom)
        //    pnl.getStyleClass().add("border_leftrightbottom");
        return pnl;
    }

    public void aktualisiereProjekte() {
        projektListe = speicherart.gebeAlleProjekte();
        zeigeProjekte();
    }

    public void zeigeProjekte() {
        pnl_projekte.getChildren().clear();
        for (Projekte projekt : projektListe) {
            //Rechteck mit Benutzerfarbe
            Color color = intToColor(projekt.getProjektFarbe());
            Rectangle rectangle = new Rectangle(20, 30, 40, 30);
            rectangle.setFill(color);

            //Benutzername
            Label label1 = new Label(projekt.getProjektName());

            //Sichtbar Button
            Button buttonVisibility = new Button();
            ImageView visibilityImageView = null;
            Image visibilityImage = null;

            buttonVisibility.setOnAction((e) -> {
                projekt.projektAnzeigen = !projekt.projektAnzeigen;
                zeigeProjekte();
                zeigeKalender();
            });


            if (projekt.projektAnzeigen == true) {
                label1.setStyle("-fx-text-fill: black");
                rectangle.setFill(color);
                visibilityImage = new Image(getClass().getResourceAsStream("bilder/visibility.png"));
            } else {
                label1.setStyle("-fx-text-fill: silver ");
                rectangle.setFill(Color.gray(0.9));
                visibilityImage = new Image(getClass().getResourceAsStream("bilder/visibility_off.png"));
            }

            buttonVisibility.setGraphic(new ImageView(visibilityImage));

            //Beabeiten Button
            Button buttonEdit = new Button();
            buttonEdit.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("bilder/edit.png"))));
            buttonEdit.setOnAction(event -> {
                Dialog<Pair<Color, String>> dialog = new Dialog<>();
                dialog.setTitle("Projekt bearbeiten");

                // Set the button types.
                ButtonType projekt_bearbeiten = new ButtonType("Projekt bearbeiten", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(projekt_bearbeiten, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                ColorPicker projektFarbe = new ColorPicker();
                projektFarbe.setValue(intToColor(projekt.getProjektFarbe()));
                TextField projektname = new TextField();
                projektname.setPromptText("Projektname");
                projektname.setText(projekt.getProjektName());


                grid.add(new Label("Projektfarbe"), 0, 0);
                grid.add(projektFarbe, 1, 0);
                grid.add(new Label("Projektname:"), 0, 1);
                grid.add(projektname, 1, 1);
                dialog.getDialogPane().setContent(grid);

                // Convert the result to a username-password-pair when the login button is clicked.
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == projekt_bearbeiten) {
                        return new Pair<>(projektFarbe.getValue(), projektname.getText());
                    }
                    return null;
                });

                Optional<Pair<Color, String>> result = dialog.showAndWait();

                result.ifPresent(projektFarbeName -> {
                    java.awt.Color awtColor = new java.awt.Color((float) projektFarbeName.getKey().getRed(),
                            (float) projektFarbeName.getKey().getGreen(),
                            (float) projektFarbeName.getKey().getBlue(),
                            (float) projektFarbeName.getKey().getOpacity());
                    speicherart.aendereProjekt(projekt.getProjektId(), projektFarbeName.getValue(), awtColor.getRGB());
                    aktualisiereProjekte();
                    zeigeKalender();
                });
            });

            //Löschen Button
            Button buttonDelete = new Button();
            buttonDelete.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("bilder/delete.png"))));
            buttonDelete.setOnAction(event -> {
                alert("Projekt löschen", "Wollen Sie wirklich " + projekt.getProjektName() + " löschen?", (b) -> {
                    speicherart.loescheProjekt(projekt.getProjektId());
                    aktualisiereProjekte();
                });

            });

            //Platz zwischen den Elementen
            Region region = new Region();
            region.setMaxWidth(10);
            region.setMinWidth(10);
            region.setPrefWidth(10);

            Region region2 = new Region();
            HBox.setHgrow(region2, Priority.ALWAYS);

            //Elemente in  Vbox
            HBox hbox = new HBox(rectangle, region, label1, region2, buttonVisibility, buttonEdit, buttonDelete);
            hbox.setPadding(new Insets(5, 0, 0, 0));
            hbox.setMinWidth(300);
            pnl_projekte.getChildren().add(hbox);
        }
    }

    //Int Wert zu Farbe
    public Color intToColor(int color) {
        int b = color & 0xFF;
        int g = (color >> 8) & 0xFF;
        int r = (color >> 16) & 0xFF;

        return Color.rgb(r, g, b);
    }

    

    public void initialize() {
        //TODO
    }

    public void buttonInit() {
        // Hinzufügen der Bilder zum öffnen und schließen der Pane

        Image leftImage = new Image(getClass().getResourceAsStream("bilder/key_left.png"));
        btn_rightPaneOpen.setGraphic(new ImageView(leftImage));
        btn_leftPaneClose.setGraphic(new ImageView(leftImage));

        Image rightImage = new Image(getClass().getResourceAsStream("bilder/key_right.png"));
        btn_rightPaneClose.setGraphic(new ImageView(rightImage));
        btn_leftPaneOpen.setGraphic(new ImageView(rightImage));

        btn_leftPaneOpen.setVisible(false);
        btn_leftPaneOpen.setMinSize(0, 0);
        btn_leftPaneOpen.setMaxSize(0, 0);
        btn_leftPaneOpen.setPrefSize(0, 0);

        btn_rightPaneOpen.setVisible(false);
        btn_rightPaneOpen.setMinSize(0, 0);
        btn_rightPaneOpen.setMaxSize(0, 0);
        btn_rightPaneOpen.setPrefSize(0, 0);

        Image upImage = new Image(getClass().getResourceAsStream("bilder/key_up.png"));
        btn_WeekBevor.setGraphic(new ImageView(upImage));

        Image doubleUpImage = new Image(getClass().getResourceAsStream("bilder/key_doubleUp.png"));
        btn_MonthBevor.setGraphic(new ImageView(doubleUpImage));

        Image todayImage = new Image(getClass().getResourceAsStream("bilder/today.png"));
        btn_DayToday.setGraphic(new ImageView(todayImage));

        Image downImage = new Image(getClass().getResourceAsStream("bilder/key_down.png"));
        btn_WeekAfter.setGraphic(new ImageView(downImage));

        Image doubleDownImage = new Image(getClass().getResourceAsStream("bilder/key_doubleDown.png"));
        btn_MonthAfter.setGraphic(new ImageView(doubleDownImage));


    }

    public void setSpeicherart(List<String> args) {

        if (!args.isEmpty() && "-h2".equals(args.get(0))) {
            speicherart = new SpeicherH2();
        } else {
            speicherart = new Speicherdatei();
        }

        speicherart.init();
        //Benutzer in der Datenbank erstellen
        //speicherart.erstelleBenutzer("Niklas Mathes",12345);

        //Datum um 7 Tage erhöhen
        //Date date = new Date(Instant.now().toEpochMilli());
        //date = new Date(date.toInstant().plus(7, ChronoUnit.DAYS).toEpochMilli());

        //Urlaub in der Datenbank erstellen
        //speicherart.erstelleUrlaub(1, new Date(), date);

//        System.out.println("-----Testen von DB Selects-----");
//        System.out.println(speicherart.gebeBenutzer(1));;
//        System.out.println(speicherart.gebeProjekt(1));;
//        System.out.println(speicherart.gebeBenutzer_Projekt(1,1));;
//        System.out.println(speicherart.gebeUrlaub(1));;
    }

    public void stop() {
        speicherart.beenden();
    }
}