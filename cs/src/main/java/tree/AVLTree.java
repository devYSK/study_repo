package tree;

/**
 * @author : ysk
 */
public class AVLTree {

    private Node root;

    private void updateHeight(Node n) {
        n.setHeight(1 + Math.max(height(n.getLeft()), height(n.getRight())));
    }

    private int height(Node n) {
        return n == null ? -1 : n.getHeight();
    }

    private int getBalanceFactor(Node n) {
        return n == null ? 0 : height(n.getRight()) - height(n.getLeft());
    }

    private Node rotateLeft(Node z) { // z 본인, 할아버지. y 본인의 오른쪽노드. x  손주 y의 왼쪽 노드
        Node y = z.getRight();
        Node x = y.getLeft();

        // y의 왼쪽 자식 노드를 z로 지정
        y.setLeft(z);
        // z의 오른쪽 자식 노드를 x로 지정
        z.setRight(x);

        updateHeight(z);
        updateHeight(y);

        return y; // 새로운 루트 노드 반환
    }

    private Node rotateRight(Node z) { // z 본인, 할아버지. y 본인의 왼쪽노드 . x  손주 y의 왼쪽 노드
        Node y = z.getLeft();
        Node x = y.getRight();

        y.setRight(z);
        z.setLeft(x);

        updateHeight(z);
        updateHeight(y);

        return y; // 새로운 루트노드
    }

    private Node insert(int key) {

        Node newNode = new Node(key);

        if (root == null) {
            root = newNode;
            return newNode;
        }

        Node rootNode = root;

        while (true) {
            if (key < rootNode.getKey()) {
                if (rootNode.getLeft() != null) {
                    rootNode = rootNode.getLeft();
                } else {
                    rootNode.setLeft(newNode);
                    return newNode;
                }
            } else if(key > rootNode.getKey()) {
                if (rootNode.getRight() != null) {
                    rootNode = rootNode.getRight();
                } else {
                    rootNode.setRight(newNode);
                    return newNode;
                }
            } else {
                throw new IllegalArgumentException("has Contain, value : " + key);
            }
        }

    }

    public void insertNode(Integer... values) {
        for (Integer value : values) {
            insertNode(value);
        }
    }

    public Node insertNode(int key) {

        Node insertNode = insert(key);

        updateHeight(insertNode);

        return rebalance(insertNode);
    }

    private Node rebalance(Node node) {

        int balanceFactor = this.getBalanceFactor(node);

        if (balanceFactor < -1) { // RR : 현재 노드 기준으로 오른쪽 서브트리가 더 커서 발란스가 작아진경우
            if (getBalanceFactor(node.getLeft()) <= 0) {
                node = rotateRight(node);
            } else { // LR
              node.setLeft(rotateLeft(node.getLeft()));
              node = rotateRight(node);
            }
        }

        if (balanceFactor > 1) { // LL
            if (getBalanceFactor(node.getRight()) > 0) { // LL
                node = rotateLeft(node);
            } else { // RL
                node.setRight(rotateRight(node.getRight()));
                node = rotateLeft(node);
            }
        }

        return node;
    }

    public Node searchNode(int key) {
        Node node = root;

        while(node != null) {
            if (key == node.getKey()) {
                return node;
            } else if (key < node.getKey()) {
                node = node.getLeft();
            } else {
                node = node.getRight();
            }
        }
        return null;
    }

    public Node getRoot() {
        return this.root;
    }

    public void inOrder(Node node) {
        if (node != null) {
            if (node.getLeft() != null) {
                inOrder(node.getLeft());
            }
            System.out.print(node.getKey() + ", ");
            if (node.getRight() != null) {
                inOrder(node.getRight());
            }
        }

    }

    public static void main(String[] args) {
        AVLTree avlTree = new AVLTree();

        avlTree.insertNode(3, 5, 4, 7, 9, 11, 15, 14, 10, 2, 99, 98, 94, 90, 18, 19, 23, 25, 53, 70, 88, 46, 64, 72);

        Node root = avlTree.getRoot();

        avlTree.inOrder(root);

    }
}