package model;

import javafx.concurrent.Task;

public class Mandelbrot extends Task<int[][]>
{
	private int startcol;
	private int startrow;
	private int width;
	private int height;
	private int maxiterations;

	public Mandelbrot(int startcol, int startrow, int width, int height, int maxiterations)
	{
		this.startcol = startcol;
		this.startrow = startrow;
		this.width = width;
		this.height = height;
		this.maxiterations = maxiterations;
	}

	@Override
	protected int[][] call() throws Exception
	{
		int[][] mm = new int[width - startcol][height - startrow];

		for (int col = startcol; col < width; col++)
		{
			// Fortschritt traken
			updateProgress(col , width);

			for (int row = startrow; row < height; row++)
			{
				double cr = (col - width / 2) * 4.0 / width;
				double ci = (row - height / 2) * 4.0 / width;
				double ar = 0, ai = 0;

				int iteration = 0;
				while (ar * ar + ai * ai < 4 && iteration < maxiterations)
				{
					double arNeu = ar * ar - ai * ai + cr;

					ai = 2 * ar * ai + ci;

					ar = arNeu;

					iteration++;
				}

				mm[col][row] = iteration;
			}
		}
		return mm;
	}
}