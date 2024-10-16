package org.example;

public class WorldSeriesProfitResult {
  private final double maxProfit;
  private final int games;

  public WorldSeriesProfitResult(double maxProfit, int games) {
    this.maxProfit = maxProfit;
    this.games = games;
  }

  public double getMaxProfit() {
    return maxProfit;
  }

  public int getGames() {
    return games;
  }
}
