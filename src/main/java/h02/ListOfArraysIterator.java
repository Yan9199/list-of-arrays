package h02;

import java.util.NoSuchElementException;

/**
 * Represents an iterator over a ListOfArrays object.
 *
 * @param <T> The generic type of the list that this iterator iterates on.
 */
public class ListOfArraysIterator<T> {
    private ListOfArraysItem<T> headPointer;
    private T elementPointer;
    private int index;
    private boolean noNext;
    private static final int LENGTH = ListOfArrays.getArrayLength();

    /**
     * A constructor to construct a ListOfArraysIterator object.
     *
     * @param head The head of the list to iterate over.
     */
    public ListOfArraysIterator(ListOfArraysItem<T> head) {
        if ((headPointer = head) == null) {
            noNext = true;
            return;
        }
        noNext = false;
        if ((elementPointer = headPointer.array[0]) == null) assignNextElement();
    }

    /**
     * Returns whether there is another element to be iterated on.
     *
     * @return True iff there is another element.
     */
    public boolean hasNext() {
        return !noNext;
    }

    /**
     * Returns the next element of this iterator and moves the iterator one element forward.
     *
     * @return The next element.
     * @throws NoSuchElementException If there is no next element.
     */
    public T next() throws NoSuchElementException {
        if (noNext) throw new NoSuchElementException();
        T elementSave = elementPointer;
        assignNextElement();
        return elementSave;
    }

    private void assignNextElement() {
        index++;
        while (headPointer != null) {
            while (index < LENGTH) {
                if ((elementPointer = headPointer.array[index]) != null) return;
                index++;
            }
            headPointer = headPointer.next;
            index = 0;
        }
        noNext = true;
    }

    public void printAll() {
        System.out.print("[");
        while (hasNext()) {
            System.out.print(next());
            if (hasNext()) System.out.print(", ");
        }
        System.out.println("]");
    }
}
