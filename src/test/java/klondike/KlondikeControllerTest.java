package klondike;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import klondike.controller.KlondikeController;
import klondike.controller.KlondikeTextualController;
import klondike.mocks.FirstMock;
import klondike.mocks.IllegalArgumentMock;
import klondike.mocks.IllegalStateMock;
import klondike.mocks.OverNoWinMock;
import klondike.mocks.WinCheckMock;
import klondike.model.hw04.BasicKlondike;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the Controller Class.
 */
public class KlondikeControllerTest {
  private BasicKlondike model;
  private StringBuilder output;

  /**
   * Set up of game for tests.
   * Going to be used to test every function in BasicKlondike.
   */
  @Before
  public void setUp() {
    model = new BasicKlondike();
    output = new StringBuilder();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRd() {
    new KlondikeTextualController(null, new StringBuilder());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAp() {
    new KlondikeTextualController(new StringReader(""), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyModel() {
    StringReader reader = new StringReader("q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(null, new ArrayList<>(), false, 7, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayWithState() {
    StringReader reader = new StringReader("q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    IllegalStateMock mock = new IllegalStateMock();
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayWithArgument() {
    StringReader reader = new StringReader("q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    IllegalArgumentMock mock = new IllegalArgumentMock();
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidDeck() {
    StringReader reader = new StringReader("q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    //IllegalStateMock mock = new IllegalStateMock();
    controller.playGame(model, new ArrayList<>(), false, 7, 1);
  }


  @Test
  public void testQuit() {
    StringReader reader = new StringReader("q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(model, model.createNewDeck(), false, 7, 1);
    String result = output.toString();
    assertTrue("Game quit!", result.contains("Game quit!"));
    //assertTrue("State of the game when quit:", result.contains("State of the game when quit:"));
  }

  @Test
  public void testWhenMpQuit() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("mp 2 q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("Game quit!", output.toString().contains("Game quit!"));
    assertFalse("Will not do movePile", mock.mpCalled);
  }

  @Test
  public void testWhenMdQuit() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("md q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("Game quit!", output.toString().contains("Game quit!"));
    assertFalse("Will not do movePile", mock.mdCalled);
  }

  @Test
  public void testWhenMpfQuit() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("mpf q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("Game quit!", output.toString().contains("Game quit!"));
    assertFalse("Will not do movePile", mock.mpfCalled);
  }

  @Test
  public void testWhenMdfQuit() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("mpf q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("Game quit!", output.toString().contains("Game quit!"));
    assertFalse("Will not do movePile", mock.mdfCalled);
  }


  @Test
  public void testInvalidCommand() {
    StringReader reader = new StringReader("xyz dd mpp 1 1 2 q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(model, model.createNewDeck(), false, 7, 1);
    String result = output.toString();
    assertTrue("Invalid move. Play again.", result.contains("Invalid move. Play again."));
  }

  @Test
  public void testNoValidCommands() {
    StringReader reader = new StringReader("xyz hij klm");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(model, model.createNewDeck(), false, 7, 1);
    String result = output.toString();
    // count lines for test
    assertTrue("Invalid move. Play again.", result.contains("Invalid move. Play again."));
  }

  @Test
  public void testSkipsNotNumbers() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("mpp abc 1 1 2 q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("mp called", mock.mpCalled);
  }

  @Test
  public void testValidDdCalled() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("dd q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("dd called", mock.ddCalled);
  }

  @Test
  public void testValidMdCalled() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("md 1 q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("md called", mock.mdCalled);
  }

  @Test
  public void testValidMppCalled() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("mpp 1 1 2 q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("mpp called", mock.mpCalled);
  }

  @Test
  public void testValidMpfCalled() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("mpf 1 1 q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("mpf called", mock.mpfCalled);
  }

  @Test
  public void testValidMdfCalled() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("mdf 2 q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("mdf called", mock.mdfCalled);
  }

  @Test
  public void testValidMovesCalled() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("dd md 1 q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("dd called", mock.ddCalled);
    assertTrue("md called", mock.mdCalled);
  }

  @Test
  public void testWinCheck() {
    WinCheckMock mock = new WinCheckMock();
    StringReader reader = new StringReader("");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    String result = output.toString();
    assertTrue("You win!", result.contains("You win!"));
  }

  @Test
  public void testGameOverFinishedInput() {
    WinCheckMock mock = new WinCheckMock();
    StringReader reader = new StringReader("mpp 1 1 1 dd md 2");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("You win!", output.toString().contains("You win!"));
  }

  @Test
  public void testGameOverNoWin() {
    OverNoWinMock mock = new OverNoWinMock();
    StringReader reader = new StringReader("mpp 1 1 1 dd md 2");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("Game over. Score:", output.toString().contains("Game over. Score:"));
  }


  @Test(expected = IllegalStateException.class)
  public void testNoMoreInput() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("mpp");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
  }

  @Test
  public void testNoMoreInputSkips() {
    FirstMock mock = new FirstMock();
    StringReader reader = new StringReader("abc");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
    assertTrue("abc", output.toString().contains("Invalid move. Play again."));
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalArgMock() {
    IllegalArgumentMock mock = new IllegalArgumentMock();
    StringReader reader = new StringReader("md 1 q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStaMock() {
    IllegalStateMock mock = new IllegalStateMock();
    StringReader reader = new StringReader("md 1 q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(mock, mock.createNewDeck(), false, 7, 1);
  }


  @Test
  public void testWithBasicModel() {
    StringReader reader = new StringReader("dd q");
    KlondikeController controller = new KlondikeTextualController(reader, output);
    controller.playGame(model, model.createNewDeck(), false, 7, 1);
    String result = output.toString();
    assertTrue("Draw:", result.contains("Draw:"));
    assertTrue("Game quit!", result.contains("Game quit!"));
  }

}
