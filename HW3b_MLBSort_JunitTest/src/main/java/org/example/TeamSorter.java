package org.example;

import java.util.ArrayList;
import java.util.List;

public class TeamSorter {
  // 取得各區第一名球隊
  public List<Team> getFirstPlaceTeams(List<Team> east, List<Team> central, List<Team> west) {
    List<Team> firstPlaceTeams = new ArrayList<>();
    firstPlaceTeams.add(east.get(0));
    firstPlaceTeams.add(central.get(0));
    firstPlaceTeams.add(west.get(0));
    return firstPlaceTeams;
  }

  // 取得各區非第一名的球隊
  public List<Team> getNotFirstPlaceTeams(List<Team> east, List<Team> central, List<Team> west) {
    List<Team> notFirstPlaceTeams = new ArrayList<>();
    for (int i = 1; i < 5; i++) {
      notFirstPlaceTeams.add(east.get(i));
      notFirstPlaceTeams.add(central.get(i));
      notFirstPlaceTeams.add(west.get(i));
    }
    return notFirstPlaceTeams;
  }

  // 將球隊按勝率排序
  public List<Team> sortTeamsByPct(List<Team> teams) {
    teams.sort((o1, o2) -> {
      int pctCompare = Double.compare(Double.parseDouble(o2.getPct()), Double.parseDouble(o1.getPct()));
      return (pctCompare != 0) ? pctCompare : o2.getTeamName().compareTo(o1.getTeamName());
    });
    return teams;
  }

  // 確認排序是否正確
  public void validateSortedTeams(List<Team> teams, String leagueName) {
    assert (Double.parseDouble(teams.get(0).getPct()) >= Double.parseDouble(teams.get(1).getPct())) : leagueName + "第一的勝率不可能 < 第二的勝率";
    assert (Double.parseDouble(teams.get(1).getPct()) >= Double.parseDouble(teams.get(2).getPct())) : leagueName + "第二的勝率不可能 < 第三的勝率";
    assert (Double.parseDouble(teams.get(0).getPct()) >= Double.parseDouble(teams.get(2).getPct())) : leagueName + "第一的勝率不可能 < 第三的勝率";
  }

}
