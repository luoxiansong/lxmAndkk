package com.cqkk.config.structure.binaryTree;

import java.util.Stack;

/*二叉树前中后遍历*/
public class BinaryTree {

    /*定义一个二叉树节点*/
    public static class MyTreeNode<T> {
        public T value;
        public MyTreeNode<T> left;
        public MyTreeNode<T> right;

        public MyTreeNode(T value) {
            this.value = value;
        }
    }

    /*前序遍历（递归方式）*/
    public static void preOrderByRecursion(MyTreeNode treeNode) {
        if (treeNode != null) {
            System.out.print((treeNode.value));
            System.out.print((""));
            preOrderByRecursion(treeNode.left);
            preOrderByRecursion(treeNode.right);
        }
    }

    //前序遍历（非递归方式）
    //1.先入栈根节点，输出根节点value值，再先后入栈其右节点，左节点
    //2.出栈左节点，输出其value值，再入栈左节点的右节点、左节点，知道遍历完该左节点子树
    //3.再出栈右节点，输出其value值，再入栈该右节点的右节点、左节点，知道遍历完右节点子树
    public static void preOrder(MyTreeNode node) {
        Stack<MyTreeNode> stack = new Stack<>();
        if (node != null) {
            stack.push(node);
        }
        while (!stack.isEmpty()) {
            MyTreeNode top = stack.pop();
            System.out.print(top.value);
            System.out.print("");
            if (top.right != null) {
                stack.push(top.right);
            }
            if (top.left != null) {
                stack.push(top.left);
            }
        }
    }

    /*中序遍历（递归方式）*/
    public static void inOrderByRecursion(MyTreeNode treeNode) {
        if (treeNode != null) {
            inOrderByRecursion(treeNode.left);
            System.out.print((treeNode.value));
            System.out.print((""));
            inOrderByRecursion(treeNode.right);
        }
    }

    //中序遍历（非递归方式）
    //1.首先从根结点出发一路向左，入栈所有的左结点
    //2.出栈一个结点，输出该结点value值，查询该结点是否存在右结点
    //若存在则从该右结点出发一路向左入栈该右结点所在子树所有的左结点
    //3.若不存在右结点，则出栈下一个结点，输出结点value值，同步骤2操作
    //4.直到结点为null，且栈为空
    public static void inOrder(MyTreeNode node) {
        Stack<MyTreeNode> stack = new Stack<>();
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
            if (!stack.isEmpty()) {
                MyTreeNode top = stack.pop();
                System.out.print((top.value));
                node = top.right;
            }
        }
    }

    /*后序遍历（递归方式）*/
    public static void postOrderByRecursion(MyTreeNode treeNode) {
        if (treeNode != null) {
            postOrderByRecursion(treeNode.left);
            postOrderByRecursion(treeNode.right);
            System.out.print((treeNode.value));
        }
    }

    //后序遍历（非递归）
    //1.首先定义两个stack，将node结点压入stack1
    //2.stack1弹出栈顶元素，然后将该元素压入stack2，再将该元素的左结点与右结点压入stack1
    //3.循环步骤2，直到stack1为空，根据栈的LIFO的特性，这样遍历Stack2就会得到后序遍历的结果
    public static void postOrder(MyTreeNode node) {
        if (node != null) {
            Stack<MyTreeNode> stack1 = new Stack<>();
            Stack<MyTreeNode> stack2 = new Stack<>();
            stack1.push(node);
            while (!stack1.isEmpty()) {
                MyTreeNode top = stack1.pop();
                stack2.push(top);
                if (top.left != null) {
                    stack1.push(top.left);
                }
                if (top.right != null) {
                    stack1.push(top.right);
                }
            }
            while (!stack2.isEmpty()) {
                System.out.print(stack2.pop().value);
            }
        }
    }

    public static void main(String[] args) {
        MyTreeNode A = new MyTreeNode("A");
        MyTreeNode B = new MyTreeNode("B");
        MyTreeNode C = new MyTreeNode("C");
        MyTreeNode D = new MyTreeNode("D");
        MyTreeNode E = new MyTreeNode("E");
        MyTreeNode F = new MyTreeNode("F");
        MyTreeNode G = new MyTreeNode("G");
        MyTreeNode H = new MyTreeNode("H");
        MyTreeNode I = new MyTreeNode("I");
        MyTreeNode J = new MyTreeNode("J");
        MyTreeNode K = new MyTreeNode("K");
        A.left = B;
        A.right = C;
        B.left = D;
        B.right = E;
        C.left = F;
        C.right = G;
        D.left = H;
        D.right = I;
        E.right = J;
        F.right = K;

        //前序遍历（递归方式）
        System.out.print(("前序遍历（递归方式）===>"));
        preOrderByRecursion(A);

        //前序遍历（非递归方式）
        System.out.println("");
        System.out.print(("前序遍历（非递归方式）===>"));
        preOrder(A);

        //中序遍历（递归方式）
        System.out.println("");
        System.out.print(("中序遍历（递归方式）===>"));
        inOrderByRecursion(A);

        //中序遍历（非递归方式）
        System.out.println("");
        System.out.print(("中序遍历（非递归方式）===>"));
        inOrder(A);

        //后序遍历（递归方式）
        System.out.println("");
        System.out.print(("后序遍历（递归方式）===>"));
        postOrderByRecursion(A);

        //后序遍历（非递归方式）
        System.out.println("");
        System.out.print(("后序遍历（非递归方式）===>"));
        postOrder(A);
    }
}
