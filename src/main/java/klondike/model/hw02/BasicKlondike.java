package klondike.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A class that implements the KlondikeModel interface with ValueCard type objects.
 * BasicKlondike provides functions that would allow one to play a game of solitaire.
 * ValueCard type for the implementation.
 */
public class BasicKlondike implements KlondikeModel<ValueCard> {
  private List<ValueCard> deck;
  private List<List<ValueCard>> piles;
  private List<List<ValueCard>> foundations;
  private List<ValueCard> hand;
  private List<ValueCard> drawDeck;
  private boolean gameStart;
  private int numDraw;
  /*
  cannot run getFirst(),getLast(),removeFirst(), 
  or removeLast() even though intellij wants me to
  subList function List<E>
  subList(int fromIndex, int toIndex)
  Returns a view of the portion of this list
  between the specified fromIndex, inclusive, and toIndex, exclusive.
  stream() lambdas for maps
   */

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

  /**
   * Return a valid and complete deck of cards for a game of Klondike.
   * There is no restriction imposed on the ordering of these cards in the deck.
   * The validity of the deck is determined by the rules of the specific game in
   * the classes implementing this interface.  This method may be called as often
   * as desired.
   *
   * @return the deck of cards as a list
   */
  @Override
  public List<ValueCard> createNewDeck() {
    List<ValueCard> newDeck = new ArrayList<ValueCard>();
    for (PossibleRank rank : PossibleRank.values()) {
      for (PossibleSuits suit : PossibleSuits.values()) {
        newDeck.add(new ValueCard(suit, rank));
      }
    }
    this.deck = new ArrayList<>(newDeck);
    return newDeck;
  }

  /**
   * Deal a new game of Klondike.
   * The cards to be used and their order are specified by the the given deck,
   * unless the {@code shuffle} parameter indicates the order should be ignored.</p>
   *
   * <p>This method first verifies that the deck is valid. It deals cards in rows
   * (left-to-right, top-to-bottom) into the characteristic cascade shape
   * with the specified number of rows, followed by (at most) the specified number of
   * draw cards. When {@code shuffle} is {@code false}, the {@code deck} must be used in order and
   * the 0th card in {@code deck} is used as the first card dealt.
   * There will be as many foundation piles as there are aces in the deck.</p>
   *
   * <p>A valid deck must consist cards that can be grouped into equal-length,
   * consecutive runs of cards (each one starting at an Ace, and each of a single suit).</p>
   *
   * <p>This method should have no side effects other than configuring this model instance,
   * and should work for any valid arguments.</p>
   *
   * @param deck     the deck to be dealt
   * @param shuffle  if {@code false}, use the order as given by {@code deck},
   *                 otherwise use a randomly shuffled order
   * @param numPiles number of piles to be dealt
   * @param numDraw  maximum number of draw cards available at a time
   * @throws IllegalStateException    if the game has already started
   * @throws IllegalArgumentException if the deck is null or invalid,
   *                                  a full cascade cannot be dealt with the given sizes,
   *                                  or another input is invalid
   */
  @Override
  public void startGame(List<ValueCard> deck, boolean shuffle, int numPiles, int numDraw)
      throws IllegalArgumentException, IllegalStateException {
    if (gameStart) {
      throw new IllegalStateException("IllegalStateException");
    }
    if (!checkingValidDecks(deck)) {
      throw new IllegalArgumentException("Decks are invalid");
    }
    paramLessCheck(numPiles, 1);
    //paramLessCheck(numDraw, 0);
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
    for (int foundationCard = 0; foundationCard < (deck.size() / 13); foundationCard++) {
      foundations.add(new ArrayList<>());
    }
  }

