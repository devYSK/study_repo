package heap;

import java.util.Arrays;

/**
 * @author : ysk
 */
public class ArrayMaxHeap implements ArrayHeap {

    private int[] element;
    private int size;
    private int maxSize;

    public ArrayMaxHeap(int maxSize) {
        this.maxSize = maxSize;
        this.element = new int[maxSize + 1];
        this.size = 0;
    }

    private int leftChildValue(int parentIndex) {
        return element[getLeftChildIndex(parentIndex)];
    }

    private int rightChildValue(int parentIndex) {
        return element[getRightChildIndex(parentIndex)];
    }

    private int parentValueFromChild(int childIndex) {
        return element[getParentIndex(childIndex)];
    }

    private int parentValue(int parentIndex) {
        return element[parentIndex];
    }

    private int getParentData(int index) {
        return element[getParentIndex(index)];
    }

    private boolean isGraterThenChild(int parentIndex) {
        return parentValue(parentIndex) > leftChildValue(parentIndex) && parentValue(parentIndex) > rightChildValue(parentIndex);
    }

    private void swap(int targetIndex, int otherIndex) {
        int temp = element[targetIndex];
        element[targetIndex] = element[otherIndex];
        element[otherIndex] = temp;
    }

    @Override
    public void add(int data) {

        validateSizeAndResize();

        this.element[++size] = data;
        heapifyUpUseWhile(); // or heapifyUpUseFor();
    }

    // insert 할때는 마지막 인덱스부터 루트 인덱스(1) 까지 bottom-up 방식으로 크기를 비교해서 재구성 한다.
    // maxHeap은 큰값이 부모가 되어야 한다
    private void heapifyUpUseFor() {
        for (int i = size; i > 1; i--) {
            int parentIndex = getParentIndex(i);

            if (element[parentIndex] < element[i]) {
                swap(parentIndex, i);
            }
        }
    }

    // insert 할때는 마지막 인덱스부터 루트 인덱스(1) 까지 bottom-up 방식으로 크기를 비교해서 재구성 한다.
    private void heapifyUpUseWhile() {
        int childIndex = size;

        while (hasParent(childIndex) && getParentData(childIndex) < element[childIndex]) {
            swap(getParentIndex(childIndex), childIndex);
            childIndex = getParentIndex(childIndex);
        }
    }

    private void validateSizeAndResize() {
        if (size == this.maxSize) {
            element = Arrays.copyOf(element, maxSize * 2);
            maxSize *= 2;
        }
    }

    @Override
    public int pop() {
        if (size == 0) {
            throw new ArrayIndexOutOfBoundsException("heap is empty");
        }

        int root = element[1];

        element[1] = element[size];
        element[size] = 0;
        this.size = size - 1;

        heapifyDownUseFor();// or heapifyDownUserWhile();

        return root;
    }

    // delete 할때는 루트 인덱스부터 마지막 인덱스까지 top-down 크기를 비교해서 재구성 한다.
    public void heapifyDownUseFor() {
        for (int parent = 1; parent * 2 <= size; ) {

            if (isGraterThenChild(parent)) {
                break;

            } else if (leftChildValue(parent) > rightChildValue(parent)) {
                swap(parent, getLeftChildIndex(parent));
                parent = parent * 2;
            } else {
                swap(parent, getRightChildIndex(parent));
                parent = parent * 2 + 1;
            }

        }
    }

    public void heapifyDownUserWhile() {
        int parent = 1;

        while (parent * 2 <= size) {

            if (isGraterThenChild(parent)) {
                break;

            } else if (leftChildValue(parent) > rightChildValue(parent)) {
                swap(parent, getLeftChildIndex(parent));
                parent = parent * 2;
            } else {
                swap(parent, getRightChildIndex(parent));
                parent = parent * 2 + 1;
            }

        }
    }

    @Override
    public void printElements() {
        if (this.size == 0) {
            return;
        }

        while (!(this.size == 0)) {
            System.out.print(pop() + ", ");
        }

    }

    @Override
    public boolean contains(int data) {
        for (int i = 1; i <= size; i++) {
            if (element[i] == data) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int peek() {
        return element[1];
    }


    @Override
    public int size() {
        return this.size;
    }

    public static void main(String[] args) {
        ArrayHeap arrayMaxHeap = new ArrayMaxHeap(10);

        arrayMaxHeap.add(5);
        arrayMaxHeap.add(4);
        arrayMaxHeap.add(2);
        arrayMaxHeap.add(7);
        arrayMaxHeap.add(8);
        arrayMaxHeap.add(9);
        arrayMaxHeap.add(3);
        arrayMaxHeap.add(6);
        arrayMaxHeap.add(15);
        arrayMaxHeap.add(999);
        arrayMaxHeap.add(2134134);
        arrayMaxHeap.add(21);
        arrayMaxHeap.add(1);
        arrayMaxHeap.add(9918324);
        arrayMaxHeap.add(239328);
        arrayMaxHeap.add(8237237);
        arrayMaxHeap.add(99999999);
        arrayMaxHeap.printElements();
    }
}
