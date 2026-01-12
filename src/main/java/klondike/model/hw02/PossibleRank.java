package klondike.model.hw02;

/**
 * Enum class that represents the rank of a solitaire card.
 * Enum class holds two values, a string and integer representing the corresponding card value.
 */
public enum PossibleRank {

  Ace("A", 1),
  Two("2", 2),
  Three("3", 3),
  Four("4", 4),
  Five("5", 5),
  Six("6", 6),
  Seven("7", 7),
  Eight("8", 8),
  Nine("9", 9),
  Ten("10", 10),
  Jack("J", 11),
  Queen("Q", 12),
  King("K", 13);

  private final String rank;
  private final int number;

  PossibleRank(String rank, int number) {
    this.rank = rank;
    this.number = number;
  }

  public String getRank() {
    return rank;
  }

  public int getNumber() {
    return number;
  }

}
