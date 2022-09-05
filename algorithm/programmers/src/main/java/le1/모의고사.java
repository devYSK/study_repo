package le1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : ysk
 */
public class 모의고사 {

    public int[] solution(int[] answers) {

        int[] man1 = new int[]{1, 2, 3, 4, 5};
        int[] man2 = new int[]{2, 1, 2, 3, 2, 4, 2, 5};
        int[] man3 = new int[]{3, 3, 1, 1, 2, 2, 4, 4, 5, 5};


        int answer1 = 0, answer2 = 0, answer3 = 0;

        for (int i = 0; i < answers.length; i++) {

            int answer = answers[i];

            if (answer == man1[i % man1.length]) answer1 ++;
            if (answer == man2[i % man2.length]) answer2 ++;
            if (answer == man3[i % man3.length]) answer3 ++;
        }


        int maxPoint = Math.max(answer1, Math.max(answer2, answer3));

        List<Integer> manOrders = new ArrayList<>();

        if (maxPoint == answer1) manOrders.add(1);
        if (maxPoint == answer2) manOrders.add(2);
        if (maxPoint == answer3) manOrders.add(3);


        return manOrders.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void main(String[] args) {
        모의고사 o = new 모의고사();

        System.out.println(Arrays.toString(o.solution(
                new int[]{1, 2, 3, 4, 5}
        )));

    }

}
