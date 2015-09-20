import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

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
     * Initializes an empty randomized queue.
     */
    public RandomizedQueue() {
        // cast needed since no generic array creation in Java
        a = (Item[]) new Object[2];
    }

    /**
     * Is this queue empty?
     * @return true if this queue is empty; false otherwise
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Returns the number of items in this queue.
     *
     * @return the number of items in this queue
     */
    public int size() {
        return N;
    }

    /**
     * Helper function to check if item is null
     *
     * @param item
     * @throws NullPointerException if item is NULL
     */
    private void checkItem(Item item) {
        if (item.equals(null)) {
            throw new java.lang.NullPointerException("Item provided is NULL");
        }
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        int count = 0;
        for (Item item : a) {
            temp[count++] = item;
        }
        a = temp;
    }

    /**
     * Adds the item to this queue.
     *
     * @param item the item to add
     * @throws NullPointerException if item is NULL
     */
    public void enqueue(Item item) {
        checkItem(item);
        if (N == a.length) resize(2*a.length);    // double size of array if necessary
        a[N++] = item;                            // add item
    }

    /**
     * Removes and returns a random item on this queue
     *
     * @return a randomly selected item on this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int randIdx = StdRandom.uniform(N);
        Item item = a[randIdx];
        if (item != null) {
            a[randIdx] = null;                              // to avoid loitering
            N--;
            // shrink size of array if necessary
            if (N > 0 && N == a.length / 4) resize(a.length / 2);
        }
        return item;
    }


    /**
     * Returns (but does not remove) a random item on this queue
     *
     * @return a random item on this queue
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return a[StdRandom.uniform(N)];
    }

    @Override
    public Iterator<Item> iterator() {
        return new ArrayIterator<Item>();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ArrayIterator<Item> implements Iterator<Item> {

        private int i;
        private Item[] notNull = (Item[]) new Object[N];; // Array of elements not null

        ArrayIterator() {
            i = 0;
            //notNull = (Item[]) new Object[N];

            int count = 0;
            for (int i = 0; i < a.length; i++) {
                if (a[i] != null) {
                    notNull[count++] = (Item) a[i];
                }
            }
        }
        public boolean hasNext()  { return i < N;                               }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return notNull[i++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Unit tests the <tt>Deque</tt> data type.
     */
    public static void main(String[] args) {

        RandomizedQueue<Integer> randomQueue =  new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();

            switch (item) {
                case "-":
                    StdOut.println(randomQueue.dequeue());
                    break;
                case "*":
                    StdOut.println(randomQueue.sample());
                    break;
                case "=":
                    StdOut.print("| ");
                    for (Integer s : randomQueue) {
                        StdOut.print(s + " | ");
                    }
                    StdOut.print("\n");
                    break;
                default:
                    randomQueue.enqueue(Integer.parseInt(item));
            }
        }
    }
}
