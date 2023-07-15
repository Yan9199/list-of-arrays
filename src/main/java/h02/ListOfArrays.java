package h02;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * A list of ListOfArraysItem objects.
 * Contains the length of the arrays of this list and a reference to the first and last item (or null as an empty list).
 *
 * @param <T> The generic type of this list
 */
public class ListOfArrays<T> {
    /**
     * The length of the arrays of the items of this list.
     */
    private static final int ARRAY_LENGTH;

    static {
        ARRAY_LENGTH = 6;
    }

    public static int getArrayLength() {
        return ARRAY_LENGTH;
    }

    /**
     * The head of this list.
     */
    public ListOfArraysItem<T> head;

    /**
     * The tail of this list.
     */
    public ListOfArraysItem<T> tail;

    /**
     * Constructs a list of ListOfArrayItem objects that represents the elements given in sequence.
     *
     * @param sequence The elements to be added to the list.
     */
    @SuppressWarnings("unchecked")
    public ListOfArrays(T[] sequence) {
        if (sequence == null || sequence.length == 0 || ARRAY_LENGTH <= 0) return;
        final int len = sequence.length, loops = len / ARRAY_LENGTH, excess = len % ARRAY_LENGTH;
        int sequenceIndex = 0;
        head = new ListOfArraysItem<>();
        ListOfArraysItem<T> pointer = head;
        for (int i = 0; i < loops; i++) {
            T[] array = (T[]) new Object[ARRAY_LENGTH];
            for (int j = 0; j < ARRAY_LENGTH; j++, sequenceIndex++) array[j] = sequence[sequenceIndex];
            pointer.add(array);
            pointer = pointer.getTail();
            pointer.currentNumber = ARRAY_LENGTH;
        }
        if (excess == 0) {
            tail = pointer;
            return;
        }
        T[] array = (T[]) new Object[ARRAY_LENGTH];
        for (int i = 0; i < excess; i++, sequenceIndex++) array[i] = sequence[sequenceIndex];
        pointer.add(array);
        tail = pointer.getTail();
        tail.currentNumber = excess;
    }

    public void printAll() {
        for (ListOfArraysItem<T> p = head; p != null; p = p.next) {
            System.out.println(Arrays.toString(p.array));
            System.out.println();
        }
    }

    public void printAllWithNumber() {
        for (ListOfArraysItem<T> p = head; p != null; p = p.next) {
            System.out.println("currentNumber: " + p.currentNumber);
            System.out.println(Arrays.toString(p.array));
            System.out.println();
        }
    }

    public void printTail() {
        if (tail == null) {
            System.out.println("Tail is null");
            return;
        }
        System.out.println("Tail");
        System.out.println(Arrays.toString(tail.array));
        System.out.println();
    }

    public void printTailWithNumber() {
        if (tail == null) {
            System.out.println("Tail is null");
            return;
        }
        System.out.println("Tail");
        System.out.println("currentNumber: " + tail.currentNumber);
        System.out.println(Arrays.toString(tail.array));
        System.out.println();
    }

    /**
     * Returns an iterator over this list.
     *
     * @return The iterator of type ListOfArraysIterator.
     */
    public ListOfArraysIterator<T> iterator() {
        return new ListOfArraysIterator<>(head);
    }

