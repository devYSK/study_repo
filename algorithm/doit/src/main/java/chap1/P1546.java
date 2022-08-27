package chap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author : ysk
 */
public class P1546 {


    private double input() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());

        int[] scores = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::valueOf).toArray();


        return calculateAverage(scores);
    }

    private double calculateAverage(int[] scores) {
        int max = Arrays.stream(scores).max().getAsInt();

        int sum = 0;
        for (int score : scores) {
            sum += score;
        }


        return sum * 100.0 / max / scores.length;
    }

    public static void main(String[] args) throws IOException {
        P1546 p = new P1546();

        System.out.println(p.input());
    }


}
