package org.example;

public class TeamPrinter {
  // 印出結果
  public void printResults(String al_one, String al_two, String al_three, String al_four, String al_five, String al_six,
                                   String nl_one, String nl_two, String nl_three, String nl_four, String nl_five, String nl_six) {
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

}
