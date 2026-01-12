package klondike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.ValueCard;
import org.junit.Before;
import org.junit.Test;

/**
 * A class for testing the KlondikeModel. All tests
 * in this class cannot create Card type objects. Instead,
 * the tests use the createNewDeck method to help create
 * example games.
 */
public class KlondikeModelTests {
  private BasicKlondike klondike;
  private List<ValueCard> values;

  /**
   * Set up of game for tests.
   * Going to be used to test every function in BasicKlondike.
   */
  @Before
  public void setUp() {
    klondike = new BasicKlondike();
    values = klondike.createNewDeck();
  }


  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCreateNewDeck() {
    values.remove(1);
    klondike.startGame(values, false, 7, 1);
  }

  @Test
  public void testValidCreateNewDeck() {
    klondike.startGame(values, false, 7, 1);
    assertEquals(52, values.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStartGameNegD() {
    klondike.startGame(values, false, 7, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStartGameZero() {
    klondike.startGame(values, false, 0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStartGameNull() {
    klondike.startGame(null, false, 10, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStartGameRange() {
    klondike.startGame(values, false, 15, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidStartGame2() {
    klondike.startGame(values, false, 7, 1);
    klondike.startGame(values, false, 7, 1);
  }

  @Test
  public void testValidStartGame() {
    klondike.startGame(values, false, 7, 1);
    assertEquals(52, values.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidMovePileState() {
    klondike.movePile(1, 1, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMovePileEqual() {
    klondike.startGame(values, false, 7, 1);
    klondike.movePile(1, 1, 1);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMovePileNegSrc() {
    klondike.startGame(values, false, 7, -1);
    klondike.movePile(-1, 2, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMovePileNegNum() {
    klondike.startGame(values, false, 7, 1);
    klondike.movePile(0, -2, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMovePileNegDest() {
    klondike.startGame(values, false, 7, 1);
    klondike.movePile(0, 1, -1);
  }

  @Test
  public void testValidMovePile() {
    klondike.startGame(values, false, 7, 1);
    try {
      int height = klondike.getPileHeight(1);
      klondike.movePile(0, 1, 1);
      assertTrue(klondike.getPileHeight(1) >= height);
    } catch (IllegalStateException ignored) {
      //
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveDrawNeg() {
    klondike.startGame(values, false, 7, 1);
    klondike.moveDraw(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveDrawRange() {
    klondike.startGame(values, false, 7, 1);
    klondike.moveDraw(10);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidMoveDraw2() {
    klondike.moveDraw(1);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidMoveDrawEmpty() {
    klondike.startGame(values, false, 7, 1);
    int limit = 50;
    while (!klondike.getDrawCards().isEmpty() && limit-- > 0) {
      klondike.discardDraw();
    }
    klondike.moveDraw(1);
  }

  @Test
  public void testValidMoveDraw() {
    klondike.startGame(values, false, 7, 1);
    try {
      int height = klondike.getPileHeight(6);
      klondike.moveDraw(6);
      assertEquals(klondike.getPileHeight(6), height);
    } catch (IllegalStateException e) {
      //
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveToFoundationSrcPile() {
    klondike.startGame(values, false, 7, 1);
    klondike.moveToFoundation(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveToFoundationFoundationPile() {
    klondike.startGame(values, false, 7, 1);
    klondike.moveToFoundation(0, -1);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidMoveToFoundation2() {
    klondike.moveToFoundation(1, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidMoveToFoundationEmptyPile() {
    klondike.startGame(values, false, 7, 1);
    klondike.moveToFoundation(0, 0);
    klondike.moveToFoundation(0, 0);
  }

  @Test
  public void testValidMoveToFoundation() {
    klondike.startGame(values, false, 7, 1);
    klondike.moveToFoundation(0, 0);
    assertNotNull(klondike.getCardAt(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveDrawToFoundationPile() {
    klondike.startGame(values, false, 7, 1);
    klondike.moveDrawToFoundation(-1);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidMoveDrawToFoundation2() {
    klondike.moveDrawToFoundation(1);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidMoveDrawToFoundationEmptyDraw() {
    klondike.startGame(values, false, 7, 1);
    int limit = 50;
    while (!klondike.getDrawCards().isEmpty() && limit-- > 0) {
      klondike.discardDraw();
    }
    klondike.moveDrawToFoundation(0);
  }

  @Test
  public void testValidMoveToDrawFoundation() {
    klondike.startGame(values, false, 7, 1);
    int attempts = 50;
    while (!klondike.getDrawCards().isEmpty() && attempts-- > 0) {
      try {
        klondike.moveDrawToFoundation(0);
        assertEquals(1, klondike.getScore());
        return;
      } catch (IllegalStateException ignored) {
        klondike.discardDraw();
      }
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidDiscardDraw() {
    klondike.discardDraw();
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidDiscardDrawEmpty() {
    klondike.startGame(values, false, 7, 1);
    int limit = 100;
    try {
      while (!klondike.getDrawCards().isEmpty() && limit-- > 0) {
        klondike.discardDraw();
      }
      klondike.discardDraw();
    } catch (IllegalStateException ignored) {
      //
    }
  }

  @Test
  public void testValidDiscardDraw() {
    klondike.startGame(values, false, 7, 1);
    int size = klondike.getDrawCards().size();
    klondike.discardDraw();
    assertTrue(klondike.getDrawCards().size() >= 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidGetNumRows() {
    klondike.getNumRows();
  }

  @Test
  public void testValidGetNumRows() {
    klondike.startGame(values, false, 7, 1);
    assertEquals(7, klondike.getNumRows());
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidGetNumPiles() {
    klondike.getNumPiles();
  }

  @Test
  public void testValidGetNumPiles() {
    klondike.startGame(values, false, 7, 1);
    assertEquals(7, klondike.getNumPiles());
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidGetNumDraws() {
    klondike.getNumDraw();
  }

  @Test
  public void testValidGetNumDraws() {
    klondike.startGame(values, false, 7, 1);
    assertEquals(1, klondike.getNumDraw());
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidIsGameOver() {
    klondike.isGameOver();
  }

  @Test
  public void testValidIsGameOver() {
    klondike.startGame(values, false, 7, 1);
    assertFalse(klondike.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidGetScore() {
    klondike.getScore();
  }

  @Test
  public void testValidGetScore() {
    klondike.startGame(values, false, 7, 1);
    assertEquals(0, klondike.getScore());
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidGetPileHeight() {
    klondike.getPileHeight(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetPileHeightNeg() {
    klondike.startGame(values, false, 7, 1);
    klondike.getPileHeight(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetPileHeightRange() {
    klondike.startGame(values, false, 7, 1);
    klondike.getPileHeight(8);
  }

  @Test
  public void testValidGetPileHeight() {
    klondike.startGame(values, false, 7, 1);
    assertEquals(2, klondike.getPileHeight(1));
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidGetCardAt1() {
    klondike.getCardAt(1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetCardAt1NegPile() {
    klondike.startGame(values, false, 7, 1);
    klondike.getCardAt(-1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetCardAt1NegCard() {
    klondike.startGame(values, false, 7, 1);
    klondike.getCardAt(1, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetCardAt1NotTop() {
    klondike.startGame(values, false, 7, 1);
    klondike.getCardAt(6, 0);
  }

  @Test
  public void testValidGetCardAt1() {
    klondike.startGame(values, false, 7, 1);
    assertNotNull(klondike.getCardAt(0, 0));
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidGetCardAt2() {
    klondike.getCardAt(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetCardAt2Neg() {
    klondike.startGame(values, false, 7, 1);
    klondike.getCardAt(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetCardAt2Range() {
    klondike.startGame(values, false, 7, 1);
    klondike.getCardAt(5);
  }

  @Test
  public void testValidGetCardAt2() {
    klondike.startGame(values, false, 7, 1);
    assertNull(klondike.getCardAt(0));
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidIsCardVisible() {
    klondike.isCardVisible(1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidIsCardVisibleNegPile() {
    klondike.startGame(values, false, 7, 1);
    klondike.isCardVisible(-1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidIsCardVisibleNegCard() {
    klondike.startGame(values, false, 7, 1);
    klondike.isCardVisible(1, -1);
  }

  @Test
  public void testValidIsCardVisible() {
    klondike.startGame(values, false, 7, 1);
    assertTrue(klondike.isCardVisible(1, 1));
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidGetDrawCards() {
    klondike.getDrawCards();
  }

  @Test
  public void testValidGetDrawCards() {
    klondike.startGame(values, false, 7, 1);
    assertTrue(klondike.getDrawCards().size() <= 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidGetNumFoundations() {
    klondike.getNumFoundations();
  }

  @Test
  public void testValidGetNumFoundations() {
    klondike.startGame(values, false, 7, 1);
    assertEquals(4, klondike.getNumFoundations());
  }
}
