package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeamSorterTest {
  private TeamSorter teamSorter;
  private List<Team> alEast, alCentral, alWest;

  @BeforeEach
  public void setUp() {
    teamSorter = new TeamSorter();
    // 建立 alEast 區球隊列表，Yankees 勝率最高
    alEast = Arrays.asList(
            new Team("Yankees", "97", "65", "0.599"),
            new Team("Red Sox", "81", "81", "0.500"),
            new Team("Blue Jays", "89", "73", "0.549"),
            new Team("Baltimore Orioles", "101", "61", "0.623"),
            new Team("Tampa Bay Rays", "99", "63", "0.611")
    );
    alCentral = Arrays.asList( //White Sox勝率較高，把White Sox放到第二筆資料
            new Team("Kansas City Royals", "92", "70", "0.568"),
            new Team("White Sox", "94", "68", "0.580"),
            new Team("Tigers", "85", "77", "0.525"),
            new Team("Minnesota Twins", "87", "75", "0.537"),
            new Team("Cleveland Guardians", "76", "86", "0.469")
    );
    alWest = Arrays.asList( //Astros勝率較高，把Astros放到第三筆資料
            new Team("Mariners", "84", "78", "0.519"),
            new Team("Athletics", "87", "75", "0.537"),
            new Team("Astros", "98", "64", "0.605"),
            new Team("Los Angeles Angels", "73", "89", "0.451"),
            new Team("Texas Rangers", "90", "72", "0.556")
    );
  }


  @Test
  void testGetFirstPlaceTeams() {
    List<Team> firstPlaceTeams = teamSorter.getFirstPlaceTeams(alEast, alCentral, alWest);
    // 驗證每個區域的第一名球隊
    assertEquals("Yankees", firstPlaceTeams.get(0).getTeamName(), "alEast 第一名應為 Yankees");
    assertEquals("White Sox", firstPlaceTeams.get(1).getTeamName(), "alCentral 第一名應為 White Sox");
    assertEquals("Astros", firstPlaceTeams.get(2).getTeamName(), "alWest 第一名應為 Astros");
  }

  @Test
  void testGetNotFirstPlaceTeams() {
    List<Team> notFirstPlaceTeams = teamSorter.getNotFirstPlaceTeams(alEast, alCentral, alWest);
    // 確認各區域的非第一名球隊正確被加入
    assertTrue(notFirstPlaceTeams.contains(alEast.get(1)), "alEast區第二名。alEast 應包含非第一名的球隊 Blue Jays");
    assertTrue(notFirstPlaceTeams.contains(alCentral.get(1)), "alCentral區第二名。alCentral 應包含非第一名的球隊 Indians");
    assertTrue(notFirstPlaceTeams.contains(alWest.get(1)), "alWest區第二名。alWest 應包含非第一名的球隊 Athletics");
  }

  @Test
  void testSortTeamsByPct() {
    List<Team> teamsToSort = Arrays.asList(
            new Team("A","89","73","0.549"),
            new Team("B","97","65","0.599"),
            new Team("C","81","81","0.500")
    );
    List<Team> sortedTeams = teamSorter.sortTeamsByPct(teamsToSort);
    // 驗證排序後的結果
    assertEquals("B", sortedTeams.get(0).getTeamName(), "排序後第一名應為 B");
    assertEquals("A", sortedTeams.get(1).getTeamName(), "排序後第二名應為 A");
    assertEquals("C", sortedTeams.get(2).getTeamName(), "排序後第三名應為 C");
  }

  @Test
  void testValidateSortedTeams() {
    List<Team> sortedTeams = Arrays.asList(
            new Team("Astros", "98", "64", "0.605"),
            new Team("Yankees", "97", "65", "0.599"),
            new Team("Indians", "92", "70", "0.568")
    );
    // 確認正確排序不會拋出 AssertionError
    assertDoesNotThrow(() -> teamSorter.validateSortedTeams(sortedTeams, "Test League"), "排序是正確的，不應該拋出異常");
  }
}