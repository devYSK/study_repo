package 프로그래머스;

/**
 * @author : ysk
 */
public class Test {


    // 문자열

    /**
     * 1 A 아스키 = 65
     * 2 B
     * 3 C
     * ...
     * 26 Z Z = 90
     * <p>
     * <p>
     * 1. 같은 숫자의 개수 셈
     * 2. 숫자가 바뀌면 개수 센걸 계산하고 answer 더함
     */

    public String solution3(String src) {
        StringBuilder answer = new StringBuilder();

        int size = src.length();
        int i = 0 ;

        while (i < size) {


            i++;
        }

        return null;
    }

    public String solution2(String src) {
        StringBuilder answer = new StringBuilder();

        int beforeCharacter = src.charAt(0);

        int sameCharCount = 1;

        answer.append(beforeCharacter);

        for (int i = 1; i < src.length() - 1; i++) {
            char nextCharacter = src.charAt(i);

            if (beforeCharacter != nextCharacter) {
                answer.append(convertCountToChar(sameCharCount));
                sameCharCount = 1;
                beforeCharacter = nextCharacter;
            } else {
                sameCharCount += 1;
            }
        }

        return answer.toString();
    }


    public String solution(String src) {
        StringBuilder answerBuilder = new StringBuilder();

        char beforeChar = src.charAt(0);

        answerBuilder.append(beforeChar);

        int sameCharCount = 1;

        for (int i = 1; i < src.length(); i++) {
            char nowChar = src.charAt(i);

            if (beforeChar == nowChar) {
                sameCharCount += 1;
            } else {
                answerBuilder.append(convertCountToChar(sameCharCount));
                sameCharCount = 1;
                beforeChar = nowChar;
            }
        }

        answerBuilder.append(convertCountToChar(sameCharCount));
        return answerBuilder.toString();
    }

    private char convertCountToChar(int count) {
        return (char) (count + 64);
    }

    private char convert(char character) {
        return character - '0' == 1 ? '0' : '1';
    }

    public static void main(String[] args) {
        Test t = new Test();

//        System.out.println(t.solution("111100100011"));
//        System.out.println(t.solution("11111"));

        System.out.println(t.solution("1"));
    }
}
