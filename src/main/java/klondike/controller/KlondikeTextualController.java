package klondike.controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.view.KlondikeTextualView;
import klondike.view.TextualView;

/**
 * Class that takes input from the user to feed to the model.
 * Uses scanner to get strings and ints from the user.
 */
public class KlondikeTextualController implements KlondikeController {
  private final Readable rd;
  private final Appendable ap;
  private final Scanner sc;

  /**
   * Constructor to initialize the Klondike Controller.
   * Takes two params one of which is used to create a scanner object to parse through the string.
   *
   * @param rd Readable to
   * @param ap Appendable to
   * @throws IllegalArgumentException params are invalid if null.
   */
  public KlondikeTextualController(Readable rd, Appendable ap) throws IllegalArgumentException {
    if (rd == null || ap == null) {
      throw new IllegalArgumentException("rd or ap is null");
    }
    this.rd = rd;
    this.ap = ap;
    this.sc = new Scanner(rd);
  }

  /**
   * Method for controller that gets inputs to play a game of Klondike.
   * Throws State Exception if model is null or if playGame is called on the controller.
   *
   * @param model    KlondikeModel of a klondike game.
   * @param deck     Cards used to play the game.
   * @param shuffle  Boolean to shuffle cards or not to.
   * @param numPiles Number of cascade piles.
   * @param numDraw  Number of draw cards allowed.
   * @throws IllegalStateException controller is unable to successfully receive
   *                               input or transmit output, or if the game cannot be started
   */
  @Override
  public <C extends Card> void playGame(KlondikeModel<C> model, List<C> deck, boolean shuffle,
                                        int numPiles, int numDraw) throws IllegalStateException {
    if (model == null) {
      throw new IllegalArgumentException("playGame() called on KlondikeTextualController");
    }
    try {
      model.startGame(deck, shuffle, numPiles, numDraw);
    } catch (IllegalStateException | IllegalArgumentException e) {
      throw new IllegalStateException("Not possible to Start", e);
    }
    TextualView view = new KlondikeTextualView(model, ap);
    try {
      renderState(view, model);
      while (!model.isGameOver()) {
        if (!sc.hasNext()) {
          throw new IllegalStateException("No more input");
        }
        String next = nextCommand();
        if (next.equals("q")) {
          quitGame(view, model);
          return;
        }
        if (doCommand(model, next, view)) {
          renderState(view, model);
        }
      }
      //view.render();
      if (model.getScore() == deck.size()) {
        ap.append("You win!\n");
      } else {
        ap.append("Game over. Score: ")
            .append(String.valueOf(model.getScore()))
            .append("\n");
      }
    } catch (QuitException e) {
      try {
        quitGame(view, model);
      } catch (IOException ex) {
        // ignore
      }
    } catch (IOException e) {
      throw new IllegalStateException("Append error", e);
    }
  }

  /**
   * Helper method used to check if a scanner has a next int needed for a model.
   * Uses a scanner object to get a next int or command.
   *
   * @return An int needed for the model.
   * @throws QuitException If user decides to quit.
   */
  private int nextInt() throws QuitException {
    while (true) {
      if (!sc.hasNext()) {
        throw new IllegalStateException("No more input");
      }
      String next = sc.next();
      if (next.equalsIgnoreCase("q")) {
        throw new QuitException();
      }
      try {
        return Integer.parseInt(next);
      } catch (NumberFormatException e) {
        // ignore
      }
    }
  }

  /**
   * Helper method helps get next Scanner object command.
   *
   * @return the cmd that pertains to a command.
   * @throws IOException if it cannot append to appendable object.
   */
  private String nextCommand() throws IOException {
    while (true) {
      if (!sc.hasNextLine()) {
        return "q";
      }
      String next = sc.next().toLowerCase();
      if (next.equals("q")) {
        return "q";
      }
      if (next.matches("m(pp|d|pf|df)?") || next.equals("dd")) {
        return next;
      }
      ap.append("Invalid move. Play again.\n");
    }
  }

  /**
   * Helper that takes in a good command and feeds it to the model.
   *
   * @param model KlondikeModel that is given inputs for the game.
   * @param next  String that contains the next command for the model.
   * @param view  TextualView that will output game model if quitException is given.
   * @param <C>   Card type for the model.
   * @return A boolean that tells play game if a move was successful.
   * @throws IOException for quitException.
   */
  private <C extends Card> boolean doCommand(KlondikeModel<C> model, String next, TextualView view)
      throws IOException {
    try {
      switch (next) {
        case "mpp":
          model.movePile(nextInt() - 1, nextInt(), nextInt() - 1);
          return true;
        case "md":
          model.moveDraw(nextInt() - 1);
          return true;
        case "mpf":
          model.moveToFoundation(nextInt() - 1, nextInt() - 1);
          return true;
        case "mdf":
          model.moveDrawToFoundation(nextInt() - 1);
          return true;
        case "dd":
          model.discardDraw();
          return true;
        default:
          ap.append("Invalid move. Play again.\n");
          return false;
      }
    } catch (IllegalArgumentException | IllegalStateException e) {
      ap.append("Invalid move. Play again.\n");
      return false;
    }
  }

  /**
   * Helper method used to append the state of the game to the appendable object.
   * Used to display score during the game.
   *
   * @param view  TextualView of the Klondike game state.
   * @param model KlondikeModel of the Klondike game.
   * @param <C>   KlondikeModel's cards can be of any type that extends the card interface.
   * @throws IOException If renderState is called on the controller.
   */
  private <C extends Card> void renderState(TextualView view, KlondikeModel<C> model)
      throws IOException {
    view.render();
    ap.append("Score: ").append(String.valueOf(model.getScore())).append("\n");
  }

  /**
   * Helper method used to append the state of the game to the appendable object.
   * Used to display when user quits or finishes game.
   *
   * @param view  TextualView of the Klondike game state.
   * @param model KlondikeModel of the Klondike game.
   * @param <C>   KlondikeModel's cards can be of any type that extends the card interface.
   * @throws IOException If quitGame is called on the controller.
   */
  private <C extends Card> void quitGame(TextualView view, KlondikeModel<C> model)
      throws IOException {
    ap.append("Game quit!\n");
    ap.append("State of game when quit:\n");
    view.render();
    ap.append("Score: ").append(String.valueOf(model.getScore())).append("\n");
  }

  /**
   * Private class used to display if a user decides to quit or not to.
   */
  private static class QuitException extends IOException {
  }

}