/*
 * doodle v0.1
 * written with love by ori yonay, 2019 <3
 */
package doodle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import javax.imageio.ImageIO;

/**
 *
 * @author oriyonay
 */

public class doodlefxmlController implements Initializable {
    @FXML
    private ColorPicker colorpicker;
    
    @FXML
    private TextField bsize;
    
    @FXML
    private Canvas canvas;
    
    @FXML
    private Button brushbutton, eraserbutton;
    
    boolean toolSelected = true;
    boolean eraserSelected = false;
    
    GraphicsContext brushTool;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        brushTool = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(e -> {
            double size = Double.parseDouble(bsize.getText());
            double x = e.getX() - size/2;
            double y = e.getY() - size/2;
            if (toolSelected && !bsize.getText().isEmpty()) {
                brushTool.setFill(colorpicker.getValue());
                brushTool.fillRoundRect(x, y, size, size, size, size);
            } else if (eraserSelected && !bsize.getText().isEmpty()) {
                brushTool.clearRect(x, y, size, size);
            }
        });
    }  
    
    @FXML
    public void toolselected(ActionEvent e) {
        toolSelected = !toolSelected;
        brushbutton.setUnderline(toolSelected);
        eraserbutton.setUnderline(false);
    }
    
    @FXML
    public void eraserselected(ActionEvent e) {
        toolSelected = false;
        eraserSelected = !eraserSelected;
        brushbutton.setUnderline(false);
        eraserbutton.setUnderline(eraserSelected);
    }
    
    @FXML
    public void clearcanvas(ActionEvent e) {
        brushTool.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    @FXML
    public void setdimensions(ActionEvent e) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Set Dimensions");

        // Set the button types
        ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();

        TextField width = new TextField();
        width.setPromptText("Width");
        TextField height = new TextField();
        height.setPromptText("Height");

        gridPane.add(new Label("Width:"), 0, 0);
        gridPane.add(width, 0, 0);
        gridPane.add(new Label("Height:"), 0, 1);
        gridPane.add(height, 0, 1);

        dialog.getDialogPane().setContent(gridPane);
    
        // Request focus on the username field by default.
        Platform.runLater(() -> width.requestFocus());
    
        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(width.getText(), height.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            canvas.setWidth(Double.parseDouble(pair.getKey()));
            canvas.setHeight(Double.parseDouble(pair.getValue()));
        });
    }
    
    @FXML
    public void savefile(ActionEvent e) {
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("png", "*.png"));
        fc.setTitle("save doodle :)");
        File file = fc.showSaveDialog(null);
        if(file != null){
            WritableImage wi = new WritableImage((int)canvas.getWidth(),(int)canvas.getHeight());
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(sp, wi), null), "png", file);
                //ImageIO.write(SwingFXUtils.fromFXImage(wi, null), "png", file);
            } catch (IOException f) {}
        }
    }
    
    @FXML
    public void openaboutdialog(ActionEvent e) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("about doodle :)");
        alert.setHeaderText("doodle v0.1");
        alert.setContentText("written with love by ori yonay, 2019 :)");
        alert.showAndWait();
    }
}
