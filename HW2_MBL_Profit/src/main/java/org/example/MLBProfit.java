package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MLBProfit {
  //設定 Logger
  private static final Logger logger = Logger.getLogger(Main.class.getName());

  private static final int tickPrice = 450; //門票
  private static final int worldGameTickPrice = 800; //門票
  private static final double hostRevenue = 0.85; //主場分潤
  private static final double clientRevenue = 0.15; //客場分潤

  // 定義 Team 類用來對應 JSON 的球隊數據
  public static class Team {
    public String team;
    public double scoreRate;
    public int Rank;
  }
  //球隊場地的 JSON檔
  public static class TeamStadium {
    public String team;
    public String stadium;
    public int seats;
    public double playoffAttendanceRate;
    public double worldSeriesAttendanceRate;
  }

  // 定義對應的 JSON 結構
  // JSON 中的字段名 "AL Leaders" 與 Java 類中的屬性 "AL_Leaders" 不匹配。JSON 中的字段名有空格，而 Java 屬性名稱是使用下劃線的。
  // 透過使用 Jackson 的 @JsonProperty 註解來指定 JSON 中的字段名稱對應 Java 類的屬性名稱。這樣，即使 JSON 中的字段名包含空格，也能夠正確解析。
  public static class MLBTeams {
    @JsonProperty("AL Leaders")
    public List<Team> AL_Leaders;
    @JsonProperty("AL Wild Card")
    public List<Team> AL_Wild_Card;
    @JsonProperty("NL Leaders")
    public List<Team> NL_Leaders;
    @JsonProperty("NL Wild Card")
    public List<Team> NL_Wild_Card;
  }

  public static void main(String[] args) {
    try {
      // 使用 ObjectMapper 讀取JSON檔案
      ObjectMapper mapper = new ObjectMapper();
      File mlbInfoFile = new File("mlb_info_2024.json");
      File teamStadiumFile = new File("teamstadium.json");
      // 檢查檔案是否存在
      if (!mlbInfoFile.exists() || !teamStadiumFile.exists()) {
        logger.log(Level.SEVERE, "JSON 檔案不存在。請檢查檔案路徑。");
        return;
      }

      //JSON解析成物件
      MLBTeams mlbTeams = mapper.readValue(new File("mlb_info_2024.json"), MLBTeams.class);
      List<TeamStadium> teamStadium = mapper.readValue(new File("teamstadium.json"), mapper.getTypeFactory().constructCollectionType(List.class, TeamStadium.class));

      // 將球場資訊存入 Map
      Map<String, TeamStadium> stadiumMap = new HashMap<>();
      if(teamStadium != null && !teamStadium.isEmpty()){
        for(TeamStadium stadium: teamStadium) {
          if(stadium.team != null && !stadium.team.isEmpty() && stadium.seats>0 && stadium.playoffAttendanceRate>0) {
            stadiumMap.put(stadium.team, stadium);
          } else {
            logger.log(Level.WARNING, "發現有問題的球場資料: {0}", stadium);
          }
        }
      } else {
        logger.log(Level.SEVERE, "球場資訊檔案為空或格式錯誤。");
        return;
      }

//      for(TeamStadium stadium: teamStadium) {
//        stadiumMap.put(stadium.team, stadium);
//      }
//      System.out.println("印出"+stadiumMap);

      // 用於保存排名對應的球隊資料的全局 Map (只保存排名1-6的隊伍)
//      Map<Integer, TeamStadium> rankToStadiumMap = new HashMap<>();
      // 處理 AL Leaders (美國聯盟領先隊伍)
//      System.out.println("=== AL Leaders ===");
//      processTeams(mlbTeams.AL_Leaders, stadiumMap, rankToStadiumMap);

      // 處理 AL Wild Card (美國聯盟外卡)
//      System.out.println("=== AL Wild Card ===");
//      processTeams(mlbTeams.AL_Wild_Card, stadiumMap, rankToStadiumMap);


      // 合併 AL Leaders 和 AL Wild Card 的球隊列表
      List<Team> allTeams = new ArrayList<>();
      allTeams.addAll(mlbTeams.AL_Leaders);
      allTeams.addAll(mlbTeams.AL_Wild_Card);

      // 合併 NL Leaders 和 NL Wild Card 的球隊列表
      List<Team> nlTeams = new ArrayList<>();
      nlTeams.addAll(mlbTeams.NL_Leaders);
      nlTeams.addAll(mlbTeams.NL_Wild_Card);

      //日誌
      if (allTeams.isEmpty() || nlTeams.isEmpty()) {
        logger.log(Level.WARNING, "空的，沒有可處理的球隊資料。");
        return;
      }

      // 處理合併後的球隊列表
      System.out.println("=== AL All Teams ===");
      logger.log(Level.INFO, "處理 AL 所有球隊資料");
      processTeams(allTeams, nlTeams, stadiumMap, teamStadium);

      // 處理合併後的 NL 球隊列表
      System.out.println("=== NL All Teams ===");
      logger.log(Level.INFO, "處理 NL 所有球隊資料");
      processTeams(nlTeams, allTeams, stadiumMap, teamStadium);

    } catch (Exception e) {
      logger.log(Level.SEVERE, "發生未預期的錯誤", e);
      e.printStackTrace();
    }
  }


  // 統一處理球隊的利潤計算與輸出
  public static void processTeams(List<Team> teams, List<Team> otherTeams, Map<String, TeamStadium> stadiumMap, List<TeamStadium> teamStadium) {
    //日誌
    if (teams == null || teams.isEmpty()) {
      logger.log(Level.WARNING, "球隊資料為空，無法處理。");
      return;
    }
    if (stadiumMap == null || stadiumMap.isEmpty()) {
      logger.log(Level.WARNING, "球場資訊為空，無法處理。");
      return;
    }

    Map<Integer, TeamStadium> rankToStadiumMap = new HashMap<>();

    for (Team team : teams) {
      if (team == null || team.team == null || team.team.isEmpty()) {
        logger.log(Level.WARNING, "跳過無效的球隊資料。");
        continue;
      }
      logger.log(Level.INFO, "處理球隊: {0}, Score Rate: {1}, Rank: {2}", new Object[]{team.team, team.scoreRate, team.Rank});
      System.out.println("Team: " + team.team);
      System.out.println("Score Rate: " + team.scoreRate);
      System.out.println("Rank: " + team.Rank);
      System.out.println("----------------------------");

      // 從 stadiumMap 中獲取球隊對應的球場資訊
      TeamStadium stadiumInfo = stadiumMap.get(team.team.split(" - ")[0]); // 切除" - "之後的聯盟標記
      if (stadiumInfo != null) {
        // 儲存排名與球場對應的資料
        rankToStadiumMap.put(team.Rank, stadiumInfo);
      } else {
        System.out.println("球場資訊未找到");
        logger.log(Level.WARNING, "未找到 {0} 的球場資訊。", team.team);
      }
    }

    // 打印 rankToStadiumMap 的內容
    if (rankToStadiumMap.isEmpty()) {
      logger.log(Level.WARNING, "球場資料為空");
      return;
    }

    System.out.println("\n===== rankToStadiumMap 內容 =====");
    for (Map.Entry<Integer, TeamStadium> entry : rankToStadiumMap.entrySet()) {
      System.out.println("排名: " + entry.getKey() +
              ", 隊伍: " + entry.getValue().team +
              ", 座位數: " + entry.getValue().seats +
              ", 季後賽上座率: " + entry.getValue().playoffAttendanceRate);
    }
    System.out.println("===============================\n"); //確定rankToStadiumMap有資料

    String baseRegion = "NL"; // 可替換為 "NL" 來進行其他區域比較
    String targetRegion = "AL"; // 可替換為 "AL" 來進行其他區域比較
    List<Team> baseTeams = getTeamsByRegion(baseRegion, teams, otherTeams);
    List<Team> targetTeams = getTeamsByRegion(targetRegion, teams, otherTeams);

    // 利潤計算 calculateAndPrintProfit
    for (Team team : teams) {
      TeamStadium stadiumInfo = rankToStadiumMap.get(team.Rank); //確定rankToStadiumMap有資料
      if (stadiumInfo != null) {
        logger.log(Level.INFO, "計算利潤: 球隊 {0}, Rank: {1}, 球場: {2}",
                new Object[]{team.team, team.Rank, stadiumInfo.team});
        calculateAndPrintProfit(team.Rank, stadiumInfo, rankToStadiumMap, baseTeams, targetTeams, teamStadium); // 傳遞所有球隊
      } else {
        logger.log(Level.WARNING, "無法找到 {0} 的對應球場資訊進行利潤計算。", team.team);
      }
    }
  }

  public static void calculateAndPrintProfit(int rank, TeamStadium stadiumInfo, Map<Integer, TeamStadium> rankToStadiumMap, List<Team> baseTeams, List<Team> targetTeams, List<TeamStadium> teamStadium) {
    logger.log(Level.INFO, "計算 Rank {0} 的球隊利潤", rank);
    double maxProfit = 0;
    double minProfit = 0;
    double worldProfit = 0;
    int playGames;
    // 根據球隊外卡排名使用 switch 來進行利潤計算
    switch(rank) {
      case 1://洋基(5戰3勝) -> (7戰4勝)
        System.out.println("排名第" + rank + "的隊伍：" + stadiumInfo.team);
        // 取得排名4和排名5的球隊資料
        TeamStadium rank4Stadium1 = rankToStadiumMap.get(4);
        TeamStadium rank5Stadium1 = rankToStadiumMap.get(5);
        if (rank4Stadium1 != null && rank5Stadium1 != null) {
          System.out.println("對戰排名第4的隊伍，球場座位：" + rank4Stadium1.seats);
          System.out.println("對戰排名第5的隊伍，球場座位：" + rank5Stadium1.seats);
          // 計算滿座率和座位數
          double rate4 = rank4Stadium1.playoffAttendanceRate;
          double rate5 = rank5Stadium1.playoffAttendanceRate;
          int seats4 = rank4Stadium1.seats;
          int seats5 = rank5Stadium1.seats;

          // 決定與排名4或排名5的隊伍比賽
          if (rate4*seats4 >= rate5*seats5) {
            // 選擇排名4的隊伍
            maxProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 3)
                    + calculateClientProfit(rank4Stadium1.seats, rank4Stadium1.playoffAttendanceRate, 2); //(5戰3勝) -> 對戰金鶯4

            minProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 2)
                    + calculateClientProfit(rank5Stadium1.seats, rank5Stadium1.playoffAttendanceRate, 1); //(3敗)
          } else {
            // 選擇排名5的隊伍
            maxProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 3)
                    + calculateClientProfit(rank5Stadium1.seats, rank5Stadium1.playoffAttendanceRate, 2); //(5戰3勝) -> 對戰金鶯4

            minProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 2)
                    + calculateClientProfit(rank4Stadium1.seats, rank4Stadium1.playoffAttendanceRate, 1); //(3敗)
          }
        }
        System.out.printf("測試maxProfit: %,.2f%n", maxProfit);
        System.out.printf("測試minProfit：%,.2f%n" , minProfit);

        // 取得排名2、排名3、排名6的球隊資料
        TeamStadium rank2Stadium = rankToStadiumMap.get(2);
        TeamStadium rank3Stadium = rankToStadiumMap.get(3);
        TeamStadium rank6Stadium = rankToStadiumMap.get(6);
        if (rank2Stadium != null && rank3Stadium != null && rank6Stadium != null) {
          System.out.println("對戰排名第2的隊伍，球場座位：" + rank2Stadium.seats);
          System.out.println("對戰排名第3的隊伍，球場座位：" + rank3Stadium.seats);
          System.out.println("對戰排名第6的隊伍，球場座位：" + rank6Stadium.seats);
          // 計算滿座率和座位數
          double rate2 = rank2Stadium.playoffAttendanceRate;
          double rate3 = rank3Stadium.playoffAttendanceRate;
          double rate6 = rank6Stadium.playoffAttendanceRate;
          int seats2 = rank2Stadium.seats;
          int seats3 = rank3Stadium.seats;
          int seats6 = rank6Stadium.seats;

          // 決定與排名2或排名3或排名6的隊伍比賽
          if ((rate6*seats6 >= rate2*seats2) && (rate6*seats6 >= rate3*seats3)) {
            // 選擇排名6的隊伍
            maxProfit = maxProfit + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
                    + calculateClientProfit(rank6Stadium.seats, rank6Stadium.playoffAttendanceRate, 3); //(7戰4勝) -> 對戰老虎6或太空人3(都是41K, 滿座率都100%)
          } else if((rate2*seats2 > rate6*seats6) && (rate2*seats2 > rate3*seats3)) {
            // 選擇排名2的隊伍
            maxProfit = maxProfit + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
                    + calculateClientProfit(rank2Stadium.seats, rank2Stadium.playoffAttendanceRate, 3); //(7戰4勝) -> 對戰老虎6或太空人3(都是41K, 滿座率都100%)
          } else {
            // 選擇排名3的隊伍
            maxProfit = maxProfit + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
                    + calculateClientProfit(rank3Stadium.seats, rank3Stadium.playoffAttendanceRate, 3); //(7戰4勝) -> 對戰老虎6或太空人3(都是41K, 滿座率都100%)
          }
        }
        //當自己是主場
        WorldSeriesProfitResult result = WorldGame.calculateWorldSeriesProfit("AL","NL", rank, baseTeams, targetTeams, teamStadium);
        worldProfit = result.getMaxProfit();
        playGames = result.getGames();
        if(playGames == 3) {
          maxProfit = maxProfit + worldProfit
                  + calculateWorldGameClientProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, (7-playGames)); //(7戰4勝) -> 對戰道奇 (56K, 滿座率100%)
        } else if (playGames == 4) {
          maxProfit = maxProfit + worldProfit
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, (7-playGames)); //(7戰4勝)
        }

