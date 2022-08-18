package week05;

/**
 * @author : ysk
 */
public class Node {

    private int value;

    private Node left;

    private Node right;

    public Node(int value) {
        this.value = value;
    }

    public void addLeftNode(Node leftNode) {
        this.left = leftNode;
    }

    public void addRightNode(Node rightNode) {
        this.right = rightNode;
    }

    public int getValue() {
        return value;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }
}
