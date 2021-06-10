package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;

public class RedBlackTree<T extends Comparable<T>,V> implements IRedBlackTree<T,V> {
    private INode<T, V> root;
    private boolean found;
    private int size = 0;

    @Override
    public INode<T, V> getRoot() {
        if (root == null)
            return null;
        return root;
    }

    @Override
    public boolean isEmpty() { return root == null || root.getKey() == null; }

    @Override
    public void clear() {
        size = 0;
        root = new Node<>();
    }

    @Override
    public V search(T key) {
        if (key != null) {
            INode<T,V> result = searchHelper(root, key);
            if (result == null)
                return null;
            return result.getValue();
        } else {
            throw new RuntimeErrorException(new Error());
        }
    }

    @Override
    public boolean contains(T key) {
        if (key != null)
            return searchHelper(root,key) != null;
        else
            throw new RuntimeErrorException(new Error());
    }

    @Override
    public void insert(T key, V value) {
        if (key != null && value != null) {
            INode<T, V> node = new Node<>(key, value);
            node.setLeftChild(new Node<>());
            node.setRightChild(new Node<>());
            //empty tree
            if (root == null || root.getKey() == null) {
                root = node;
                root.setColor(Node.BLACK);
                size++;
                return;
            }
            //non-empty tree
            INode<T, V> x = this.root;
            INode<T, V> y = null;
            //get the parent of the inserted node
            while (x.getKey() != null) {
                y = x;
                if (node.getKey().compareTo(x.getKey()) < 0) {
                    x = x.getLeftChild();
                } else if (node.getKey().compareTo(x.getKey()) > 0) {
                    x = x.getRightChild();
                } else {
                    //change the value of the node if the key already exists
                    x.setValue(value);
                    //no need to fix
                    return;
                }
            }
            size++;
            //check the parent to insert the node as right or left child
            node.setParent(y);
            if (y != null ) {
                if (node.getKey().compareTo(y.getKey()) < 0) {
                    y.setLeftChild(node);
                } else {
                    y.setRightChild(node);
                }
            }
            //check if the parent is the root (black node), then no need to fix colors
            if (node.getParent() == null || node.getParent().getParent() == null)
                return;
            fixInsert(node);
        }else {
            throw new RuntimeErrorException(new Error());
        }
    }

    /**
     * search for the node with given key in the given subtree.
     * @param node the root of the subtree to be searched.
     * @param key the key searched for.
     * @return the node with the key searched for or nil if not found.
     */
    public INode<T, V> searchHelper(INode<T, V> node, T key) {
        if (node == null || node.getKey() == null)
            return null;
        if (key.compareTo(node.getKey()) == 0)
            return node;
        if (key.compareTo(node.getKey()) < 0)
            return searchHelper(node.getLeftChild(), key);
        return searchHelper(node.getRightChild(), key);
    }

    /**
     * fix the tree after insertion of a node recoloring & rotations.
     * @param z the node to be fixed.
     */
    private void fixInsert(INode<T, V> z) {
        INode<T, V> u ; //uncle of the inserted z
        //fix while z and its parent are red
        while (z.getParent().getColor() == Node.RED) {
            //check if the parent of z is right child
            if (z.getParent() == z.getParent().getParent().getRightChild()) {
                u = z.getParent().getParent().getLeftChild();
                //if uncle is red
                if (u.getColor() == Node.RED) {
                    u.setColor(Node.BLACK);
                    z.getParent().setColor(Node.BLACK);
                    z.getParent().getParent().setColor(Node.RED);
                    z = z.getParent().getParent();
                } else { //if uncle is black
                    //if z is left child, make it right child
                    if (z == z.getParent().getLeftChild()) {
                        z = z.getParent();
                        rightRotate(z);
                    }
                    //z is right child
                    z.getParent().setColor(Node.BLACK);
                    z.getParent().getParent().setColor(Node.RED);
                    leftRotate(z.getParent().getParent());
                }
                //if the parent of z is left child
            } else {
                u = z.getParent().getParent().getRightChild();
                //if uncle is red
                if (u.getColor() == Node.RED) {
                    u.setColor(Node.BLACK);
                    z.getParent().setColor(Node.BLACK);
                    z.getParent().getParent().setColor(Node.RED);
                    z = z.getParent().getParent();
                } else { //if uncle is black
                    //if z is right child, make it left child
                    if (z == z.getParent().getRightChild()) {
                        z = z.getParent();
                        leftRotate(z);
                    }
                    //z is left child
                    z.getParent().setColor(Node.BLACK);
                    z.getParent().getParent().setColor(Node.RED);
                    rightRotate(z.getParent().getParent());
                }
            }
            if (z == root) {
                break;
            }
        }
        root.setColor(Node.BLACK);
    }