//        maxProfit = maxProfit + calculateWorldGameHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
//                + calculateWorldGameClientProfit(56000, 1.0, 3); //(7戰4勝) -> 對戰老道奇 (56K, 滿座率100%)
        break;
//        maxProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 3)
//                + calculateClientProfit(45000, 1.0, 2) //(5戰3勝) -> 對戰金鶯4
//        maxProfit = maxProfit + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
//                + calculateClientProfit(41000, 1.0, 3); //(7戰4勝) -> 對戰老虎6或太空人3(都是41K, 滿座率都100%)
//        minProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 2)
//                + calculateClientProfit(37000, 1.0, 1); //(3敗)

      case 2: //守護者
        System.out.println("排名第" + rank + "的隊伍：" + stadiumInfo.team);
        // 取得排名3和排名6的球隊資料
        rank3Stadium = rankToStadiumMap.get(3);
        rank6Stadium = rankToStadiumMap.get(6);
        if (rank3Stadium != null && rank6Stadium != null) {
          System.out.println("對戰排名第3的隊伍，球場座位：" + rank3Stadium.seats);
          System.out.println("對戰排名第6的隊伍，球場座位：" + rank6Stadium.seats);
          // 計算滿座率和座位數
          double rate3 = rank3Stadium.playoffAttendanceRate;
          double rate6 = rank6Stadium.playoffAttendanceRate;
          int seats3 = rank3Stadium.seats;
          int seats6 = rank6Stadium.seats;

          // 決定與排名3或排名6的隊伍比賽
          if (rate3*seats3 >= rate6*seats6) {
            // 選擇排名3的隊伍
            maxProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 3) //主場
                    + calculateClientProfit(rank3Stadium.seats, rank3Stadium.playoffAttendanceRate, 2); //(5戰3勝) -> 對戰老虎6或太空人3(都是41K, 滿座率都100%)

            minProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 2)
                    + calculateClientProfit(rank3Stadium.seats, rank3Stadium.playoffAttendanceRate, 1); //(3敗)
          } else {
            // 選擇排名6的隊伍
            maxProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 3) //主場
                    + calculateClientProfit(rank6Stadium.seats, rank6Stadium.playoffAttendanceRate, 2); //(5戰3勝) -> 對戰老虎6或太空人3(都是41K, 滿座率都100%)

            minProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 2)
                    + calculateClientProfit(rank6Stadium.seats, rank6Stadium.playoffAttendanceRate, 1); //(3敗)
          }
        }

        // 取得排名1、排名4、排名5的球隊資料
        TeamStadium rank4Stadium = rankToStadiumMap.get(4);
        TeamStadium rank5Stadium = rankToStadiumMap.get(5);
        if (rank4Stadium != null && rank5Stadium != null) {
          System.out.println("對戰排名第4的隊伍，球場座位：" + rank4Stadium.seats);
          System.out.println("對戰排名第5的隊伍，球場座位：" + rank5Stadium.seats);
          // 計算滿座率和座位數
          double rate4 = rank4Stadium.playoffAttendanceRate;
          double rate5 = rank5Stadium.playoffAttendanceRate;
          int seats4 = rank4Stadium.seats;
          int seats5 = rank5Stadium.seats;

          // 決定與排名1或排名4或排名5的隊伍比賽
          if (rate4*seats4 >= rate5*seats5) {
            // 選擇排名4的隊伍
            maxProfit = maxProfit + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
                    + calculateClientProfit(rank4Stadium.seats, rank4Stadium.playoffAttendanceRate, 3); //(7戰4勝) -> 對戰金鶯4
          } else {
            // 選擇排名5的隊伍
            maxProfit = maxProfit + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
                    + calculateClientProfit(rank5Stadium.seats, rank5Stadium.playoffAttendanceRate, 3); //(7戰4勝) -> 對戰金鶯4
          }
        }
        //當自己是主場
        WorldSeriesProfitResult result2 = WorldGame.calculateWorldSeriesProfit("AL","NL", rank, baseTeams, targetTeams, teamStadium);
        worldProfit = result2.getMaxProfit();
        playGames = result2.getGames();
        if(playGames == 3) {
          maxProfit = maxProfit + worldProfit
                  + calculateWorldGameClientProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, (7-playGames));
        } else if (playGames == 4) {
          maxProfit = maxProfit + worldProfit
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, (7-playGames)); //(7戰4勝)
        }
