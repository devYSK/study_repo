package le2;

/**
 * @author : ysk
 */
public class 카펫 {
    public int[] solution(int brown, int yellow) {
        int[] answer = new int[2];

        int fullCount = brown + yellow;


        for (int height = 3; height < fullCount; height++) {


            if (fullCount % height == 0) {  // 합을 높이로 나누면 가로길이

                int width = fullCount / height;

                if ((height - 2) * (width - 2) == yellow) {
                    answer[0] = width;
                    answer[1] = height;
                    break;
                }

            }
        }

        return answer;
    }
}
