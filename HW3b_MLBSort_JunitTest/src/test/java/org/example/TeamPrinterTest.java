package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TeamPrinterTest {
  private TeamPrinter printer;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @BeforeEach
  public void setUp() {
    printer = new TeamPrinter();
    System.setOut(new PrintStream(outputStreamCaptor)); // 設置系統輸出為 ByteArrayOutputStream
  }

  @Test
  void testPrintResults() {
    // 模擬的輸入數據
    String al_one = "New York Yankees";
    String al_two = "Toronto Blue Jays";
    String al_three = "Boston Red Sox";
    String al_four = "Tampa Bay Rays";
    String al_five = "Baltimore Orioles";
    String al_six = "Detroit Tigers";

    String nl_one = "Los Angeles Dodgers";
    String nl_two = "San Diego Padres";
    String nl_three = "San Francisco Giants";
    String nl_four = "Colorado Rockies";
    String nl_five = "Arizona Diamondbacks";
    String nl_six = "Chicago Cubs";

    // 呼叫要測試的方法
    printer.printResults(al_one, al_two, al_three, al_four, al_five, al_six, nl_one, nl_two, nl_three, nl_four, nl_five, nl_six);

    // 構建預期輸出，使用 System.lineSeparator() 保持一致性
    String expectedOutput = String.format("(AMERICAN LEAGUE)%n" +
                    "%-5s %d -----%n" +
                    "%-5s %d ----- ? -----%n" +
                    "        %-5s %d ----- ?%n" +
                    "%-5s %d -----%n" +
                    "%-5s %d ----- ? -----%n" +
                    "        %-5s %d ----- ? ----- ?%n" +
                    "                               ---- ?%n" +
                    "(NATIONAL LEAGUE)%n" +
                    "%-5s %d ----- ? ----- ? ----- ?%n" +
                    "%-5s %d -----%n" +
                    "        %-5s %d ----%n" +
                    "%-5s %d ----- ? ----- ?%n" +
                    "%-5s %d -----%n" +
                    "        %-5s %d -----%n",
            al_six, 6,
            al_three, 3,
            al_two, 2,
            al_five, 5,
            al_four, 4,
            al_one, 1,
            nl_six, 6,
            nl_three, 3,
            nl_two, 2,
            nl_five, 5,
            nl_four, 4,
            nl_one, 1
    );

    // 比較捕獲到的輸出與預期的輸出
    assertEquals(expectedOutput.trim(), outputStreamCaptor.toString().trim(), "測試資料與預期輸出資料不一致");
  }
}