//        maxProfit = maxProfit + calculateWorldGameHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
//                + calculateWorldGameClientProfit(56000, 1.0, 3); //(7戰4勝)
        break;

//        maxProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 3)
//                + calculateClientProfit(41000, 1.0, 2) //(5戰3勝) -> 對戰老虎6或太空人3(都是41K, 滿座率都100%)

//        maxProfit = maxProfit + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
//                + calculateClientProfit(45000, 1.0, 3); //(7戰4勝) -> 對戰金鶯4

//        minProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 2)
//                + calculateClientProfit(41000, 1.0, 1); //(3敗)

      case 3: //太空人
        System.out.println("排名第" + rank + "的隊伍：" + stadiumInfo.team);
        TeamStadium rank2Stadium1 = rankToStadiumMap.get(2); // 抓取排名第2的球隊資料 -> (5戰3勝)一定會對到排名為2的隊伍
        if(rank2Stadium1 != null) {
          System.out.println("對戰排名第2的隊伍，球場座位：" + rank2Stadium1.seats);
          maxProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 3) //(3戰2勝) -> 對戰老虎6
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 2) // 對戰 排名第2的隊伍
                  + calculateClientProfit(rank2Stadium1.seats, rank2Stadium1.playoffAttendanceRate, 3); //(5戰3勝) -> 對戰克里夫蘭守護者2
