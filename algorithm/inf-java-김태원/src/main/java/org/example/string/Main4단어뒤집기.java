package org.example.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main4단어뒤집기 {

//    public static List<String> solution(String[] strs) {
//        List<String> answer = new ArrayList<>();
//
//        for(String x : strs) {
//            String tmp = new StringBuilder(x).reverse().toString();
//            answer.add(tmp);
//        }
//
//        return answer;
//    }

    public static List<String> solution(String[] strs) {
        List<String> answer = new ArrayList<>();

        for (String x : strs) {
            char[] s = x.toCharArray();
            int lt = 0, rt = x.length() - 1;

            while (lt < rt) {
                char tmp = s[lt];
                s[lt] = s[rt];
                s[rt] = tmp;
                lt++;
                rt--;
            }

            answer.add(Arrays.toString(s));
        }

        return answer;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        String[] strs = new String[3];
        for (int i = 0; i < n; i++) {
            strs[i] = sc.nextLine();
        }

        solution(strs).forEach(System.out::println);
    }

}
