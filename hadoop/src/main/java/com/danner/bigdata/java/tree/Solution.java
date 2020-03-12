package com.danner.bigdata.java.tree;

import java.util.LinkedList;
import java.util.List;

/**
 * java 实现反转二叉树
 *
 */
public class Solution {

  private int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  private static List<TreeNode> nodeList = null;

  public void createTree() {
    nodeList = new LinkedList();
    for(int index = 0; index < array.length; index++) {
      nodeList.add(new TreeNode(array[index]));
    }
    //最后一个父节点在数组中的索引
    int lastParentIndex = array.length / 2 - 1;
    for(int parentInex = 0; parentInex < lastParentIndex; parentInex++) {
      nodeList.get(parentInex).left = nodeList.get(parentInex * 2 + 1);
      nodeList.get(parentInex).right = nodeList.get(parentInex * 2 + 2);
    }
    // 最后一个父节点:因为最后一个父节点可能没有右孩子，所以单独拿出来处理
    // 左孩子
    nodeList.get(lastParentIndex).left = nodeList.get(lastParentIndex * 2 + 1);
    // 右孩子
    if(array.length % 2 == 1) {
      nodeList.get(lastParentIndex).right = nodeList.get(lastParentIndex * 2 + 2);
    }
  }

  // 层次遍历
  public void levelTraverse(TreeNode root) {
    if(root == null) return;

    LinkedList<TreeNode> list = new LinkedList<>();
    list.add(root);
    while(list.size() != 0) {
      TreeNode node = list.removeFirst();
      System.out.print(node.val + " ");
      if(node.left != null) {
        list.add(node.left);
      }
      if(node.right != null) {
        list.add(node.right);
      }
    }
    System.out.println();
  }

  /**
   * 反转二叉树
   * @param root
   * @return
   */
  public TreeNode invertTree(TreeNode root) {
    if(root == null) return null;
    TreeNode tmp = root.left;
    root.left = root.right;
    root.right = tmp;
    invertTree(root.left);
    invertTree(root.right);

    return root;
  }

  public static void main(String[] args) {
    Solution solution = new Solution();
    solution.createTree();
    solution.levelTraverse(nodeList.get(0));
    TreeNode newRoot = solution.invertTree(nodeList.get(0));
    solution.levelTraverse(newRoot);
  }
  public class TreeNode {

    int val = 0;
    TreeNode left = null;
    TreeNode right = null;

    public TreeNode(int val) {
      this.val = val;
    }
  }

}
