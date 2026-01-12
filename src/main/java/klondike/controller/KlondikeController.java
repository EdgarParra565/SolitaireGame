package klondike.controller;

import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Interface used for the KlondikeModel to get inputs from the user.
 * One method play game that takes in the same params as startGame but feeds
 * the game input from the user via a scanner object when implemented.
 */
public interface KlondikeController {

  /**
   * Method for controller that gets inputs to play a game of Klondike.
   * Throws State Exception if model is null or if playGame is called on the controller.
   *
   * @param model KlondikeModel of a klondike game.
   * @param deck Cards used to play the game.
   * @param shuffle Boolean to shuffle cards or not to.
   * @param numPiles Number of cascade piles.
   * @param numDraw Number of draw cards allowed.
   * @throws IllegalStateException controller is unable to successfully receive
   *                               input or transmit output, or if the game cannot be started
   */
  <C extends Card> void playGame(KlondikeModel<C> model, List<C> deck, boolean shuffle,
                                 int numPiles, int numDraw)
      throws IllegalStateException;


}
