package klondike.model.hw02;

import java.util.Objects;

/**
 * ValueCard represents a solitaire card object.
 * ValueCard implements the card class and utilizes the possibleRank and possibleSuit
 * enum classes to make a ValueCard object.
 */
public class ValueCard implements Card {
  protected final PossibleSuits possibleSuits;
  protected final PossibleRank possibleRank;

  /**
   * Constructs a ValueCard object.
   * Requires two parameters being the two enum class values.
   *
   * @param possibleSuits suit of the solitaire card.
   * @param possibleRank rank of the solitaire card.
   */
  public ValueCard(PossibleSuits possibleSuits, PossibleRank possibleRank) {
    this.possibleSuits = possibleSuits;
    this.possibleRank = possibleRank;
  }

  @Override
  public String toString() {
    return possibleRank.getRank() + possibleSuits.getSymbol();
  }

  @Override
  public int hashCode() {
    return Objects.hash(possibleSuits, possibleRank);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ValueCard valueCard)) {
      return false;
    }
    return possibleSuits == valueCard.possibleSuits && possibleRank == valueCard.possibleRank;
  }
}
