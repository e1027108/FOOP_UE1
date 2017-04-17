package game;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import artifacts.Artifact;

/**
 * Test class for {@link SnakeImpl} All {@link Artifact} effects are tested as
 * well as movement of the snake.
 */
public class SnakeImplSizeAndHealthModifierTest {

	private Snake testSnake;
	private int startHealth, startMaxHealth, startSize;
	
	@Before
	public void setUp() {
		testSnake = new SnakeImpl("test", 1, Directions.N);
		startHealth = testSnake.getHealth();
		startMaxHealth = testSnake.getMaxHealth();
		startSize = testSnake.getSize();
	}
	
	@After
	public void tearDown() {
		testSnake = null;
	}

	@Test
	public void changeHealthModifierMinus1Plus1() {
		testSnake.changeHealthModifier(-1);
		assertEquals(testSnake.getHealth(), startHealth - 1);
		testSnake.changeHealthModifier(1);
		assertEquals(testSnake.getHealth(), startHealth);
	}

	@Test
	public void changeHealthModifierPlus1AtMaxHealth() {
		testSnake.changeHealthModifier(1);
		assertEquals(testSnake.getHealth(), startMaxHealth);
	}

	@Test
	public void changeHealthModifierPlus1AfterSizeIncrease() {
		testSnake.changeSizeModifier(1);
		testSnake.changeHealthModifier(1);
		assertEquals(testSnake.getHealth(), startMaxHealth + 1);
	}

	@Test
	public void changeHealthModifierPlus1AfterSizeDecrease() {
		testSnake.changeSizeModifier(-1);
		testSnake.changeHealthModifier(0);
		assertEquals(testSnake.getHealth(), startHealth - 1);
	}

	@Test
	public void changeSizeModifierPlus1() {
		testSnake.changeSizeModifier(1);
		assertEquals(testSnake.getSize(), startSize + 1);
	}

	@Test
	public void changeSizeModifierMinus1() {
		testSnake.changeSizeModifier(-1);
		assertEquals(testSnake.getSize(), startSize - 1);
	}
}
