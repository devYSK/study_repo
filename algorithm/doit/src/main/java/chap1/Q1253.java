package chap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author : ysk
 */
public class Q1253 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());

        int[] array = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt)
                .sorted().toArray();
        //두 수의 합으로 표현되는 수의 총 갯수 찾기

        int count = 0;


        for (int k = 0; k < n; k++) {
            int find = array[k];
            int i = 0;
            int j = n - 1;

            while (i < j) {


                if (array[i] + array[j] == find) {
                    if (i != k && j != k) {
                        count++;
                        break;
                    } else if (i == k) {
                        i++;
                    } else {
                        j--;
                    }
                } else if (array[i] + array[j] < find) {
                    i++;
                } else {
                    j--;
                }

            }
        }


        System.out.println(count);
    }
}
