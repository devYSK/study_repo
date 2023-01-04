package org.example.string;

import java.util.Scanner;

public class Main9숫자만추출 {

    //문자와 숫자가 섞여있는 문자열이 주어지면 숫자만 추출하여 순서대로 자연수를 만든다.

    public static int solution(String str) {
        int answer = 0;

//        for (char x : str.toCharArray()) {
//            if (x >= 48 && x <= 57) {
//                answer = answer * 10 + (x - 48); // 아스키 연산 주의.
//            }
//        }

        StringBuilder sb = new StringBuilder();
        for (char x : str.toCharArray()) {
            if (Character.isDigit(x)) {
                sb.append(x);
            }
        }
        //return Integer.parseInt(sb);
        return answer;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println(solution(sc.nextLine()));
    }
}
