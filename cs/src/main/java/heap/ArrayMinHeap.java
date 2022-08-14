package heap;

import java.util.Arrays;

/**
 * @author : ysk
 */
public class ArrayMinHeap implements ArrayHeap {

    private int[] element;

    private int size;

    private int maxSize;

    private int leftChildValue(int parentIndex) {
        return element[getLeftChildIndex(parentIndex)];
    }

    private int rightChildValue(int parentIndex) {
        return element[getRightChildIndex(parentIndex)];
    }

    private int parentValue(int parentIndex) {
        return element[parentIndex];
    }

    private int getParentData(int index) {
        return element[getParentIndex(index)];
    }

    private boolean hasLeftChild(int index) { // 배열이기때문에 음수 값도 들어갈수 있어서 값 대신 size로 비교
        return getLeftChildIndex(index) < size;
    }

    private boolean hasRightChild(int index) { // 배열이기때문에 음수 값도 들어갈수 있어서 값 대신 size로 비교
        return getRightChildIndex(index) < size;
    }

    @Override
    public boolean hasParent(int index) {
        return getParentIndex(index) >= 1;
    }

    private void swap(int targetIndex, int otherIndex) {
        int temp = element[targetIndex];
        element[targetIndex] = element[otherIndex];
        element[otherIndex] = temp;
    }

    private void validateSizeAndResize() {
        if (size == this.maxSize) {
            element = Arrays.copyOf(element, maxSize * 2);
            maxSize *= 2;
        }
    }

    public ArrayMinHeap(int maxSize) {
        this.maxSize = maxSize;
        this.element = new int[maxSize + 1];
        Arrays.fill(element, Integer.MAX_VALUE);
        this.size = 0;
    }

    @Override
    public void add(int data) {
        validateSizeAndResize();

        this.element[++size] = data;
        heapifyUpUseWhile(); // or heapifyUpUseFor();
    }

    // insert 할때는 마지막 인덱스부터 루트 인덱스(1) 까지 bottom-up 방식으로 크기를 비교해서 재구성 한다.
    // min heap은 작은값이 부모가 되야한다.
    private void heapifyUpUseFor() {
        for (int i = size; i > 1; i--) {
            int parentIndex = getParentIndex(i);

            if (hasParent(i) && element[parentIndex] > element[i]) {
                swap(parentIndex, i);
            }
        }
    }

    private void heapifyUpUseWhile() {
        int childIndex = size;

        while (hasParent(childIndex) && getParentData(childIndex) > element[childIndex]) {
            swap(getParentIndex(childIndex), childIndex);
            childIndex = getParentIndex(childIndex);
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

    private void heapifyDownUseFor() {
        for (int parent = 1; hasLeftChild(parent); ) {

            if (parentValue(parent) < leftChildValue(parent) && parentValue(parent) < rightChildValue(parent)) {
                break;

            } else if (leftChildValue(parent) < rightChildValue(parent)) {
                swap(parent, getLeftChildIndex(parent));
                parent = parent * 2;
            } else {
                swap(parent, getRightChildIndex(parent));
                parent = parent * 2 + 1;
            }

        }
    }

    // 자식 값중 더 작은 값을 교체해줘야 한다.
    public void heapifyDownUserWhile() {

        int parent = 1;

        while (hasLeftChild(parent)) {

            int smallIndex = smallerOfTheChildren(parent);

            if (hasRightChild(parent) && rightChildValue(parent) < leftChildValue(parent)) {
                smallIndex = getRightChildIndex(parent);
            }

            if (element[parent] < element[smallIndex]) {
                break;
            }

            swap(parent, smallIndex);
            parent = smallIndex;

        }
    }

    private int smallerOfTheChildren(int parentIndex) {

        if (hasLeftChild(parentIndex) && leftChildValue(parentIndex) < rightChildValue(parentIndex)) {
            return getLeftChildIndex(parentIndex);
        } else {
            return getRightChildIndex(parentIndex);
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
        ArrayMinHeap arrayMinHeap = new ArrayMinHeap(10);

        arrayMinHeap.add(5);
        arrayMinHeap.add(4);
        arrayMinHeap.add(2);
        arrayMinHeap.add(7);
        arrayMinHeap.add(8);
        arrayMinHeap.add(9);
        arrayMinHeap.add(3);
        arrayMinHeap.add(6);
        arrayMinHeap.add(15);
        arrayMinHeap.add(999);
        arrayMinHeap.add(2134134);
        arrayMinHeap.add(21);
        arrayMinHeap.add(1);
        arrayMinHeap.add(9918324);
        arrayMinHeap.add(239328);
        arrayMinHeap.add(8237237);
        arrayMinHeap.add(99999999);

        arrayMinHeap.printElements();
    }
}