    /**
     * Inserts a given collection into this list at index i
     * (elements in front of index i will stay and elements at index i and after will be pushed behind the added collection).
     *
     * @param collection The collection to add.
     * @param i          The index at which the collection should be inserted.
     * @throws IndexOutOfBoundsException If the given index is not within the bounds of this list.
     */
    @SuppressWarnings("unchecked")
    public void insert(Collection<T> collection, int i) throws IndexOutOfBoundsException {
        if (i < 0 || (head != null && i > head.getNumberOfElementsInSequence()) || (head == null && i != 0))
            throw new IndexOutOfBoundsException(i);
        if (collection.isEmpty()) return;
        Iterator<T> it = collection.iterator();
        int items = 0;
        removeEmptyArrays();
        if (head == null) {
            head = new ListOfArraysItem<>();
            ListOfArraysItem<T> tailHelper = head;
            while (it.hasNext()) {
                T[] array = (T[]) new Object[ARRAY_LENGTH];
                int counter = 0;
                for (; counter < ARRAY_LENGTH && it.hasNext(); counter++) array[counter] = it.next();
                tailHelper.add(array);
                tailHelper = tailHelper.getTail();
                tailHelper.currentNumber = counter;
            }
            tail = tailHelper;
            return;
        }
        ListOfArraysItem<T> itemContainingIndex = head;
        int index = i;
        while (index > itemContainingIndex.currentNumber) {
            index -= itemContainingIndex.currentNumber;
            itemContainingIndex = itemContainingIndex.next;
        }
        if (index == itemContainingIndex.currentNumber) {
            while (index < ARRAY_LENGTH && it.hasNext()) {
                itemContainingIndex.array[index] = it.next();
                items++;
                itemContainingIndex.currentNumber++;
                index++;
            }
            if (!it.hasNext()) {
                tail = head.getTail();
                return;
            }
            ListOfArraysItem<T> previous = itemContainingIndex;
            if ((itemContainingIndex = itemContainingIndex.next) == null) {
                previous.next = new ListOfArraysItem<>();
                ListOfArraysItem<T> tailHelper = previous.next;
                while (it.hasNext()) {
                    T[] array = (T[]) new Object[ARRAY_LENGTH];
                    int counter = 0;
                    for (; counter < ARRAY_LENGTH && it.hasNext(); counter++) array[counter] = it.next();
                    tailHelper.add(array);
                    tailHelper = tailHelper.getTail();
                    tailHelper.currentNumber = counter;
                }
                tail = tailHelper;
                return;
            }
            index = 0;
        }
        T[] tmp = itemContainingIndex.getArrayCopy(index);
        final int size = collection.size() - items + ListOfArraysItem.arrayLength(tmp);
        int tmpIndex = 0;
        if (size <= ARRAY_LENGTH) {
            while (it.hasNext()) {
                itemContainingIndex.array[index] = it.next();
                index++;
            }
            for (final int len = tmp.length; index < len; index++, tmpIndex++) {
                T t = tmp[tmpIndex];
                if (t == null) break;
                itemContainingIndex.array[index] = t;
            }
            itemContainingIndex.currentNumber = size;
            return;
        }
        itemContainingIndex.currentNumber = ARRAY_LENGTH;
        while (index < ARRAY_LENGTH && it.hasNext()) {
            itemContainingIndex.array[index] = it.next();
            index++;
        }
        if (!it.hasNext()) {
            T[] array = (T[]) new Object[ARRAY_LENGTH];
            for (; index < ARRAY_LENGTH; index++, tmpIndex++) itemContainingIndex.array[index] = tmp[tmpIndex];
            int counter = 0;
            for (; tmpIndex < ARRAY_LENGTH; counter++, tmpIndex++) {
                T t = tmp[tmpIndex];
                if (t == null) break;
                array[counter] = t;
            }
            ListOfArraysItem<T> next = new ListOfArraysItem<>(array);
            next.currentNumber = counter;
            next.next = itemContainingIndex.next;
            itemContainingIndex.next = next;
            tail = next.getTail();
            return;
        }
        ListOfArraysItem<T> saveNext = itemContainingIndex.next;
        itemContainingIndex.next = null;
        ListOfArraysItem<T> tailHelper = itemContainingIndex;
        while (it.hasNext()) {
            T[] array = (T[]) new Object[ARRAY_LENGTH];
            for (index = 0; index < ARRAY_LENGTH && it.hasNext(); index++) array[index] = it.next();
            tailHelper.add(array);
            tailHelper = tailHelper.getTail();
            tailHelper.currentNumber = index;
        }
        final int len = tmp.length;
        boolean end = tmpIndex == len;
        while (index < ARRAY_LENGTH) {
            if (tmpIndex == len) {
                end = true;
                break;
            }
            T t = tmp[tmpIndex];
            if (t == null) {
                end = true;
                break;
            }
            tailHelper.array[index] = t;
            index++;
            tmpIndex++;
            if (tmpIndex < len && tmp[tmpIndex] == null) {
                end = true;
                break;
            }
        }
        if (end) {
            tailHelper.currentNumber = index;
            tailHelper.next = saveNext;
            tail = tailHelper.getTail();
            return;
        }
        tailHelper.currentNumber = ARRAY_LENGTH;
        T[] array = (T[]) new Object[ARRAY_LENGTH];
        for (index = 0; tmpIndex < len; index++, tmpIndex++) {
            T t;
            if ((t = tmp[tmpIndex]) == null) break;
            array[index] = t;
        }
        tailHelper.add(array);
        tailHelper = tailHelper.getTail();
        tailHelper.currentNumber = index;
        tailHelper.next = saveNext;
        tail = tailHelper.getTail();
    }

