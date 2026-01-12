package klondike.mocks;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Mock class used for checking quits.
 */
public class FirstMock implements KlondikeModel<Card> {
  public boolean ddCalled = false;
  public boolean mdCalled = false;
  public boolean mpCalled = false;
  public boolean mpfCalled = false;
  public boolean mdfCalled = false;
  boolean throwNew =  false;


  @Override
  public List<Card> createNewDeck() {
    List<Card> deck = new ArrayList<>();
    for (int i = 0; i < 52; i++) {
      deck.add(new MockCard());
    }
    return deck;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
      throws IllegalArgumentException, IllegalStateException {
    //
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
      throws IllegalArgumentException, IllegalStateException {
    if (throwNew) {
      throwNew = false;
      throw new IllegalStateException();
    }
    mpCalled = true;
  }

  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
    if (throwNew) {
      throwNew = false;
      throw new IllegalStateException();
    }
    mdCalled = true;
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    if (throwNew) {
      throwNew = false;
      throw new IllegalStateException();
    }
    mpfCalled = true;
  }

  @Override
  public void moveDrawToFoundation(int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    if (throwNew) {
      throwNew = false;
      throw new IllegalStateException();
    }
    mdfCalled = true;
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    if (throwNew) {
      throwNew = false;
      throw new IllegalStateException();
    }
    ddCalled = true;
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    return 1;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    return 7;
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    return 1;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    return false;
  }

  @Override
  public int getScore() throws IllegalStateException {
    return 1;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    return 1;
  }

  @Override
  public Card getCardAt(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public boolean isCardVisible(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    return true;
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    return List.of();
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    return 4;
  }
}
