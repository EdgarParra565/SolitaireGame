package klondike.model.hw02;

/**
 * Enum class that represent the possible suit of a solitaire card.
 * Enum class holds one value, a string representing one of the four suits.
 */
public enum PossibleSuits {

  Clover("♣"),
  Diamond("♢"),
  Heart("♡"),
  Spade("♠");

  private final String symbol;

  PossibleSuits(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return symbol;
  }

}
