package org.example;

import java.util.List;

public class Main {
  public static void main(String[] args) {

    MlbSort ms = new MlbSort();
    TeamSorter teamSorter = new TeamSorter();
    TeamPrinter teamPrinter = new TeamPrinter();

    ms.load("2023MLB_regularSeason.csv");
    ms.printTeamsByArea(); // 檢查用

    // 取得美聯與國聯各區第一名的球隊
    List<Team> al_firstPlaceTeams = teamSorter.getFirstPlaceTeams(ms.alEastTeams, ms.alCentralTeams, ms.alWestTeams);
    List<Team> nl_firstPlaceTeams = teamSorter.getFirstPlaceTeams(ms.nlEastTeams, ms.nlCentralTeams, ms.nlWestTeams);

    // 對各區第一名球隊進行排序
    al_firstPlaceTeams = teamSorter.sortTeamsByPct(al_firstPlaceTeams);
    nl_firstPlaceTeams = teamSorter.sortTeamsByPct(nl_firstPlaceTeams);
    // 取得並驗證美聯與國聯第一名排序結果
    teamSorter.validateSortedTeams(al_firstPlaceTeams, "美聯第一的勝率");
    teamSorter.validateSortedTeams(nl_firstPlaceTeams, "國聯第一的勝率");

    // 將排序後的美聯與國聯前三名
    String al_one = al_firstPlaceTeams.get(0).getTeamName();
    String al_two = al_firstPlaceTeams.get(1).getTeamName();
    String al_three = al_firstPlaceTeams.get(2).getTeamName();
    String nl_one = nl_firstPlaceTeams.get(0).getTeamName();
    String nl_two = nl_firstPlaceTeams.get(1).getTeamName();
    String nl_three = nl_firstPlaceTeams.get(2).getTeamName();

    // 取得美聯與國聯各區"非"第一名的球隊並進行排序
    List<Team> al_notFirstPlaceTeams = teamSorter.getNotFirstPlaceTeams(ms.alEastTeams, ms.alCentralTeams, ms.alWestTeams);
    List<Team> nl_notFirstPlaceTeams = teamSorter.getNotFirstPlaceTeams(ms.nlEastTeams, ms.nlCentralTeams, ms.nlWestTeams);

    al_notFirstPlaceTeams = teamSorter.sortTeamsByPct(al_notFirstPlaceTeams);
    nl_notFirstPlaceTeams = teamSorter.sortTeamsByPct(nl_notFirstPlaceTeams);
    teamSorter.validateSortedTeams(al_notFirstPlaceTeams, "美聯第四的勝率");
    teamSorter.validateSortedTeams(nl_notFirstPlaceTeams, "國聯第四的勝率");

    // 將排序後的美聯與國聯"非"第一名的球隊前三名放入變數
    String al_four = al_notFirstPlaceTeams.get(0).getTeamName();
    String al_five = al_notFirstPlaceTeams.get(1).getTeamName();
    String al_six = al_notFirstPlaceTeams.get(2).getTeamName();
    String nl_four = nl_notFirstPlaceTeams.get(0).getTeamName();
    String nl_five = nl_notFirstPlaceTeams.get(1).getTeamName();
    String nl_six = nl_notFirstPlaceTeams.get(2).getTeamName();

    // 印出結果
    teamPrinter.printResults(al_one, al_two, al_three, al_four, al_five, al_six, nl_one, nl_two, nl_three, nl_four, nl_five, nl_six);
  }

}
