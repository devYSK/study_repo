package week04.problem2;

/**
 * @author : ysk
 */
public class ListNode implements LinkedList {

    private int data;

    private ListNode nextNode;

    private int size = 0;

    public int getSize() {
        return this.size;
    }

    public int getData() {
        return data;
    }

    public ListNode getNextNode() {
        return nextNode;
    }

    public ListNode() {
        this.nextNode = null;
        this.size = 0;
    }

    public ListNode(int data) {
        this.data = data;
        this.nextNode = null;
        this.size = 1;
    }

    public ListNode(int data, ListNode nextNode) {
        this.data = data;
        this.nextNode = nextNode;
    }

    @Override
    public ListNode add(ListNode head, ListNode nodeToAdd, int position) {

        ListNode node = head;

        validatePositionValue(position);

        for (int i = 0; i < position; i++) {
            node = node.nextNode;
        }

        nodeToAdd.nextNode = node.nextNode;
        node.nextNode = nodeToAdd;

        this.size += 1;
        return node;
    }

    public ListNode add(ListNode head, ListNode nodeToAdd) {
        ListNode node = head;

        while (node.nextNode != null) {
            node = node.nextNode;
        }
        node.nextNode = nodeToAdd;
        this.size += 1;
        return node;
    }


    @Override
    public ListNode remove(ListNode head, int positionToRemove) {

        ListNode node = head;
        validatePositionValue(positionToRemove);

        if (size == 0) {
            throw new RuntimeException("node size is 0");
        }

        if (positionToRemove == 0) {
            head = node.nextNode;
            node.nextNode = null;
            return head;
        }

        ListNode beforeNode = node;

        for (int i = 0; i < positionToRemove + 1; i++) {
            beforeNode = node;
            node = node.nextNode;
        }

        ListNode deleteNode = node;
        beforeNode.nextNode = deleteNode.nextNode;

        size -= 1;
        return deleteNode;
    }

    public void changeNextNode(ListNode node) {
        this.nextNode = node;
    }

    public void changeSize(int size) {
        this.size = size;
    }

    @Override
    public boolean contains(ListNode head, ListNode nodeToCheck) {

        while (head.nextNode != null) {
            if (head.nextNode == nodeToCheck) {
                return true;
            }
            head = head.nextNode;
        }

        return false;
    }


    private void validatePositionValue(int position) {

        if (position < 0) {
            throw new RuntimeException("position must be grater 0");
        }
        if (size < position) {
            throw new RuntimeException("size must be grater position. this size : " + size);
        }

    }

    public void printAllNode(ListNode head) {

        while (head.nextNode != null) {
            System.out.println("print! data : " + head.data + " size : " + size);
            head = head.nextNode;
        }
    }

    public void printAllData(ListNode head) {
        ListNode node = head.nextNode;
        while (node.nextNode != null) {
            System.out.print(" " + node.data + ",");
            node = node.nextNode;
        }
    }

}