    private void removeEmptyArrays() {
        for (ListOfArraysItem<T> p = head, previous = head; p != null; p = p.next) {
            if (p.currentNumber == 0) {
                if (p == head) previous = head = head.next;
                else previous.next = p.next;
                continue;
            }
            if (p != head) previous = previous.next;
        }
    }

    /**
     * Extracts a block of elements of this list. The block is defined by the boundary indices i and j (both included in the block that will be extracted) as a ListOfArrays object.
     * This will delete the extracted elements from the list.
     *
     * @param i The lower boundary index.
     * @param j The higher boundary index.
     * @return The extracted elements as a ListOfArrays object.
     */
    @SuppressWarnings("unchecked")
    public ListOfArrays<T> extract(int i, int j) {
        if (i < 0) throw new IndexOutOfBoundsException(i);
        if (j > (head == null ? -1 : head.getNumberOfElementsInSequence() - 1))
            throw new IndexOutOfBoundsException(j);
        if (i > j) throw new IndexOutOfBoundsException(i + " is greater than " + j);
        removeEmptyArrays();
        int numberOfElementsToExtract = j - i + 1;
        T[] resultArray = (T[]) new Object[numberOfElementsToExtract];
        int arrayIndex = 0;
        ListOfArraysItem<T> itemContainingIndex = head;
        int index = i;
        while (index >= itemContainingIndex.currentNumber) {
            index -= itemContainingIndex.currentNumber;
            itemContainingIndex = itemContainingIndex.next;
        }
        ListOfArraysItem<T> firstItem = itemContainingIndex;
        int currentNumber = itemContainingIndex.currentNumber;
        while (true) {
            resultArray[arrayIndex] = itemContainingIndex.array[index];
            itemContainingIndex.deleteIndex(index);
            numberOfElementsToExtract--;
            if (numberOfElementsToExtract == 0) break;
            index++;
            if (index == currentNumber) {
                index = 0;
                if ((itemContainingIndex = itemContainingIndex.next) == null) break;
                currentNumber = itemContainingIndex.currentNumber;
            }
            arrayIndex++;
        }
        ListOfArraysItem<T> lastItem = itemContainingIndex;
        if (!sort()) return new ListOfArrays<>(resultArray);
        checkSort(firstItem, lastItem);
        tail = head.getTail();
        return new ListOfArrays<>(resultArray);
    }

    private boolean sort() {
        for (ListOfArraysItem<T> p = head, previous = head; p != null; p = p.next) {
            if (p.currentNumber == 0) {
                if (p == head) {
                    if ((previous = head = head.next) == null) {
                        tail = null;
                        return false;
                    }
                } else previous.next = p.next;
                continue;
            }
            if (p != head) previous = previous.next;
        }
        return true;
    }

    private void checkSort(ListOfArraysItem<T> firstItem, ListOfArraysItem<T> lastItem) {
        if (firstItem == lastItem && firstItem.currentNumber != 0) firstItem.sort();
        else if (lastItem != null && lastItem.currentNumber != 0) lastItem.sort();
    }

