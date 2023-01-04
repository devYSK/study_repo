package org.example.string;

import java.util.Arrays;
import java.util.Scanner;

public class Main10문자거리 {

    public static int[] solution(String str, char t) {
        int[] answer = new int[str.length()];

        int p = 1000;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == t) {
                p = 0;
            } else {
                p +=1;
            }
            answer[i] = p;
        }

        p = 1000;

        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) == t) {
                p = 0;
            } else {
                p +=1;

                answer[i] = Math.min(answer[i], p); // 기존에 있떤 거리보다 작은 값을 넣어야 한다
            }
        }

        return answer;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String str = sc.next();
        char c = sc.next().charAt(0);

        for (int x : solution(str, c)) {
            System.out.print(x + " ");
        }
    }

}
