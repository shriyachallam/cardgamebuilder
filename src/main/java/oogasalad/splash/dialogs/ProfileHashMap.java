package oogasalad.splash.dialogs;

import java.util.ArrayList;
import java.util.Iterator;

public class ProfileHashMap implements Iterable<String> {
    private ArrayList<String> keys;
    private ArrayList<String> vals;

    public ProfileHashMap() {
        this.keys = new ArrayList<>();
        this.vals = new ArrayList<>();
    }

    public void add(String k, String v) {
        this.keys.add(k);
        this.vals.add(v);
    }

    public String getKey(int index) {
        return keys.get(index);
    }

    public String getVal(int index) {
        return vals.get(index);
    }

    public String toString(int index) {
        return keys.get(index) + ":\t" + vals.get(index);
    }

    @Override
    public Iterator<String> iterator() {
        return new ProfileHashMapIterator();
    }

    private class ProfileHashMapIterator implements Iterator<String> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < keys.size();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements to iterate");
            }
            String key = keys.get(currentIndex);
            String val = vals.get(currentIndex);
            currentIndex++;
            return key + ":\t" + val;
        }
    }
}
