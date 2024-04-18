import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class MainSceneController {

    @FXML
    private ComboBox<String> ComboStart;
    @FXML
    private ComboBox<String> ComboEnd;
    @FXML
    private ComboBox<String> ComboDiscount;
    @FXML
    private Button calculateButton;
    @FXML
    private TextField fareField;
    @FXML
    private TextField TotalFareCollected;
    @FXML
    private TextField Vat;
    @FXML
    private TextField Net;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView ButtonClose;

    private final double BASE_FARE = 30.00;
    private final Map<String, Function<Double, Double>> discountMap = new HashMap<>();

    public void initialize() {
        ComboStart.getItems().addAll("Bamban", "Capas", "Concepcion", "Tarlac City", "Gerona", "Paniqui", "Moncada");
        ComboEnd.getItems().addAll("Bamban", "Capas", "Concepcion", "Tarlac City", "Gerona", "Paniqui", "Moncada");
        ComboDiscount.getItems().addAll("None", "Senior Citizen (25%)", "Student (20%)", "Pregnant/PWD (15%)");

        discountMap.put("None", fare -> 0.0);
        discountMap.put("Senior Citizen (25%)", fare -> fare * 0.25);
        discountMap.put("Student (20%)", fare -> fare * 0.20);
        discountMap.put("Pregnant/PWD (15%)", fare -> fare * 0.15);

        calculateButton.disableProperty().bind(
            Bindings.createBooleanBinding(
                () -> !validateInputs(),
                ComboStart.valueProperty(),
                ComboEnd.valueProperty(),
                ComboDiscount.valueProperty()
            )
        );

        ComboStart.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        ComboEnd.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        ComboDiscount.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs());
    }

    @FXML
    void calculateFare(ActionEvent event) {
        Optional<String> startStationOpt = Optional.ofNullable(ComboStart.getValue());
        Optional<String> endStationOpt = Optional.ofNullable(ComboEnd.getValue());
        Optional<String> discountOpt = Optional.ofNullable(ComboDiscount.getValue());

        int stationsTraveled = startStationOpt.flatMap(start ->
            endStationOpt.map(end ->
                Math.abs(ComboStart.getItems().indexOf(start) - ComboStart.getItems().indexOf(end)) + 1))
            .orElse(0);

        double fare = stationsTraveled * BASE_FARE;

        double discountAmount = discountOpt.map(discount -> {
            Function<Double, Double> discountFunction = discountMap.get(discount);
            return discountFunction.apply(fare);
        }).orElse(0.0);

        double totalFare = fare - discountAmount;
        double vat = totalFare * 0.12;
        double netCollection = totalFare + vat;

        String errorMessage = startStationOpt.flatMap(start ->
            endStationOpt.map(end ->
                start.equals(end) ? "Start and end destinations cannot be the same." : ""))
            .orElse("");

        errorLabel.setText(errorMessage);

        fareField.setText(String.format("Php %.2f", totalFare));
        TotalFareCollected.setText(String.format("Php %.2f", totalFare));
        Vat.setText(String.format("Php %.2f", vat));
        Net.setText(String.format("Php %.2f", netCollection));
    }

    private boolean validateInputs() {
        String startStation = ComboStart.getValue();
        String endStation = ComboEnd.getValue();
    
        return startStation != null && !startStation.isEmpty() &&
               endStation != null && !endStation.isEmpty() &&
               !startStation.equals(endStation);
    }
    
    

    @FXML
    void handleClose(ActionEvent event) {
        Stage stage = (Stage) ButtonClose.getScene().getWindow();
        stage.close();
    }
}
