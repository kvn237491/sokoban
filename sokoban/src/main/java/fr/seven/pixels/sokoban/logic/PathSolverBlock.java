package fr.seven.pixels.sokoban.logic;

import java.awt.Point;
import java.util.List;

public class PathSolverBlock extends PathSolver {

	public PathSolverBlock(int width, int height, Point start, Point end, List<Point> walls) {
		super(width, height, start, end, walls);
	}

	@Override
	protected boolean isImpossibleMove(int x, int y, char move) {
		if (move == MOVE_UP) {
			y++;
		} else if (move == MOVE_DOWN) {
			y--;
		} else if (move == MOVE_LEFT) {
			x++;
		} else if (move == MOVE_RIGHT) {
			x--;
		}
		return isOutside(x, y) || get(x, y) == VALUE_WALL;
	}

}