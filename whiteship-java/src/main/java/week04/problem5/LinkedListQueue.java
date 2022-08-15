package week04.problem5;

import week04.problem2.ListNode;

/**
 * @author : ysk
 */
public class LinkedListQueue implements Queue {

    private ListNode head;

    public LinkedListQueue() {
    }

    @Override
    public boolean isEmpty() {
        return head == null || head.getSize() == 0;
    }

    @Override
    public int peek() {
        if (size() == 0) {
            throw new RuntimeException("queue is empty");
        }
        return head.getData();
    }

    @Override
    public void push(int data) {
        if (this.head == null) {
            this.head = new ListNode(data);
            return;
        }

        head.add(head, new ListNode(data));
    }

    @Override
    public int pop() {

        ListNode deleteNode = head;

        if (head.getSize() == 0) {
            throw new RuntimeException("deleteNode size is 0");
        } else if (size() == 1) {
            int data = head.getData();
            head = null;
            return data;
        }

        head = deleteNode.getNextNode();
        deleteNode.changeNextNode(null);
        head.changeSize(deleteNode.getSize() -1);

        return deleteNode.getData();
    }

    @Override
    public int size() {
        return head.getSize();
    }

    public static void main(String[] args) {
        LinkedListQueue queue = new LinkedListQueue();

        for (int i = 0; i < 100; i++) {
            queue.push(i);
        }

        while (!queue.isEmpty()) {
            System.out.println(queue.pop());
        }

    }
}
