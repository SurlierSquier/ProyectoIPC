/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;  
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.geometry.Point2D;
import model.*;
import utils.*;

import java.io.InputStream;
import java.util.*;

public class ProblemViewController {

    @FXML private ImageView chartImage;
    @FXML private Canvas drawingCanvas;
    @FXML private ScrollPane scrollPane;
    @FXML private Group zoomGroup;
    @FXML private Pane pane;
    @FXML private ImageView protractorImageView;
    @FXML private ColorPicker colorPicker;
    @FXML private ComboBox<Double> strokeSizeCombo;
    @FXML private Button backButton;
    @FXML private Label problemStatement;
    @FXML private RadioButton answer1, answer2, answer3, answer4;
    @FXML private ImageView rulerImageView;
    @FXML private TitledPane problemPane;



    private final ToggleGroup answerGroup = new ToggleGroup();

    private enum Mode {
    NONE, POINT, LINE, ARC, TEXT, DISTANCE, COORDINATES
    }

    private Mode currentMode = Mode.NONE;

    private double zoomFactor = 1;
    private final List<DrawableMark> marks = new ArrayList<>();

    private boolean deleteMode = false;
    private boolean colorEditMode = false;
    private DrawableMark selectedMark = null;
    private LineMark tempLine = null;

    private User user;
    private List<Problem> problems;
    private int currentProblemIndex = 0;

    private int hits = 0;
    private int faults = 0;
    
   private DynamicArcMark tempArc = null;

    
    

    public void setUser(User user) {
        this.user = user;
        System.out.println("Sesión iniciada con: " + user.getNickName());
    }

    public void initialize() throws NavDAOException {
    answer1.setToggleGroup(answerGroup);
    answer2.setToggleGroup(answerGroup);
    answer3.setToggleGroup(answerGroup);
    answer4.setToggleGroup(answerGroup);

    loadChart();
    loadProtractor();
    loadRuler();

    initTools();
    setupMouseHandlers();
    
    problems = Navigation.getInstance().getProblems();
    if (!problems.isEmpty()) {
        Problem selectedProblem = SceneManager.getInstance().getSelectedProblem();
        
        if (selectedProblem != null) {
            for (int i = 0; i < problems.size(); i++) {
                if (problems.get(i).getText().equals(selectedProblem.getText())) {
                    currentProblemIndex = i;
                    break;
                }
            }
            SceneManager.getInstance().setSelectedProblem(null);
        }
        
        showProblem(problems.get(currentProblemIndex));
    }
}

    private void showProblem(Problem problem) {
        problemStatement.setText(problem.getText());
        List<Answer> answers = new ArrayList<>(problem.getAnswers());
        Collections.shuffle(answers);

        answer1.setText(answers.get(0).getText());
        answer2.setText(answers.get(1).getText());
        answer3.setText(answers.get(2).getText());
        answer4.setText(answers.get(3).getText());

        answer1.setVisible(true);
        answer2.setVisible(true);
        answer3.setVisible(true);
        answer4.setVisible(true);
        answerGroup.selectToggle(null);
    }

    @FXML
    
private void handleNextProblem() {
    currentProblemIndex++;
    if (currentProblemIndex < problems.size()) {
        showProblem(problems.get(currentProblemIndex));
    } else {
        problemStatement.setText("¡Has completado todos los problemas!");
        answer1.setVisible(false);
        answer2.setVisible(false);
        answer3.setVisible(false);
        answer4.setVisible(false);

        if (hits > 0 || faults > 0) {
    SceneManager.getInstance().addSession(hits, faults);
    System.out.println("Sesión guardada: aciertos=" + hits + ", errores=" + faults);
}

    }
}


    @FXML
    private void checkAnswer() {
        RadioButton selected = (RadioButton) answerGroup.getSelectedToggle();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Selecciona una respuesta.").show();
            return;
        }

        Problem currentProblem = problems.get(currentProblemIndex);
        boolean isCorrect = currentProblem.getAnswers().stream()
            .anyMatch(a -> a.getText().equals(selected.getText()) && a.getValidity());

        if (isCorrect) {
            hits++;  
        } else {
            faults++;  
        }

