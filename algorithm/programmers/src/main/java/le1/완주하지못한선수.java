package le1;

import java.util.Arrays;

/**
 * @author : ysk
 */
public class 완주하지못한선수 {

    public String solution(String[] participant, String[] completion) {
        String answer = "";

        Arrays.sort(participant);
        Arrays.sort(completion);

        for (int i = 0; i < completion.length; i++) {
            if (!completion[i].equals(participant[i])) {
                return participant[i];
            }
        }

        return participant[participant.length - 1];
    }

    public static void main(String[] args) {
        완주하지못한선수 o = new 완주하지못한선수();

        System.out.println(o.solution(
                new String[]{"leo", "kiki", "eden"},
                new String[]{"eden", "kiki"}

        ));
    }
}
