package org.example;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    //輸入壘包狀況
    System.out.println("壘包無人：0\n一壘:1B\n二壘:2B\n三壘:3B\n本壘:HB");
    System.out.println("請輸入壘包狀況：(ex:一壘、二壘壘上有人，輸入：1B,2B)");
    String basesStatus = sc.nextLine();

    //呼叫 getForcedOutBases方法，並輸出結果
    String forcedOutBases = getForcedOutBases(basesStatus);
    System.out.println("可以封殺的壘包：" + forcedOutBases);
  }

  public static String getForcedOutBases(String bases) {
    //使用集合來表示壘包狀態
    //集合（Set），不會考慮順序
    Set<String> baseStatus = new HashSet<>();
    if(!bases.isEmpty()) {
      //將輸入的壘包狀態轉換成大寫，然後分割並放入集合
      String[] baseArray = bases.toUpperCase().split(",");
      for(String base: baseArray) {
        baseStatus.add(base.trim());//去除空白並加入集合
      }
    }

    // 檢查輸入的壘包狀態是否不是"0,1B,2B,3B,HB"
    if (!baseStatus.isEmpty() && !isValidBaseStatus(baseStatus)) {
      return "請重新輸入。0：本壘, 1B：一壘, 2B：二壘, 3B：三壘, HB：本壘"; // 如果輸入無效，返回提示訊息
    }
    // 檢查輸入為空值
    if (baseStatus.isEmpty()) {
      return "輸入值為空，請重新輸入";
    }

    // 檢查特定輸入 "0,1B,2B,3B,HB"，因為 0 為壘上無人
    if (baseStatus.contains("0") && baseStatus.contains("1B") &&
            baseStatus.contains("2B") && baseStatus.contains("3B") &&
            baseStatus.contains("HB")) {
      return "輸入錯誤，請重新輸入。不會壘上無人且所有壘包都有人";
    }
    // 檢查特定輸入 "0,1B"，因為 0 為壘上無人
    if (baseStatus.contains("0") && baseStatus.contains("1B")) {
      return "輸入錯誤，請重新輸入。不會壘上無人但一壘有人";
    }
    // 檢查特定輸入 "0,2B"，因為 0 為壘上無人
    if (baseStatus.contains("0") && baseStatus.contains("2B")) {
      return "輸入錯誤，請重新輸入。不會壘上無人但二壘有人";
    }
    // 檢查特定輸入 "0,3B"，因為 0 為壘上無人
    if (baseStatus.contains("0") && baseStatus.contains("3B")) {
      return "輸入錯誤，請重新輸入。不會壘上無人但三壘有人";
    }
    // 檢查特定輸入 "0,HB"，因為 0 為壘上無人
    if (baseStatus.contains("0") && baseStatus.contains("HB")) {
      return "輸入錯誤，請重新輸入。不會壘上無人且本壘也有人";
    }
    // 檢查特定輸入 "0,1B,2B"，因為 0 為壘上無人
    if (baseStatus.contains("0") && baseStatus.contains("1B") &&
            baseStatus.contains("2B")) {
      return "輸入錯誤，請重新輸入。不會壘上無人但一壘、二壘有人";
    }
    // 檢查特定輸入 "0,1B,3B"，因為 0 為壘上無人
    if (baseStatus.contains("0") && baseStatus.contains("1B") &&
            baseStatus.contains("3B")) {
      return "輸入錯誤，請重新輸入。不會壘上無人但一壘、三壘有人";
    }
    // 檢查特定輸入 "0,1B,HB"，因為 0 為壘上無人
    if (baseStatus.contains("0") && baseStatus.contains("1B") &&
            baseStatus.contains("HB")) {
      return "輸入錯誤，請重新輸入。不會壘上無人但一壘、本壘有人";
    }
    // 檢查特定輸入 "0,2B,3B"，因為 0 為壘上無人
    if (baseStatus.contains("0") && baseStatus.contains("2B") &&
            baseStatus.contains("3B")) {
      return "輸入錯誤，請重新輸入。不會壘上無人但二壘、三壘有人";
    }
    // 檢查特定輸入 "0,3B,HB"，因為 0 為壘上無人
    if (baseStatus.contains("0") && baseStatus.contains("3B") &&
            baseStatus.contains("HB")) {
      return "輸入錯誤，請重新輸入。不會壘上無人但三壘、本壘有人";
    }


    //建立要輸出的集合
    Set<String> forcedOutBases = new HashSet<>();
    //檢查壘包狀態，決定可以封殺的壘包
    // 如果壘上無人，可封殺 1B
    if(baseStatus.contains("0")) {
      forcedOutBases.add("1B");
    }
    // 如果一壘上有跑者，可封殺 1B、2B
    if(baseStatus.contains("1B")) {
      forcedOutBases.add("1B");
      forcedOutBases.add("2B");
    }
    // 如果一壘和二壘上都有跑者，可封殺 1B、2B、3B
    if(baseStatus.contains("1B") && baseStatus.contains("2B")) {
      forcedOutBases.add("1B");
      forcedOutBases.add("2B");
      forcedOutBases.add("3B");
    }
    // 如果一壘、二壘和三壘上都有跑者，可封殺 1B、2B、3B、HB
    if(baseStatus.contains("1B") && baseStatus.contains("2B") && baseStatus.contains("3B")) {
      forcedOutBases.add("1B");
      forcedOutBases.add("2B");
      forcedOutBases.add("3B");
      forcedOutBases.add("HB");
    }
    // 如果只有二壘或只有三壘上有跑者，可封殺 1B
    if(baseStatus.contains("2B") || baseStatus.contains("3B")) {
      forcedOutBases.add("1B");
    }

    //回傳結果組合成字串
    return String.join(",", forcedOutBases);
  }

  // 檢查壘包狀態是否有效
  private static boolean isValidBaseStatus(Set<String> baseStatus) {
    // 允許的壘包狀態
    Set<String> validBases = Set.of("0", "1B", "2B", "3B", "HB");
    // 檢查每個壘包狀態是否在允許的集合中
    for (String base : baseStatus) {
      if (!validBases.contains(base)) {
        return false; // 如果有無效的壘包狀態，返回 false
      }
    }
    return true; // 所有壘包狀態都是有效的
  }
}