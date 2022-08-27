package chap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author : ysk
 */
public class Q1940 {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // 2개의 재료로 만듬, 2가지 재료의 고유한 번호를 합치면 갑옷이 된다.

        int n = Integer.parseInt(br.readLine()); // 재료의 개수 갑옷은 2개의 재료로 만듬
        int m = Integer.parseInt(br.readLine()); // 갑옷을 만들 수 있는 2개의 재료를 합친 값.

        int[] array = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt)
                .sorted()
                .toArray();

        int count = 0;
        int i = 0;
        int j = array.length - 1;
        while (i < j) {
            if (array[i] + array[j] == m) {
                count += 1;
                i += 1;
                j -= 1;
            } else if (array[i] + array[j] < m) {
                i +=1;
            } else {
                j -=1;
            }
        }

        System.out.println(count);
    }
}
