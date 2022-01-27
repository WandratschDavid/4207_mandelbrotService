package main;

import javafx.application.Application;
import javafx.stage.Stage;
import viewcontroller.MandelbrotC;

public class TheMain extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		MandelbrotC.show(primaryStage);
	}
}