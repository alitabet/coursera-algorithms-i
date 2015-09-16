import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The <tt>Deque</tt> class represents a percolate data structure.
 * This implementation supports <em>percolates</em> operation to determine
 * whether a system percolates.
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
 *
 * </p>
 * @author Ali K Thabet
 **/
public class Deque<Item> implements Iterable<Item>  {

    private int N;               // number of elements in bag
    private Node<Item> first;    // beginning of bag

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    /**
     * Is this stack empty?
     * @return true if this stack is empty; false otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in the stack.
     * @return the number of items in the stack
     */
    public int size() {
        return N;
    }

    /**
     * Returns an iterator that iterates over the items in this bag in arbitrary order.
     *
     * @return an iterator that iterates over the items in this bag in arbitrary order
     */
    public Iterator<Item> iterator()  {
        return new ListIterator<Item>(first);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

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

    }
}
