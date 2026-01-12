package klondike.model.hw04;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import klondike.model.hw02.PossibleRank;
import klondike.model.hw02.PossibleSuits;

/**
 * Abstract class for klondike game model types.
 * Abstracted for assigment and saves code for model types via inheritance.
 */
public abstract class AbstractKlondike implements klondike.model.hw04.KlondikeModel<ValueCard> {

  protected List<ValueCard> deck;
  protected List<List<ValueCard>> piles;
  protected List<List<ValueCard>> foundations;
  protected List<ValueCard> hand;
  protected List<ValueCard> drawDeck;
  protected boolean gameStart;
  protected int numDraw;

  /**
   * Constructor to initialize the implemented KlondikeModel methods.
   */
  protected AbstractKlondike() {
    this.deck = new ArrayList<>();
    this.piles = new ArrayList<>();
    this.foundations = new ArrayList<>();
    this.hand = new ArrayList<>();
    this.drawDeck = new ArrayList<>();
    this.gameStart = false;
    this.numDraw = 0;
  }

  /**
   * Method that checks if card can be placed onto another pile.
   *
   * @param move card being moved.
   * @param dest destination to where card is being moved.
   * @return boolean if the move is allowed.
   */
  protected abstract boolean validBuild(ValueCard move, ValueCard dest);

  /**
   * Method that determines if a card can be placed on an empty slot.
   *
   * @param card card being moved.
   * @return boolean if the move is allowed.
   */
  protected abstract boolean canMoveToEmpty(ValueCard card);

  /**
   * Method that checks if cards are in the same suit.
   *
   * @param move list of cards being moved.
   * @return boolean if all cards are the same.
   */
  protected abstract boolean moveSameSuit(List<ValueCard> move);

  @Override
  public List<ValueCard> createNewDeck() {
    List<ValueCard> newDeck = new ArrayList<>();
    for (PossibleRank rank : PossibleRank.values()) {
      for (PossibleSuits suit : PossibleSuits.values()) {
        newDeck.add((ValueCard) new ValueCard(suit, rank));
      }
    }
    this.deck = new ArrayList<>(newDeck);
    return newDeck;
  }

  @Override
  public void startGame(List<ValueCard> deck, boolean shuffle, int numPiles, int numDraw) {
    if (gameStart) {
      throw new IllegalStateException("IllegalStateException");
    }
    if (!checkingValidDecks(deck)) {
      throw new IllegalArgumentException("Decks are invalid");
    }
    paramLessCheck(numPiles, 1);
    paramLessCheck(numDraw, 1);
    paramLessCheck(deck.size(), numDraw);
    int neededCards = (numPiles * (numPiles + 1)) / 2;
    paramLessCheck(deck.size(), neededCards);
    gameStart = true;
    List<ValueCard> change = new ArrayList<>(deck);
    if (shuffle) {
      Collections.shuffle(change);
    }
    this.numDraw = numDraw;
    this.piles.clear();
    this.foundations.clear();
    this.hand.clear();
    this.drawDeck.clear();
    for (int pile = 0; pile < numPiles; pile++) {
      piles.add(new ArrayList<>());
    }
    for (int rows = 0; rows < numPiles; rows++) {
      for (int columns = rows; columns < numPiles; columns++) {
        piles.get(columns).add(change.remove(0));
      }
    }
    drawDeck.addAll(change);
    addCardHand();
    Set<PossibleSuits> possibleSuits = new HashSet<>();
    for (ValueCard card : deck) {
      if (card.possibleRank == PossibleRank.Ace) {
        possibleSuits.add(card.possibleSuits);
      }
    }
    for (int foundationCard = 0; foundationCard < possibleSuits.size(); foundationCard++) {
      foundations.add(new ArrayList<>());
    }
  }

  /**
   * Helper to add a card to the hand from the deck.
   * Only adds card if drawDeck is not empty and will only add however
   * many you are allowed via numDraw.
   */
  protected void addCardHand() {
    while (hand.size() < numDraw && !drawDeck.isEmpty()) {
      hand.add(drawDeck.remove(0));
    }
  }

