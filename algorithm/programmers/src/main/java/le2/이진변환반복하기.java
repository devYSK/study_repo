package le2;

import java.util.Arrays;

/**
 * @author : ysk
 */
public class 이진변환반복하기 {

    public static int[] solution(String s) {

        int transferCount = 0;

        int removeCount = 0;

        while (s.length() != 1) {
            transferCount +=1;

            String removed = s.replaceAll("0", "");

            removeCount += (s.length() - removed.length());

            s = Integer.toBinaryString(removed.length());

        }

        return new int[]{transferCount, removeCount};
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution("110010101001")));
    }

}