//                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
//                  + calculateClientProfit(45000, 1.0, 3); //(7戰4勝) -> 對戰金鶯4

          minProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 2); //主場分潤 * 場數 (2敗)
        } else {
          System.out.println("排名第2的隊伍資料未找到");
        }

        // 取得排名4、排名5的球隊資料
        rank4Stadium = rankToStadiumMap.get(4);
        rank5Stadium = rankToStadiumMap.get(5);
        if (rank4Stadium != null && rank5Stadium != null) {
          System.out.println("對戰排名第4的隊伍，球場座位：" + rank4Stadium.seats);
          System.out.println("對戰排名第5的隊伍，球場座位：" + rank5Stadium.seats);
          // 計算滿座率和座位數
          double rate4 = rank4Stadium.playoffAttendanceRate;
          double rate5 = rank5Stadium.playoffAttendanceRate;
          int seats4 = rank4Stadium.seats;
          int seats5 = rank5Stadium.seats;

          // 決定與排名4或排名5的隊伍比賽
          if (rate4 * seats4 >= rate5 * seats5) {
            // 選擇排名4的隊伍
            maxProfit = maxProfit + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
                  + calculateClientProfit(rank4Stadium.seats, rank4Stadium.playoffAttendanceRate, 3); //(7戰4勝) -> 對戰金鶯4
          } else {
            // 選擇排名5的隊伍
            maxProfit = maxProfit + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
                    + calculateClientProfit(rank5Stadium.seats, rank5Stadium.playoffAttendanceRate, 3); //(7戰4勝) -> 對戰金鶯4
          }
        }
        //當自己是主場
        WorldSeriesProfitResult result3 = WorldGame.calculateWorldSeriesProfit("AL","NL", rank, baseTeams, targetTeams, teamStadium);
        worldProfit = result3.getMaxProfit();
        playGames = result3.getGames();
        if(playGames == 3) {
          maxProfit = maxProfit + worldProfit
                  + calculateWorldGameClientProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, (7-playGames));
        } else if (playGames == 4) {
          maxProfit = maxProfit + worldProfit
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, (7-playGames)); //(7戰4勝)
        }
