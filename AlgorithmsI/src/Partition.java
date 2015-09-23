import edu.princeton.cs.algs4.StdOut;

/**
 * Created by alitabet on 9/23/15.
 */
public class Partition {

    public int partition(Comparable[] a, int lo, int hi) {

        int i = 0, j = hi+1;

        Comparable v = a[lo];
        printArray(a);
        while (true) {
            while (a[++i].compareTo(v) < 0) if (i == hi) break;
            while (v.compareTo(a[--j]) < 0) if (j == lo) break;
            if (i >= j) break;
            exch(a, i, j);
            printArray(a);
        }
        exch(a, lo, j);
        printArray(a);
        return j;
    }

    public int partitionThreeWay(Comparable[] a, int lo, int hi) {

        int lt = lo, i = lo + 1, gt = hi;
        Comparable v = a[lo];
        printArray(a);
        while (i <= gt) {
            int cmp = a[i].compareTo(v);
            if      (cmp < 0) exch(a, lt++, i++);
            else if (cmp > 0) exch(a, i, gt--);
            else    i++;
            printArray(a);
        }
        printArray(a);
        return i;
    }

    private void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private void printArray(Comparable[] a) {
        for (Comparable i : a) {
            StdOut.print(i.toString() + " ");
        }
        StdOut.print("\n");
    }

    public static void main(String[] args) {

        boolean threeWay = true;

        String[] temp = "40 13 17 40 80 43 40 78 45 40".split(" ");
        Integer[] a = new Integer[temp.length];
        for (int i = 0; i < temp.length; i++) {
            a[i] = Integer.parseInt(temp[i]);
        }
        //String[] a = temp;

        Partition partition = new Partition();

        if (!threeWay) partition.partition(a, 0, a.length - 1);
        else           partition.partitionThreeWay(a, 0, a.length - 1);
    }

}
