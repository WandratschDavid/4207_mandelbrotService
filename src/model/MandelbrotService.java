package model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MandelbrotService extends Service
{
	private int startcol;
	private int startrow;
	private int width;
	private int height;
	private final int maxIterations;

	public MandelbrotService(int startcol, int startrow, int width, int height, int maxIterations)
	{
		this.startcol = startcol;
		this.startrow = startrow;
		this.width = width;
		this.height = height;
		this.maxIterations = maxIterations;
	}

	@Override
	protected Task<int[][]> createTask()
	{
		return new Mandelbrot(startcol, startrow, width, height, maxIterations);
	}
}