//        maxProfit = maxProfit + calculateWorldGameHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
//                + calculateWorldGameClientProfit(56000, 1.0, 3); //(7戰4勝)
        break;

      case 4: //金鶯
        System.out.println("排名第" + rank + "的隊伍：" + stadiumInfo.team);
        TeamStadium rank1Stadium1 = rankToStadiumMap.get(1); // 抓取排名第1的球隊資料 -> (5戰3勝)一定會對到排名為1的隊伍
        if(rank1Stadium1 != null) {
          System.out.println("對戰排名第1的隊伍，球場座位：" + rank1Stadium1.seats);

          maxProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 3) //(3戰2勝)
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 2)
                  + calculateClientProfit(rank1Stadium1.seats, rank1Stadium1.playoffAttendanceRate, 3); //(5戰3勝) -> 對戰洋基1
//                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
//                  + calculateClientProfit(41000, 1.0, 3); //(7戰4勝) -> 對戰老虎6

          minProfit = calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 2); //主場分潤 * 場數 (2敗)
        } else {
          System.out.println("排名第1的隊伍資料未找到");
        }

        // 取得排名6的球隊資料
        rank6Stadium = rankToStadiumMap.get(6);
        if (rank6Stadium != null) {
          System.out.println("對戰排名第6的隊伍，球場座位：" + rank6Stadium.seats);
          // 計算滿座率和座位數
          double rate6 = rank6Stadium.playoffAttendanceRate;
          int seats6 = rank6Stadium.seats;

          // 決定與排名6的隊伍比賽
            maxProfit = maxProfit + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
                    + calculateClientProfit(rank6Stadium.seats, rank6Stadium.playoffAttendanceRate, 3); //(7戰4勝) -> 對戰老虎6
        }
        //當自己是主場
        WorldSeriesProfitResult result4 = WorldGame.calculateWorldSeriesProfit("AL","NL", rank, baseTeams, targetTeams, teamStadium);
        worldProfit = result4.getMaxProfit();
        playGames = result4.getGames();
        if(playGames == 3) {
          maxProfit = maxProfit + worldProfit
                  + calculateWorldGameClientProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, (7-playGames));
        } else if (playGames == 4) {
          maxProfit = maxProfit + worldProfit
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, (7-playGames)); //(7戰4勝)
        }
