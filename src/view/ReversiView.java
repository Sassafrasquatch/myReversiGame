package view;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import controller.ReversiController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ReversiBoard;
import model.ReversiModel;

/**
 * Main view class for Reversi GUI.
 * 
 * @author Wes Rodgers
 *
 */
@SuppressWarnings("deprecation")
public class ReversiView extends javafx.application.Application implements Observer{
	
	private ReversiBoard observableBoard;
	private StackPane[][] positions;
	private Label score = new Label();
	private boolean humanTurn = true;
	private boolean gameOver = false;

	@Override
	/**
	 * start method for JavaFX
	 * 
	 * @param primaryStage the Stage object passed by launch in main
	 */
	public void start(Stage primaryStage) {
		
		ReversiModel model = new ReversiModel();

		//checks if save_game.dat exists in the same directory as the program file.
		//if so, builds model based on existing ReversiBoard object.
		File f = new File("save_game.dat");
		if(f.exists()) {
			
			FileInputStream fis = null;
			
			try {
				fis = new FileInputStream("save_game.dat");
				ObjectInputStream ois = new ObjectInputStream(fis);
				observableBoard = (ReversiBoard) ois.readObject();
				model = new ReversiModel(observableBoard);
				ois.close();
				fis.close();
				this.humanTurn = observableBoard.humanTurn;
				
			} catch (IOException | ClassNotFoundException e) {
				// since we already checked for existence of save_game, this should only
				// occur when the save_game.dat doesn't contain a ReversiBoard object.
				// Then it will just ignore it and create a new board
			}	
		}
		
		//adds to observer list of model
		model.addObserver(this);		
		ReversiController controller = new ReversiController(model);
		
		//calculates initial legal moves, any other calculations to be done during mouse event
		controller.calculateLegal();		
		
		//creates the board as a TilePane with black borders, 8x8
		Border border = new Border(new BorderStroke(Paint.valueOf("BLACK"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));
		TilePane board = new TilePane();
		board.setPrefColumns(8);
		board.setPrefRows(8);
		board.setPadding(new Insets(8));
		board.setBackground(new Background(new BackgroundFill(Paint.valueOf("GREEN"), null, null)));
		
		//creates the stackpanes holding the circles that represent game pieces
		positions = new StackPane[8][8];
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				positions[i][j] = new StackPane();
				positions[i][j].setBorder(border);
				positions[i][j].setPadding(new Insets(2));
				
				positions[i][j].getChildren().add(new Circle(20, Paint.valueOf("TRANSPARENT")));
				board.getChildren().add(positions[i][j]);
			}
		}		
		
		//make sure we have the correct board
		observableBoard = model.getBoard();
		
		//initial board set up, or loads old game's set up
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(observableBoard.getColorAt(i,j) == '_') {
					continue;
				}
				((Shape) positions[i][j].getChildren().get(0)).setFill(Paint.valueOf(observableBoard.getColorAt(i, j) == 'W' ? "WHITE" : "BLACK"));
			}
		}
		score.setText("White: " + observableBoard.getHumanCount() + " - Black: " + observableBoard.getComputerCount());
		
		//formatting for our score board
		score.setBackground(new Background(new BackgroundFill(Paint.valueOf("WHITE"), null, null)));
		
		//creates the menu bar with a single menu and menu item in that menu
		MenuBar menu = new MenuBar();
		menu.getMenus().add(new Menu("File"));
		menu.getMenus().get(0).getItems().add(new MenuItem("New Game"));
		
		menu.getMenus().get(0).getItems().get(0).setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				
				//if we create a new game, delete the old save_game file
				File f = new File("save_game.dat");
				f.delete();
				humanTurn = true;
				start(primaryStage);
			}
			
		});
		
		//connects all the pieces of the board in a border pane
		BorderPane bp = new BorderPane();
		bp.setCenter(board);
		bp.setBottom(score);
		bp.setTop(menu);
		
		//makes so we can't resize the board and screw up the 8x8 formatting
		//and shows the scene on screen
		Scene scene = new Scene(bp);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Reversi");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		// mouse click listener, checks where we clicked and translates that
		// to an attempted human move on the stack pane at that location.
		// then performs a computer turn. 
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {			
			public void handle(MouseEvent event) {
				controller.calculateLegal();
				if(humanTurn) {
				
					double x = event.getSceneX();
					double y = event.getSceneY();
					
					//ignores click if outside of board
					if(x < 9 || x > 375 || y < 9 || y > 400) {
						return;
					}
					
					//figures out which tile the click was in
					int xTile = ((int)x - 9) / 46;
					int yTile = ((int)y - 34) / 46;
					
					//if there are legal human moves, try humanTurn where the click occurred
					if(controller.hasLegal(true)) {	
						try {
							controller.humanTurn(yTile, xTile);
						}
						
						//ignore click if an illegal move
						catch(Exception e) {
							return;
						}
					}
						
					//recalculate legal moves
					controller.calculateLegal();
					
					humanTurn = false;
				}
				
				else {
					//make computer turn
					controller.calculateLegal();
					if(controller.hasLegal(false)) {
						controller.computerTurn();
						controller.calculateLegal();
					}
					humanTurn = true;
				}
				
				//update score
				score.setText("White: " + observableBoard.getHumanCount() + " - Black: " + observableBoard.getComputerCount());
				
				//if nobody has moves left, alert that the game is over and determine whether the human won
				if(!controller.hasLegal(true) && !controller.hasLegal(false)) {	
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.initModality(Modality.WINDOW_MODAL);
					
					
					
					if(controller.didHumanWin()) {
						alert.setContentText("You Won!");
						alert.showAndWait();
					}
					else {
						alert.setContentText("You Lost...");
						alert.showAndWait();
					}
					
					File f = new File("save_game.dat");
					f.delete();
					
					gameOver = true;
				}
			}
		});
		
		//create a save_game.dat file storing the ReversiBoard when we close the window before a game is over.
		primaryStage.setOnCloseRequest(event -> {
			FileOutputStream fos;
			try {
				
				observableBoard.humanTurn = humanTurn;
				
				if(!gameOver) {
					fos = new FileOutputStream("save_game.dat");
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(observableBoard);
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
	}

	@Override
	/**
	 * automatically called when the observable notifies observers of changes
	 * 
	 * @param arg0 Observable an object this observer is observing
	 * @param board a ReversiBoard object
	 */
	public void update(Observable arg0, Object board) {
		this.observableBoard = (ReversiBoard) board;
		int[] move = observableBoard.getMove();
		char color = observableBoard.getMoveColor();
		
		//updates the board with the most recent change
		if(move != null) {
			((Shape) this.positions[move[0]][move[1]].getChildren().get(0)).setFill(Paint.valueOf(color == 'W' ? "WHITE" : "BLACK"));
			
			score.setText("White: " + observableBoard.getHumanCount() + " - Black: " + observableBoard.getComputerCount());
		}
	}
}
