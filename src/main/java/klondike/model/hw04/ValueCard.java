package klondike.model.hw04;

import java.util.Objects;
import klondike.model.hw02.PossibleRank;
import klondike.model.hw02.PossibleSuits;

/**
 * ValueCard represents a solitaire card object.
 * ValueCard implements the card class and utilizes the possibleRank and possibleSuit
 * enum classes to make a ValueCard object.
 * ValueCard for hw04 needed b/c issues.
 */
public class ValueCard extends klondike.model.hw02.ValueCard implements klondike.model.hw04.Card {
  protected final PossibleSuits possibleSuits;
  protected final PossibleRank possibleRank;

  /**
   * Constructs a ValueCard object.
   * Requires two parameters being the two enum class values.
   *
   * @param possibleSuits suit of the solitaire card.
   * @param possibleRank  rank of the solitaire card.
   */
  public ValueCard(PossibleSuits possibleSuits, PossibleRank possibleRank) {
    super(possibleSuits, possibleRank);
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
