package viewcontroller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.MandelbrotService;

import java.io.IOException;

public class MandelbrotC
{
	@FXML
	private Button btStart;

	@FXML
	private ProgressBar pbBuild;

	@FXML
	private Label lbTime;

	@FXML
	private Canvas cvMandelbrot;

	private MandelbrotService mandelbrotService;

	private long timeTracker;

	@FXML
	private void btStartOnAction(ActionEvent actionEvent)
	{
		if (!mandelbrotService.isRunning())
		{
			pbBuild.setVisible(true);
			btStart.setText("Cancel");
			lbTime.setText(null);
			timeTracker = System.currentTimeMillis();
			mandelbrotService.start();
		}
		else
		{
			mandelbrotService.cancel();
			pbBuild.setVisible(false);
			lbTime.setText(null);
			btStart.setText("Start");
		}
	}

	public static void show(Stage stage)
	{
		try
		{
			Parent root = FXMLLoader.load(MandelbrotC.class.getResource("mandelbrotV.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Mandelbrot-Menge");
			stage.show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Platform.exit();
		}
	}

	@FXML
	public void initialize()
	{
		// Größe und Auflösung: später mit mehr Flexibilität?!
		final int WIDTH = (int) cvMandelbrot.getWidth();
		final int HEIGHT = (int) cvMandelbrot.getHeight();
		final int MAXITERATIONS = 10 * WIDTH;

		// Service
		mandelbrotService = new MandelbrotService(0, 0, WIDTH, HEIGHT, MAXITERATIONS);

		// Progressbar
		pbBuild.setMinWidth(cvMandelbrot.getWidth() / 2.0);
		pbBuild.progressProperty().bind(mandelbrotService.progressProperty());
		pbBuild.setVisible(false);

		timeTracker = 0;

		// Ergebnis abholen und darstellen
		mandelbrotService.setOnSucceeded((Event e) -> {
			// Ergebnis abholen
			int[][] mm = (int[][]) mandelbrotService.getValue();

			// Farben
			Color[] colors = new Color[MAXITERATIONS + 1];
			for (int i = 0; i < MAXITERATIONS; i++) {
				colors[i] = Color.hsb(i / 256f, 1.0, i / (i + 8f));
			}
			colors[MAXITERATIONS] = Color.BLACK;

			// Image für Mandelbrot-Baum
			WritableImage imMandelbrot = new WritableImage(WIDTH, HEIGHT);
			PixelWriter pw = imMandelbrot.getPixelWriter();

			// Umsetzung in Farben
			for (int col = 0; col < WIDTH; col++)
			{
				for (int row = 0; row < HEIGHT; row++)
				{
					pw.setColor(col, row, colors[mm[col][row]]);
				}
			}

			// Mandelbrot-Baum zeichnen
			cvMandelbrot.getGraphicsContext2D().drawImage(imMandelbrot, 0, 0);

			// update visuals
			btStart.setText("Start");
			pbBuild.setVisible(false);
			timeTracker = System.currentTimeMillis() - timeTracker;
			lbTime.setText(timeTracker + " ms");

			mandelbrotService.reset();
		});

		mandelbrotService.setOnCancelled((Event e) -> {
			// Button toggeln
			btStart.setText("Start");
			lbTime.setText(null);
			mandelbrotService.reset();
		});
	}
}