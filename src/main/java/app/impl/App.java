package app.impl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.Constants;

/*
 * In the constructor, I create a top level Pane and pass the root from the
 * PaneOrganizer class into the new Scene. I then title the stage "Pacman"
 * and show the stage. Test comment
 */

public class App extends Application {

    @Override
    public void start (Stage stage) {

    	PaneOrganizer paneorganizer = new PaneOrganizer ();
		Scene scene = new Scene(paneorganizer.getRoot(), Constants.PANE_SIZE_X, Constants.PANE_SIZE_Y);
		stage.setScene(scene);
		stage.setTitle("Pacman");
		stage.show();

    }



    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}
