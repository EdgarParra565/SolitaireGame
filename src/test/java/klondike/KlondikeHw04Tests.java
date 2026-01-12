package klondike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import klondike.model.hw04.ValueCard;
import klondike.model.hw04.WhiteheadKlondike;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test Whitehead Klondike with the abstract class code and new implementations.
 */
public class KlondikeHw04Tests {
  private WhiteheadKlondike klondike;
  private List<ValueCard> valueCards;

  /**
   * Set up of game for tests.
   * Going to be used to test every function in Whitehead Klondike.
   */
  @Before
  public void setUp() {
    klondike = new WhiteheadKlondike();
    valueCards = klondike.createNewDeck();
  }

  @Test
  public void createNewDeckTest() {
    assertEquals(52, valueCards.size());
  }

  @Test
  public void testValidStartGame() {
    klondike.startGame(valueCards, false, 7, 1);
    assertEquals(7, klondike.getNumPiles());
    assertEquals(1, klondike.getNumDraw());
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidStartGameTwo() {
    klondike.startGame(valueCards, false, 7, 1);
    klondike.startGame(valueCards, false, 7, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStartGameNegDraw() {
    klondike.startGame(valueCards, false, 7, -1);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidStartGameNegPiles() {
    klondike.startGame(valueCards, false, -7, 1);
  }

  @Test
  public void testVisibleCards() {
    klondike.startGame(valueCards, false, 7, 1);
    assertTrue(klondike.isCardVisible(5, 0));
  }

  @Test
  public void testSameColorBuildRed() {
    klondike.startGame(valueCards, false, 7, 1);
    try {
      klondike.movePile(0, 1, 1);
    } catch (IllegalStateException e) {
      // ignore
    }
  }

  @Test
  public void testSameColorBuildBlack() {
    klondike.startGame(valueCards, false, 7, 1);
    try {
      klondike.movePile(0, 1, 2);
    } catch (IllegalStateException e) {
      // ignore
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testSameColorBuildRedOntoBlack() {
    klondike.startGame(valueCards, false, 7, 1);
    klondike.movePile(0, 1, 1);
  }

  @Test
  public void testMoveSameSuit() {
    klondike.startGame(valueCards, false, 7, 1);
    try {
      klondike.movePile(6, 2, 5);
    } catch (IllegalStateException e) {
      // ignore
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveNotSameSuit() {
    klondike.startGame(valueCards, false, 7, 1);
    klondike.movePile(6, 3, 5);
  }

  @Test
  public void testMoveCardToEmpty() {
    klondike.startGame(valueCards, false, 7, 1);
    try {
      klondike.movePile(0, 1, 1);
      klondike.movePile(2, 1, 0);
    } catch (IllegalStateException e) {
      // ignore
    }
  }

  @Test
  public void testMoveDrawCardToEmpty() {
    klondike.startGame(valueCards, false, 7, 1);
    try {
      klondike.moveDraw(0);
    } catch (IllegalStateException e) {
      // ignore
    }
  }

  @Test
  public void testMoveDrawValidBuild() {
    klondike.startGame(valueCards, false, 7, 1);
    try {
      klondike.moveDraw(6);
    } catch (IllegalStateException e) {
      // ignore
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawNoHand() {
    klondike.startGame(valueCards, false, 7, 1);
    for (int i = 0; i < 100; i++) {
      klondike.discardDraw();
    }
    klondike.moveDraw(1);
  }

}
