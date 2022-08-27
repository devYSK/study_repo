package chap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * 구간 합 구가기 4
 * @author : ysk
 */
public class P11659 {

    public static void main(String[] args) throws IOException {

        P11659 p = new P11659();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());

        int[] s = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();

        p.initSum(n, s);

        for (int i = 0; i < m; i++) {
            stringTokenizer = new StringTokenizer(br.readLine());
            int section1 = Integer.parseInt(stringTokenizer.nextToken());
            int section2 = Integer.parseInt(stringTokenizer.nextToken());
            System.out.println(p.sum(section1, section2));
        }

    }


    private long[] sum;

    private void initSum(int n, int[] s) throws IOException {

        sum = new long[n + 1];
        for (int i = 1; i <= n; i++ ) {
            sum[i] = sum[i - 1] + s[i - 1];
        }

    }

    private long sum(int section1, int section2) {
        return sum[section2] - sum[section1-1];
    }

}