    @Override
    public boolean delete(T key) {
        if (key == null)
            throw new RuntimeErrorException(null);
        //get the node to be deleted and assign it to z
        INode<T, V> z = searchHelper(this.root, key);
        //if node is not found, return false
        if (z == null)
            return false;
        //otherwise, decrement the size by 1 and delete z
        size--;
        //if z is the root, make the root null and return true
        if (size == 0) {
            root = new Node<>();
            return true;
        }

        INode<T, V> x, y;
        y = z;
        boolean yOriginalColor = y.getColor();
        //if z has one child
        if (z.getLeftChild().getKey() == null){ //if z doesn't have left child, replace z with its right child
            x = z.getRightChild();
            rbHandler(z, z.getRightChild());
        } else if (z.getRightChild().getKey() == null) { //if z doesn't have right child, replace z with its left child
            x = z.getLeftChild();
            rbHandler(z, z.getLeftChild());
        } else { //if z has two children, get z's successor
            y = successor(z);
            yOriginalColor = y.getColor();
            x = y.getRightChild();
            if (z == y.getParent()) { //if z is parent of y, set x parent as y
                x.setParent(y);
            } else { //replace y with its right child
                rbHandler(y, y.getRightChild());
                y.setRightChild(z.getRightChild());
                y.getRightChild().setParent(y);
            }
            //replace z with y and get its original color
            rbHandler(z,y);
            y.setLeftChild(z.getLeftChild());
            y.getLeftChild().setParent(y);
            y.setColor(z.getColor());
        }
        if (yOriginalColor == INode.BLACK) //if the original color was black, fix
            fixDelete(x);
        return true;
    }

