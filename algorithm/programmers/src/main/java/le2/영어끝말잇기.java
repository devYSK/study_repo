package le2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : ysk
 */
public class 영어끝말잇기 {
    // n 사람수
    public int[] solution(int n, String[] words) {
        int[] answer = {};

        Set<String> wordSet = new HashSet<>();
        wordSet.add(words[0]);

        for (int i = 1; i < words.length; i++) {

            // 횟수
            int count = (i / n) + 1;

            // 자기자신의 차례
            int orderNum = i % n + 1;

            String beforeWord = words[i - 1];
            String nowWord = words[i];

            if (beforeWord.charAt(beforeWord.length() - 1) != nowWord.charAt(0)) {
                return new int[]{orderNum, count};
            }

            if (wordSet.contains(nowWord)) {
                return new int[]{orderNum, count};
            }

            wordSet.add(nowWord);
        }


        return new int[]{0, 0};
    }

    public static void main(String[] args) {
        영어끝말잇기 o = new 영어끝말잇기();

        System.out.println(Arrays.toString(o.solution(3, new String[]{"tank", "kick", "know", "wheel", "land", "dream", "mother", "robot", "tank"})));

        System.out.println("\n\n\n");
        System.out.println(Arrays.toString(o.solution(2, new String[]{"hello", "one", "even", "never", "now", "world", "draw"})));
    }
}
