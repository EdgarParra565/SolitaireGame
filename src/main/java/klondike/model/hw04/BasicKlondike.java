package klondike.model.hw04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A class that implements the KlondikeModel interface with ValueCard type objects.
 * BasicKlondike provides functions that would allow one to play a game of solitaire.
 * ValueCard type for the implementation.
 */
public class BasicKlondike extends AbstractKlondike {

  /**
   * Constructor that sets the fields.
   */
  public BasicKlondike() {
    this.deck = new ArrayList<ValueCard>();
    this.piles = new ArrayList<>();
    this.foundations = new ArrayList<>();
    this.hand = new ArrayList<>();
    this.drawDeck = new ArrayList<>();
    this.gameStart = false;
    this.numDraw = 0;
  }

  @Override
  protected boolean validBuild(ValueCard move, ValueCard dest) {
    return oppositeColorCheck(move.possibleSuits.getSymbol(), dest.possibleSuits.getSymbol())
        && (move.possibleRank.getNumber() == dest.possibleRank.getNumber() - 1);
  }

  @Override
  protected boolean canMoveToEmpty(ValueCard card) {
    return card.possibleRank.getNumber() == 13;
  }

  @Override
  protected boolean moveSameSuit(List<ValueCard> move) {
    if (move.isEmpty()) {
      return false;
    }
    for (int size = 0; size < move.size() - 1; size++) {
      ValueCard lower = move.get(size);
      ValueCard upper = move.get(size + 1);
      if (!(oppositeColorCheck(lower.possibleSuits.getSymbol(), upper.possibleSuits.getSymbol())
          && (lower.possibleRank.getNumber() == upper.possibleRank.getNumber() - 1))) {
        return false;
      }
      //return false;
    }
    return true;
  }


}
