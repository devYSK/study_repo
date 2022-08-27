package chap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author : ysk
 */
public class Q2018 {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());


        int[] arr = new int[n];

        for (int i = 1; i < n; i++) {
            arr[i - 1] = i;
        }

        /**
         *  sum > n : sum = sum - startIndex; startIndex++;
         *  sum < n : endIndex++; sum = sum + endIndex;
         *  sum == n : endIndex++; sum= sum + endIndex; count ++
         */
        int count = 1 ; // n 제외
        int startIndex = 0;
        int endIndex = 0;

        int sum = 0;
        while (endIndex != n) {
            if (sum == n) {
                count += 1;
                endIndex += 1;
                sum = sum + endIndex;
            } else if (sum > n) {
                sum = sum - startIndex;
                startIndex += 1;
            } else {
                endIndex +=1;
                sum = sum + endIndex;
            }
        }

        System.out.println(count);
    }
}
