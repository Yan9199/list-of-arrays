package h02;

/**
 * An item of a list analogous to ListItem.
 * Contains an array of the type T.
 * Attribute next references the next item of this list or null.
 * Attribute currentNumber is the amount of elements currently in array.
 *
 * @param <T> The generic type of this list
 */
class ListOfArraysItem<T> {
    private static final int LENGTH = ListOfArrays.getArrayLength();

    /**
     * The current amount of objects in the array of this item.
     */
    public int currentNumber;

    /**
     * The array of this item.
     */
    public T[] array;

    /**
     * The next item of the list - or null.
     */
    public ListOfArraysItem<T> next;

    public ListOfArraysItem(T[] array, int currentNumber) {
        this.array = array;
        this.currentNumber = currentNumber;
    }

    public ListOfArraysItem(T[] array) {
        this.array = array;
    }

    public ListOfArraysItem() {
    }

    public boolean addElement(T element) {
        for (int i = 0; i < LENGTH; i++) {
            if (array[i] == null) {
                array[i] = element;
                currentNumber++;
                return true;
            }
        }
        return false;
    }

    public boolean hasNext() {
        return array[0] != null;
    }

    public T next() {
        T saveElement = array[0];
        array[0] = null;
        currentNumber--;
        sort();
        return saveElement;
    }

    @SuppressWarnings("unchecked")
    public T[] getArrayCopy() {
        T[] a = (T[]) new Object[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            T t = array[i];
            if (t == null) break;
            a[i] = t;
        }
        return a;
    }

    @SuppressWarnings("unchecked")
    public T[] getArrayCopy(int index) {
        T[] a = (T[]) new Object[LENGTH];
        for (int i = 0; index < LENGTH; i++, index++) {
            T t = array[index];
            if (t == null) break;
            a[i] = t;
        }
        return a;
    }

    @SuppressWarnings("unchecked")
    public T[] getArrayCopyLong() {
        T[] a = (T[]) new Object[LENGTH];
        int counter = 0;
        for (int i = 0; i < LENGTH; i++) {
            T t = array[i];
            if (t == null) continue;
            a[counter] = t;
            counter++;
        }
        return a;
    }

    public int getNumberOfElementsInArray() {
        int elementsInArray = 0;
        for (int i = 0; i < LENGTH; i++) {
            T t = array[i];
            if (t == null) break;
            elementsInArray++;
        }
        return elementsInArray;
    }

    public int getNumberOfElementsInArrayLong() {
        int elementsInArray = 0;
        for (T t : array) if (t != null) elementsInArray++;
        return elementsInArray;
    }

    public int getNumberOfElementsInSequence() {
        int elementsInSequence = 0;
        for (ListOfArraysItem<T> p = this; p != null; p = p.next) elementsInSequence += p.currentNumber;
        return elementsInSequence;
    }

    public void add(T[] array) {
        if (this.array == null) {
            this.array = array;
            return;
        }
        addNext(array);
    }

    public void add(T[] array, int currentNumber) {
        if (this.array == null) {
            this.array = array;
            this.currentNumber = currentNumber;
            return;
        }
        addNext(array, currentNumber);
    }

    public void addNext(T[] array) {
        ListOfArraysItem<T> p = this;
        while (p.next != null) p = p.next;
        p.next = new ListOfArraysItem<>(array);
    }

    public void addNext(T[] array, int currentNumber) {
        ListOfArraysItem<T> p = this;
        while (p.next != null) p = p.next;
        p.next = new ListOfArraysItem<>(array, currentNumber);
    }

    public ListOfArraysItem<T> getTail() {
        ListOfArraysItem<T> tail = this;
        while (tail.next != null) tail = tail.next;
        return tail;
    }

    public void deleteIndex(int index) {
        if (index >= LENGTH) return;
        array[index] = null;
        currentNumber--;
    }

    public void deleteIndexInSequence(int index) {
        for (ListOfArraysItem<T> p = this; p != null; p = p.next) {
            if (index >= LENGTH) {
                index -= LENGTH;
                continue;
            }
            p.array[index] = null;
            p.currentNumber--;
            return;
        }
    }

    public void sort() {
        array = getArrayCopyLong();
    }

    public void update() {
        currentNumber = getNumberOfElementsInArray();
    }

    public void updateLong() {
        currentNumber = getNumberOfElementsInArrayLong();
    }

    public void printAll() {
        System.out.print("[");
        while (hasNext()) {
            System.out.print(next());
            if (hasNext()) System.out.print(", ");
        }
        System.out.println("]");
    }

    public static <T> int arrayLength(T[] array) {
        for (int i = 0, len = array.length; i < len; i++) if (array[i] == null) return i;
        return array.length;
    }
}
