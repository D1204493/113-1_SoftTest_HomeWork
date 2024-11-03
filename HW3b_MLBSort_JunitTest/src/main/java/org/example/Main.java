package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main {
  public static void main(String[] args) {
    String al_one, al_two, al_three, al_four, al_five, al_six; //美聯前6名
    String nl_one, nl_two, nl_three, nl_four, nl_five, nl_six; //國聯前6名

    MlbSort ms = new MlbSort();
    ms.load("2023MLB_regularSeason.csv");
    ms.printTeamsByArea(); //檢查用

    //美聯各區第一名
    Team alEastFirst = ms.alEastTeams.get(0); // 取得東區第一個 Team
    Team alCentralFirst = ms.alCentralTeams.get(0); // 取得中區第一個 Team
    Team alWestFirst = ms.alWestTeams.get(0); // 取得西區第一個 Team
    //國聯各區第一名
    Team nlEastFirst = ms.nlEastTeams.get(0); // 取得東區第一個 Team
    Team nlCentralFirst = ms.nlCentralTeams.get(0); // 取得中區第一個 Team
    Team nlWestFirst = ms.nlWestTeams.get(0); // 取得西區第一個 Team

    // 建立一個 ArrayList 並將美聯各區第一名球隊加入
    ArrayList<Team> al_firstPlaceTeams = new ArrayList<>();
    al_firstPlaceTeams.add(alEastFirst);
    al_firstPlaceTeams.add(alCentralFirst);
    al_firstPlaceTeams.add(alWestFirst);

    // 國聯各區第一名球隊加入 ArrayList
    ArrayList<Team> nl_firstPlaceTeams = new ArrayList<>();
    nl_firstPlaceTeams.add(nlEastFirst);
    nl_firstPlaceTeams.add(nlCentralFirst);
    nl_firstPlaceTeams.add(nlWestFirst);

    // 調用排序方法對美聯與國聯各區第一名之球隊進行排序
    al_firstPlaceTeams = sortTeamsByPct(al_firstPlaceTeams);
    nl_firstPlaceTeams = sortTeamsByPct(nl_firstPlaceTeams);
    assert (Double.parseDouble(al_firstPlaceTeams.get(0).getPct()) >= Double.parseDouble(al_firstPlaceTeams.get(1).getPct())): "美聯第一的勝率不可能 < 美聯第二的勝率";
    assert (Double.parseDouble(al_firstPlaceTeams.get(1).getPct()) >= Double.parseDouble(al_firstPlaceTeams.get(2).getPct())): "美聯第二的勝率不可能 < 美聯第三的勝率";
    assert (Double.parseDouble(al_firstPlaceTeams.get(0).getPct()) >= Double.parseDouble(al_firstPlaceTeams.get(2).getPct())): "美聯第一的勝率不可能 < 美聯第三的勝率";

    assert (Double.parseDouble(nl_firstPlaceTeams.get(0).getPct()) >= Double.parseDouble(nl_firstPlaceTeams.get(1).getPct())): "國聯第一的勝率不可能 < 國聯第二的勝率";
    assert (Double.parseDouble(nl_firstPlaceTeams.get(1).getPct()) >= Double.parseDouble(nl_firstPlaceTeams.get(2).getPct())): "國聯第二的勝率不可能 < 國聯第三的勝率";
    assert (Double.parseDouble(nl_firstPlaceTeams.get(0).getPct()) >= Double.parseDouble(nl_firstPlaceTeams.get(2).getPct())): "國聯第一的勝率不可能 < 國聯第三的勝率";

    // 將美聯各區第一名排序結果放入變數
    al_one = al_firstPlaceTeams.get(0).getTeamName();
    al_two = al_firstPlaceTeams.get(1).getTeamName();
    al_three = al_firstPlaceTeams.get(2).getTeamName();
    // 將國聯各區第一名排序結果放入變數
    nl_one = nl_firstPlaceTeams.get(0).getTeamName();
    nl_two = nl_firstPlaceTeams.get(1).getTeamName();
    nl_three = nl_firstPlaceTeams.get(2).getTeamName();


    //美聯"非"第一名之球隊 ArrayList
    ArrayList<Team> al_notFirstPlaceTeams = new ArrayList<>();
    //國聯"非"第一名之球隊 ArrayList
    ArrayList<Team> nl_notFirstPlaceTeams = new ArrayList<>();
    for(int i=1; i<5; i++){
      //美聯各區"非"第一名球隊加入 ArrayList
      al_notFirstPlaceTeams.add(ms.alEastTeams.get(i));
      al_notFirstPlaceTeams.add(ms.alCentralTeams.get(i));
      al_notFirstPlaceTeams.add(ms.alWestTeams.get(i));

      //國聯各區"非"第一名球隊加入 ArrayList
      nl_notFirstPlaceTeams.add(ms.nlEastTeams.get(i));
      nl_notFirstPlaceTeams.add(ms.nlCentralTeams.get(i));
      nl_notFirstPlaceTeams.add(ms.nlWestTeams.get(i));
    }
//    System.out.println("AL_notFirstPlaceTeams:"+al_notFirstPlaceTeams);
//    System.out.println("NL_notFirstPlaceTeams:"+nl_notFirstPlaceTeams);

    // 美聯"非"第一名之排序
    al_notFirstPlaceTeams = sortTeamsByPct(al_notFirstPlaceTeams);
//    System.out.println("排序AL_notFirstPlaceTeams:"+al_notFirstPlaceTeams);
    assert (Double.parseDouble(al_notFirstPlaceTeams.get(0).getPct()) >= Double.parseDouble(al_notFirstPlaceTeams.get(1).getPct())): "美聯第四的勝率不可能 < 美聯第五的勝率";
    assert (Double.parseDouble(al_notFirstPlaceTeams.get(1).getPct()) >= Double.parseDouble(al_notFirstPlaceTeams.get(2).getPct())): "美聯第五的勝率不可能 < 美聯第六的勝率";
    assert (Double.parseDouble(al_notFirstPlaceTeams.get(0).getPct()) >= Double.parseDouble(al_notFirstPlaceTeams.get(2).getPct())): "美聯第四的勝率不可能 < 美聯第六的勝率";

    // 抓取美聯"非"第一名隊伍排序後的前三名，並放入變數
    al_four = al_notFirstPlaceTeams.get(0).getTeamName();
    al_five = al_notFirstPlaceTeams.get(1).getTeamName();
    al_six = al_notFirstPlaceTeams.get(2).getTeamName();

    // 國聯"非"第一名之排序
    nl_notFirstPlaceTeams = sortTeamsByPct(nl_notFirstPlaceTeams);
//    System.out.println("排序NL_notFirstPlaceTeams:"+nl_notFirstPlaceTeams);
    assert (Double.parseDouble(nl_notFirstPlaceTeams.get(0).getPct()) >= Double.parseDouble(nl_notFirstPlaceTeams.get(1).getPct())): "國聯第四的勝率不可能 < 國聯第五的勝率";
    assert (Double.parseDouble(nl_notFirstPlaceTeams.get(1).getPct()) >= Double.parseDouble(nl_notFirstPlaceTeams.get(2).getPct())): "國聯第五的勝率不可能 < 國聯第六的勝率";
    assert (Double.parseDouble(nl_notFirstPlaceTeams.get(0).getPct()) >= Double.parseDouble(nl_notFirstPlaceTeams.get(2).getPct())): "國聯第四的勝率不可能 < 國聯第六的勝率";

    // 抓取國聯"非"第一名隊伍排序後的前三名，並放入變數
    nl_four = nl_notFirstPlaceTeams.get(0).getTeamName();
    nl_five = nl_notFirstPlaceTeams.get(1).getTeamName();
    nl_six = nl_notFirstPlaceTeams.get(2).getTeamName();


    // 印出結果
    System.out.printf("(AMERICAN LEAGUE)\n");
    System.out.printf("%-5s %d -----\n", al_six, 6);
    System.out.printf("%-5s %d ----- ? -----\n", al_three, 3);
    System.out.printf("        %-5s %d ----- ?\n", al_two, 2);
    System.out.printf("%-5s %d -----\n", al_five, 5);
    System.out.printf("%-5s %d ----- ? -----\n", al_four, 4);
    System.out.printf("        %-5s %d ----- ? ----- ?\n", al_one, 1);
    System.out.printf("                               ---- ?\n");

    System.out.printf("(NATIONAL LEAGUE)\n");
    System.out.printf("%-5s %d ----- ? ----- ? ----- ?\n", nl_six, 6);
    System.out.printf("%-5s %d -----\n", nl_three, 3);
    System.out.printf("        %-5s %d ----\n", nl_two, 2);
    System.out.printf("%-5s %d ----- ? ----- ?\n", nl_five, 5);
    System.out.printf("%-5s %d -----\n", nl_four, 4);
    System.out.printf("        %-5s %d -----\n", nl_one, 1);
  }


  //排序的方法
  public static ArrayList<Team> sortTeamsByPct(ArrayList<Team> teams) {
    Collections.sort(teams, new Comparator<Team>() {
      @Override
      public int compare(Team o1, Team o2) {
        // 將 String 型態的 pct 轉換為 double 來進行比較
        // 由高到低排序
        int pctCompare = Double.compare(Double.parseDouble(o2.getPct()), Double.parseDouble(o1.getPct()));
        // -1 表示第一個值小於第二個值
        // 0 表示兩者相等
        // 1 表示第一個值大於第二個值

        // 如果 pct 相同，則按隊伍名稱的字母順序排序
        if(pctCompare == 0) {
          return o2.getTeamName().compareTo(o1.getTeamName()); // 依照英文字母順序從大到小排序
        }
        // 否則按勝率排序
        return pctCompare;
      }
    });
    return teams; // 返回排序後的球隊列表
  }

}