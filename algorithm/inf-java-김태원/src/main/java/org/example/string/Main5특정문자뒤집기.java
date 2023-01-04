package org.example.string;

import java.util.Scanner;

public class Main5특정문자뒤집기 {

    public static String solution(String str) {

        int lt = 0;
        int rt = str.length() - 1;

        char[] chars = str.toCharArray();

        while (lt < rt) {

            if (!Character.isAlphabetic(chars[lt])) {
                lt++;
            } else if (!Character.isAlphabetic(chars[rt])) {
                rt--;
            } else {
                char tmp = chars[lt];
                chars[lt] = chars[rt];
                chars[rt] = tmp;

                lt++;
                rt--;
            }
        }

        return String.valueOf(chars);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String str = sc.nextLine();

        System.out.println(solution(str));
    }

}
