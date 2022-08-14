package graph;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @author : ysk
 */
public class GraphDFS {

    public static class AdjList {
        private int vertexCount;

        private LinkedList<Integer> adj[];

        private boolean[] visited;

        public AdjList(int vertexCount) {
            this.vertexCount = vertexCount;
            adj = new LinkedList[vertexCount];
            for (int i = 0; i < vertexCount; i++) {
                adj[i] = new LinkedList<>();
            }
            visited = new boolean[vertexCount];
        }

        public void addEdge(int vertex, int otherVertex) {
            adj[vertex].add(otherVertex);
            adj[otherVertex].add(vertex);

        }

        public void dfsRecursive(int vertex) {
            visited[vertex] = true;

            System.out.println("visited -> " + vertex);

            for (int next : adj[vertex]) {
                if (!visited[next]) {
                    dfsRecursive(next);
                }
            }
//            while (iterator.hasNext()) {
//                int next = iterator.next();
//
//                if (!visited[next]) {
//                    dfsRecursive(next);
//                }
//            }
        }

        public void dfsStack(int startVertex) {

            Stack<Integer> stack = new Stack<>();
            stack.push(startVertex);
            visited[startVertex] = true;

            while (!stack.isEmpty()) {
                int vertex = stack.pop();

                System.out.println("visited -> " + vertex);

                for (int next : adj[vertex]) {
                    if (!visited[next]) {
                        stack.push(next);
                        visited[next] = true;
                    }
                }

            }

        }

    }


    public static class AdjArray {
        private int[][] adjArray;
        private int vertexCount;
        private boolean[] visited;

        public AdjArray(int vertexCount) {
            this.vertexCount = vertexCount;
            adjArray = new int[vertexCount][vertexCount];
            visited = new boolean[vertexCount];
        }

        public void addEdge(int vertex, int otherVertex) {
            adjArray[vertex][otherVertex] = 1;
            adjArray[otherVertex][vertex] = 1;
        }

        public void dfsRecursive(int vertex) {
            visited[vertex] = true;

            System.out.println("visited -> " + vertex);

            for (int i = 0; i < adjArray[vertex].length; i++) {
                if (adjArray[vertex][i] == 1 && !visited[i]) {
                    dfsRecursive(i);
                }
            }

        }

        public void dfsStack(int startVertex) {

            Stack<Integer> stack = new Stack<>();
            stack.push(startVertex);
            visited[startVertex] = true;

            while (!stack.isEmpty()) {
                int vertex = stack.pop();

                System.out.println("visited -> " + vertex);

                for (int i = 0; i < adjArray[vertex].length; i++) {
                    if (adjArray[vertex][i] == 1 && !visited[i]) {
                        stack.push(i);
                        visited[i] = true;
                    }
                }

            }

        }

    }

    public static void main(String[] args) {
//        GraphDFS.AdjList listGraph = new GraphDFS.AdjList(6);
//
//        listGraph.addEdge(0, 1);
//        listGraph.addEdge(1, 2);
//        listGraph.addEdge(2, 3);
//        listGraph.addEdge(3, 4);
//        listGraph.addEdge(3, 5);
//
//        listGraph.dfsRecursive(0);
//
//        listGraph = new GraphDFS.AdjList(6);
//
//        listGraph.addEdge(0, 1);
//        listGraph.addEdge(1, 2);
//        listGraph.addEdge(2, 3);
//        listGraph.addEdge(3, 4);
//        listGraph.addEdge(3, 5);
//
//        listGraph.dfsStack(0);

        AdjArray arrayGraph = new AdjArray(6);

        arrayGraph.addEdge(0, 1);
        arrayGraph.addEdge(1, 2);
        arrayGraph.addEdge(2, 3);
        arrayGraph.addEdge(3, 4);
        arrayGraph.addEdge(3, 5);

        arrayGraph.dfsRecursive(0);

        arrayGraph = new GraphDFS.AdjArray(6);

        arrayGraph.addEdge(0, 1);
        arrayGraph.addEdge(1, 2);
        arrayGraph.addEdge(2, 3);
        arrayGraph.addEdge(3, 4);
        arrayGraph.addEdge(3, 5);

        arrayGraph.dfsStack(0);

    }


}
