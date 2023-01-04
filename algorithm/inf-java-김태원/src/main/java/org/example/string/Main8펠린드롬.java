package org.example.string;

import java.util.Scanner;

public class Main8펠린드롬 {
// 앞에서 읽을 때나 뒤에서 읽을 때나 같은 문자열을 팰린드롬이라고 합니다.
// 알파벳 이외의 문자들의 무시합니다

    // 알파벳들만 가지고 회문 검사.

    public static String solution(String str) {

        // 먼저 알파벳 제외하고 제거
        str = str.toUpperCase().replaceAll("[^A-Z]", "");

        String tmp = new StringBuilder(str).reverse().toString();

        if (str.equals(tmp)) return "YES";
        else return "NO";
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String input = sc.nextLine();
        System.out.println(solution(input));
    }
}
