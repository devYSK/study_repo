package chap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * @author : ysk
 */
public class P12891 {

    static int checkARr[];
    static int myArr[];
    static int checkSecret;

    public static void main(String[] args) throws IOException {
        // 문자열에 등장하는 문자가 A C G T 여야함

        // 부분 문자열의 길이
        // A C G T 가 각각 몇번 이상 등장해야 비밀번호로 사용할 수 있는지의 총 종류의 수

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int dnaStrLength = Integer.parseInt(st.nextToken()); // 문자열 크기  S
        int dnaSubStrLength = Integer.parseInt(st.nextToken()); // 부분 문자열 크기 P

        String dnaStr = br.readLine(); // 문자열 데이터

        st = new StringTokenizer(br.readLine());

        int aCount = Integer.parseInt(st.nextToken());
        int cCount = Integer.parseInt(st.nextToken());
        int gCount = Integer.parseInt(st.nextToken());
        int tCount = Integer.parseInt(st.nextToken());


    }
}
