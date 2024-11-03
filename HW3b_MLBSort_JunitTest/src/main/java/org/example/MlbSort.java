package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MlbSort {
  // 使用 Map 來將不同區域的球隊分類存放
  private Map<String, List<Team>> teamsByArea = new HashMap<>();

  // 區域名稱 (final表示這個變數的值是不可改變的，它被初始化後就不能再修改。)
  private static final String AL_EAST = "AL EAST";
  private static final String AL_CENTRAL = "AL Central";
  private static final String AL_WEST = "AL West";
  private static final String NL_EAST = "NL East";
  private static final String NL_CENTRAL = "NL Central";
  private static final String NL_WEST = "NL West";

  // 為每個區域分別創建 List
  List<Team> alEastTeams = new ArrayList<>();
  List<Team> alCentralTeams = new ArrayList<>();
  List<Team> alWestTeams = new ArrayList<>();
  List<Team> nlEastTeams = new ArrayList<>();
  List<Team> nlCentralTeams = new ArrayList<>();
  List<Team> nlWestTeams = new ArrayList<>();

  public void load(String filename) {
    try(BufferedReader br = new BufferedReader(new FileReader(filename)))  {
      String line;
      String currentArea = ""; //當前區域名稱
      int lineNumber = 0;

      while((line = br.readLine()) != null) {
        lineNumber++;
        System.out.println("讀取第 " + lineNumber + " 行: " + line); // 調試輸出
        // 移除可能的 BOM 標記
        if (lineNumber == 1 && line.startsWith("\uFEFF")) {
          line = line.substring(1);
        }

        // 檢查是否是區域名稱
        if (line.startsWith(AL_EAST)) {
          currentArea = AL_EAST;
          continue;
        } else if (line.startsWith(AL_CENTRAL)) {
          currentArea = AL_CENTRAL;
          continue;
        } else if (line.startsWith(AL_WEST)) {
          currentArea = AL_WEST;
          continue;
        } else if (line.startsWith(NL_EAST)) {
          currentArea = NL_EAST;
          continue;
        } else if (line.startsWith(NL_CENTRAL)) {
          currentArea = NL_CENTRAL;
          continue;
        } else if (line.startsWith(NL_WEST)) {
          currentArea = NL_WEST;
          continue;
        }

        try {
          // 分割球隊資料
          String[] splitLine = line.split(",");
          String teamName = splitLine[0];
          String win = splitLine[1];
          String lose = splitLine[2];
          String pctRate = splitLine[3];
          Team team = new Team(teamName, win, lose, pctRate);

          //判斷當前區域名稱
          switch (currentArea) {
            case AL_EAST:
              alEastTeams.add(team);
              break;
            case AL_CENTRAL:
              alCentralTeams.add(team);
              break;
            case AL_WEST:
              alWestTeams.add(team);
              break;
            case NL_EAST:
              nlEastTeams.add(team);
              break;
            case NL_CENTRAL:
              nlCentralTeams.add(team);
              break;
            case NL_WEST:
              nlWestTeams.add(team);
              break;
            default:
              System.err.println("未知的區域: " + currentArea);
              break;
          }
        } catch (NumberFormatException e) {
          System.err.println("第 " + lineNumber + " 行格式無法解析成數字: " + line);
        }
      }

      // 將結果存入 Map 中
      teamsByArea.put(AL_EAST, alEastTeams);
      teamsByArea.put(AL_CENTRAL, alCentralTeams);
      teamsByArea.put(AL_WEST, alWestTeams);
      teamsByArea.put(NL_EAST, nlEastTeams);
      teamsByArea.put(NL_CENTRAL, nlCentralTeams);
      teamsByArea.put(NL_WEST, nlWestTeams);
    } catch (FileNotFoundException e) {
      System.err.println("無法找到文件: " + filename);
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("讀取文件時發生錯誤: " + filename);
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println("處理文件時發生意外錯誤: " + e.getMessage());
      e.printStackTrace();
    }
  }

  //取得 TeamsByArea來進行JUnit測試
  public Map<String, List<Team>> getTeamsByArea() {
    return teamsByArea;
  }

  //檢查用
  public void printTeamsByArea() {
    // 使用 Map.Entry 來遍歷 teamsByArea Map 的每個條目
    for(Map.Entry<String, List<Team>> entry: teamsByArea.entrySet()) {
      // 從每個條目中提取區域 (key) 和球隊列表 (value)
      String area = entry.getKey(); // 區域名稱
      List<Team> teams = entry.getValue(); // 球隊列表

      // 輸出區域名稱
      System.out.println("Area: " + area);

      // 使用傳統的 for 迴圈遍歷球隊列表，並輸出每支球隊
      for (Team team : teams) {
        System.out.println(team);
      }
    }
  }

}
