package eg.edu.alexu.csd.filestructure.redblacktree;

public class RBTreeTest {
    public static void main(String[] args) {
        /*RedBlackTree<Integer, Integer> tree = new RedBlackTree<>();
        tree.delete(10);
        tree.insert(5, 50);
        tree.insert(27, 40);
        tree.delete(5);
        tree.insert(15, 30);
        tree.delete(27);
        tree.delete(15);
        tree.delete(5);

        tree.printTree();
        System.out.println("--------");
        System.out.println(tree.getRoot().getKey() + " " + tree.getRoot().getColor());
        System.out.println("--------");
        //System.out.println(tree.getRoot().getLeftChild().getKey() + " " + tree.getRoot().getLeftChild().getColor());*/
        IRedBlackTree<String, String> redBlackTree = new RedBlackTree<>();
        INode<String, String> root = null;
        redBlackTree.insert("B", "v");
        System.out.println(redBlackTree.delete("V"));


//        redBlackTree.insert("Soso", "Toto");
//        root = redBlackTree.getRoot();
//        System.out.println(root.getKey() + ", " + root.getValue());


    }
}