//        maxProfit = maxProfit + calculateWorldGameHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
//                + calculateWorldGameClientProfit(56000, 1.0, 3); //(7戰4勝)
        break;

      case 5: //皇家
        System.out.println("排名第" + rank + "的隊伍：" + stadiumInfo.team);
        rank4Stadium = rankToStadiumMap.get(4); //與排名第4對戰
        TeamStadium rank1Stadium2 = rankToStadiumMap.get(1); // 抓取排名第1的球隊資料 -> (5戰3勝)一定會對到排名為1的隊伍
        if(rank1Stadium2 != null && rank4Stadium != null) {
          System.out.println("對戰排名第4的隊伍，球場座位：" + rank4Stadium.seats);
          System.out.println("對戰排名第1的隊伍，球場座位：" + rank1Stadium2.seats);

          maxProfit = calculateClientProfit(rank4Stadium.seats, rank4Stadium.playoffAttendanceRate, 3) //(3戰2勝) -> 對戰 排名4的隊伍
                  + calculateClientProfit(rank1Stadium2.seats, rank1Stadium2.playoffAttendanceRate, 3)
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 2); //(5戰3勝) -> 對戰洋基1


//                  + calculateClientProfit(41000, 1.0, 3)
//                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4); //(7戰4勝) -> 對戰老虎6

          minProfit = calculateClientProfit(rank4Stadium.seats, rank4Stadium.playoffAttendanceRate, 2); //客場分潤 * 場數 (2敗)
        } else {
          System.out.println("排名第4的隊伍資料未找到");
        }

        // 取得排名6的球隊資料
        rank6Stadium = rankToStadiumMap.get(6);
        if (rank6Stadium != null) {
          System.out.println("對戰排名第6的隊伍，球場座位：" + rank6Stadium.seats);
          // 計算滿座率和座位數
          double rate6 = rank6Stadium.playoffAttendanceRate;
          int seats6 = rank6Stadium.seats;

          // 決定與排名6的隊伍比賽
          maxProfit = maxProfit + calculateClientProfit(rank6Stadium.seats, rank6Stadium.playoffAttendanceRate, 3)
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4); //(7戰4勝) -> 對戰老虎6
        }

        //當自己是主場
        WorldSeriesProfitResult result5 = WorldGame.calculateWorldSeriesProfit("AL","NL", rank, baseTeams, targetTeams, teamStadium);
        worldProfit = result5.getMaxProfit();
        playGames = result5.getGames();
        if(playGames == 3) {
          maxProfit = maxProfit + worldProfit
                  + calculateWorldGameClientProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, (7-playGames));
        } else if (playGames == 4) {
          maxProfit = maxProfit + worldProfit
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, (7-playGames)); //(7戰4勝)
        }
