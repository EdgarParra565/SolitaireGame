package klondike;

import klondike.controller.KlondikeController;
import klondike.controller.KlondikeTextualController;
import klondike.model.hw04.KlondikeCreator;
import klondike.model.hw04.KlondikeModel;
import klondike.model.hw04.ValueCard;


/**
 * Klondike class that cannot be extended.
 * Contains the main method that runs the Klondike Game depending on which type of game was chosen.
 */
public final class Klondike {

  /**
   * Main method that runs Klondike and deciphers the chosen game via the string given by the user.
   * Parses through the input given by user for piles param and numDraws param.
   *
   * @param args system.in string array.
   */
  public static void main(String[] args) {
    try {
      if (args.length == 0) {
        throw new IllegalArgumentException();
      }
      String variant = args[0].toLowerCase();
      int piles = args.length > 1 ? Integer.parseInt(args[1]) : 7;
      int draws = args.length > 2 ? Integer.parseInt(args[2]) : 3;
      if (piles <= 0 || draws <= 0) {
        return;
      }
      KlondikeModel<ValueCard> model = switch (variant) {
        case "whitehead" -> KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
        case "basic" -> KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
        default -> throw new IllegalArgumentException();
      };
      KlondikeController controller =
          new KlondikeTextualController(new java.io.InputStreamReader(System.in), System.out);
      controller.playGame(model, model.createNewDeck(), true, piles, draws);
    } catch (Exception e) {
      //
    }
  }
}
