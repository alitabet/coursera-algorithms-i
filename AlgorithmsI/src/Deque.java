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
 * The implementation uses a <em>DoublyLinkedList</em> and stores
 * a two virtual elements <em>pre</em> and <em>post</em>. It supports
 * methods <em>addFirst</em> to add en element to the beginning of
 * the deque, <em>addLast</em> to add en element to the end of the
 * deque, and methods <em>removeFirst</em> and <em>removeLast</em>
 * to remove elements from the beginning and end of the deque.
 * In addition to adding and removing, the implementation also
 * has methods to check if the deque is empty, return its size,
 * and iterate through its items
 * </p>
 *
 * @author Ali K Thabet
 **/
public class Deque<Item> implements Iterable<Item> {

    private int N = 0;          // number of elements in deque
    private Node<Item> pre;     // sentinel before first item
    private Node<Item> post;    // sentinel after last item

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    /**
     * Initializes an empty deque
     */
    public Deque() {
        pre = new Node<Item>();
        post = new Node<Item>();
        pre.next = post;
        post.prev = pre;
    }

    /**
     * Is this deque empty?
     *
     * @return <tt>true</tt> if this deque is empty; <tt>false</tt> otherwise
     */
    public boolean isEmpty() {
        return N == 0;
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
    private void checkItem(Item item) {
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
        Node<Item> first = pre.next;
        Node<Item> x = new Node<Item>();
        x.item = item;
        x.next = first;
        x.prev = pre;
        pre.next = x;
        first.prev = x;
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
        Node<Item> last = post.prev;
        Node<Item> x = new Node<Item>();
        x.item = item;
        x.next = post;
        x.prev = last;
        post.prev = x;
        last.next = x;
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
        Item item = pre.next.item;     // save item to return
        pre = pre.next;                // delete first node
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
        Item item = post.prev.item;        // save item to return
        post = post.prev;                  // delete last node
        N--;
        return item;                   // return the saved item
    }

    /**
     * Returns an iterator that iterates over the items in this deque in arbitrary order.
     *
     * @return an iterator that iterates over the items in this deque in arbitrary order
     */
    public Iterator<Item> iterator() {
        return new ListIterator<Item>(pre, post);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;
        private Node<Item> last;

        public ListIterator(Node<Item> prev, Node<Item> post) {
            current = prev.next;
            last = post;
        }

        public boolean hasNext() {
            return !current.equals(last);
            //return current != null;
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

            switch (item.charAt(0)) {
                case '+':
                    deque.addFirst(item.substring(1));
                    break;
                case '*':
                    deque.addLast(item.substring(1));
                    break;
                case '-':
                    StdOut.println(deque.removeFirst());
                    break;
                case '/':
                    StdOut.println(deque.removeLast());
                    break;
                case '=':
                    for (String s : deque) {
                        StdOut.print(s + " ");
                    }
                    StdOut.print("\n");
                    break;
            }
        }

        if (deque.isEmpty()) StdOut.println("Deque is empty");
        else StdOut.println("(" + deque.size() + " left on deque)");
    }
}
