package fr.seven.pixels.sokoban.logic;

import java.awt.Point;
import java.util.List;

public class PathSolverPlayer extends PathSolver {

	public PathSolverPlayer(int width, int height, Point start, Point end, List<Point> walls) {
		super(width, height, start, end, walls);
	}

}