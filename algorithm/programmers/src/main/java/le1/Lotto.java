package le1;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : ysk
 */
public class Lotto {

    public int[] solution(int[] lottos, int[] win_nums) {


        List<Integer> lottoList = Arrays.stream(lottos).boxed().collect(Collectors.toList());

        int hitCount = 0;
        int zeroCount = 0;

        for (int i = 0; i < lottoList.size(); i++) {

            if (lottoList.contains(win_nums[i])) {
                hitCount += 1;
            }
            if (lottoList.get(i) == 0) {
                zeroCount += 1;
            }
        }


        return new int[]{getMaxRank(hitCount, zeroCount), getMinRank(hitCount)};
    }

    private int getMaxRank(int hitCount, int zeroCount) {
        return getRank(hitCount + zeroCount);
    }

    private int getMinRank(int hitCount) {
        return getRank(hitCount);
    }

    private int getRank(int countOfHit) {
        return RANK.of(countOfHit).getRanking();
    }


    enum RANK {

        FIRST(6, 1),
        SECOND(5, 2),
        THIRD(4, 3),
        FOURTH(3, 4),
        FIFTH(2, 5),
        SIXTH(1, 6)
        ;

        private int hitCount;

        private int ranking;

        RANK(int hitCount, int ranking) {
            this.hitCount = hitCount;
            this.ranking = ranking;
        }

        public int getRanking() {
            return ranking;
        }

        public static RANK of(int hitCount) {
            return Arrays.stream(RANK.values())
                    .filter(RANK -> RANK.hitCount == hitCount)
                    .findFirst().orElseGet(() -> SIXTH);
        }
    }

    public static void main(String[] args) {
        Lotto lotto = new Lotto();

        int[] solution = lotto.solution(
                new int[]{44, 1, 0, 0, 31, 25},
                new int[]{31, 10, 45, 1, 6, 19}
        );
        System.out.println();
    }
}
