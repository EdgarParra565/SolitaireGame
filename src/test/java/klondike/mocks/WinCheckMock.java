package klondike.mocks;

/**
 * Always winning mock model.
 */
public class WinCheckMock extends FirstMock {
  @Override
  public boolean isGameOver() {
    return true;
  }

  @Override
  public int getScore() {
    return 52;
  }
}
