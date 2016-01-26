package fr.seven.pixels.sokoban;

import java.awt.Point;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import fr.seven.pixels.sokoban.logic.SokobanSolver;
import junit.framework.Assert;

public class AppTest {

	@Test
	public void classic() {
		SokobanSolver sokobanSolver = new SokobanSolver();
		sokobanSolver.setBoard(10, 10);
		sokobanSolver.setPlayer(new Point(0, 5));
		sokobanSolver.setBlock(new Point(3, 7));
		sokobanSolver.setEnd(new Point(9, 3));
		sokobanSolver.setWalls(Arrays.asList(new Point(1, 5), new Point(2, 5), new Point(2, 6), new Point(2, 7), new Point(2, 4)));
		System.out.println(sokobanSolver);

		Set<String> solutions = sokobanSolver.getSolutions();
		for (String solution : solutions)
			System.out.println(solution);

		String[] expectedList = new String[] { "DRDDRRUUUURUULLDRRRRRR", "DDRDRRUUUURUULLDRRRRRR", "DDDRRRUUUURUULLDRRRRRR" };
		Assert.assertTrue(solutions.size() == expectedList.length);
		for (String expected : expectedList)
			Assert.assertTrue(solutions.contains(expected));
	}

}
