package fr.seven.pixels.sokoban.logic;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathSolver {

	public static final char MOVE_DOWN = 'D';
	public static final char MOVE_LEFT = 'L';
	public static final char MOVE_RIGHT = 'R';
	public static final char MOVE_UP = 'U';
	public static final char[] MOVES = new char[] { MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT };

	public static final int VALUE_EMPTY = -1;
	public static final int VALUE_END = -3;
	public static final int VALUE_WALL = -4;

	protected int[] board;
	protected int boardHeight;
	protected int boardWidth;
	protected Point pointStart;
	protected Set<String> solutions;
	protected Integer solutionsLength;

	public PathSolver(int width, int height, Point start, Point end, List<Point> walls) {
		this.boardWidth = width;
		this.boardHeight = height;
		this.board = new int[width * height];
		clean();

		this.pointStart = start;

		set(end.x, end.y, VALUE_END);

		for (Point p : walls)
			set(p.x, p.y, VALUE_WALL);
	}

	protected void clean() {
		for (int i = 0; i < board.length; i++)
			board[i] = VALUE_EMPTY;
	}

	protected void clean(int value) {
		for (int i = 0; i < board.length; i++)
			if (board[i] == value)
				board[i] = VALUE_EMPTY;
	}

	protected int get(int x, int y) {
		return board[y * boardWidth + x];
	}

	public Set<String> getSolutions() {

		solutions = new HashSet<String>();
		solutionsLength = null;
		play("", pointStart.x, pointStart.y);

		return solutions;
	}

	protected boolean isImpossibleMove(int x, int y, char move) {
		return false;
	}

	protected boolean isOutside(int x, int y) {
		return x < 0 || x >= boardWidth || y < 0 || y >= boardHeight;
	}

	protected void play(String path, int currentX, int currentY) {

		// do nothing if out of the grid
		if (isOutside(currentX, currentY)) {
			return;
		}

		// solution found?
		int currentValue = get(currentX, currentY);
		int currentLength = path.length();
		if (currentValue == VALUE_END) {
			if ((solutionsLength == null || currentLength <= solutionsLength)) {

				// reset solutions if it's a shorter path
				if (solutionsLength == null || currentLength < solutionsLength) {
					solutionsLength = currentLength;
					solutions = new HashSet<String>();
				}

				solutions.add(path);
			}
			return;
		}

		// do nothing if wall
		if (currentValue == VALUE_WALL) {
			return;
		}

		// do nothing if a shorter path exists to this cell
		if (currentValue >= 0 && currentLength > currentValue) {
			return;
		} else if (currentValue < 0 || currentLength < currentValue) {
			set(currentX, currentY, currentLength);
		}

		// continue the game
		for (char move : MOVES) {

			// do nothing if impossible move
			if (isImpossibleMove(currentX, currentY, move))
				continue;

			// calculate the next position
			int nextX = currentX;
			int nextY = currentY;
			if (move == MOVE_UP) {
				nextY--;
			} else if (move == MOVE_DOWN) {
				nextY++;
			} else if (move == MOVE_LEFT) {
				nextX--;
			} else if (move == MOVE_RIGHT) {
				nextX++;
			}

			play(path + move, nextX, nextY);
		}
	}

	protected void set(int x, int y, int value) {
		board[y * boardWidth + x] = value;
	}

	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < board.length; i++) {
			if (i % boardWidth == 0 && result != "")
				result += "\n";
			result += board[i] + " ";
		}
		return result;
	}

}