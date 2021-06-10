package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;
import java.util.*;

public class TreeMap<T extends Comparable<T>, V> implements ITreeMap<T, V>{

    private RedBlackTree<T, V> redBlackTree = new RedBlackTree<>();
    private Set<Map.Entry<T, V>> entrySet;
    private Collection<V> values;
    private ArrayList<Map.Entry<T, V>> headMapList = new ArrayList<>();

    TreeMap() {
        this.entrySet = new LinkedHashSet<>();
        this.values = new LinkedHashSet<>();
    }

    public Map.Entry<T, V> nodeToEntry(INode<T, V> node){
        if (node == null)
            return null;
        Map.Entry<T, V> entry = new Map.Entry<>() {
            @Override
            public T getKey() {
               return node.getKey();
            }

            @Override
            public V getValue() {
                return node.getValue();
            }

            @Override
            public V setValue(V value) {
                V val = node.getValue();
                node.setValue(value);
                return val;
            }
        };
        return entry;
    }

    public AbstractMap.SimpleEntry<T, V> nodeToSimpleEntry(INode<T, V> node){
        if (node == null)
            return null;
        AbstractMap.SimpleEntry<T, V> entry = new AbstractMap.SimpleEntry<>(node.getKey(), node.getValue());
        return entry;
    }

    @Override
    public Map.Entry<T, V> ceilingEntry(T key) {
        if (key == null)
            throw new RuntimeErrorException(new Error());
        //get the node with the given key
        INode<T, V> node = redBlackTree.searchHelper(redBlackTree.getRoot(), key);
        //if key is found, return the entry with the given key
        if (node != null || node.getKey() != null)
            return nodeToSimpleEntry(node);
        //if key is not found, get the least key greater than or equal it
        node = redBlackTree.getRoot();
        if (node != null && node.getKey() != null && node.getKey().compareTo(key) > 0) { //ceiling node is left
            while (node.getKey().compareTo(key) > 0)
                node = redBlackTree.predecessor(node);
            return nodeToSimpleEntry(redBlackTree.successor(node));
        } else { //ceiling node is right
            while (node != null && node.getKey() != null && node.getKey().compareTo(key) < 0)
                node = redBlackTree.successor(node);
            if (node != null && node.getKey() != null && node.getKey().compareTo(key) < 0)
                return null;
            return nodeToSimpleEntry(redBlackTree.predecessor(node));
        }
    }

    @Override
    public T ceilingKey(T key) {
        if (key == null)
            throw new RuntimeErrorException(new Error());
        Map.Entry<T, V> entry = ceilingEntry(key);
        if (entry == null)
            throw new RuntimeErrorException(new Error());
        return entry.getKey();
    }

    @Override
    public void clear() {
        redBlackTree.clear();
    }

    @Override
    public boolean containsKey(T key) {
        if (key == null)
            throw new RuntimeErrorException(new Error());
        return redBlackTree.contains(key);
    }

    @Override
    public boolean containsValue(V value) {
        if (value == null)
            throw new RuntimeErrorException(new Error());
        return redBlackTree.containsValue(value);
    }

    @Override
    public Set<Map.Entry<T, V>> entrySet() {
        //TODO
        entrySet.clear();
        entrySetHelper(redBlackTree.getRoot());
        return entrySet;
    }

    @Override
    public Map.Entry<T, V> firstEntry() {
        if (redBlackTree.isEmpty())
            return null;
        return nodeToEntry(redBlackTree.minimum(redBlackTree.getRoot()));
    }

    @Override
    public T firstKey() {
        if (redBlackTree.isEmpty())
            return null;
        return firstEntry().getKey();
    }

    @Override
    public Map.Entry<T, V> floorEntry(T key) {
        //TODO
        if (key == null)
            throw new RuntimeErrorException(new Error());
        //get the node with the given key
        INode<T, V> node = redBlackTree.searchHelper(redBlackTree.getRoot(), key);
        //if key is found, return the entry with the given key
        if (node != null || node.getKey() != null)
            return nodeToSimpleEntry(node);
        //if key is not found, get the greatest key less than or equal it
        node = redBlackTree.getRoot();
        if (node != null && node.getKey() != null && node.getKey().compareTo(key) < 0) { //floor node is right
            while (node.getKey().compareTo(key) < 0)
                node = redBlackTree.successor(node);
            return nodeToSimpleEntry(redBlackTree.predecessor(node));
        } else{ //floor node is left
            while (node != null && node.getKey() != null && node.getKey().compareTo(key) > 0)
                node = redBlackTree.predecessor(node);
            if (node != null && node.getKey() != null && node.getKey().compareTo(key) > 0)
                return null;
            return nodeToSimpleEntry(node);
        }
    }