  /**
   * Helper to add a card to the hand from the deck.
   * Only adds card if drawDeck is not empty and will only add however
   * many you are allowed via numDraw.
   */
  private void addCardHand() {
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
  private boolean checkingValidDecks(List<ValueCard> deck) {
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
  private void gameInProgress() {
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
  private void paramLessCheck(int param1, int param2) {
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
  private void paramLessEqualCheck(int param1, int param2) {
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
  private void emptyCheck(List<ValueCard> any) {
    if (any.isEmpty()) {
      throw new IllegalStateException("Illegal State Exception");
    }
  }

  /**
   * Moves the requested number of cards from the source pile to the destination pile,
   * if allowable by the rules of the game.
   *
   * @param srcPile  the 0-based index (from the left) of the pile to be moved
   * @param numCards how many cards to be moved from that pile
   * @param destPile the 0-based index (from the left) of the destination pile for the moved cards
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if either pile number is invalid, if the pile numbers are
   *                                  the same, or there are not enough cards to move from the
   *                                  srcPile to the destPile (i.e. the move is not physically
   *                                  possible)
   * @throws IllegalStateException    if the move is not allowable (i.e. the move is not logically
   *                                  possible)
   */
  @Override
  public void movePile(int srcPile, int numCards, int destPile)
      throws IllegalArgumentException, IllegalStateException {
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

  /**
   * Moves the topmost draw-card to the destination pile.  If no draw cards remain, reveal the next
   * available draw cards.
   *
   * @param destPile the 0-based index (from the left) of the destination pile for the card
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if destination pile number is invalid
   * @throws IllegalStateException    if there are no draw cards, or if the move is not allowable
   */
  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
    gameInProgress();
    emptyCheck(hand);
    paramLessCheck(destPile, 0);
    paramLessEqualCheck(piles.size(), destPile);
    ValueCard card = hand.get(0);
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

  /**
   * Moves the top card of the given pile to the requested foundation pile.
   *
   * @param srcPile        the 0-based index (from the left) of the pile to move a card
   * @param foundationPile the 0-based index (from the left) of the foundation pile to place card
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if either pile number is invalid
   * @throws IllegalStateException    if the source pile is empty or if the move is not allowable
   */
  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    gameInProgress();
    paramLessCheck(srcPile, 0);
    paramLessEqualCheck(piles.size(), srcPile);
    paramLessCheck(foundationPile, 0);
    paramLessEqualCheck(foundations.size(), foundationPile);
    List<ValueCard> src = piles.get(srcPile);
    //paramLessCheck(src.size(), foundationPile);
    emptyCheck(src);
    ValueCard card = src.get(src.size() - 1);
    List<ValueCard> foundation = foundations.get(foundationPile);
    if (!canPlaceFoundationCheck(card, foundation)) {
      throw new IllegalStateException("Card cannot be moved to foundation");
    }
    foundation.add(card);
    src.remove(src.size() - 1);
  }

  /**
   * Moves the topmost draw-card directly to a foundation pile.
   *
   * @param foundationPile the 0-based index (from the left) of the foundation pile to place card
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the foundation pile number is invalid
   * @throws IllegalStateException    if there are no draw cards or if the move is not allowable
   */
  @Override
  public void moveDrawToFoundation(int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
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
    /*
    paramLessCheck(foundationPile, 0);
    paramLessEqualCheck(foundations.size(), foundationPile);
     */
    if (foundationPile < 0 || foundationPile >= foundations.size()) {
      throw new IllegalArgumentException("Illegal Argument Exception");
    }
    //emptyCheck(hand);
    ValueCard card = hand.get(0);
    List<ValueCard> foundation = foundations.get(foundationPile);
    //moveDrawTOFoundation with Legal Moves 0/2 here
    if (!canPlaceFoundationCheck(card, foundation)) {
      throw new IllegalStateException("Foundation only card that can be moved");
    }
    foundation.add(card);
    hand.remove(0);
    if (!drawDeck.isEmpty()) {
      addCardHand();
    }
  }

  /**
   * Discards the topmost draw-card.
   *
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalStateException if move is not allowable
   */
  @Override
  public void discardDraw() throws IllegalStateException {
    gameInProgress();
    emptyCheck(hand);
    drawDeck.add(hand.remove(0));
    addCardHand();
  }

  /**
   * Returns the number of rows currently in the game.
   *
   * @return the height of the current table of cards
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumRows() throws IllegalStateException {
    gameInProgress();
    int numRows = 0;
    for (List<ValueCard> row : piles) {
      numRows = Math.max(numRows, row.size());
    }
    return numRows;
  }

  /**
   * Returns the number of piles for this game.
   *
   * @return the number of piles
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumPiles() throws IllegalStateException {
    gameInProgress();
    return piles.size();
  }

  /**
   * Returns the maximum number of visible cards in the draw pile.
   *
   * @return the number of visible cards in the draw pile
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumDraw() throws IllegalStateException {
    gameInProgress();
    return numDraw;
  }

  /**
   * Signal if the game is over or not.  A game is over if there are no more
   * possible moves to be made, or draw cards to be used (or discarded).
   *
   * @return true if game is over, false otherwise
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public boolean isGameOver() throws IllegalStateException {
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
    if (!availableMove()) {
      return true;
    }
    return hand.isEmpty() && drawDeck.isEmpty();
  }

  /**
   * Helper to check if any move can still be made in the game.
   *
   * @return boolean checking if a move can be made.
   */
  private boolean availableMove() {
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
  private boolean handDeckChecker() {
    if (!hand.isEmpty()) {
      for (int hands = 0; hands < hand.size(); hands++) {
        ValueCard drawCard = hand.get(hands);
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

  /**
   * Return the current score, which is the sum of the values of the topmost cards
   * in the foundation piles.
   *
   * @return the score
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getScore() throws IllegalStateException {
    gameInProgress();
    int score = 0;
    for (List<ValueCard> cards : foundations) {
      score += cards.size();
    }
    return score;
  }

  /**
   * Returns the number of cards in the specified pile.
   *
   * @param pileNum the 0-based index (from the left) of the pile
   * @return the number of cards in the specified pile
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if pile number is invalid
   */
  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    gameInProgress();
    paramLessCheck(pileNum, 0);
    paramLessEqualCheck(piles.size(), pileNum);
    return piles.get(pileNum).size();
  }

  /**
   * Returns the card at the specified coordinates, if it is visible.
   *
   * @param pileNum column of the desired card (0-indexed from the left)
   * @param card    row of the desired card (0-indexed from the top)
   * @return the card at the given position
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  @Override
  public ValueCard getCardAt(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
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

  /**
   * Returns the card at the top of the specified foundation pile.
   *
   * @param foundationPile 0-based index (from the left) of the foundation pile
   * @return the card at the given position, or null if no card is there
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the foundation pile number is invalid
   */
  @Override
  public ValueCard getCardAt(int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    gameInProgress();
    paramLessCheck(foundationPile, 0);
    paramLessEqualCheck(foundations.size(), foundationPile);
    List<ValueCard> foundation = foundations.get(foundationPile);
    if (foundation.isEmpty()) {
      return null;
    }
    return foundation.get(foundation.size() - 1);
  }

  /**
   * Returns whether the card at the specified coordinates is face-up or not.
   *
   * @param pileNum column of the desired card (0-indexed from the left)
   * @param card    row of the desired card (0-indexed from the top)
   * @return whether the card at the given position is face-up or not
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  @Override
  public boolean isCardVisible(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    gameInProgress();
    paramLessCheck(pileNum, 0);
    paramLessEqualCheck(piles.size(), pileNum);
    List<ValueCard> cards = piles.get(pileNum);
    paramLessCheck(card, 0);
    paramLessEqualCheck(cards.size(), card);
    return (card == cards.size() - 1);
  }

  /**
   * Returns the currently available draw cards.
   * There should be at most {@link KlondikeModel#getNumDraw} cards (the number
   * specified when the game started) -- there may be fewer, if cards have been removed.
   * If any user modifies the resulting list, there should be no effect on
   * the model.
   *
   * @return the ordered list of available draw cards
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public List<ValueCard> getDrawCards() throws IllegalStateException {
    gameInProgress();
    return new ArrayList<>(hand);
  }

  /**
   * Return the number of foundation piles in this game.
   *
   * @return the number of foundation piles
   * @throws IllegalStateException if the game hasn't been started yet
   */
  @Override
  public int getNumFoundations() throws IllegalStateException {
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
  private boolean canPlaceFoundationCheck(ValueCard card, List<ValueCard> foundation) {
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
  private boolean oppositeColorCheck(String suitA, String suitB) {
    boolean redA = suitA.equals(PossibleSuits.Diamond.getSymbol())
        || suitA.equals(PossibleSuits.Heart.getSymbol());
    boolean redB = suitB.equals(PossibleSuits.Diamond.getSymbol())
        || suitB.equals(PossibleSuits.Heart.getSymbol());
    return redA != redB;
  }
}
