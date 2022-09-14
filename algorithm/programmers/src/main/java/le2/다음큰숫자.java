package le2;

/**
 * @author : ysk
 */
public class 다음큰숫자 {
    public int solution(int n) {
        int answer = 0;


        for (int i = n + 1; i < 1000000; i++) {
            if (isSameBitCount(n, i)) {
                return i;
            }
        }

        return answer;
    }

    private boolean isSameBitCount(int n, int nextNum) {
        return Integer.bitCount(n) == Integer.bitCount(nextNum);
    }

}
