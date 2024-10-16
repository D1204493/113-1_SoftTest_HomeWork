package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldGame {
  // 設定 Logger
  private static final Logger logger = Logger.getLogger(WorldGame.class.getName());

  public static void main(String[] args) {
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      // 讀取球場資訊 JSON 檔案
      File teamStadiumFile = new File("teamstadium.json");
      File mlbInfoFile = new File("mlb_info_2024.json");

      // 檢查檔案是否存在
      if (!mlbInfoFile.exists() || !teamStadiumFile.exists()) {
        logger.log(Level.SEVERE, "JSON 檔案不存在。請檢查檔案路徑。");
        return;
      }

      // 讀取球場資訊
      List<MLBProfit.TeamStadium> stadiumInfos = objectMapper.readValue(
              teamStadiumFile,
              new TypeReference<List<MLBProfit.TeamStadium>>() {}
      );

      // 讀取球隊資訊
      MLBProfit.MLBTeams teamData = objectMapper.readValue(mlbInfoFile, MLBProfit.MLBTeams.class);

      List<MLBProfit.Team> allALTeams = new ArrayList<>(teamData.AL_Leaders);
      allALTeams.addAll(teamData.AL_Wild_Card);
      List<MLBProfit.Team> allNLTeams = new ArrayList<>(teamData.NL_Leaders);
      allNLTeams.addAll(teamData.NL_Wild_Card);

      // 建立 AL 和 NL 對應的 Map，將球隊名稱對應到球場資訊
      Map<String, MLBTeamWithInfo> alMap = new HashMap<>();
      Map<String, MLBTeamWithInfo> nlMap = new HashMap<>();

      // 將 AL 球隊及其球場資訊放入 alMap
      populateTeamStadiumMap(alMap, teamData.AL_Leaders, stadiumInfos);
      populateTeamStadiumMap(alMap, teamData.AL_Wild_Card, stadiumInfos);
      // 將 NL 球隊及其球場資訊放入 nlMap
      populateTeamStadiumMap(nlMap, teamData.NL_Leaders, stadiumInfos);
      populateTeamStadiumMap(nlMap, teamData.NL_Wild_Card, stadiumInfos);

      // 印出 AL 球隊和球場對應
      System.out.println("\nAL Teams and Stadiums:");
      printTeamStadiumMap(alMap);
      // 印出 NL 球隊和球場對應
      System.out.println("\nNL Teams and Stadiums:");
      printTeamStadiumMap(nlMap);


      // 將 AL 或 NL 設定為變數
      String baseRegion = "AL"; // 可替換為 "NL" 來進行其他區域比較
      String targetRegion = "NL"; // 可替換為 "AL" 來進行其他區域比較
      List<MLBProfit.Team> baseTeams = getTeamsByRegion(baseRegion, allALTeams, allNLTeams);
      List<MLBProfit.Team> targetTeams = getTeamsByRegion(targetRegion, allALTeams, allNLTeams);

      // 選擇一個基準隊伍
      int baseTeamRank = 6; // 隊伍排名
      // 假設我們選擇基準隊伍來自 AL 區，並對比 NL 區的隊伍
      Optional<MLBProfit.TeamStadium> bestCandidate = findBestCandidate(baseTeamRank, baseTeams, targetTeams, stadiumInfos);
      // 輸出結果
      if (bestCandidate.isPresent()) {
        MLBProfit.TeamStadium bestTeam = bestCandidate.get();
        System.out.println("最佳 " + targetRegion + " 球隊: " + bestTeam.team + ", Stadium: " + bestTeam.stadium +
                ", Seats: " + bestTeam.seats + ", World Series Attendance Rate: " + bestTeam.worldSeriesAttendanceRate);
      } else {
        System.out.println("未找到符合條件的 " + targetRegion + " 球隊。");
      }


    } catch (IOException e) {
      logger.log(Level.SEVERE, "讀取 JSON 時發生錯誤", e);
    }
  }

  // 定義新類別來存放球隊資訊和球場資訊
  static class MLBTeamWithInfo {
    MLBProfit.Team team;
    MLBProfit.TeamStadium stadium;

    MLBTeamWithInfo(MLBProfit.Team team, MLBProfit.TeamStadium stadium) {
      this.team = team;
      this.stadium = stadium;
    }
  }

  // 填充 Map，根據球隊名稱將球隊和球場資訊對應起來
  private static void populateTeamStadiumMap(Map<String, MLBTeamWithInfo> map, List<MLBProfit.Team> teams, List<MLBProfit.TeamStadium> stadiumInfos) {
    for (MLBProfit.Team team : teams) {
      // 將 team 名稱中的聯盟標記去掉（" - E", " - C", " - W"）
      String cleanedTeamName = team.team.split(" - ")[0]; // 切除" - "之後的聯盟標記
      boolean found = false;
      for (MLBProfit.TeamStadium stadium : stadiumInfos) {
        // 檢查 cleanedTeamName 是否和球場資訊的球隊名稱匹配
        if (cleanedTeamName.equals(stadium.team)) {
          map.put(team.team, new MLBTeamWithInfo(team, stadium));  // 儲存球隊與球場資訊
          found = true;
          break;
        }
      }
      // 如果沒有找到對應的球場資訊，記錄錯誤信息
      if (!found) {
        System.out.println("未找到球隊: " + cleanedTeamName + " 對應的球場資訊");
      }
    }
  }

  // 印出 Map 中的球隊及對應的球場資訊
  private static void printTeamStadiumMap(Map<String, MLBTeamWithInfo> map) {
    for (Map.Entry<String, MLBTeamWithInfo> entry : map.entrySet()) {
      MLBTeamWithInfo teamWithInfo = entry.getValue();
      MLBProfit.Team team = teamWithInfo.team;
      MLBProfit.TeamStadium stadium = teamWithInfo.stadium;

      System.out.println("Team: " + team.team +
              ", Score Rate: " + team.scoreRate +
              ", Rank: " + team.Rank +
              ", Stadium: " + stadium.stadium +
              ", Seats: " + stadium.seats +
              ", Playoff Attendance Rate: " + stadium.playoffAttendanceRate +
              ", World Series Attendance Rate: " + stadium.worldSeriesAttendanceRate);
    }
  }


  // 方法：查找另一區域中 scoreRate 比指定隊伍還要小的隊伍，並找到 Seats * World Series Attendance Rate 最高者
  private static Optional<MLBProfit.TeamStadium> findBestCandidate(int baseTeamRank, List<MLBProfit.Team> baseTeams, List<MLBProfit.Team> targetTeams, List<MLBProfit.TeamStadium> targetStadiumInfos) {
    // Step 1: 找出指定的 base 隊伍和它的 scoreRate
    double baseScoreRate = 0.0;
    boolean teamFound = false;
    String baseTeamName = ""; // 初始化 baseTeamName

    for (MLBProfit.Team team : baseTeams) {
      if (team.Rank == baseTeamRank) {
        baseScoreRate = team.scoreRate;
        baseTeamName = team.team; // 找到隊伍後設置隊伍名稱
        teamFound = true;
        break;
      }
    }

    if (!teamFound) {
      System.out.println("\n未找到基準隊伍，Rank：" + baseTeamRank);
      return Optional.empty();
    }
    System.out.println("\n找到基準隊伍: " + baseTeamName + " - Score Rate: " + baseScoreRate);

    // Step 2: 檢查目標區域隊伍的 scoreRate 是否都大於基準隊伍的 scoreRate
    boolean allGreaterThanBase = true;
    for (MLBProfit.Team targetTeam : targetTeams) {
      if (targetTeam.scoreRate <= baseScoreRate) {
        allGreaterThanBase = false;
        break;
      }
    }

    MLBProfit.TeamStadium bestTeam = null;
    double maxSeatsAttendance = 0.0;
    if (allGreaterThanBase) {
      // Step 3A: 如果所有目標隊伍的 scoreRate 都大於基準隊伍，選擇 seats * worldSeriesAttendanceRate 最大者
      System.out.println("\n所有目標隊伍的 scoreRate 都大於基準隊伍。");
      for (MLBProfit.Team targetTeam : targetTeams) {
        // 找到 target 球隊對應的球場資訊
        for (MLBProfit.TeamStadium targetStadium : targetStadiumInfos) {
          if (targetTeam.team.split(" - ")[0].equals(targetStadium.team)) {
            // 計算該球隊的 Seats * World Series Attendance Rate
            double seatsAttendance = targetStadium.seats * targetStadium.worldSeriesAttendanceRate;
            if (seatsAttendance > maxSeatsAttendance) {
              maxSeatsAttendance = seatsAttendance;
              bestTeam = targetStadium;
            }
          }
        }
      }
    } else {
      // Step 3B: 如果有 target 隊伍的 scoreRate 小於 base 隊伍，按照原邏輯選擇 seats * worldSeriesAttendanceRate 最大者
      System.out.println("\n找到 scoreRate 小於基準隊伍的目標隊伍。");
      for (MLBProfit.Team targetTeam : targetTeams) {
        if (targetTeam.scoreRate < baseScoreRate) {
          // 找到 target 球隊對應的球場資訊
          for (MLBProfit.TeamStadium targetStadium : targetStadiumInfos) {
            if (targetTeam.team.split(" - ")[0].equals(targetStadium.team)) {
              // 計算該球隊的 Seats * World Series Attendance Rate
              double seatsAttendance = targetStadium.seats * targetStadium.worldSeriesAttendanceRate;
              if (seatsAttendance > maxSeatsAttendance) {
                maxSeatsAttendance = seatsAttendance;
                bestTeam = targetStadium;
              }
            }
          }
        }
      }
    }
    // 如果找到最合適的隊伍，返回該隊伍的球場資訊，否則返回空的 Optional
    return Optional.ofNullable(bestTeam);
  }

  private static List<MLBProfit.Team> getTeamsByRegion(String region, List<MLBProfit.Team> alTeams, List<MLBProfit.Team> nlTeams) {
    return region.equalsIgnoreCase("AL") ? alTeams : nlTeams;
  }

}
