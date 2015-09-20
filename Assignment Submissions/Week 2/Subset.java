import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * The <tt>Subset</tt> class implements a client
 * that takes an input <em>k</em> and returns a
 * subset of <em>k</em> strings from a collection
 * of <em>N</em> strings read from standard input
 *
 * @author Ali K Thabet
 **/
public class Subset {
    public static void main(String[] args) {

        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> randomQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            randomQueue.enqueue(StdIn.readString());
        }

        int N = randomQueue.size();
        /*if (k > N) {
            StdOut.println("Value provided for k (" + k
                    + ") is larger than N (" + N + ")");
        }
        StdOut.println("String list is: ");
        for (String s : randomQueue) {
            StdOut.print(s + " ");
        }
        StdOut.print("\n");
        StdOut.println("Random list of size " + k);*/

        for (int i = 0; i < k; i++) {
            String s = randomQueue.dequeue();
            while (s == null) {
                s = randomQueue.dequeue();
            }
            StdOut.println(s + " ");
        }
    }
}