        Alert result = new Alert(isCorrect ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                isCorrect ? "¡Respuesta correcta!" : "Respuesta incorrecta");
        result.showAndWait();

                
    }

    private void loadChart() {
        InputStream is = getClass().getResourceAsStream("/resources/carta_nautica.jpg");
        if (is == null) {
            System.err.println("Imagen no encontrada");
            return;
        }
        Image image = new Image(is);
        chartImage.setImage(image);
        double maxWidth = 2000;
        chartImage.setFitWidth(maxWidth);
        chartImage.setPreserveRatio(true);
        double scaledHeight = image.getHeight() * (maxWidth / image.getWidth());
        drawingCanvas.setWidth(maxWidth);
        drawingCanvas.setHeight(scaledHeight);
    }

    private void loadProtractor() {
        InputStream is = getClass().getResourceAsStream("/resources/transportador.png");
        if (is != null) {
            Image protractor = new Image(is);
            protractorImageView.setImage(protractor);
            protractorImageView.setFitWidth(300);
            protractorImageView.setPreserveRatio(true);
            protractorImageView.setOpacity(0.55);
            protractorImageView.setLayoutX(300);
            protractorImageView.setLayoutY(300);
            protractorImageView.setOnMousePressed(e -> {
                e.consume();
                Point2D localPoint = pane.sceneToLocal(e.getSceneX(), e.getSceneY());
                double offsetX = localPoint.getX() - protractorImageView.getLayoutX();
                double offsetY = localPoint.getY() - protractorImageView.getLayoutY();
                protractorImageView.setOnMouseDragged(ev -> {
                    ev.consume();
                    Point2D localDrag = pane.sceneToLocal(ev.getSceneX(), ev.getSceneY());
                    protractorImageView.setLayoutX(localDrag.getX() - offsetX);
                    protractorImageView.setLayoutY(localDrag.getY() - offsetY);
                });
            });
        }
    }
    private void loadRuler() {
    InputStream is = getClass().getResourceAsStream("/resources/regla.png");
    if (is != null) {
        Image ruler = new Image(is);
        rulerImageView.setImage(ruler);
        rulerImageView.setFitWidth(350);
        rulerImageView.setPreserveRatio(true);
        rulerImageView.setOpacity(0.95);
        rulerImageView.setLayoutX(300); 
        rulerImageView.setLayoutY(300);

        rulerImageView.setOnMousePressed(e -> {
            e.consume();
            Point2D localPoint = pane.sceneToLocal(e.getSceneX(), e.getSceneY());
            double offsetX = localPoint.getX() - rulerImageView.getLayoutX();
            double offsetY = localPoint.getY() - rulerImageView.getLayoutY();

            rulerImageView.setOnMouseDragged(ev -> {
                ev.consume();
                Point2D dragPoint = pane.sceneToLocal(ev.getSceneX(), ev.getSceneY());
                rulerImageView.setLayoutX(dragPoint.getX() - offsetX);
                rulerImageView.setLayoutY(dragPoint.getY() - offsetY);
            });
        });
    } else {
        System.err.println("Imagen de la regla no encontrada.");
    }
}

    

    private void initTools() {
        strokeSizeCombo.getItems().addAll(1.0, 2.0, 4.0, 6.0, 8.0, 10.0);
        strokeSizeCombo.setValue(4.0);
        colorPicker.setValue(Color.RED);
    }

    private void setupMouseHandlers() {
        drawingCanvas.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.isSecondaryButtonDown()) {
                resetDrawingMode();
                event.consume();
            }
        });

        drawingCanvas.setOnMousePressed(e -> {
            if (currentMode == Mode.LINE && !e.isSecondaryButtonDown()) {
                scrollPane.setPannable(false);
                Color color = colorPicker.getValue();
                double stroke = strokeSizeCombo.getValue();
                tempLine = new LineMark(e.getX(), e.getY(), e.getX(), e.getY(), color, stroke);
                marks.add(tempLine);
                redrawCanvas();
            }else if(currentMode == Mode.ARC && !e.isSecondaryButtonDown()) {
    scrollPane.setPannable(false);
    Color color = colorPicker.getValue();
    double stroke = strokeSizeCombo.getValue();

    tempArc = new DynamicArcMark(e.getX(), e.getY(), color, stroke);
    marks.add(tempArc);
    redrawCanvas();
}
        });

        drawingCanvas.setOnMouseDragged(e -> {
            if (currentMode == Mode.LINE && tempLine != null) {
                tempLine.setEnd(e.getX(), e.getY());
                redrawCanvas();
            }else if(currentMode == Mode.ARC && tempArc != null) {
    tempArc.setEnd(e.getX(), e.getY());
    redrawCanvas();
}
        });

        drawingCanvas.setOnMouseReleased(e -> {
            if (currentMode == Mode.LINE && tempLine != null) {
                tempLine.setEnd(e.getX(), e.getY());
                tempLine = null;
                scrollPane.setPannable(true);
                redrawCanvas();
            }else if(currentMode == Mode.ARC && tempArc != null) {
    tempArc = null;
    scrollPane.setPannable(true);
    redrawCanvas();
}
        });

        drawingCanvas.setOnMouseClicked(event -> {
            if (event.isSecondaryButtonDown()) return;

            double x = event.getX();
            double y = event.getY();
            Color color = colorPicker.getValue();
            double stroke = strokeSizeCombo.getValue();

            if (colorEditMode) {
                for (int i = marks.size() - 1; i >= 0; i--) {
                    if (marks.get(i).contains(x, y)) {
                        selectedMark = marks.get(i);
                        selectedMark.setColor(color);
                        redrawCanvas();
                        selectedMark = null;
                        colorEditMode = false;
                        return;
                    }
                }
                return;
            }

            if (deleteMode) {
                for (int i = marks.size() - 1; i >= 0; i--) {
                    if (marks.get(i).contains(x, y)) {
                        marks.remove(i);
                        redrawCanvas();
                        return;
                    }
                }
                return;
            }

            switch (currentMode) {
                case POINT -> marks.add(new PointMark(x, y, color, stroke));
                case ARC -> {
                    if (tempLine == null) {
                        tempLine = new LineMark(x, y, x, y, color, stroke);
                    } else {
                        double radius = Math.hypot(x - tempLine.getStartX(), y - tempLine.getStartY());
                        marks.add(new DynamicArcMark(tempLine.getStartX(), tempLine.getStartY(),  color, stroke));
                        tempLine = null;
                    }
                }
                case TEXT -> {
                    TextInputDialog dialog = new TextInputDialog("Anotación");
                    dialog.setHeaderText("Escribe el texto:");
                    dialog.showAndWait().ifPresent(text -> marks.add(new TextMark(x, y, text, color, stroke)));
                }
                case COORDINATES -> {
    marks.add(new CrossLineMark(x, y, drawingCanvas.getWidth(), drawingCanvas.getHeight(), color, stroke));
}

                default -> {}
            }
            redrawCanvas();
        });
    }

    private void resetDrawingMode() {
        currentMode = Mode.NONE;
        deleteMode = false;
        colorEditMode = false;
        tempLine = null;
        scrollPane.setPannable(true);
    }

    private void redrawCanvas() {
        GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
        for (DrawableMark mark : marks) {
            mark.draw(gc);
        }
    }

    @FXML private void setPointMode() { setMode(Mode.POINT); }
    @FXML private void setLineMode() { setMode(Mode.LINE); }
    @FXML private void setArcMode()  { setMode(Mode.ARC); }
    @FXML private void setTextMode() { setMode(Mode.TEXT); }

    private void setMode(Mode mode) {
        currentMode = mode;
        deleteMode = false;
        colorEditMode = false;
    }

    @FXML private void enableDeleteMode() {
        deleteMode = true;
        currentMode = Mode.NONE;
        colorEditMode = false;
    }

    @FXML private void enableColorEditMode() {
        colorEditMode = true;
        deleteMode = false;
        currentMode = Mode.NONE;
    }

    @FXML private void zoomIn() { 
            zoomFactor *= 1.2;
         applyZoom(); 
    
    }
    
    @FXML private void zoomOut() {        
        if(zoomFactor>1){
            zoomFactor /= 1.2; 
            applyZoom(); 
        }
    }

    private void applyZoom() {
        chartImage.setScaleX(zoomFactor);
        chartImage.setScaleY(zoomFactor);
        drawingCanvas.setScaleX(zoomFactor);
        drawingCanvas.setScaleY(zoomFactor);
        protractorImageView.setScaleX(zoomFactor);
        protractorImageView.setScaleY(zoomFactor);
    }

    @FXML private void clearCanvas() {
        marks.clear();
        redrawCanvas();
    }
    

    @FXML private void back() {
        
        if (hits > 0 || faults > 0) {
    SceneManager.getInstance().addSession(hits, faults);
    System.out.println("📊 Sesión parcial guardada al volver atrás.");
}

        SceneManager.getInstance().showMainView(user);
    }
    @FXML private void toggleProblemPanel() {
    problemPane.setExpanded(!problemPane.isExpanded());
}
    @FXML private void setCoordinatesMode() { setMode(Mode.COORDINATES); }


    @FXML
private void showAllProblems() {
    if (hits > 0 || faults > 0) {
        SceneManager.getInstance().addSession(hits, faults);
        System.out.println("Sesión parcial guardada al navegar a todos los problemas.");
    }
    
    SceneManager.getInstance().showAllProblemsView();
}
}








