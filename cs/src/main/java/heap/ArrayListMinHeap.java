package heap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : ysk
 */
public class ArrayListMinHeap<T extends Comparable<T>> extends ListHeap<T> {

    private final List<T> elements;

    public ArrayListMinHeap() {
        this.elements = new ArrayList<>();
    }

    @Override
    public void add(T data) {
        elements.add(data);
        heapifyUp();
    }

    @Override
    public T pop() {
        if (this.size() == 0) {
            throw new ArrayIndexOutOfBoundsException("heap is empty");
        }

        final T pop = elements.get(0);

        elements.set(0, elements.get(lastIndex()));
        elements.remove(lastIndex());

        heapifyDown();

        return pop;
    }

    private boolean isParentBigThenChild(int childIndex) {
        return elements.get(getParentIndex(childIndex)).compareTo(elements.get(childIndex)) > 0;
    }

    @Override
    protected void heapifyUp() {
        int childIndex = lastIndex();

        while (hasParent(childIndex) && isParentBigThenChild(childIndex)) {
            swap(getParentIndex(childIndex), childIndex);
            childIndex = getParentIndex(childIndex);
        }
    }

    @Override
    protected void heapifyDown() {
        int index = 0;

        while (hasLeftChild(index)) { // 왼쪽 자식이 없으면 오른쪽 자식도 없다.
            int smallChildIndex = getLeftChildIndex(index);

            if (hasRightChild(index) && isRightChildSmallThenLeftChild(index)) {
                smallChildIndex = getRightChildIndex(index);
            }

            if (isParentSmallThenSmallChild(index, smallChildIndex)) {
                break;
            } else {
                swap(smallChildIndex, index);
                index = smallChildIndex;
            }
        }

    }

    private boolean isParentSmallThenSmallChild(int parentIndex, int childIndex) {
        return elements.get(parentIndex).compareTo(elements.get(childIndex)) < 0;
    }

    private boolean isRightChildSmallThenLeftChild(int parentIndex) {
        return elements.get(getRightChildIndex(parentIndex)).compareTo(elements.get(getLeftChildIndex(parentIndex))) < 0;
    }

    @Override
    public T peek() {
        return this.elements.get(0);
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public boolean contains(T data) {
        return this.elements.contains(data);
    }

    @Override
    protected void swap(int i, int j) {
        final T temp = elements.get(i);
        elements.set(i, elements.get(j));
        elements.set(j, temp);
    }

    public static void main(String[] args) {
        ListHeap<Integer> arrayListListHeap = new ArrayListMinHeap<>();

        arrayListListHeap.add(5);
        arrayListListHeap.add(4);
        arrayListListHeap.add(2);
        arrayListListHeap.add(7);
        arrayListListHeap.add(8);
        arrayListListHeap.add(9);
        arrayListListHeap.add(3);
        arrayListListHeap.add(6);
        arrayListListHeap.add(15);
        arrayListListHeap.add(999);
        arrayListListHeap.add(21);
        arrayListListHeap.add(1);
        arrayListListHeap.add(2393);
        arrayListListHeap.add(8237);
        arrayListListHeap.add(9999);
        arrayListListHeap.add(999);
        arrayListListHeap.add(999);
        arrayListListHeap.add(999);


        arrayListListHeap.printElements();
    }
}
