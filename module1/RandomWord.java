// RandomWord.java
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = null;
        int i = 0;

        // Read until EOF
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            i++;

            // With probability 1/i, replace the current champion
            if (StdRandom.bernoulli(1.0 / i)) {
                champion = word;
            }
        }

        // Print the final champion (if any words were read)
        if (champion != null) {
            StdOut.println(champion);
        }
    }
}
