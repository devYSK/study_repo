package org.example.string;

import java.util.Scanner;

public class Main6중복문자제거 {

    public static String solution(String str) {
        StringBuilder sb = new StringBuilder();

        // indexOf로 중복여부를 알 수 있따. 자기자신의 인덱스와 indexOf()의 결과 인덱스가 다르면 중복이다.
        for (int i = 0; i < str.length(); i++) {
            if (str.indexOf(str.charAt(i)) == i) {
                sb.append(str.charAt(i));
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println(solution(sc.nextLine()));
    }
}
