package klondike;

import static org.junit.Assert.assertEquals;

import java.util.List;
import klondike.model.hw04.BasicKlondike;
import klondike.model.hw04.KlondikeModel;
import klondike.model.hw04.ValueCard;
import klondike.view.KlondikeTextualView;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the KlondikeTextualView.
 * Using assertEquals to make sure that the view is the same and what the model is doing.
 */
public class KlondikeTextualViewTests {

  private KlondikeModel<ValueCard> model;
  private KlondikeTextualView view;
  private List<ValueCard> drawCards;

  /**
   * Set up and initialize fields before the test.
   */
  @Before
  public void setUp() {
    model = new BasicKlondike();
    view = new KlondikeTextualView(model);
    drawCards = model.createNewDeck();
  }

  @Test
  public void testToString() {
    model.startGame(drawCards, false, 7, 1);
    String result = view.toString();
    System.out.println(result);
    assertEquals("Draw: 8♣\n"
        +
        "Foundation: <none>, <none>, <none>, <none>\n"
        +
        " A♣  ?  ?  ?  ?  ?  ?\n"
        +
        "    2♠  ?  ?  ?  ?  ?\n"
        +
        "       4♢  ?  ?  ?  ?\n"
        +
        "          5♡  ?  ?  ?\n"
        +
        "             6♡  ?  ?\n"
        +
        "                7♢  ?\n"
        +
        "                   7♠\n", result);

  }
}