  /**
   * Checks the deck for complete suits.
   * Returns false if suit is incomplete.
   *
   * @param deck deck being checked if it can be used in klondike.
   * @return boolean if the deck can be used.
   */
  protected boolean checkingValidDecks(List<ValueCard> deck) {
    if (deck == null || deck.isEmpty()) {
      return false;
    }
    for (ValueCard card : deck) {
      if (card == null) {
        return false;
      }
    }
    boolean ace = deck.stream().anyMatch(card -> card.possibleRank == PossibleRank.Ace);
    if (!ace) {
      return false;
    }
    Map<PossibleSuits, Set<Integer>> map1 = new HashMap<>();
    for (ValueCard card : deck) {
      map1.computeIfAbsent(card.possibleSuits, k -> new HashSet<>())
          .add(card.possibleRank.getNumber());
    }
    for (Set<Integer> set : map1.values()) {
      int min = set.stream().mapToInt(i -> i).min().orElse(1);
      if (min != 1) {
        return false;
      }
      int max = set.stream().mapToInt(i -> i).max().orElse(13);
      for (int gapCheck = min; gapCheck <= max; gapCheck++) {
        if (!set.contains(gapCheck)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Helper method that checks game state.
   * Throws exception if start game has not yet been run.
   */
  protected void gameInProgress() {
    if (!gameStart) {
      throw new IllegalStateException("Game has not started");
    }
  }

  /**
   * Helper that checks Illegal Argument Exception using less than.
   * Throws Argument if parameter one is less than param two and lets
   * user know that parameters are invalid.
   *
   * @param param1 integer that has to be less than param2.
   * @param param2 integer that had to be greater than param1.
   */
  protected void paramLessCheck(int param1, int param2) {
    if (param1 < param2) {
      throw new IllegalArgumentException("Illegal Argument Exception");
    }
  }

  /**
   * Helper that checks Illegal Argument Exception using less than or equal to.
   * Throws Argument if parameter one is less or equal to param two and lets
   * user know that parameters are invalid.
   *
   * @param param1 integer that has to be less than param2.
   * @param param2 integer that had to be greater than param1.
   */
  protected void paramLessEqualCheck(int param1, int param2) {
    if (param1 <= param2) {
      throw new IllegalArgumentException("Illegal Argument Exception");
    }
  }

  /**
   * Helper that checks Illegal Argument Exception using .isEmpty().
   * Throws State Exception if List is empty when helper is called.
   *
   * @param any any arrayList field checking if it has valueCards.
   */
  protected void emptyCheck(List<?> any) {
    if (any.isEmpty()) {
      throw new IllegalStateException("Illegal State Exception");
    }
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
    ValueCard srcCard = src.get(src.size() - numCards);
    if (dest.isEmpty()) {
      if (srcCard.possibleRank != PossibleRank.King) {
        throw new IllegalStateException("Only King can be moved");
      }
    } else {
      ValueCard destCard = dest.get(dest.size() - 1);
      if (srcCard.possibleRank.getNumber() != destCard.possibleRank.getNumber() - 1
          || !oppositeColorCheck(srcCard.possibleSuits.getSymbol(),
          destCard.possibleSuits.getSymbol())) {
        throw new IllegalStateException("Cannot place card");
      }
    }
    List<ValueCard> card = new ArrayList<>(src.subList(src.size() - numCards, src.size()));
    dest.addAll(card);
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
      if (card.possibleRank != PossibleRank.King) {
        throw new IllegalStateException("King only card that can be moved");
      }
    } else {
      ValueCard topCard = dest.get(dest.size() - 1);
      if (card.possibleRank.getNumber() != topCard.possibleRank.getNumber() - 1
          || !oppositeColorCheck(card.possibleSuits.getSymbol(),
          topCard.possibleSuits.getSymbol())) {
        throw new IllegalStateException("InvalidState");
      }
    }
    dest.add(card);
    hand.remove(0);
    addCardHand();
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
    gameInProgress();
    paramLessCheck(srcPile, 0);
    paramLessEqualCheck(piles.size(), srcPile);
    paramLessCheck(foundationPile, 0);
    paramLessEqualCheck(foundations.size(), foundationPile);
    List<ValueCard> src = piles.get(srcPile);
    emptyCheck(src);
    ValueCard card = (ValueCard) src.get(src.size() - 1);
    List<ValueCard> foundation = foundations.get(foundationPile);
    if (!canPlaceFoundationCheck(card, foundation)) {
      throw new IllegalStateException("Card cannot be moved to foundation");
    }
    foundation.add(card);
    src.remove(src.size() - 1);
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
    gameInProgress();
    if (hand.isEmpty() && drawDeck.isEmpty()) {
      throw new IllegalStateException("IllegalStateException");
    }
    if (hand.isEmpty()) {
      addCardHand();
      if (hand.isEmpty()) {
        throw new IllegalStateException("IllegalStateException");
      }
    }
    if (foundationPile < 0 || foundationPile >= foundations.size()) {
      throw new IllegalArgumentException("Illegal Argument Exception");
    }
    ValueCard card = (ValueCard) hand.get(0);
    List<ValueCard> foundation = foundations.get(foundationPile);
    if (!canPlaceFoundationCheck(card, foundation)) {
      throw new IllegalStateException("Foundation only card that can be moved");
    }
    foundation.add(card);
    hand.remove(0);
    if (!drawDeck.isEmpty()) {
      addCardHand();
    }
  }

  @Override
  public void discardDraw() {
    gameInProgress();
    emptyCheck(hand);
    drawDeck.add((ValueCard) hand.remove(0));
    addCardHand();
  }

  @Override
  public int getNumRows() {
    gameInProgress();
    int numRows = 0;
    for (List<ValueCard> row : piles) {
      numRows = Math.max(numRows, row.size());
    }
    return numRows;
  }

  @Override
  public int getNumPiles() {
    gameInProgress();
    return piles.size();
  }

  @Override
  public int getNumDraw() {
    gameInProgress();
    return numDraw;
  }

  @Override
  public boolean isGameOver() {
    gameInProgress();
    boolean gameComplete = true;
    for (List<ValueCard> foundation : foundations) {
      if (foundation.size() != 13) {
        gameComplete = false;
        break;
      }
    }
    if (gameComplete) {
      return true;
    }
    if (availableMove()) {
      return false;
    }
    return true;
  }

  /**
   * Helper to check if any move can still be made in the game.
   *
   * @return boolean checking if a move can be made.
   */
  protected boolean availableMove() {
    for (int srcPile = 0; srcPile < piles.size(); srcPile++) {
      List<ValueCard> src = piles.get(srcPile);
      if (src.isEmpty()) {
        continue;
      }
      ValueCard card = src.get(src.size() - 1);
      for (List<ValueCard> foundation : foundations) {
        if (canPlaceFoundationCheck(card, foundation)) {
          return true;
        }
      }
    }
    for (int srcPile = 0; srcPile < piles.size(); srcPile++) {
      List<ValueCard> src = piles.get(srcPile);
      if (src.isEmpty()) {
        continue;
      }
      ValueCard card = src.get(src.size() - 1);
      for (int destPile = 0; destPile < piles.size(); destPile++) {
        if (srcPile == destPile) {
          continue;
        }
        List<ValueCard> dest = piles.get(destPile);
        if (dest.isEmpty()) {
          if (card.possibleRank == PossibleRank.King) {
            return true;
          }
        } else {
          ValueCard destCard = dest.get(dest.size() - 1);
          if (card.possibleRank.getNumber() == destCard.possibleRank.getNumber() - 1
              && oppositeColorCheck(card.possibleSuits.getSymbol(),
              destCard.possibleSuits.getSymbol())) {
            return true;
          }
        }
      }
    }
    if (handDeckChecker()) {
      return true;
    }
    return false;
  }

  /**
   * Helper for available move. Handles hand and deck checks for available moves.
   *
   * @return a boolean if an available move can be made from the deck or hand.
   */
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
            if (drawCard.possibleRank == PossibleRank.King) {
              return true;
            }
          } else {
            ValueCard topCard = pile.get(pile.size() - 1);
            if (drawCard.possibleRank.getNumber() == topCard.possibleRank.getNumber() - 1
                && oppositeColorCheck(drawCard.possibleSuits.getSymbol(),
                topCard.possibleSuits.getSymbol())) {
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
          if (drawCard.possibleRank == PossibleRank.King) {
            return true;
          }
        } else {
          ValueCard topCard = pile.get(pile.size() - 1);
          if (drawCard.possibleRank.getNumber() == topCard.possibleRank.getNumber() - 1
              && oppositeColorCheck(drawCard.possibleSuits.getSymbol(),
              topCard.possibleSuits.getSymbol())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  @Override
  public int getScore() {
    gameInProgress();
    int score = 0;
    for (List<ValueCard> cards : foundations) {
      score += cards.size();
    }
    return score;
  }

  @Override
  public int getPileHeight(int pileNum) {
    gameInProgress();
    paramLessCheck(pileNum, 0);
    paramLessEqualCheck(piles.size(), pileNum);
    return piles.get(pileNum).size();
  }

  @Override
  public ValueCard getCardAt(int pileNum, int card) {
    gameInProgress();
    paramLessCheck(pileNum, 0);
    paramLessEqualCheck(piles.size(), pileNum);
    List<ValueCard> pile = piles.get(pileNum);
    paramLessCheck(card, 0);
    paramLessEqualCheck(pile.size(), card);
    if (!isCardVisible(pileNum, card)) {
      throw new IllegalArgumentException("IllegalArgumentException");
    }
    return pile.get(card);
  }

  @Override
  public ValueCard getCardAt(int foundationPile) {
    gameInProgress();
    paramLessCheck(foundationPile, 0);
    paramLessEqualCheck(foundations.size(), foundationPile);
    List<ValueCard> foundation = foundations.get(foundationPile);
    if (foundation.isEmpty()) {
      return null;
    }
    return foundation.get(foundation.size() - 1);
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) {
    gameInProgress();
    paramLessCheck(pileNum, 0);
    paramLessEqualCheck(piles.size(), pileNum);
    List<ValueCard> cards = piles.get(pileNum);
    paramLessCheck(card, 0);
    paramLessEqualCheck(cards.size(), card);
    return (card == cards.size() - 1);
  }

  @Override
  public List<ValueCard> getDrawCards() {
    gameInProgress();
    return new ArrayList<>(hand);
  }

  @Override
  public int getNumFoundations() {
    gameInProgress();
    return foundations.size();
  }

  /**
   * Helper method used to check if a card can be placed next on the foundation.
   * Checks to see if card is ace and same suit.
   *
   * @param card       card being used.
   * @param foundation foundation pile being checked.
   * @return boolean if card can be placed or not.
   */
  protected boolean canPlaceFoundationCheck(ValueCard card,
                                            List<ValueCard> foundation) {
    if (foundation.isEmpty()) {
      return card.possibleRank == PossibleRank.Ace;
    }
    ValueCard topCard = foundation.get(foundation.size() - 1);
    return card.possibleSuits.getSymbol().equals(topCard.possibleSuits.getSymbol())
        && card.possibleRank.getNumber() == topCard.possibleRank.getNumber() + 1;
  }

  /**
   * Helper method used to check if a card can be placed next on pile.
   * Checks to see if card is red or black.
   *
   * @param suitA string symbol of card 1 (going to be card being placed on pile).
   * @param suitB string symbol of card 2 (going to be pile card).
   * @return boolean if card is or is not same color.
   */
  protected boolean oppositeColorCheck(String suitA, String suitB) {
    boolean redA = suitA.equals(PossibleSuits.Diamond.getSymbol())
        || suitA.equals(PossibleSuits.Heart.getSymbol());
    boolean redB = suitB.equals(PossibleSuits.Diamond.getSymbol())
        || suitB.equals(PossibleSuits.Heart.getSymbol());
    return redA != redB;
  }
}
