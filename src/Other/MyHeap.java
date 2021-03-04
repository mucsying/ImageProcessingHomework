package Other;

import java.util.ArrayList;

/**
 * 一个MyHeap类
 * 顺序存储结构，用数组线性表来建立
 * 保证堆的性质：1，完全二叉树
 *          2，根节点的值大于或等于左右孩子的值
 * 用于堆排序
 * 方法有：
 *          1,增加一个新的节点
 *          2,删除根节点
 * 泛型程序设计
 * E继承自Comparable接口 比较方法compareTo
 */
public class MyHeap<E extends Comparable<E>>{
    /**数组线性表用于存储堆中的元素 */
    private final ArrayList<E> list = new ArrayList<>();
    /**构造一个空的二叉堆 */
    public MyHeap(){

    }

    /**用指定的对象数组构造二叉堆 */
    public MyHeap(E[] objects){
        for (E object : objects) {
            add(object);
        }
    }

    /**添加新的节点到堆中 */
    public void add(E e){
        /*将节点添加到堆末尾 */
        list.add(e);

        /*重构堆 */
        int indexOfCurrent = list.size() - 1;
        int indexOfParent;
        /*当当前节点更新到根节点时退出循环 */
        while(indexOfCurrent > 0){
            /*得到父节点的下标 */
            indexOfParent = (indexOfCurrent -1 ) / 2;

            if(list.get(indexOfCurrent).compareTo(list.get(indexOfParent)) > 0){
                /*将当前节点和父节点交换 */
                E temp = list.get(indexOfCurrent);
                list.set(indexOfCurrent, list.get(indexOfParent));
                list.set(indexOfParent, temp);
            }
            else{
                break; //当当前节点小于等于父节点退出循环
            }
            /*更新当前节点的下标 */
            indexOfCurrent = indexOfParent;
        }
    }
    /**删除根节点 并返回该值 */
    public E remove(){
        if(list.size() == 0) return null;
        /*末尾元素赋值给根节点元素之后，删除堆的末尾元素 */
        E root = list.get(0);//暂存根节点的元素
        list.set(0, list.get(list.size() - 1) );
        list.remove(list.size() - 1);

        /*根节点为当前节点 */
        int indexOfCurrent = 0;
        int indexOfSon;

        /*重构二叉堆 当当前节点为叶节点时退出循环*/
        while(indexOfCurrent < list.size()){
            int indexOfLeft = indexOfCurrent*2 + 1;
            int indexOfRight = indexOfCurrent*2 + 2;

            /*选出左右孩子中较大的一个作为子节点 */
            if(indexOfLeft == list.size() - 1){  //只有左孩子
                indexOfSon = indexOfLeft;
            }
            else if(indexOfLeft > list.size() - 1 ){    //无孩子
                break;
            }
            else{   //有左右孩子
                if(list.get(indexOfLeft).compareTo(list.get(indexOfRight)) > 0){
                    indexOfSon = indexOfLeft;
                }
                else{
                    indexOfSon = indexOfRight;
                }
            }


            if(list.get(indexOfCurrent).compareTo(list.get(indexOfSon)) < 0){
                /*将当前节点和子节点交换 */
                E temp = list.get(indexOfSon);
                list.set(indexOfSon, list.get(indexOfCurrent));
                list.set(indexOfCurrent, temp );
            }
            else{
                break;//当前节点大于等于子节点退出循环
            }
            /*更新当前节点的下标 */
            indexOfCurrent = indexOfSon;
        }

        return root;
    }
    /*返回堆的大小 */
    public int getSize(){
        return list.size();
    }
}
