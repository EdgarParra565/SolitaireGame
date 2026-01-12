package klondike.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Class that provides a model of the game in a string format.
 * String format is just like the given example in the instructions.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel<? extends Card> model;
  private final Appendable ap;
  // ... any other fields you need

  /**
   * Constructor that instantiates the fields.
   *
   * @param model the BasicKlondike game model.
   */
  public KlondikeTextualView(KlondikeModel<?> model) {
    /*
    this.model = model;
    this.log = null;
     */
    this(model, new StringBuilder());
  }

  /**
   * Constructor that instantiates the fields.
   *
   * @param model the BasicKlondike game model.
   * @param ap    the appendable object that appends strings.
   */
  public KlondikeTextualView(KlondikeModel<?> model, Appendable ap) {
    this.model = model;
    this.ap = ap;
  }

  // your implementation goes here
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    getDrawCards(sb);
    getFoundations(sb);
    getPiles(sb);
    return sb.toString();
  }

  /**
   * Adds the drawCards to the string builder.
   * Modifies the stringBuilder parameter.
   *
   * @param sb stringBuilder that will be modified.
   */
  private void getDrawCards(StringBuilder sb) {
    List<?> drawCards = model.getDrawCards();
    sb.append("Draw:");
    if (drawCards.isEmpty()) {
      sb.append("");
    } else {
      //if (drawCards.isEmpty())
      sb.append(" ");
      List<String> drawCardsList = new ArrayList<>();
      for (Object drawCard : drawCards) {
        drawCardsList.add(drawCard.toString());
      }
      sb.append(String.join(", ", drawCardsList));
    }
  }

  /**
   * Adds the foundation card list to the string builder.
   * Modifies the string Builder parameter.
   *
   * @param sb stringBuilder that will be modified.
   */
  private void getFoundations(StringBuilder sb) {
    sb.append("\nFoundation: ");
    List<String> foundationsList = new ArrayList<>();
    for (int numberOfFoundations = 0; numberOfFoundations < model.getNumFoundations();
         numberOfFoundations++) {
      Object card = model.getCardAt(numberOfFoundations);
      //foundationsList.add(model.getCardAt(numberOfFoundations).toString());
      //(IllegalArgumentException | IllegalStateException e) {
      if (card == null) {
        foundationsList.add("<none>");
      } else {
        foundationsList.add(card.toString());
      }
    }
    sb.append(String.join(", ", foundationsList));
    sb.append("\n");
  }

  /**
   * Adds the piles card list to the string Builder.
   * Modifies the string builder parameter
   *
   * @param sb stringBuilder that will be modified.
   */
  private void getPiles(StringBuilder sb) {
    int numRows = model.getNumRows();
    int numPiles = model.getNumPiles();
    for (int rows = 0; rows < numRows; rows++) {
      List<String> pilesList = new ArrayList<>();
      for (int pile = 0; pile < numPiles; pile++) {
        String str;
        if (rows < model.getPileHeight(pile)) {
          if (model.isCardVisible(pile, rows)) {
            Card card = model.getCardAt(pile, rows);
            str = (card != null) ? String.format("%3s", card.toString()) : "   ";
            //str = String.format("%3s", model.getCardAt(pile, rows).toString());
          } else {
            str = "  ?";
          }
        } else if (model.getPileHeight(pile) == 0 && rows == 0) {
          str = "  X";
        } else {
          str = "   ";
          //rowString.append(" ");
        }
        pilesList.add(str);
      }
      sb.append(String.join("", pilesList));
      sb.append("\n");
    }
  }

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  @Override
  public void render() throws IOException {
    ap.append(this.toString());
  }
}
