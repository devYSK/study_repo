package week04.problem5;

import java.util.Arrays;

/**
 * @author : ysk
 */
public class ArrayQueue implements Queue {

    private int[] dataArray;
    private int front;
    private int rear;
    private int size;
    private int count;

    public ArrayQueue() {
        this.size = 10;
        this.dataArray = new int[10];
        this.front = 0;
        this.rear = 0;
        this.count = 0;
    }

    @Override
    public boolean isEmpty() {
        return this.count == 0;
    }

    @Override
    public int peek() {
        return dataArray[front];
    }

    private boolean isFull() {
        return rear == (this.size - 1);
    }

    @Override
    public void push(int data) {
        if ((rear + 1) % dataArray.length == front) {
            reSize(size * 2);
        }

        rear = (rear + 1) % size;
        dataArray[rear] = data;
        count++;
    }

    @Override
    public int pop( ) {
        if (count == 0) {
            throw new RuntimeException("queue is empty");
        }

        front = (front + 1) % size;
        int popData = dataArray[front];
        dataArray[front] = 0;
        this.count -= 1;
        return popData;
    }

    @Override
    public int size() {
        return this.size;
    }

    public void printData() {
        System.out.println(Arrays.toString(dataArray));
    }

    public static void main(String[] args) {
        ArrayQueue arrayQueue = new ArrayQueue();

        for (int i = 1; i <= 20; i++) {
            arrayQueue.push(i);
        }

        arrayQueue.printData();

        for (int i = 0; i < 5; i++) {
            System.out.println("pop" + arrayQueue.pop());
        }

        arrayQueue.printData();

        for (int i = 20; i < 30; i++) {
            arrayQueue.push(i);
        }
        arrayQueue.printData();
    }

    private void reSize(int newCapacity) {
//        this.dataArray = Arrays.copyOf(dataArray, newCapacity);


        int[] newArray = new int[newCapacity];

        for (int i = 0, j = front + 1; i <= count; i++, j++) {
            newArray[i] = dataArray[j % this.size];
        }

        this.dataArray = newArray;
        this.size = newCapacity;
    }

}
