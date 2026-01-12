package klondike.mocks;

import klondike.model.hw02.Card;

/**
 * Mock card class so that I can use FirstMock model.
 */
public class MockCard implements Card {

  @Override
  public String toString() {
    return "5♢";
  }
}
