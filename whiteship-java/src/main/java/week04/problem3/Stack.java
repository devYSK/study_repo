package week04.problem3;

import java.util.Arrays;

/**
 * @author : ysk
 */
public class Stack {

    private int[] dataArray;

    private int top;

    private int size;

    public Stack(int data) {
        this.dataArray = new int[10];
        this.top = 0;
        this.dataArray[0] = data;
        this.size = 10;
    }

    public void push(int data) {
        if (top + 1 == size) {
            int upSize = size * 2;
            dataArray = Arrays.copyOf(dataArray, upSize);
            this.size = upSize;
        }

        dataArray[++top] = data;
    }

    public int pop() {
        if (top < 0) {
            throw new RuntimeException("stack is empty");
        }

        int topValue = dataArray[top];
        dataArray[top] = 0;
        top--;

        return topValue;
    }

    public int getTop() {
        return top;
    }

    public int getSize() {
        return size;
    }

    public static void main(String[] args) {
        Stack stack = new Stack(0);

        for (int i = 1; i < 100; i++) {
            stack.push(i);
        }

        for (int i = 0; i < 90; i++) {
            stack.pop();
        }

        System.out.println();
    }
}
