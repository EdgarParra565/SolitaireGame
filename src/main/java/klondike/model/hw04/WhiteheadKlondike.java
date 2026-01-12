package klondike.model.hw04;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.PossibleRank;

/**
 * A class that implements the KlondikeModel interface with ValueCard type objects.
 * WhiteheadKlondike provides functions that would allow one to play a game of solitaire.
 * ValueCard type for the implementation.
 * Changed isCardVisible, movePile, and moveDraw to conform to game using new methods
 * given in abstract class.
 */
public class WhiteheadKlondike extends AbstractKlondike {

  /**
   * Constructor that sets the fields.
   */
  public WhiteheadKlondike() {
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
    return (!oppositeColorCheck(move.possibleSuits.getSymbol(), dest.possibleSuits.getSymbol()))
        && (move.possibleRank.getNumber() == dest.possibleRank.getNumber() - 1);
  }

  @Override
  protected boolean canMoveToEmpty(ValueCard card) {
    return true;
  }

  @Override
  protected boolean moveSameSuit(List<ValueCard> move) {
    if (move.isEmpty()) {
      return false;
    }
    String suit = move.get(0).possibleSuits.getSymbol();
    for (ValueCard card : move) {
      if (!card.possibleSuits.getSymbol().equals(suit)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) {
    gameInProgress();
    paramLessCheck(pileNum, 0);
    paramLessEqualCheck(piles.size(), pileNum);
    List<ValueCard> cards = piles.get(pileNum);
    paramLessCheck(card, 0);
    paramLessEqualCheck(cards.size(), card);
    return true;
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    gameInProgress();
    paramLessCheck(srcPile, 0);
    paramLessEqualCheck(piles.size(), srcPile);
    paramLessCheck(destPile, 0);
    paramLessEqualCheck(piles.size(), destPile);
    if (srcPile == destPile) {
      throw new IllegalArgumentException("Illegal Argument Exception");
    }
    paramLessCheck(numCards, 1);
    List<ValueCard> src = piles.get(srcPile);
    List<ValueCard> dest = piles.get(destPile);
    paramLessCheck(src.size(), numCards);
    List<ValueCard> cardsMove = new ArrayList<>(src.subList(src.size() - numCards, src.size()));
    if (!moveSameSuit(cardsMove)) {
      throw new IllegalStateException("Illegal State Exception");
    }
    ValueCard srcCard = cardsMove.get(0);
    if (dest.isEmpty()) {
      if (!canMoveToEmpty(srcCard)) {
        throw new IllegalStateException("Only King can be moved");
      }
    } else {
      ValueCard destCard = dest.get(dest.size() - 1);
      if (!validBuild(srcCard, destCard)) {
        throw new IllegalStateException("Cannot place card");
      }
    }
    //List<ValueCard> card = new ArrayList<>(src.subList(src.size() - numCards, src.size()));
    dest.addAll(cardsMove);
    for (int value = 0; value < numCards; value++) {
      src.remove(src.size() - 1);
    }
  }

  @Override
  public void moveDraw(int destPile) {
    gameInProgress();
    emptyCheck(hand);
    paramLessCheck(destPile, 0);
    paramLessEqualCheck(piles.size(), destPile);
    ValueCard card = (ValueCard) hand.get(0);
    List<ValueCard> dest = piles.get(destPile);
    if (dest.isEmpty()) {
      if (!canMoveToEmpty(card)) {
        throw new IllegalStateException("King only card that can be moved");
      }
    } else {
      ValueCard topCard = dest.get(dest.size() - 1);
      if (!validBuild(card, topCard)) {
        throw new IllegalStateException("InvalidState");
      }
    }
    dest.add(card);
    hand.remove(0);
    addCardHand();
  }

  @Override
  protected boolean availableMove() {
    for (int srcPile = 0; srcPile < piles.size(); srcPile++) {
      List<ValueCard> src = piles.get(srcPile);
      if (src.isEmpty()) {
        continue;
      }
      for (int index = 0; index < src.size(); index++) {
        ValueCard card = src.get(index);
        //ValueCard card = src.get(src.size() - 1);
        for (List<ValueCard> foundation : foundations) {
          if (canPlaceFoundationCheck(card, foundation)) {
            /*
            List<ValueCard> cardMove = new ArrayList<>(src.subList(index, src.size()));
            if (moveSameSuit(cardMove)) {
            }
             */
            return true;
          }
        }
      }
    }
    for (int srcPile = 0; srcPile < piles.size(); srcPile++) {
      List<ValueCard> src = piles.get(srcPile);
      if (src.isEmpty()) {
        continue;
      }
      for (int index = 0; index < src.size(); index++) {
        ValueCard card = src.get(index);
        List<ValueCard> cardMove = new ArrayList<>(src.subList(index, src.size()));
        if (!moveSameSuit(cardMove)) {
          continue;
        }
        //ValueCard card = src.get(src.size() - 1);
        for (int destPile = 0; destPile < piles.size(); destPile++) {
          if (srcPile == destPile) {
            continue;
          }
          List<ValueCard> dest = piles.get(destPile);
          if (dest.isEmpty()) {
            if (canMoveToEmpty(card)) {
              return true;
            }
          } else {
            ValueCard destCard = dest.get(dest.size() - 1);
            if (validBuild(card, destCard)) {
              return true;
            }
          }
        }
      }
    }
    if (handDeckChecker()) {
      return true;
    }
    return false;
  }

  @Override
  protected boolean handDeckChecker() {
    if (!hand.isEmpty()) {
      for (int hands = 0; hands < hand.size(); hands++) {
        ValueCard drawCard = (ValueCard) hand.get(hands);
        for (List<ValueCard> foundation : foundations) {
          if (canPlaceFoundationCheck(drawCard, foundation)) {
            return true;
          }
        }
        for (List<ValueCard> pile : piles) {
          if (pile.isEmpty()) {
            if (canMoveToEmpty(drawCard)) {
              return true;
            }
          } else {
            ValueCard topCard = pile.get(pile.size() - 1);
            if (validBuild(drawCard, topCard)) {
              return true;
            }
          }
        }
      }
    }
    for (ValueCard drawCard : drawDeck) {
      for (List<ValueCard> foundation : foundations) {
        if (canPlaceFoundationCheck(drawCard, foundation)) {
          return true;
        }
      }
      for (List<ValueCard> pile : piles) {
        if (pile.isEmpty()) {
          if (canMoveToEmpty(drawCard)) {
            return true;
          }
        } else {
          ValueCard topCard = pile.get(pile.size() - 1);
          if (validBuild(drawCard, topCard)) {
            return true;
          }
        }
      }
    }
    return false;
  }


}
