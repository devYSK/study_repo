package org.example.string;

import java.util.Scanner;

public class Main12암호 {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        String str = sc.next();

        System.out.println(solution(str, n));
    }

    private static String solution(String str, int n) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String tmp = str.substring(0, 7).replace('#', '1').replace('*', '0');
            int num = Integer.parseInt(tmp, 2);
            sb.append((char) num);
            str = str.substring(7);

        }

        return sb.toString();
    }

}
