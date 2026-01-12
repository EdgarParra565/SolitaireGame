package klondike.model.hw04;

/**
 * Class that instantiates the klondike game model depending on which model was chosen.
 * Class is final so that the class cannot be extended.
 */
public final class KlondikeCreator {
  /**
   * Enum class represents the two different types of klondike game models.
   * Enum values store a string within that can be used to initialize new model objects.
   */
  public enum GameType {
    BASIC,
    WHITEHEAD,
  }

  /**
   * Method that deciphers which game is going to be instantiated via the PossibleGameType enum.
   *
   * @param gameType enum given that guarantees method is given only one of two options.
   * @return an instantiated klondike game model of the type chosen.
   */
  public static KlondikeModel<ValueCard> create(GameType gameType) {
    return switch (gameType) {
      case BASIC -> new BasicKlondike();
      case WHITEHEAD -> new WhiteheadKlondike();
    };
  }
}
