package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ForcedOutBasesTest {

  private Set<String> toSet(String result) {
    return new HashSet<>(Arrays.asList(result.split(",")));
  }

  @Test
  void testNoRunners() {
    //沒有跑者在壘上，可封殺一壘
    Set<String> result = toSet(Main.getForcedOutBases("0"));
    Set<String> expected = toSet("1B");
    assertEquals(expected, result, "壘包無人時，可封殺一壘");
  }

  @Test
  void testFirstBaseOccupied() {
    //一壘有人，可封殺一、二壘
    Set<String> result = toSet( Main.getForcedOutBases("1B"));
    Set<String> expected = toSet("1B,2B");
    assertEquals(expected, result, "一壘有跑者時，可封殺一、二壘");
  }

  @Test
  void testSecondBaseOccupied() {
    //二壘有人，可封殺一壘
    Set<String> result = toSet(Main.getForcedOutBases("2B"));
    Set<String> expected = toSet("1B");
    assertEquals(expected, result, "二壘有跑者時，可封殺一壘");
  }

  @Test
  void testThirdBaseOccupied() {
    //三壘有人，可封殺一壘
    Set<String> result = toSet(Main.getForcedOutBases("3B"));
    Set<String> expected = toSet("1B");
    assertEquals(expected, result, "三壘有跑者時，可封殺一壘");
  }

  @Test
  void testFirstAndSecondBaseOccupied() {
    //一、二壘有人，可封殺一壘、二壘、三壘
    Set<String> result = toSet(Main.getForcedOutBases("1B,2B"));
    Set<String> expected = toSet("1B,2B,3B");
    assertEquals(expected, result, "一、二壘有跑者時，可封殺一壘、二壘、三壘");
  }

  @Test
  void testFirstAndThirdBaseOccupied() {
    //一、三壘有人，可封殺一壘、二壘
    Set<String> result = toSet(Main.getForcedOutBases("1B,3B"));
    Set<String> expected = toSet("1B,2B");
    assertEquals(expected, result, "一、三壘有跑者時，可封殺一壘、二壘");
  }

  @Test
  void testSecondAndThirdBaseOccupied() {
    //二、三壘有人，可封殺一壘
    Set<String> result = toSet(Main.getForcedOutBases("2B,3B"));
    Set<String> expected = toSet("1B");
    assertEquals(expected, result, "二、三壘有跑者時，可封殺一壘");
  }

  @Test
  void testAllBaseOccupied() {
    //一、二、三壘有人，可封殺一壘、二壘、三壘、本壘
    Set<String> result = toSet(Main.getForcedOutBases("1B,2B,3B"));
    Set<String> expected = toSet("1B,2B,3B,HB");
    assertEquals(expected, result, "一、二、三壘有跑者時，可封殺一壘、二壘、三壘、本壘");
  }

  @Test
  void testInvalidBaseStatus() {
    // 測試無效輸入，例如 "4B"
    String result = Main.getForcedOutBases("4B");
    assertEquals("請重新輸入。0：本壘, 1B：一壘, 2B：二壘, 3B：三壘, HB：本壘", result, "輸入無效時應該返回提示訊息");
  }

  @Test
  void testEmptyInput() {
    // 測試空輸入
    String result = Main.getForcedOutBases("");
    assertEquals("輸入值為空，請重新輸入", result, "空輸入時應該返回提示訊息");
  }

  @Test
  void testDuplicateBaseStatus() {
    // 測試重複輸入，例如 "1B,1B,2B"
    Set<String> result = toSet(Main.getForcedOutBases("1B,1B,2B"));
    Set<String> expected = toSet("1B,2B,3B");
    assertEquals(expected, result, "重複輸入時應該返回正確的封殺壘包");
  }

  @Test
  void testAllBaseOccupiedWithZero() {
    // 測試包含所有狀態的情況，例如 "0,1B,2B,3B,HB"
    Set<String> result = toSet(Main.getForcedOutBases("0,1B,2B,3B,HB"));
    Set<String> expected = toSet("輸入錯誤，請重新輸入。不會壘上無人且所有壘包都有人");
    assertEquals(expected, result, "包含所有狀態時應該返回 輸入錯誤，請重新輸入");
  }

}