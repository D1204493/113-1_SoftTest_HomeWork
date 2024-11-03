package org.example;

public class Team {
  private String teamName;
  private String win;
  private String lose;
  private String pct;

  public Team(String teamName, String win, String lose, String pct) {
    this.teamName = teamName;
    this.win = win;
    this.lose = lose;
    this.pct = pct;
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }

  public String getWin() {
    return win;
  }

  public void setWin(String win) {
    this.win = win;
  }

  public String getLose() {
    return lose;
  }

  public void setLose(String lose) {
    this.lose = lose;
  }

  public String getPct() {
    return pct;
  }

  public void setPct(String pct) {
    this.pct = pct;
  }

  @Override
  public String toString() {
    return "Team{" +
            "teamName='" + teamName + '\'' +
            ", win='" + win + '\'' +
            ", lose='" + lose + '\'' +
            ", pct='" + pct + '\'' +
            '}';
  }
}

