import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The <tt>Deque</tt> class implements a double-ended queue,
 * which is a generalization of a stack and a queue that
 * supports adding and removing items from either the
 * front or the back of the data structure
 * <p>
 * The implementation uses an object <tt>WeightedQuickUnionUF</tt>  to store
 * the sites. For a <tt>Percolation</tt> object of size N, a
 * <tt>WeightedQuickUnionUF</tt> of size N^2 + 2 is created.
 * The N^2 corresponds to all the sites in the Percolation, and
 * the extra 2 represent 2 virtual sites, one top and one bottom
 * Whenever a site is opened, the <tt>WeightedQuickUnionUF</tt>
 * will connect it to all its open neighbors (use a 4-connectivity).
 * </p>
 * <p>
 * To implement the percolation efficiently, the <tt>WeightedQuickUnionUF</tt>
 * stores 2 extra virtual sites. The top site is always connected to the open
 * sites of the first row. The bottom site is always connected to the open sites
 * of the last row. To check whether a system percolates, we just check whether
 * the first site (virtual) is connected to the last site (virtual) using
 * the <tt>WeightedQuickUnionUF</tt> method <em>connected</em>
 * <p/>
 * </p>
 *
 * @author Ali K Thabet
 **/
public class Deque<Item> implements Iterable<Item> {

    private int N;               // number of elements in deque
    private Node<Item> first;    // beginning of deque
    private Node<Item> last;     // end of deque

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    /**
     * Initializes an empty deque
     */
    public Deque() {
        first = null;
        last = null;
        N = 0;
    }

    /**
     * Is this deque empty?
     *
     * @return <tt>true</tt> if this deque is empty; <tt>false</tt> otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in the deque.
     *
     * @return the number of items in the deque
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
    private void checkItem(Item item){
        if (item.equals(null)) {
            throw new java.lang.NullPointerException("Item provided is NULL");
        }
    }
    /**
     * Adds an item to the beginning of the deque
     *
     * @param item
     * @throws NullPointerException if item is NULL
     */
    public void addFirst(Item item) {
        checkItem(item);
        Node<Item> oldfirst = first;
        first = new Node<Item>();
        first.item = item;
        first.next = oldfirst;
        N++;
    }

    /**
     * Adds an item to the end of the deque
     *
     * @param item
     * @throws NullPointerException if item is NULL
     */
    public void addLast(Item item) {
        checkItem(item);
        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else oldlast.next = last;
        N++;
    }

    /**
     * Removes and returns the first item of the deque
     *
     * @return the first item of the deque
     * @throws NoSuchElementException if this deque is empty
     */
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = first.item;        // save item to return
        first = first.next;            // delete first node
        N--;
        return item;                   // return the saved item
    }

    /**
     * Removes and returns the last item of the deque
     *
     * @return the last item of the deque
     */
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = last.item;        // save item to return
        last = null;                  // delete last node
        N--;
        return item;                   // return the saved item
    }

    /**
     * Returns an iterator that iterates over the items in this deque in arbitrary order.
     *
     * @return an iterator that iterates over the items in this deque in arbitrary order
     */
    public Iterator<Item> iterator() {
        return new ListIterator<Item>(first);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    /**
     * Unit tests the <tt>Deque</tt> data type.
     */
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item.charAt(0) == '+') deque.addFirst(item.substring(1));
            else if (item.charAt(0) == '*') deque.addLast(item.substring(1));
            else if (item.charAt(0) == '-' && !deque.isEmpty()) deque.removeFirst();
            else if (item.charAt(0) == '/' && !deque.isEmpty()) deque.removeLast();
            /*if (!item.equals("-")) deque.addFirst(item);
            else if (!deque.isEmpty()) StdOut.print(deque.removeFirst() + " ");*/
        }

        if (deque.isEmpty()) StdOut.println("Deque is empty");
        else StdOut.println("(" + deque.size() + " left on deque)");
    }
}
