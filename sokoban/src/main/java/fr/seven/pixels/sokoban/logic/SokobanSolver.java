package fr.seven.pixels.sokoban.logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SokobanSolver {

	public static final char MOVE_DOWN = 'D';
	public static final char MOVE_LEFT = 'L';
	public static final char MOVE_RIGHT = 'R';
	public static final char MOVE_UP = 'U';
	public static final char[] MOVES = new char[] { MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT };

	public static final char VALUE_BLOCK = 'B';
	public static final char VALUE_EMPTY = '.';
	public static final char VALUE_END = 'E';
	public static final char VALUE_PLAYER = 'P';
	public static final char VALUE_WALL = 'W';

	public static Point GetExpectedPositionToDoFollowingMove(Point target, char move) {
		if (move == PathSolver.MOVE_DOWN)
			return new Point(target.x, target.y - 1);
		if (move == PathSolver.MOVE_UP)
			return new Point(target.x, target.y + 1);
		if (move == PathSolver.MOVE_RIGHT)
			return new Point(target.x - 1, target.y);
		if (move == PathSolver.MOVE_LEFT)
			return new Point(target.x + 1, target.y);
		throw new RuntimeException("unknown move: " + move);
	}

	public static Set<String> merge(Set<String> bases, Set<String> suffixes) {

		// return null if suffixes are null
		if (bases == null)
			return null;

		// return directly suffixes if bases is null or empty
		if (bases.isEmpty())
			return suffixes;

		// return directly bases if suffixes is null or empty
		if (suffixes == null || suffixes.isEmpty())
			return bases;

		Set<String> result = new HashSet<String>();
		for (String base : bases)
			for (String suffix : suffixes)
				result.add(base + suffix);
		return result;
	}

	private char[] board;
	private int boardHeight;
	private int boardWidth;
	private Point pointBlock;
	private Point pointEnd;

	private Point pointPlayer;

	private List<Point> pointWalls;

	private void clean() {
		for (int i = 0; i < board.length; i++)
			board[i] = VALUE_EMPTY;
	}

	private void clean(char value) {
		for (int i = 0; i < board.length; i++)
			if (board[i] == value)
				board[i] = VALUE_EMPTY;
	}

	@SuppressWarnings("unused")
	private char get(Point p) {
		return board[p.y * boardWidth + p.x];
	}

	public Set<String> getSolutions() {

		// do nothing if not enough information
		if (pointPlayer == null || pointBlock == null || pointEnd == null) {
			System.out.println("missing parameters");
			return null;
		}

		// calculate shortest paths for the block
		PathSolverBlock pathSolverBlock = new PathSolverBlock(boardWidth, boardHeight, pointBlock, pointEnd, pointWalls);
		Set<String> blockSolutions = pathSolverBlock.getSolutions();

		Set<String> solutions = new HashSet<String>();
		bpathloop: for (String blockSolution : blockSolutions) {
			Set<String> pathSolutions = new HashSet<String>();
			Point blockPosition = new Point(pointBlock);
			Point playerPosition = new Point(pointPlayer);
			for (char move : blockSolution.toCharArray()) {
				Point expectedPosition = GetExpectedPositionToDoFollowingMove(blockPosition, move);
				PathSolverPlayer pathSolverPlayer = new PathSolverPlayer(boardWidth, boardHeight, playerPosition, expectedPosition, merge(pointWalls, blockPosition, pointEnd));
				Set<String> paths = pathSolverPlayer.getSolutions();

				// skip if there is no possible path
				if (paths == null) {
					continue bpathloop;
				}

				playerPosition = expectedPosition;
				pathSolutions = merge(pathSolutions, paths);
				pathSolutions = merge(pathSolutions, move);
				move(blockPosition, move);
				move(playerPosition, move);
			}
			solutions.addAll(pathSolutions);
		}

		return solutions;
	}

	private List<Point> merge(List<Point> points, Point... ps) {
		List<Point> result = new ArrayList<Point>();
		result.addAll(points);
		for (Point p : ps)
			result.add(p);
		return result;
	}

	private Set<String> merge(Set<String> bases, char suffix) {
		Set<String> result = new HashSet<String>();

		// return directly suffixes if bases is null or empty
		if (bases == null || bases.isEmpty()) {
			result.add(suffix + "");
			return result;
		}

		for (String base : bases)
			result.add(base + suffix);
		return result;
	}

	private void move(Point p, char move) {
		if (move == PathSolver.MOVE_DOWN)
			p.y++;
		else if (move == PathSolver.MOVE_UP)
			p.y--;
		else if (move == PathSolver.MOVE_RIGHT)
			p.x++;
		else if (move == PathSolver.MOVE_LEFT)
			p.x--;
		else
			throw new RuntimeException("unknown move: " + move);
	}

	private void set(Point p, char value) {
		board[p.y * boardWidth + p.x] = value;
	}

	public void setBlock(Point p) {
		clean(VALUE_BLOCK);
		this.pointBlock = p;
		set(p, VALUE_BLOCK);
	}

	public void setBoard(int width, int height) {
		this.boardWidth = width;
		this.boardHeight = height;
		this.board = new char[width * height];
		clean();
	}

	public void setEnd(Point p) {
		clean(VALUE_END);
		this.pointEnd = p;
		set(p, VALUE_END);
	}

	public void setPlayer(Point p) {
		clean(VALUE_PLAYER);
		this.pointPlayer = p;
		set(p, VALUE_PLAYER);
	}

	public void setWalls(List<Point> ps) {
		clean(VALUE_WALL);
		this.pointWalls = ps;
		for (Point p : ps)
			set(p, VALUE_WALL);
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
