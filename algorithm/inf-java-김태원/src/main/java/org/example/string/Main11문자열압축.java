package org.example.string;

import java.util.Scanner;

public class Main11문자열압축 {

    public static String solution(String str) {
        StringBuilder answer = new StringBuilder();

        str = str + " ";

        int cnt = 1;
        for (int i = 0; i < str.length() - 1; i++) {
            if (str.charAt(i) == str.charAt(i + 1)) {
                cnt += 1;
            } else {
                answer.append(str.charAt(i));

                if (cnt > 1) {
                    answer.append(cnt);
                    cnt = 1;
                }
            }
        }

        return answer.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String str = sc.nextLine();

        System.out.println(solution(str));
    }
}
