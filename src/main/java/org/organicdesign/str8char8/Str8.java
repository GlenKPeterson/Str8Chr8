package org.organicdesign.str8char8;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Pronounced, "Straight", this class represents a string of utf-8 characters.
 * Currently neither converts combining diacritical marks to a single character representation nor splits them into
 * two characters.
 */
public class Str8 extends AbstractList<Chr8> {
    private final byte[] bytes;
    private final int startIdx;
    private final int endIdx;
    private transient List<Chr8> chr8List = null;

    private Str8(byte[] bs, int si, int ei) { bytes = bs; startIdx = si; endIdx = ei; }

    private void initList() {
        synchronized (this) {
            if (chr8List == null) {
                chr8List = new ArrayList<>();
                int idx = startIdx;
                while (idx < endIdx) {
                    idx += Chr8.numBytes(bytes, idx);
                    chr8List.add(Chr8.char8(bytes, idx));
                }
            }
        }
    }

    @Override public int size() {
        initList();
        return chr8List.size();
    }

//    @Override public boolean contains(Object o) {
//        initList();
//        return char8List.contains(o);
//    }
//
//    @Override public Iterator<Char8> iterator() {
//        initList();
//        return char8List.iterator();
//    }
//
//    @Override public Object[] toArray() {
//        initList();
//        return char8List.toArray();
//    }
//
//    @Override public <T> T[] toArray(T[] ts) {
//        initList();
//        return char8List.toArray(ts);
//    }
//
//    @Override public boolean containsAll(Collection<?> collection) {
//        initList();
//        return char8List.containsAll(collection);
//    }

    @Override public Chr8 get(int i) {
        initList();
        return chr8List.get(i);
    }

//    @Override public int indexOf(Object o) {
//        initList();
//        return char8List.indexOf(o);
//    }
//
//    @Override public int lastIndexOf(Object o) {
//        initList();
//        return char8List.lastIndexOf(o);
//    }
//
//    @Override public ListIterator<Char8> listIterator() {
//        initList();
//        return char8List.listIterator();
//    }
//
//    @Override public ListIterator<Char8> listIterator(int i) {
//        initList();
//        return char8List.listIterator(i);
//    }
//
//    @Override public List<Char8> subList(int i, int i1) {
//        initList();
//        return char8List.subList(i, i1);
//    }

    /**
     * Factory method (so we can control instances later if we wish).
     */
    public static Str8 str8(byte[] bytes, int startIdx, int endIdx) { return new Str8(bytes, startIdx, endIdx); }

}
