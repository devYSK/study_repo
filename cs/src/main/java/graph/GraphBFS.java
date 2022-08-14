package graph;

import java.util.*;

/**
 * @author : ysk
 */
public class GraphBFS {

    public static class AdjList {

        private final LinkedList<Integer>[] adj;
        private final int vertexCount;

        public AdjList(int vertexCount) {
            this.vertexCount = vertexCount;
            adj = new LinkedList[vertexCount];

            for (int i = 0; i < vertexCount; i++) {
                adj[i] = new LinkedList<>();
            }
        }

        public void addEdge(int v, int otherV) {
            adj[v].add(otherV);
        }

        public List<Integer> bfs(int startVertex) {
            boolean visited[] = new boolean[this.vertexCount];

            Queue<Integer> queue = new LinkedList<>();

            visited[startVertex] = true;

            queue.add(startVertex);

            List<Integer> visitOrder = new ArrayList<>();

            while (!queue.isEmpty()) {
                int visitVertex = queue.poll();

                visitOrder.add(visitVertex);

                for (int nextVertex : adj[visitVertex]) {
                    if (!visited[nextVertex]) {
                        visited[nextVertex] = true;
                        queue.add(nextVertex);
                    }
                }
                // 같은 방법
//            Iterator<Integer> iterator = adj[visitVertex].iterator();
//
//            while (iterator.hasNext()) {
//                int nextVertex = iterator.next();
//
//                if (!visited[nextVertex]) {
//                    visited[nextVertex] = true;
//                    queue.add(nextVertex);
//                }
//            }

            }

            return visitOrder;
        }
    }

    public static class AdjArray {
        private final int[][] adjArray;
        private final int vertexCount;

        public AdjArray(int vertexCount) {
            this.vertexCount = vertexCount;
            this.adjArray = new int[vertexCount][vertexCount];
        }

        public void addEdge(int vertex, int otherVertex) {
            adjArray[vertex][otherVertex] = 1;
            adjArray[otherVertex][vertex] = 1;
        }

        public List<Integer> bfs(int startVertex) {
            List<Integer> visitOrder = new ArrayList<>();

            Queue<Integer> queue = new LinkedList<>();
            boolean[] visited = new boolean[vertexCount];

            queue.add(startVertex);
            visited[startVertex] = true;

            while (!queue.isEmpty()) {
                int visitVertex = queue.poll();
                visitOrder.add(visitVertex);

                for (int i = 0; i < adjArray[visitVertex].length; i++) {
                    if (adjArray[visitVertex][i] == 1 && !visited[i]) {
                        queue.add(i);
                        visited[i] = true;
                    }
                }
//                  같은 방식
//                for (int i = 0; i < vertexCount; i++) {
//                    if (adjArray[visitVertex][i] == 1 && !visited[i]) {
//                        queue.add(i);
//                        visited[i] = true;
//                    }
//                }
            }

            return visitOrder;
        }

    }

    public static void main(String[] args) {
        // 인접리스트
        GraphBFS.AdjList listGraph = new GraphBFS.AdjList(4);

        listGraph.addEdge(0, 1);
        listGraph.addEdge(0, 2);
        listGraph.addEdge(1, 2);
        listGraph.addEdge(2, 0);
        listGraph.addEdge(2, 3);
        listGraph.addEdge(3, 3);

        System.out.println(listGraph.bfs(2));

        listGraph = new GraphBFS.AdjList(5);

        listGraph.addEdge(0, 1);
        listGraph.addEdge(0, 2);
        listGraph.addEdge(0, 4);
        listGraph.addEdge(1, 2);
        listGraph.addEdge(3, 4);
        listGraph.addEdge(2, 3);
        listGraph.addEdge(2, 4);

        System.out.println(listGraph.bfs(0));

        // 인접행렬
        GraphBFS.AdjArray arrayGraph = new GraphBFS.AdjArray(4);

        arrayGraph.addEdge(0, 1);
        arrayGraph.addEdge(0, 2);
        arrayGraph.addEdge(1, 2);
        arrayGraph.addEdge(2, 0);
        arrayGraph.addEdge(2, 3);
        arrayGraph.addEdge(3, 3);

        System.out.println(arrayGraph.bfs(2));

        arrayGraph = new GraphBFS.AdjArray(5);

        arrayGraph.addEdge(0, 1);
        arrayGraph.addEdge(0, 2);
        arrayGraph.addEdge(0, 4);
        arrayGraph.addEdge(1, 2);
        arrayGraph.addEdge(3, 4);
        arrayGraph.addEdge(2, 3);
        arrayGraph.addEdge(2, 4);

        System.out.println(arrayGraph.bfs(0));

    }

}