    /**
     * fix property(5) that states that all paths have same number of black nodes.
     * recoloring & rotations.
     * @param x the node to be fixed.
     */
    private void fixDelete(INode<T, V> x){
        INode<T, V> s; //sibling
        //iterate until x is not root and its color is black
        while (x != root && x.getColor() == INode.BLACK) {
            if (x == x.getParent().getLeftChild()) { //if x is left child
                s = x.getParent().getRightChild();
                if (s.getColor() == INode.RED) { //case 1 : s is red
                    //make s red, make parent black, rotate left around parent, make s right again
                    s.setColor(INode.BLACK);
                    x.getParent().setColor(INode.RED);
                    caseLeftRight(x.getParent());
                    s = x.getParent().getRightChild();
                }
                if (s.getKey() != null && s.getLeftChild().getColor() == INode.BLACK
                        && s.getRightChild().getColor() == INode.BLACK) { //case 2 : s is black and both its children are black
                    //make s red
                    s.setColor(INode.RED);
                    x = x.getParent();
                } else {
                    if (s.getKey() != null && s.getRightChild().getColor() == INode.BLACK){ //case 3 : s is black and its right child is black
                        //make s red and its left child black, rotate right around s, make s right again
                        s.getLeftChild().setColor(INode.BLACK);
                        s.setColor(INode.RED);
                        caseRightRight(s);
                        s = x.getParent().getRightChild();
                    }
                    //case 4 : s is black and its left child is black
                    //make s color as parent's color, make parent black, make right child of s black, rotate left around the parent, start from root again
                    if (s.getKey() != null) {
                        s.setColor(x.getParent().getColor());
                        x.getParent().setColor(INode.BLACK);
                        s.getRightChild().setColor(INode.BLACK);
                        caseLeftRight(x.getParent());
                    }
                    x = root;
                }
            } else {
                //if node is right child
                s = x.getParent().getLeftChild();
                if (s.getColor() == INode.RED) {
                    s.setColor(INode.BLACK);
                    x.getParent().setColor(INode.RED);
                    caseRightRight(x.getParent());
                    s = x.getParent().getLeftChild();
                }
                if (s.getKey() != null && s.getLeftChild().getColor() == INode.BLACK
                        && s.getRightChild().getColor() == INode.BLACK) {
                    s.setColor(INode.RED);
                    x = x.getParent();
                } else {
                    if (s.getKey() != null && s.getLeftChild().getColor() == INode.BLACK) {
                        s.getRightChild().setColor(INode.BLACK);
                        s.setColor(INode.RED);
                        caseLeftRight(s);
                        s = x.getParent().getLeftChild();
                    }
                    if (s.getKey() != null) {
                        s.setColor(x.getParent().getColor());
                        x.getParent().setColor(INode.BLACK);
                        s.getLeftChild().setColor(INode.BLACK);
                        caseRightRight(x.getParent());
                    }
                    x = root;
                }
            }
        }
        x.setColor(INode.BLACK);
    }

    /**
     * handles the case of deleting a node that has one child
     * @param u the node to be deleted.
     * @param v the child of u
     */
    private void rbHandler(INode<T, V> u, INode<T, V> v) {
        if (u.getParent() == null) //if u is root, make v the root
            root = v;
        else if (u == u.getParent().getLeftChild()) //if u is a left child, make v the left child of u's parent
            u.getParent().setLeftChild(v);
        else //if u is a right child, make v the right child of u's parent
            u.getParent().setRightChild(v);
        //make the parent of u the parent of v
        v.setParent(u.getParent());
    }

    private void caseLeftRight(INode<T, V> x) {
        INode<T, V> y = x.getRightChild();
        x.setRightChild(y.getLeftChild());
        if (y.getLeftChild().getKey() != null)
            y.getLeftChild().setParent(x);
        y.setParent(x.getParent());
        if (x.getParent() == null)
            this.root = y;
        else if (x == x.getParent().getLeftChild())
            x.getParent().setLeftChild(y);
        else
            x.getParent().setRightChild(y);
        y.setLeftChild(x);
        x.setParent(y);
    }

    private void caseRightRight(INode<T, V> x) {
        INode<T, V> y = x.getLeftChild();
        x.setLeftChild(y.getRightChild());
        if (y.getRightChild().getKey() != null)
            y.getRightChild().setParent(x);
        y.setParent(x.getParent());
        if (x.getParent() == null)
            this.root = y;
        else if (x == x.getParent().getRightChild())
            x.getParent().setRightChild(y);
        else
            x.getParent().setLeftChild(y);
        y.setRightChild(x);
        x.setParent(y);
    }

    /**
     * get the node with the minimum key in the given subtree.
     * @param node the root of the subtree searched for minimum node in it.
     * @return the minimum node.
     */
    public INode<T, V> minimum(INode<T, V> node) {
        while (node != null && node.getLeftChild().getKey() != null)
            node = node.getLeftChild();
        return node;
    }

