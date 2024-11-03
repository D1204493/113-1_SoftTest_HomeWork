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


}