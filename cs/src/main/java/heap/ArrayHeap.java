package heap;

/**
 * @author : ysk
 */
public interface ArrayHeap {
    void add(int data);

    void printElements();

    boolean contains(int data);

    int peek();

    int pop();

    int size();

    default int getParentIndex(int childIndex) {
        return (int) Math.floor(childIndex / 2);// 바닥함수. 내려야한다.
    }

    default int getLeftChildIndex(int parentIndex) {
        return (2 * parentIndex) + 1;
    }

    default int getRightChildIndex(int parentIndex) {
        return (2 * parentIndex) + 2;
    }
    default boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

}
