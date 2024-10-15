package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class Main {
  public static void main(String[] args) {

    int year = 2024;
    Map<String, List<Map<String, Object>>> map = MLBInfo(2024);
    System.out.println("Main 的 map： " + map);

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      // 將 map 轉換為 JSON 字串
      String jsonString = objectMapper.writeValueAsString(map);
      System.out.println("JSON 字串： " + jsonString);

      // 將 JSON 字串寫入檔案
      objectMapper.writeValue(new File("mlb_info_" + year + ".json"), map);
      System.out.println("已將 JSON 寫入檔案：mlb_info_" + year + ".json");
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  //爬蟲
  private static Map<String, List<Map<String, Object>>> MLBInfo(int year) {
    WebDriver driver = new ChromeDriver();
    driver.get("https://www.mlb.com/standings/" + year);

    //隱式等待時間來延長查找元素的時間
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    System.out.println("開始爬取資料");

    List<String> ALWildCardTeams = Arrays.asList("AL Leaders", "AL Wild Card"); //AL區 外卡前6名
    List<String> NLWildCardTeams = Arrays.asList("NL Leaders", "NL Wild Card"); //NL區 外卡前6名
    List<WebElement> teams = driver.findElements(By.className("jnhStu"));
    List<String> teamsName = new ArrayList<>();
    try {
      for(int i=0; i< teams.size(); i++) {
        String name = teams.get(i).getText();
//        System.out.println("TeamsName內： " + name);
        if((i%2)==0){
          teamsName.add(name);
        }
      }
      System.out.println("teamsName內： " + teamsName);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    List<WebElement> scoreData = driver.findElements(By.className("table-cellstyle__StyledTableCell-sc-xpntj7-2"));
    List<Double> teamScore = new ArrayList<>();
    try {
      for(int i=0; i<scoreData.size(); i++) {
        String score = scoreData.get(i).getText();
        System.out.println("ScoreData內： " + score);
        if((i%14)==2){
          if(!score.equals("PCT")) {
            teamScore.add(Double.parseDouble(score));
            System.out.println("TeamScore內： " + teamScore);
          }
        }
      }
      System.out.println("teamScore內： " + teamScore);
    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }
    driver.close();

    Map<String, List<Map<String, Object>>> map = new LinkedHashMap<>();
    int teamIndex = 0;
    int ALrank = 1;
    for(String ALWildCardTeam: ALWildCardTeams) { //AL區
      List<Map<String, Object>> teamList = new ArrayList<>();
      for(int i=0; i<3 && teamIndex<teamsName.size(); i++, teamIndex++) {
        Map<String, Object> ALwildCardTeam = new LinkedHashMap<>();
        ALwildCardTeam.put("team", teamsName.get(teamIndex));
        ALwildCardTeam.put("scoreRate", teamScore.get(teamIndex));
        ALwildCardTeam.put("Rank", ALrank);
        ALrank++;
        teamList.add(ALwildCardTeam);
      }
      map.put(ALWildCardTeam, teamList);
      System.out.println("AL_map為： " + map);
    }
    int teamIndex2 = 15;
    int NLrank = 1;
    for(String NLWildCardTeam: NLWildCardTeams) { //NL區
      List<Map<String, Object>> teamList = new ArrayList<>();
      for(int i=0; i<3 && teamIndex2<teamsName.size(); i++, teamIndex2++) {
        Map<String, Object> NLwildCardTeam = new LinkedHashMap<>();
        NLwildCardTeam.put("team", teamsName.get(teamIndex2));
        NLwildCardTeam.put("scoreRate", teamScore.get(teamIndex2));
        NLwildCardTeam.put("Rank", NLrank);
        NLrank++;
        teamList.add(NLwildCardTeam);
      }
      map.put(NLWildCardTeam, teamList);
      System.out.println("NL 後 map 為： " + map);
    }
    return map;
  }

}