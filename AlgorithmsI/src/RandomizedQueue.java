import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The <tt>RandomizedQueue</tt> class implements a different
 * version of a queue, where the item to remove is selected at
 * random from the available items.
 * <p>
 * The implementation uses an array with resizing...
 * </p>
 *
 * @author Ali K Thabet
 **/
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] a;         // array of items
    private int N;            // number of elements in random queue


    /**
     * Initializes an empty queue.
     */
    public RandomizedQueue() {
        // cast needed since no generic array creation in Java
        a = (Item[]) new Object[2];
    }

    /**
     * Is this stack empty?
     * @return true if this stack is empty; false otherwise
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Returns the number of items in the stack.
     * @return the number of items in the stack
     */
    public int size() {
        return N;
    }


    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    /**
     * Adds the item to this stack.
     * @param item the item to add
     */
    public void push(Item item) {
        if (N == a.length) resize(2*a.length);    // double size of array if necessary
        a[N++] = item;                            // add item
    }

    /**
     * Removes and returns the item most recently added to this stack.
     * @return the item most recently added
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    public Item pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = a[N-1];
        a[N-1] = null;                              // to avoid loitering
        N--;
        // shrink size of array if necessary
        if (N > 0 && N == a.length/4) resize(a.length/2);
        return item;
    }


    /**
     * Returns (but does not remove) the item most recently added to this stack.
     * @return the item most recently added to this stack
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        return a[N-1];
    }

    @Override
    public Iterator<Item> iterator() {
        return new ArrayIterator<Item>();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ArrayIterator<Item> implements Iterator<Item> {

        public ArrayIterator() {
        }

        public boolean hasNext() {
            return false;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            return null;
        }
    }
}
