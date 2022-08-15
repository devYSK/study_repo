package week04.problem4;

import week04.problem2.ListNode;

/**
 * @author : ysk
 */
public class LinkedListStack {

    private final ListNode headNode;

    private int top;

    public LinkedListStack() {
        this.headNode = new ListNode();
        this.top = -1;
    }

    public LinkedListStack(int data) {
        this.headNode = new ListNode(data);
        this.top = 0;
    }

    public void push(int data) {
        ListNode newNode = new ListNode(data);
        this.headNode.add(headNode, newNode, ++top);
    }

    public int pop() {
        if (this.top < 0) {
            throw new RuntimeException("stack is empty");
        }

        return this.headNode.remove(headNode, top--).getData();
    }

    public void printData() {
        headNode.printAllData(headNode);
    }


    public static void main(String[] args) {
        LinkedListStack stack = new LinkedListStack();

        for (int i = 0; i < 100; i++) {
            stack.push(i);
        }

        while (true) {
            try {
                System.out.println(stack.pop());
            } catch (RuntimeException e) {
                break;
            }
        }
    }

}
