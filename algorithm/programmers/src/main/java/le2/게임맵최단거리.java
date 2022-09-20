package le2;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author : ysk
 */
public class 게임맵최단거리 {

    private final int[] dx = {1, 0, -1, 0};
    private final int[] dy = {0, 1, 0, -1};

    public int solution(int[][] maps) {
        return bfs(maps);
    }

    private int bfs(int[][] maps) {


        Queue<int[]> queue = new LinkedList<>();

        int x = 0;
        int y = 0;

        int[][] visited = new int[maps.length][maps[0].length];

        visited[x][y] = 1;

        queue.add(new int[]{x, y});


        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentX = current[0];
            int currentY = current[1];

            for (int i = 0; i < 4; i++) {

                int nextX = currentX + dx[i];
                int nextY = currentY + dy[i];

                if ( nextX >= 0 && nextX < maps.length  && nextY >= 0 && nextY < maps[0].length
                        && visited[nextX][nextY] == 0
                        && isCanMoveToNext(maps, nextX, nextY)) {

                    visited[nextX][nextY] = visited[currentX][currentY] + 1;
                    queue.add(new int[]{nextX, nextY});
                }

            }

        }

        int distance = visited[maps.length - 1][maps[0].length - 1];

        return distance == 0 ? -1 : distance;
    }

    private boolean isCanMoveToNext(int[][] maps, int x, int y) {
        return maps[x][y] == 1;
    }

    public static void main(String[] args) {
        게임맵최단거리 o = new 게임맵최단거리();

        int[][] maps = new int[][] {
                {1,0,1,1,1},
                {1,0,1,0,1},
                {1,0,1,1,1},
                {1,1,1,0,1},
                {0,0,0,0,1}
        };


        o.solution(maps);
    }
}
