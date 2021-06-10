package eg.edu.alexu.csd.filestructure.redblacktree;

public class Node<T extends Comparable<T>,V> implements INode<T, V> {
    private T key;
    private V value;
    private INode<T, V> parent;
    private INode<T, V> leftChild;
    private INode<T, V> rightChild;
    private boolean color;

    Node(T key, V value) {
        this.key = key;
        this.value = value;
        this.color = Node.RED;
        this.parent = null;
    }
    Node(){
        this.color = Node.BLACK;
    }

    @Override
    public void setParent(INode<T, V> parent) {
        this.parent = parent;
    }

    @Override
    public INode<T, V> getParent() {
        if (this.parent == null)
            return null;
        return this.parent;
    }

    @Override
    public void setLeftChild(INode<T, V> leftChild) {
        this.leftChild = leftChild;
    }

    @Override
    public INode<T, V> getLeftChild() {
        if (leftChild == null)
            return null;

        return leftChild;
    }

    @Override
    public void setRightChild(INode<T, V> rightChild) {
        this.rightChild = rightChild;
    }

    @Override
    public INode<T, V> getRightChild() {
        if (rightChild == null)
            return null;
        return rightChild;
    }

    @Override
    public T getKey() {
        return this.key;
    }

    @Override
    public void setKey(T key) {
        this.key = key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean getColor() {
            return color;
    }

    @Override
    public void setColor(boolean color) {
        this.color = color;
    }

    @Override
    public boolean isNull() { return key == null; }
}