//        maxProfit = maxProfit + calculateWorldGameHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
//                + calculateWorldGameClientProfit(56000, 1.0, 3); //(7戰4勝) -> 世界大賽
        break;

      case 6: //老虎
        System.out.println("排名第" + rank + "的隊伍：" + stadiumInfo.team);
        rank3Stadium = rankToStadiumMap.get(3); // 抓取排名第3的球隊資料
        TeamStadium rank2Stadium2 = rankToStadiumMap.get(2); // 抓取排名第2的球隊資料
        if (rank2Stadium2 != null && rank3Stadium != null) {
          System.out.println("對戰排名第3的隊伍，球場座位：" + rank3Stadium.seats);
          System.out.println("對戰排名第2的隊伍，球場座位：" + rank2Stadium2.seats);

          maxProfit = calculateClientProfit(rank3Stadium.seats, rank3Stadium.playoffAttendanceRate, 3) //(3戰2勝) -> 對戰 排名第3的隊伍
                  + calculateClientProfit(rank2Stadium2.seats, rank2Stadium2.playoffAttendanceRate, 3) // 對戰 排名第2的隊伍
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 2); //(5戰3勝) -> 對戰克里夫蘭守護者2
//                  + calculateClientProfit(47000, 1.0, 4)
//                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 3); //(7戰4勝) -> 對戰洋基1

          minProfit = calculateClientProfit(rank3Stadium.seats, rank3Stadium.playoffAttendanceRate, 2); //客場分潤 * 場數 (2敗)
        } else {
          System.out.println("排名第3的隊伍資料未找到");
        }

        // 取得排名1、排名4、排名5的球隊資料
        TeamStadium rank1Stadium = rankToStadiumMap.get(1);
        rank4Stadium = rankToStadiumMap.get(4);
        rank5Stadium = rankToStadiumMap.get(5);
        if (rank1Stadium != null && rank4Stadium != null && rank5Stadium != null) {
          System.out.println("對戰排名第1的隊伍，球場座位：" + rank1Stadium.seats);
          System.out.println("對戰排名第4的隊伍，球場座位：" + rank4Stadium.seats);
          System.out.println("對戰排名第5的隊伍，球場座位：" + rank5Stadium.seats);
          // 計算滿座率和座位數
          double rate1 = rank1Stadium.playoffAttendanceRate;
          double rate4 = rank4Stadium.playoffAttendanceRate;
          double rate5 = rank5Stadium.playoffAttendanceRate;
          int seats1 = rank1Stadium.seats;
          int seats4 = rank4Stadium.seats;
          int seats5 = rank5Stadium.seats;

          // 決定與排名1 或排名4 或排名5 的隊伍比賽
          if ((rate1 * seats1 >= rate4 * seats4) && (rate1 * seats1 >= rate5 * seats5)) {
            // 選擇排名1的隊伍
            maxProfit = maxProfit + calculateClientProfit(rank1Stadium.seats, rank1Stadium.playoffAttendanceRate, 4)
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 3); //(7戰4勝) -> 對戰洋基1
          } else if ((rate4 * seats4 > rate1 * seats1) && (rate4 * seats4 > rate5 * seats5)) {
            // 選擇排名4的隊伍
            maxProfit = maxProfit + calculateClientProfit(rank4Stadium.seats, rank4Stadium.playoffAttendanceRate, 4)
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 3); //(7戰4勝) -> 對戰洋基1
          } else {
            // 選擇排名5的隊伍
            maxProfit = maxProfit + calculateClientProfit(rank5Stadium.seats, rank5Stadium.playoffAttendanceRate, 4)
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 3); //(7戰4勝) -> 對戰洋基1
          }
        }
        //當自己是主場
        WorldSeriesProfitResult result6 = WorldGame.calculateWorldSeriesProfit("AL","NL", rank, baseTeams, targetTeams, teamStadium);
        worldProfit = result6.getMaxProfit();
        playGames = result6.getGames();
        if(playGames == 3) {
          maxProfit = maxProfit + worldProfit
                  + calculateWorldGameClientProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, (7-playGames));
        } else if (playGames == 4) {
          maxProfit = maxProfit + worldProfit
                  + calculateHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, (7-playGames)); //(7戰4勝)
        }
