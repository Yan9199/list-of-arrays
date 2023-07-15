package h02;

/**
 * Represents a pair of an element with a given index.
 *
 * @param <T> The generic type of the contained element.
 */
@SuppressWarnings("all")
public class ElementWithIndex<T> {
    /**
     * The element.
     */
    private T element;

    /**
     * The index.
     */
    private int index;

    /**
     * Constructs a new ElementWithIndex object with the given element and index.
     *
     * @param element The element to be contained in this object.
     * @param index   The index to be contained in this object.
     */
    public ElementWithIndex(T element, int index) {
        this.element = element;
        this.index = index;
    }

    /**
     * Returns the element contained in this object.
     *
     * @return The element.
     */
    public T getElement() {
        return element;
    }

    /**
     * Returns the index contained in this object.
     *
     * @return The index.
     */
    public int getIndex() {
        return index;
    }
}
