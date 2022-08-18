package week05;

import java.util.*;

/**
 * @author : ysk
 */
public class BinaryTree {

    private Node root;

    private int size;

    public BinaryTree() {
    }

    public BinaryTree(Node rootNode) {
        this.root = rootNode;
    }

    public void insert(int value) {
        Node newNode = new Node(value);

        if (this.root == null) {
            this.root = newNode;
            return;
        }

        Node currentNode = root;
        Node parent;
        while (true) {
            parent = currentNode;
            if (currentNode.getValue() > value) {
                currentNode = currentNode.getLeft();

                if (currentNode == null) {
                    parent.addLeftNode(newNode);
                    this.size += 1;
                    break;
                }

            } else {
                currentNode = currentNode.getRight();

                if (currentNode == null) {
                    parent.addRightNode(newNode);
                    this.size += 1;
                    break;
                }

            }


        }

    }

    public boolean find(int value) {

        Node currentNode = root;

        while (currentNode != null) {
            if (currentNode.getValue() == value) {
                return true;
            } else if (currentNode.getValue() < value) {
                currentNode = currentNode.getLeft();
            } else {
                currentNode = currentNode.getRight();
            }
        }

        return false;
    }

    private List<Integer> preOrder(Node node, List<Integer> order) {
        if (node != null) {
            order.add(node.getValue());
            preOrder(node.getLeft(), order);
            preOrder(node.getRight(), order);
        }

        return order;
    }

    private List<Integer> inOrder(Node node, List<Integer> order) {
        if (node != null) {
            inOrder(node.getLeft(), order);
            order.add(node.getValue());
            inOrder(node.getRight(), order);
        }
        return order;
    }

    private List<Integer> postOrder(Node node, List<Integer> order) {
        if (node != null) {
            postOrder(node.getLeft(), order);
            postOrder(node.getRight(), order);
            order.add(node.getValue());
        }

        return order;
    }


    public List<Integer> dfs(Node node) { // 전위탐색 = 깊이우선탐색

        if (root == null) {
            return null;
        }

        List<Integer> order = new ArrayList<>();

        Stack<Node> stack = new Stack<>();

        Node current = root;
        stack.push(node);

        while (!stack.isEmpty()) {

            while (current.getLeft() != null) {
                if (current.getLeft() != null) {
                    current = current.getLeft();
                    stack.push(current);
                }
            }
            current = stack.pop();
            order.add(current.getValue());

            if (current.getRight() != null) {
                current = current.getRight();
                stack.push(current);
            }
        }


        return order;
    }

    public List<Integer> dfsRecursive(Node node, List<Integer> order) {
        if (node == null) return null;

        if (node.getLeft() != null) {
            dfsRecursive(node.getLeft(), order);
        }

        order.add(node.getValue());

        if (node.getRight() != null) {
            dfsRecursive(node.getRight(), order);
        }
        return order;
    }


    public List<Integer> bfs(Node node) {
        if (node == null) {
            return null;
        }

        List<Integer> order = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        queue.add(node);

        while (!queue.isEmpty()) {
            Node poll = queue.poll();
            order.add(poll.getValue());

            if (poll.getLeft() != null) {
                queue.offer(poll.getLeft());
            }

            if (poll.getRight() != null) {
                queue.offer(poll.getRight());
            }

        }

        return order;
    }


    public Node getRoot() {
        return root;
    }

    public static void main(String[] args) {
        BinaryTree binaryTree = new BinaryTree();

        binaryTree.insert(7);
        binaryTree.insert(5);
        binaryTree.insert(9);
        binaryTree.insert(3);
        binaryTree.insert(6);
        binaryTree.insert(8);
        binaryTree.insert(10);


//        System.out.println(binaryTree.inOrder(binaryTree.getRoot(), new ArrayList<>()));
//        System.out.println(binaryTree.bfs(binaryTree.getRoot()));

        System.out.println(binaryTree.dfs(binaryTree.getRoot()));
        System.out.println(binaryTree.dfsRecursive(binaryTree.getRoot(), new ArrayList<>()));
    }
}
