package le1;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : ysk
 */
public class MBTI {

    private String[] indicators = {"RT", "CF", "JM", "AN"};

    private Map<Character, Integer> mbtiPoints = new HashMap<>();

    public MBTI() {
        this.init();
    }

    private void init() {
        for (int i = 0; i < indicators.length; i++) {
            String indicator = indicators[i];
            char frontChar = indicator.charAt(0);
            char rearChar = indicator.charAt(1);

            mbtiPoints.put(frontChar, 0);
            mbtiPoints.put(rearChar, 0);
        }
    }

    /**
     * 각 성격 유형 점수가 같으면, 두 성격 유형 중 사전 순으로 빠른 성격 유형을 검사자의 성격 유형이라고 판단합니다.
     *
     * @param survey  질문마다 판단하는 지표를 담은 1차원 문자열 배열
     * @param choices 검사자가 각 질문마다 선택한 선택지를 담은 1차원 정수 배열
     * @return 검사자의 성격 유형 검사 결과를 지표 번호 순서대로 return
     */
    public String solution(String[] survey, int[] choices) {


        for (int i = 0; i < survey.length; i++) {
            int point = choices[i];

            if (point < 4) {
                char mbti = survey[i].charAt(0);
                mbtiPoints.put(mbti, mbtiPoints.getOrDefault(mbti, 0) + 4 - point);
            } else {
                char mbti = survey[i].charAt(1);
                mbtiPoints.put(mbti, mbtiPoints.getOrDefault(mbti, 0) + point - 4);
            }

        }

        StringBuilder resultBuilder = new StringBuilder();

        for (int i = 0; i < indicators.length; i++) {
            String indicator = indicators[i];
            char frontChar = indicator.charAt(0);
            char rearChar = indicator.charAt(1);

            Integer frontCharScore = mbtiPoints.get(frontChar);
            Integer rearCharScore = mbtiPoints.get(rearChar);

            resultBuilder.append(frontCharScore >= rearCharScore ?
                    frontChar : rearChar);
        }


        return resultBuilder.toString();
    }

    public static void main(String[] args) {
        MBTI o1 = new MBTI();

        System.out.println(o1.solution(
                new String[]{"AN", "CF", "MJ", "RT", "NA"},
                new int[]{5, 3, 2, 7, 5}
        ));

        MBTI o2 = new MBTI();

        System.out.println(o2.solution(
                new String[]{"TR", "RT", "TR"},
                new int[]{7, 1, 3}
        ));

    }
}