//        maxProfit = maxProfit + calculateWorldGameHostProfit(stadiumInfo.seats, stadiumInfo.playoffAttendanceRate, 4)
//                + calculateWorldGameClientProfit(56000, 1.0, 3); //(7戰4勝) -> 世界大賽
        break;

      default:
        System.out.println("無效的排名");
        return;
    }
    System.out.printf("最大利潤：USD$ %,.2f%n", maxProfit);
    System.out.printf("最小利潤：USD$ %,.2f%n", minProfit);
    System.out.println("----------------------------");
  }


  // 計算利潤的方法
  public static double calculateProfit(int seats, double attendanceRate) {
    // 座位數*滿座率*票價
    return seats * attendanceRate * tickPrice;
  }
  //客場分潤方法
  public static double calculateClientProfit(int seats, double attendanceRate, int games) {
    return calculateProfit(seats, attendanceRate) * clientRevenue * games;
  }
  //主場分潤方法
  public static double calculateHostProfit(int seats, double attendanceRate, int games) {
    return calculateProfit(seats, attendanceRate) * hostRevenue * games;
  }

  //世界大賽客場分潤方法
  public static double calculateWorldGameClientProfit(int seats, double attendanceRate, int games) {
    return seats * attendanceRate * worldGameTickPrice * clientRevenue * games;
  }
  //世界大賽主場分潤方法
  public static double calculateWorldGameHostProfit(int seats, double attendanceRate, int games) {
    return seats * attendanceRate * worldGameTickPrice * hostRevenue * games;
  }

  private static List<MLBProfit.Team> getTeamsByRegion(String region, List<MLBProfit.Team> alTeams, List<MLBProfit.Team> nlTeams) {
    return region.equalsIgnoreCase("AL") ? alTeams : nlTeams;
  }

}
