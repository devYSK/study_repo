package chap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * @author : ysk
 */
public class P11660 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());

        int[][] original = new int[n + 1][n + 1];


        for (int i = 1; i <= n; i++) {
            stringTokenizer = new StringTokenizer(br.readLine());
            for (int j = 1; j <= n; j++) {
                original[i][j] = Integer.parseInt(stringTokenizer.nextToken());
            }
        }

        int[][] sum = new int[n + 1][n + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                // sum[i][j] = sum[i][j-1] + sum[i-1][j] - sum[i-1][j-1] + org[i][j]
                sum[i][j] = sum[i][j - 1] + sum[i - 1][j] - sum[i - 1][j - 1] + original[i][j];
            }
        }

        for (int i = 0; i < m; i++) {
            stringTokenizer = new StringTokenizer(br.readLine());
            int x1 = Integer.parseInt(stringTokenizer.nextToken());
            int y1 = Integer.parseInt(stringTokenizer.nextToken());
            int x2 = Integer.parseInt(stringTokenizer.nextToken());
            int y2 = Integer.parseInt(stringTokenizer.nextToken());
            // ex) 2 2 3 4 -> 2행 2열부터 3행 4열까지 합
            // s[x2][y2] - s[x1-1][y2] - s[x2][y1-1] + s[1][1]
            int answer = sum[x2][y2] - sum[x1-1][y2] - sum[x2][y1-1] + sum[x1 - 1][y1 -1];
            System.out.println(answer);
        }

    }
}
