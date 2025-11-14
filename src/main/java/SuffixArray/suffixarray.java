package SuffixArray;

import java.util.Arrays;
import java.util.Comparator;

public class suffixarray {
    public static int[] build_sa(String s) {
        int n = s.length();
        Integer[] saObj = new Integer[n];
        int[] sa = new int[n];
        int[] rank = new int[n];
        int[] temp = new int[n];

        for (int i = 0; i < n; i++) {
            saObj[i] = i;
            rank[i] = s.charAt(i);
        }

        for (int step = 1; step < n; step <<= 1) {
            final int k = step;

            Arrays.sort(saObj, new Comparator<Integer>() {
                public int compare(Integer a, Integer b) {
                    if (rank[a] != rank[b])
                        return Integer.compare(rank[a], rank[b]);

                    int ra = (a + k < n) ? rank[a + k] : -1;
                    int rb = (b + k < n) ? rank[b + k] : -1;

                    return Integer.compare(ra, rb);
                }
            });

            temp[saObj[0]] = 0;
            for (int i = 1; i < n; i++) {
                int prev = saObj[i - 1];
                int curr = saObj[i];

                boolean diff = rank[prev] != rank[curr] ||
                        ((prev + k < n ? rank[prev + k] : -1) !=
                                (curr + k < n ? rank[curr + k] : -1));

                temp[curr] = temp[prev] + (diff ? 1 : 0);
            }

            for (int i = 0; i < n; i++)
                rank[i] = temp[i];

            if (rank[saObj[n - 1]] == n - 1)
                break;
        }

        for (int i = 0; i < n; i++)
            sa[i] = saObj[i];

        return sa;
    }

    public static int[] build_lcp(String s, int[] sa) {
        int n = s.length();
        int[] rank = new int[n];
        int[] lcp = new int[n];

        for (int i = 0; i < n; i++)
            rank[sa[i]] = i;

        int k = 0;

        for (int i = 0; i < n; i++) {
            if (rank[i] == 0) {
                lcp[0] = 0;
                continue;
            }

            int j = sa[rank[i] - 1];

            while (i + k < n && j + k < n && s.charAt(i + k) == s.charAt(j + k))
                k++;

            lcp[rank[i]] = k;

            if (k > 0)
                k--;
        }

        return lcp;
    }

    public static void print_arrays(String s, int[] sa, int[] lcp) {
        System.out.println("Index\tSA\tLCP\tSuffix");
        for (int i = 0; i < sa.length; i++) {
            System.out.printf("%d\t%d\t%d\t%s\n",
                    i, sa[i], lcp[i], s.substring(sa[i]));
        }
    }

    public static void run_test(String s) {
        System.out.println("\nString: \"" + s + "\"");

        if (!s.endsWith("$"))
            s += "$";

        int[] sa = build_sa(s);
        int[] lcp = build_lcp(s, sa);

        print_arrays(s, sa, lcp);
    }

    public static void main(String[] args) {

        run_test("ananas");

        run_test("designandanalytucs");

        run_test("ineedmyscholarshippleasemakefinalseasy");
    }
}
