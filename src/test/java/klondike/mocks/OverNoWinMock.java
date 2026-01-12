package klondike.mocks;

/**
 * Game is over and it is not a win mock model.
 */
public class OverNoWinMock extends FirstMock {
  @Override
  public boolean isGameOver() {
    return true;
  }

  @Override
  public int getScore() {
    return 2;
  }
}
