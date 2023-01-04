package org.example.string;

import java.util.Scanner;

public class Main1 {

    public static int solution(String str, char t) {
        int count = 0;
        str = str.toUpperCase();
        t = Character.toUpperCase(t);
        for (char c : str.toCharArray()) {
            if (c == t) {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String inputStr = in.nextLine();

        char findChar = in.next().charAt(0);

        int solution = solution(inputStr, findChar);
        System.out.print(solution);

    }
}
