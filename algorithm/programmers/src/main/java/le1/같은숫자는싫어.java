package le1;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : ysk
 */
public class 같은숫자는싫어 {
    public int[] solution(int[] arr) {

        List<Integer> integers = new ArrayList<>();

        int temp = 10;

        for (int e : arr) {
            if (e != temp ) {
                integers.add(e);
            }

            temp = e;
        }

        return integers.stream().mapToInt(value -> value).toArray();
    }


}
