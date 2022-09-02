package le1;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : ysk
 */
public class 숫자문자열과영단어 {

    private Map<String, Integer> engWords = Map.of(
            "zero", 0,
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9);

    public int solution(String s) {

        for (String num : engWords.keySet()) {
            s = s.replaceAll(num, engWords.get(num).toString());
        }

        return Integer.parseInt(s);
    }

    public static void main(String[] args) {
        숫자문자열과영단어 s = new 숫자문자열과영단어();

        System.out.println(s.solution("one4seveneight"));
        System.out.println(s.solution("23four5six7"));
        System.out.println(s.solution("2three45sixseven"));
        System.out.println(s.solution("123"));
    }

}
