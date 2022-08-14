package heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : ysk
 */
public abstract class ListHeap<T> {

    abstract void add(T data);

    abstract T pop();

    abstract void heapifyUp();

    abstract void heapifyDown();

    abstract T peek();

    abstract int size();

    abstract boolean contains(T data);

    abstract void swap(int i, int j);

    protected int getParentIndex(int childIndex) {
        return (int) Math.floor((int) ((childIndex - 1) / 2));// 바닥함수. 내려야한다.
    }

    protected int getLeftChildIndex(int parentIndex) {
        return (2 * parentIndex) + 1;
    }

    protected int getRightChildIndex(int parentIndex) {
        return (2 * parentIndex) + 2;
    }

    protected boolean hasRightChild(int index) {
        return getRightChildIndex(index) < size();
    }

    protected boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < size();
    }

    protected boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    protected int lastIndex() {
        return this.size() - 1;
    }

    public void printElements() {
        while (this.size() > 0) {
            System.out.print(this.pop() + (this.size() != 1 ? ", " : ""));
        }
    }

}