    @Override
    public T floorKey(T key) {
        if (key == null)
            throw new RuntimeErrorException(new Error());
        Map.Entry<T, V> temp = floorEntry(key);
        if (temp == null)
            throw new RuntimeErrorException(new Error());
        return temp.getKey();
    }

    @Override
    public V get(T key) {
        return redBlackTree.search(key);
    }

    @Override
    public ArrayList<Map.Entry<T, V>> headMap(T toKey) {
        if (toKey == null)
            throw new RuntimeErrorException(new Error());
        headMapHelper(redBlackTree.getRoot(), toKey);
        return headMapList;
    }

    private void headMapHelper(INode<T, V> node, T toKey){
        if (node == null || node.getKey() == null)
            return;
        headMapHelper(node.getLeftChild(),toKey);
        if (node.getKey().compareTo(toKey) < 0)
            headMapList.add(nodeToSimpleEntry(node));
        headMapHelper( node.getRightChild(),toKey);
    }

    @Override
    public ArrayList<Map.Entry<T, V>> headMap(T toKey, boolean inclusive) {
        if (toKey == null)
            throw new RuntimeErrorException(new Error());
        ArrayList<Map.Entry<T, V>> specialHeadMapList = headMap(toKey);
        if (!inclusive) {
            return specialHeadMapList;
        } else {
            INode<T, V> node = redBlackTree.searchHelper(redBlackTree.getRoot(),toKey);
            if (node != null)
                specialHeadMapList.add(nodeToSimpleEntry(node));
            return specialHeadMapList;
        }
    }

    @Override
    public Set<T> keySet() {
        //TODO
        Set<T> keys = new LinkedHashSet<>();
        entrySet = entrySet();
        for (Map.Entry<T,V> entry : entrySet)
            keys.add(entry.getKey());
        return keys;
    }

    @Override
    public Map.Entry<T, V> lastEntry() {
        if (redBlackTree.isEmpty())
            return null;
        return nodeToEntry(redBlackTree.maximum(redBlackTree.getRoot()));
    }

    @Override
    public T lastKey() {
        if (redBlackTree.isEmpty())
            return null;
        return lastEntry().getKey();
    }

    @Override
    public Map.Entry<T, V> pollFirstEntry() {
        if (redBlackTree.isEmpty())
            return null;
        Map.Entry<T, V> temp = firstEntry();
        if (temp == null)
            return null;
        remove(temp.getKey());
        return temp;
    }

    @Override
    public Map.Entry<T, V> pollLastEntry() {
        if (redBlackTree.isEmpty())
            return null;
        Map.Entry<T, V> temp = lastEntry();
        if (temp == null)
            return null;
        remove(temp.getKey());
        return temp;
    }

    @Override
    public void put(T key, V value) {
        redBlackTree.insert(key, value);
    }

    @Override
    public void putAll(Map<T, V> map) {
        if (map == null)
            throw new RuntimeErrorException(new Error());
        for (Map.Entry<T,V> entry : map.entrySet())
            put(entry.getKey() , entry.getValue());
    }

    @Override
    public boolean remove(T key) {
        return redBlackTree.delete(key);
    }

    @Override
    public int size() {
        return redBlackTree.getSize();
    }


    @Override
    public Collection<V> values() {
        entrySet = entrySet();
        for (Map.Entry<T,V> entry : entrySet)
            values.add(entry.getValue());
        return values;
    }

    private void entrySetHelper(INode<T, V> node) {
        if (node.getKey() == null || node == null)
            return;
        entrySetHelper(node.getLeftChild());
        entrySet.add(nodeToSimpleEntry(node));
        entrySetHelper(node.getRightChild());
    }

}
