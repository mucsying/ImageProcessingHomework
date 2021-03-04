package Other;

/**定义内部类Tree用于构建Huffman树
 * 要求实现能通过权重比较树的大小
 * 实现Comparable接口
 * 改写compareTo方法
 */
public class MyTree implements Comparable<MyTree>{
    public Node root;
    /**构造方法 */
    public MyTree(int weight, char element){  //单个节点的树
        root = new Node(weight, element);
    }
    public MyTree(MyTree t1,MyTree t2){   //两个子树合并成一个树
        root = new Node();  //空的根节点
        root.left = t1.root;    //连接子树
        root.right = t2.root;
        root.weight = t1.root.weight + t2.root.weight; //权重相加
    }
    /**改写compareTo方法
     * 比较树根节点的权值
     */
    @Override
    public int compareTo(MyTree t){
        return Integer.compare(t.root.weight, root.weight);
    }

    /**定义Tree的内部类Node
     * 用于定义树的节点
     */
    public static class Node{
        public int element; //存储叶节点的灰度级
        public int weight; //该灰度级的权重
        public Node left;  //左子树
        public Node right; //右子树
        public String code = ""; //该字符的编码

        public Node(){
        }
        public Node(int weight,char element){
            this.weight = weight;
            this.element = element;
        }
    }
}
