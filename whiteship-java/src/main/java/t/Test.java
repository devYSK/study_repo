package t;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author : ysk
 */
public class Test {

    public static void main(String[] args) {

        Test test = new Test();


        test.drawDandyNumberFromString("122233344");

    }

    public int solution(String s) {
        int answer = 0;
        return answer;
    }

    // 멋쟁이 숫자는 길이가 3임

    private List<Integer> dandyNumbers = new ArrayList<>();

    private int getBigNumber() {
        Integer integer = dandyNumbers.stream().max(Integer::compareTo).orElse(-1);
        return integer == -1 ? -1 :
    }

    private void drawDandyNumberFromString(String s) {
        Map<Integer, Integer> integerCountMap = getIntegerCountMap();
        int[] digits = strToDigits(s);


        for (int digit : digits) {
            integerCountMap.put(digit, integerCountMap.get(digit) + 1);
        }


        for (int i = 0; i < integerCountMap.size(); i++) {
            if (integerCountMap.get(i) == 3) {
                dandyNumbers.add(i);
            }
        }

        System.out.println(integerCountMap);

    }

    private int[] strToDigits(String s) {
        return Stream.of(s.split("")).mapToInt(Integer::parseInt).toArray();
    }

    private Map<Integer, Integer> getIntegerCountMap() {
        Map<Integer, Integer> integerMap = new HashMap<>();

        IntStream.rangeClosed(0, 9).forEach(value -> {
            integerMap.put(value, 0);
        });

        return integerMap;
    }

}
