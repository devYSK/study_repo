package org.example.string;

import java.util.Scanner;

public class Main2대소문자변환 {

    public static String solution(String str) {
        StringBuilder sb = new StringBuilder();

        // 아스키넘버 알파벳 대문자 = 65~90
        // 알파벳 소문자 97~122
        // 소문자에서 32를 빼면 대문자가 된다.

        for (char x : str.toCharArray()) {
            if (Character.isUpperCase(x)) {
                sb.append(Character.toLowerCase(x));
            } else {
                sb.append(Character.toUpperCase(x));
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String str = in.next();

        System.out.println(solution(str));
    }

}
