/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avl.tree;

/**
 *
 * @author Santi Mercado
 */
public class AVLTree {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //UI ui = new UI();
        //ui.setSize(800, 900);
        //ui.setResizable(false);
        //ui.setVisible(true);
        Tree tree = new Tree(); //We create a Binary Tree and add some values to it.
        tree.AddNode(5);
        tree.DeleteNode(5);
        tree.NormalTraverse(tree.root);
    }
    
}
