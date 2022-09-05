package le1;

import java.util.Stack;

/**
 * @author : ysk
 */
public class 크레인인형뽑기게임 {

    public int solution(int[][] board, int[] moves) {
        int answer = 0;

        Stack<Integer> bucket = new Stack();

        bucket.push(0);

        for (int move : moves) {

            for (int i = 0; i < board.length; i++) {
                if (board[i][move - 1] != 0) {
                    if (bucket.isEmpty()) {
                        bucket.push(board[i][move - 1]);
                        board[i][move - 1] = 0;
                        break;
                    }
                    if (board[i][move - 1] == bucket.peek()) {
                        bucket.pop();
                        answer += 2;
                    } else
                        bucket.push(board[i][move - 1]);
                    board[i][move - 1] = 0;
                    break;
                }
            }

        }

        return answer;
    }


    public static void main(String[] args) {
        크레인인형뽑기게임 o = new 크레인인형뽑기게임();

        System.out.println(o.solution(new int[][]{{0, 0, 0, 0, 0}, {0, 0, 1, 0, 3}, {0, 2, 5, 0, 1}, {4, 2, 4, 4, 2}, {3, 5, 1, 3, 1}},
                new int[]{1, 5, 3, 5, 1, 2, 1, 4}));


        System.out.println(o.solution(
                new int[][]{{3, 3, 3, 3, 3}, {3, 3, 3, 3, 3}, {3, 3, 3, 3, 3}, {3, 3, 3, 3, 3}, {3, 3, 3, 3, 3}}, new int[]{1, 5, 3, 5, 1, 2, 1, 4})
        );

        System.out.println(o.solution(
                new int[][]{{0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}}, new int[]{1, 5, 3, 5, 1, 2, 1, 4})
        );
    }
}