    /**
     * left rotate around the given node.
     * @param x the node to rotate around.
     */
    private void leftRotate(INode<T, V> x) {
        INode<T, V> y = x.getRightChild();
        //make the left child of y the right child of x
        x.setRightChild(y.getLeftChild());
        //if y has a left child (not nil), make x its parent
        if (y.getLeftChild().getKey() != null)
            y.getLeftChild().setParent(x);
        //make the parent of x the parent of y
        y.setParent(x.getParent());
        if (x.getParent() == null) //if x is the root, make y the root
            this.root = y;
        else if (x == x.getParent().getLeftChild()) //if x is the left child of p[x], make y the left child of p[x]
            x.getParent().setLeftChild(y);
        else //if x is the right child of p[x], make y the right child of p[x]
            x.getParent().setRightChild(y);

        //switch roles of parent & child between x & y
        y.setLeftChild(x);
        x.setParent(y);
    }

    /**
     * right rotate around the given node.
     * @param x the node to rotate around.
     */
    private void rightRotate(INode<T, V> x) {
        INode<T, V> y = x.getLeftChild();
        //make the right child of y the left child of x
        x.setLeftChild(y.getRightChild());
        //if y has a right child (not nil), make x its parent
        if (y.getRightChild().getKey() != null) {
            y.getRightChild().setParent(x);
        }
        //make the parent of x the parent of y
        y.setParent(x.getParent());
        if (x.getParent() == null) { //if x is the root, make y the root
            this.root = y;
        } else if (x == x.getParent().getRightChild()) { //if x is the right child of p[x], make y the right child of p[x]
            x.getParent().setRightChild(y);
        } else { //if x is the left child of p[x], make y the left child of p[x]
            x.getParent().setLeftChild(y);
        }
        //switch roles of parent & child between x & y
        y.setRightChild(x);
        x.setParent(y);
    }

    /**
     * traverse the given subtree inorder (left - root - right).
     * @param node the root of the subtree to be traversed.
     */
    private void inorderTraversal(INode<T, V> node) {
        if (node.getKey() == null) {
            System.out.println("nil");
            return;
        }
        inorderTraversal(node.getLeftChild());
        System.out.println("key: " + node.getKey() + ", value: " + node.getValue() + ", color: " + node.getColor());
        inorderTraversal(node.getRightChild());
    }

    /**
     * print the tree inorder
     */
    public void printTree() { inorderTraversal(this.root); }

    //used in TreeMap

    /**
     * checks if the tree has a node with the given value.
     * @param value the value to be checked.
     * @return true if a node with the given value is found, otherwise returns false.
     */
    public boolean containsValue(V value) {
        found = false;
        searchByValueHelper(this.root, value);
        return found;
    }

    /**
     * inorder traversal of the tree searching for the node with the given value.
     * @param node the root of the subtree to be searched.
     * @param value the value searched for.
     */
    private void searchByValueHelper(INode<T, V> node, V value) {
        if (node == null || node.getKey() == null)
            return;
        searchByValueHelper(node.getLeftChild(), value);
        if (node.getValue().equals(value)) {
            found = true;
            return;
        }
        searchByValueHelper(node.getRightChild(), value);
    }

    /**
     * @return the size of the tree.
     */
    public int getSize() { return this.size;}

    /**
     * get the node with the maximum key in the given subtree.
     * @param node the root of the subtree searched for maximum node in it.
     * @return the maximum node.
     */
    public INode<T, V> maximum(INode<T,V> node) {
        while (node.getRightChild().getKey() != null)
            node = (Node) node.getRightChild();
        return node;
    }

    /**
     * @param x a node in the tree
     * @return the successor of x
     */
    public INode<T, V> successor(INode<T, V> x) {
        if (x.getRightChild().getKey() != null)
            return minimum(x.getRightChild());
        INode<T, V> y = x.getParent();
        while (y.getKey() != null && x == y.getRightChild()) {
            x = y;
            y = y.getParent();
        }
        return y;
    }

    /**
     * @param x a node in the tree
     * @return the predecessor of x
     */
    public INode<T, V> predecessor(INode<T, V> x) {
        if (x.getLeftChild().getKey() != null)
            return maximum(x.getLeftChild());
        INode<T, V> y = x.getParent();
        while (y.getKey() != null && x == y.getLeftChild()) {
            x = y;
            y = y.getParent();
        }
        return y;
    }
}
