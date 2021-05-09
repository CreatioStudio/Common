package vip.creatio.common.util;

import vip.creatio.common.collection.Pair;

public class IndexPair<V> extends Pair<Integer, V> {

    private final int index;

    public IndexPair(int index, V value) {
        super(index, value);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