    /**
     * Inserts the elements of given Iterator each with their given offset from the last inserted element (or the first element) into this list.
     *
     * @param iterator The Iterator over the elements (and their offsets) that should be added.
     * @throws IndexOutOfBoundsException If an offset is negative.
     */
    @SuppressWarnings("unchecked")
    public void insert(Iterator<ElementWithIndex<T>> iterator) throws IndexOutOfBoundsException {
        if (!iterator.hasNext()) return;
        ElementWithIndex<T> elementWithIndex = iterator.next();
        int offset;
        checkOffset(offset = elementWithIndex.getIndex());
        T element = elementWithIndex.getElement();
        removeEmptyArrays();
        if (head == null) {
            if (offset != 0) return;
            ListOfArraysItem<T> tailHelper = head = new ListOfArraysItem<>();
            tailHelper.currentNumber = 1;
            tailHelper.array = (T[]) new Object[ARRAY_LENGTH];
            tailHelper.array[0] = element;
            int counter = 1;
            if (counter == ARRAY_LENGTH && iterator.hasNext()) {
                counter = 0;
                tailHelper.add((T[]) new Object[ARRAY_LENGTH]);
                tailHelper = tailHelper.next;
            }
            while (iterator.hasNext()) {
                elementWithIndex = iterator.next();
                checkOffset(offset = elementWithIndex.getIndex());
                if (offset != 0) break;
                element = elementWithIndex.getElement();
                tailHelper.array[counter] = element;
                tailHelper.currentNumber++;
                if (++counter == ARRAY_LENGTH && iterator.hasNext()) {
                    counter = 0;
                    tailHelper.add((T[]) new Object[ARRAY_LENGTH]);
                    tailHelper = tailHelper.next;
                }
            }
            tail = tailHelper;
            return;
        }
        ListOfArraysItem<T> itemContainingIndex = head;
        int index = offset;
        while (index >= itemContainingIndex.currentNumber) {
            index -= itemContainingIndex.currentNumber;
            if ((itemContainingIndex = itemContainingIndex.next) == null) return;
        }
        ListOfArraysItem<T> saveArray = new ListOfArraysItem<>((T[]) new Object[ARRAY_LENGTH]);
        ListOfArraysItem<T> newList;
        if (itemContainingIndex.array[index] != null)
            checkArray(saveArray.addElement(itemContainingIndex.array[index]));
        else itemContainingIndex.currentNumber++;
        itemContainingIndex.array[index] = element;
        index++;
        while (iterator.hasNext()) {
            elementWithIndex = iterator.next();
            int counter;
            checkOffset(counter = elementWithIndex.getIndex());
            element = elementWithIndex.getElement();
            if (counter == 0) {
                if (index == ARRAY_LENGTH) {
                    index = 0;
                    ListOfArraysItem<T> previous = itemContainingIndex;
                    if ((itemContainingIndex = itemContainingIndex.next) == null) {
                        itemContainingIndex = previous;
                        itemContainingIndex.add((T[]) new Object[ARRAY_LENGTH]);
                        itemContainingIndex = itemContainingIndex.next;
                    }
                }
                if (itemContainingIndex.array[index] != null)
                    checkArray(saveArray.addElement(itemContainingIndex.array[index]));
                else itemContainingIndex.currentNumber++;
                itemContainingIndex.array[index] = element;
                index++;
            } else {
                boolean noNext = false;
                while (true) {
                    if (index == ARRAY_LENGTH) {
                        ListOfArraysItem<T> previous = itemContainingIndex;
                        if ((itemContainingIndex = itemContainingIndex.next) == null) {
                            if (counter - saveArray.currentNumber <= 0) {
                                itemContainingIndex = previous;
                                itemContainingIndex.add((T[]) new Object[ARRAY_LENGTH]);
                                itemContainingIndex = itemContainingIndex.next;
                            } else {
                                noNext = true;
                                itemContainingIndex = previous;
                                break;
                            }
                        }
                        index = 0;
                    }
                    if (itemContainingIndex.array[index] != null)
                        checkArray(saveArray.addElement(itemContainingIndex.array[index]));
                    else itemContainingIndex.currentNumber++;
                    if (saveArray.hasNext()) itemContainingIndex.array[index] = saveArray.next();
                    if (--counter == 0) {
                        if (++index == ARRAY_LENGTH) {
                            ListOfArraysItem<T> previous = itemContainingIndex;
                            if ((itemContainingIndex = itemContainingIndex.next) == null) {
                                itemContainingIndex = previous;
                                itemContainingIndex.add((T[]) new Object[ARRAY_LENGTH]);
                                itemContainingIndex = itemContainingIndex.next;
                            }
                            index = 0;
                        }
                        if (itemContainingIndex.array[index] != null)
                            checkArray(saveArray.addElement(itemContainingIndex.array[index]));
                        else itemContainingIndex.currentNumber++;
                        itemContainingIndex.array[index] = element;
                        index++;
                        break;
                    }
                    if (!saveArray.hasNext()) {
                        noNext = true;
                        break;
                    }
                    index++;
                }
                if (noNext) break;
            }
        }
        while (saveArray.hasNext()) {
            if (index == ARRAY_LENGTH) {
                newList = new ListOfArraysItem<>((T[]) new Object[ARRAY_LENGTH]);
                newList.next = itemContainingIndex.next;
                itemContainingIndex = itemContainingIndex.next = newList;
                index = 0;
                continue;
            }
            T t = saveArray.next();
            if (itemContainingIndex.array[index] != null)
                checkArray(saveArray.addElement(itemContainingIndex.array[index]));
            else itemContainingIndex.currentNumber++;
            itemContainingIndex.array[index] = t;
            index++;
        }
        tail = itemContainingIndex.getTail();
    }

    private static void checkOffset(int offset) {
        if (offset < 0) throw new IndexOutOfBoundsException("" + offset);
    }

    private void checkArray(boolean added) {
        if (!added) throw new IndexOutOfBoundsException("array could not hold elements to be moved");
    }
}
