package org.example.string;

import java.util.Scanner;

public class Main3문장속단어 {

    public static String solution(String str) {
        String answer = "";
        int min = Integer.MIN_VALUE;

        String[] split = str.split(" ");

        for (String s : split) {
            int len = s.length();

            if (len > min) {
                min = len;
                answer = s;
            }

        }

        return answer;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String input = in.nextLine();

        System.out.println(solution(input));
    }
}
