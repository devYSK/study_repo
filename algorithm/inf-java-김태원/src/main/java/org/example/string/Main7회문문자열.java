package org.example.string;

import java.util.Scanner;

public class Main7회문문자열 {

    public static boolean solution(String str) {
        // 조건 i < len / 2
        // 홀수랑 짝수랑 조건이 같다.
        int len = str.length();

        String tmp = new StringBuilder(str).reverse().toString();

        if (str.equalsIgnoreCase(tmp)) return true;
        else return false;

    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String str = sc.nextLine();

        System.out.println(solution(str) ? "YES" : "NO");
    }
}
