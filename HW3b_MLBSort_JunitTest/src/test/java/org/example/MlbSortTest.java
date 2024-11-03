package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MlbSortTest {

  private MlbSort mlbSort;
  private String filename;

  @BeforeEach
  void setUp() {
    mlbSort = new MlbSort();
    filename = "testFile.txt";
  }

  @AfterEach
  void tearDown() {
    // 清理測試檔案
    File file = new File(filename);
    if(file.exists()) {
      file.delete();
    }
  }

  @Test
  void testLoadData() {
    // 建立一個臨時檔案，用來測試
    try (FileWriter writer = new FileWriter(filename)) {
      //AL EAST
      writer.write("AL EAST\n");
      writer.write("New York Yankees,91,71,0.562\n");
      writer.write("Toronto Blue Jays,89,73,0.549\n");
      //AL Central
      writer.write("AL Central\n");
      writer.write("Minnesota Twins,101,61,0.623\n");
      writer.write("Chicago White Sox,72,89,0.447\n");
      //AL West
      writer.write("AL West\n");
      //給錯誤的 例行賽總數(162) 和 PctRate
      writer.write("Houston Astros,92,70,0.568\n");
      writer.write("Texas Rangers,90,72,0.556\n");
      //NL EAST
      writer.write("NL East\n");
      writer.write("Atlanta Braves,104,58,0.642\n");
      writer.write("Philadelphia Phillies,90,72,0.556\n");
      //NL Central
      writer.write("NL Central\n");
      writer.write("Milwaukee Brewers,92,70,0.568\n");
      //NL West
      writer.write("NL West\n");
      writer.write("Los Angeles Dodgers,106,56,0.654\n");
    } catch (IOException e) {
      fail("建立測試檔案失敗: " + e.getMessage());
    }
    // 載入檔案
    mlbSort.load(filename);
    // 驗證各區域的球隊是否正確解析
    Map<String, List<Team>> teamsByArea = mlbSort.getTeamsByArea();
    assertEquals(2, teamsByArea.get("AL EAST").size(), "AL East 球隊數不正確");
    assertEquals(2, teamsByArea.get("AL Central").size(), "AL Central 球隊數不正確");
    assertEquals(2, teamsByArea.get("AL West").size(), "AL West 球隊數不正確");
    assertEquals(2, teamsByArea.get("NL East").size(),"NL East 球隊數不正確");
    assertEquals(1, teamsByArea.get("NL Central").size(),"NL Central 球隊數不正確");
    assertEquals(1, teamsByArea.get("NL West").size(),"NL West 球隊數不正確");

    //驗證 Yankees 球隊的數據是否正常
    Team yankees = teamsByArea.get("AL EAST").get(0);
    assertEquals("New York Yankees", yankees.getTeamName());
    int winValue = Integer.parseInt(yankees.getWin());
    int loseValue = Integer.parseInt(yankees.getLose());
    assertEquals(162, (winValue+loseValue), "勝負和應為 162");
    double pctRateValue = Double.parseDouble(yankees.getPct());
    assertTrue(pctRateValue > 0, "PctRate 應大於 0");
  }


  @Test
  void testInvalidData() {
    String invalidFilename = "invalidFile.txt";
    try(FileWriter writer = new FileWriter(invalidFilename)) {
      writer.write("AL EAST\n");
      writer.write("Invalid Data Line\n"); //錯誤格式的行
      writer.write("Toronto Blue Jays,89,73,0.549\n");
    } catch(IOException e) {
      fail("建立測試檔案失敗：" + e.getMessage());
    }
    mlbSort.load(invalidFilename);
    Map<String, List<Team>> teamsByArea = mlbSort.getTeamsByArea();

    assertEquals(1, teamsByArea.get("AL EAST").size(), "錯誤行處理不正確，AL East 球隊數應為 1");
    Team team = teamsByArea.get("AL EAST").get(0);
    assertEquals("Toronto Blue Jays", team.getTeamName(), "AL East中排名第一的球隊應為 Toronto Blue Jays");

    // 清理無效文件
    File invalidFile = new File(invalidFilename);
    if(invalidFile.exists()) {
      invalidFile.delete();
    }
  }
}