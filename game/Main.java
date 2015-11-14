package application;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
	private BorderPane root = null;
	StackPane playField = null;
	Scene playScene = null;
	Stage screen = null;
	StackPane menu = null;
	Scene menuScene = null;
	Button newGame = null;
	Engine engine = null;
	
	//Text
	Text livesText = null;
	Text slowDownsText = null;
	Text score = null;
	Text scorePoints = null;
	
	// Images
	private Image live = null;
	private Image slowDown = null;
	private Image logo = null;
	
	//Views
	ImageView spotView = null;
	ImageView liveView = null;
	ImageView slowDownView = null;
	ImageView liveCounterView = null;
	ImageView slowDownCounterView = null;
	ImageView logoView = null;
	
	//Variables
	int points = 0;
	int time = 1500;
	int slowDownCount = 0;
	boolean clicked = false;
	DoubleProperty lives = null;

	@Override
	public void start(Stage primaryStage) {
		try {
			screen = primaryStage;
			screen.setOnCloseRequest(e -> {
				engine.interrupt();
			});
			
			lives = new SimpleDoubleProperty(1);
			lives.addListener(e -> {
				if (lives.get() == 0) {
					engine.interrupt();
					newGame.setText("Start new game");
					changeScene();
				}
			});
			
			logo = new Image(getClass().getResourceAsStream("logo.png"), 256, 256, true, true);
			logoView = new ImageView(logo);
			menu = new StackPane(logoView);
			
			livesText = new Text(String.valueOf((int)lives.get()));
			slowDownsText = new Text(String.valueOf(slowDownCount));
			score = new Text("Score");
			scorePoints = new Text(String.valueOf(points));
			
			livesText.setFill(Color.BEIGE);
			slowDownsText.setFill(Color.BEIGE);
			score.setFill(Color.BEIGE);
			scorePoints.setFill(Color.BEIGE);
			
			live = new Image(getClass().getResourceAsStream("live.png"));
			slowDown = new Image(getClass().getResourceAsStream("slowdown.png"));
			
			spotView = new ImageView(new Image(getClass().getResourceAsStream("spot.png")));
			liveView = new ImageView(live);
			slowDownView = new ImageView(slowDown);
			liveCounterView = new ImageView(live);
			slowDownCounterView = new ImageView(slowDown);
			
			spotView.setVisible(false);
			liveView.setVisible(false);
			slowDownView.setVisible(false);
			
			spotView.setOnMouseClicked(e ->{
				spotView.setVisible(false);
				clicked = true;
				points ++;
				if (time > 400) {
					time -= 20;
				}
				scorePoints.setText(String.valueOf(points * 10));
			});
			
			liveView.setOnMouseClicked(e ->{
				liveView.setVisible(false);
				clicked = true;
				points ++;
				lives.set(lives.get() + 1);
				livesText.setText(String.valueOf((int) lives.get()));
				scorePoints.setText(String.valueOf(points * 10));
			});
			
			slowDownView.setOnMouseClicked(e ->{
				slowDownView.setVisible(false);
				clicked = true;
				points ++;
				slowDownCount ++;
				slowDownsText.setText(String.valueOf(slowDownCount));
				scorePoints.setText(String.valueOf(points * 10));
			});
			
			slowDownCounterView.setOnMouseClicked(e ->{
				
				if (slowDownCount > 0) {
					slowDownCount --;
					slowDownsText.setText(String.valueOf(slowDownCount));
					time += 150;
				}
			});
			
			Separator sepOne = new Separator(Orientation.VERTICAL);
			sepOne.setOpacity(0.1);
			
			Separator sepTwo = new Separator(Orientation.VERTICAL);
			sepTwo.setOpacity(0.1);
			
			HBox header = new HBox(5, liveCounterView, livesText, sepOne, slowDownCounterView, slowDownsText, sepTwo, score, scorePoints);
			header.setAlignment(Pos.CENTER_LEFT);
			header.setStyle("-fx-background-color:linear-gradient(#242424 50%, #2D2D2D 75%, #3C3C3C 100%);");
			header.setPadding(new Insets(3, 1, 3, 1));
			
			playField = new StackPane(spotView, liveView, slowDownView);
			playField.setAlignment(Pos.TOP_LEFT);
			playField.setStyle("-fx-background-color: linear-gradient(#9d9e9d 5%, #6b6a6b 20%, #343534 80%, #242424 100%);");
			
			root = new BorderPane(playField, header, null, null, null);
			root.setStyle("-fx-border-color: #13BFF8");

			playScene = new Scene(root, 300, 500);
			
			newGame = new Button("Start");
			newGame.setStyle("-fx-background-color: linear-gradient(TRANSPARENT 50%, #FFFFFF 100%);  -fx-text-fill: #FFFFFF; -fx-min-width: 128px;");
			newGame.setOnMouseEntered(e ->newGame.setStyle("-fx-background-color: linear-gradient(TRANSPARENT 10%, #FFFFFF 100%);  -fx-text-fill: #FFFFFF; -fx-min-width: 128px;"));
			newGame.setOnMouseExited(e -> newGame.setStyle("-fx-background-color: linear-gradient(TRANSPARENT 50%, #FFFFFF 100%);  -fx-text-fill: #FFFFFF; -fx-min-width: 128px;"));
			newGame.setTranslateY(80);
			newGame.setOnMouseClicked(e ->{
				points = 0;
				time = 1500;
				clicked = false;
				slowDownCount = 0;
				lives.set(1);
				livesText.setText(String.valueOf((int)lives.get()));
				screen.setScene(playScene);
				engine = new Engine(this);
				engine.start();
				
			});
			menu.getChildren().add(newGame);
			
			menuScene = new Scene(menu, 256, 256);
			menu.setStyle("-fx-background-color: linear-gradient(#9d9e9d 5%, #6b6a6b 20%, #343534 80%, #242424 100%); -fx-border-color: #13BFF8;");
			
			primaryStage.setScene(menuScene);
			primaryStage.setResizable(false);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void changeScene(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				screen.setScene(menuScene);
			}
		});
	}
}
