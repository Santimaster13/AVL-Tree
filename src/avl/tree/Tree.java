/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avl.tree;

import java.awt.List;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author Santi Mercado
 */
public class Tree {
    
    Node root;
    
    private void AddNode2(Node n, int data){ //To add a node we start traversing the tree according to the value given. 
            boolean c = true;
            while(c == true){
            if (data == n.value){ //If the value already exist, a new node is not created.
                c = false;
            } else {
            if (data < n.value){ //We take advantage of the properties of a search tree to minimize the iterations required, we look at the current value and if its lower than the given value, we go to the right, else we go left.
                if (n.left == null){ //Once we find the spot where the value should go, we create a new node with the value given, link it to the tree and end the iterations.
                   n.left = new Node(data); 
                   c = false;
                } else {
                    n = n.left;
                }
            } else {
                if (n.right == null){
                   n.right = new Node(data); 
                   c = false;
                } else {
                    n = n.right;
                }
        }
        }
            }
    
    }
    
    public void AddNode(int data){ //We create a secondary subroutine for easier balancing of the tree
        if (this.root == null){ //If there are no nodes, we create the new node with the given value and assign it as the root.
            this.root = new Node(data); 
        } else {
        this.AddNode2(root, data); //We use the secondary subroutine explained above 
        boolean keep = this.RebalanceNeeded(root, data); //We check if the tree is unbalanced after adding the node and after each rebalance, and keep rebalancing it until the tree is fully balanced.
        while (keep == true){
          this.RebalancingTraverse(root, data); 
          keep = this.RebalanceNeeded(root, data);
        }
        
        }
    }
    
    
    
    private void DeleteNode2(Node n, int data){
        boolean c = true;
        Node temp = n;
        while (c == true){
        if (temp == null){ //If we get to the end of the tree without finding the node, we end the iterations.
                c = false;
                System.out.println("No existe un nodo con el valor dado");
            } else {
                if (temp.value == data){ //We traverse the tree looking for the given value, taking advantage of the properties of a search tree. 
                    c = false;
                    DeletingTraverse(temp); //Once we find the node with the given value, we delete it and end the iterations.
                }  else if (temp.value > data){
                    temp = temp.left;
                } else {
                    temp = temp.right;
                }
            }
        }
       
    }
    
    private void DeletingTraverse(Node n){ //To delete a node we use this subroutine.
        Node temp = n;
        boolean stop = false;
        while (stop == false){ //Once we know that there is a valid node to delete, we traverse the tree until we get to the bottom.
            if (temp.right == null && temp.left == null){ //Once we get to the last level and find the value we're looking for, we stop the iterations. Note that even if we find the node with the given value, we keep iterating until finding a leaf node.
              stop = true;
          } else {
         if (temp == this.root){ //In case the root is going to be deleted, we know it must have at least one child (as we'll see in other subroutine).
             if (temp.left == null){ //Because of this, we check if one of the subtrees is empty and if it's true, we go to the non-empty subtree of the root.
                 temp = temp.right;
             } else  if (temp.right == null){
                 temp = temp.left;
             } else {
            if (this.Height(temp.left) <= this.Height(temp.right)){ //If we can choose between subtrees (none is empty), we traverse to the subtree with the lowest height to minimize iterations.
                temp = temp.left;
            } else if (this.Height(temp.right) < this.Height(temp.left)){
                temp = temp.right;
            }
             }
         } else {
         if (temp.value > this.root.value){ //If we are traversing the right subtree, we look for the leftmost value.  
             if (temp.left == null && temp.right != null){
                 if (temp.right.left == null && temp.right.right == null){ //In the specific case that we can't easily acces the lowest value of the right subtree becasue it has a child, we swap that value with the one from the child. 
                   int swap = temp.value;
                 temp.value = temp.right.value; 
                 temp.right.value = swap;  
                 }
                 
                 temp = temp.right;
             } else {
                 if (temp.left != null){
                 temp = temp.left;
                 }
             }
         } else {
            if (temp.right == null && temp.left != null){ //If we're in the left subtree, we look for the rightmost value. We apply the same logic as before.
                if (temp.left.left == null && temp.left.right == null){
                   int swap = temp.value;
                 temp.value = temp.left.value; 
                 temp.left.value = swap;  
                 }
                 temp = temp.left;
             } else {
                if (temp.right != null){
                 temp = temp.right;
                }
             } 
         }
         }
        }
        }
        
        int newval = temp.value; //We store the value of the node in the bottom of the tree and then look for their parent.
        if (SearchParent(this.root, newval).right == temp){ //Once we find the parent, we delete the node.
            SearchParent(this.root, newval).right = null;
        } else {
            SearchParent(this.root, newval).left = null; 
        }
        n.value = newval; //At last, we assign the node with the value given by the user the value we stored previously.
        //This way, rather than deleting the node with the value given, we swap the value with the one from a node in the bottom of the tree and delete that node instead for simplicity.
        //In order for this to work, we search for the lowest value of the right subtree, or the highest value of the left subtree. That way we keep the number balance.
    }
    
    public Node SearchParent(Node n, int data){ //A simple level traverse using the properties of a search tree. With every iterarion, we check look for the node with a child with the given value and return it.
         Queue<Node> queue = new LinkedList<Node>(); //We assume that the node we're looking for exists because we know that the child node exists and the node that's going to be deleted isn't the root.
        queue.add(n);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
            if (temp.left != null){
                if (temp.left.value == data){ //We check if the current node has a child, and if that said child is the node with the given value.
                    return temp;
                }
            }
            if (temp.right != null){
                if (temp.right.value == data){
                    return temp;
                }
            }
            if (temp.right != null && data > temp.value){
                queue.add(temp.right);
            }
            if (temp.left != null && data < temp.value){
                queue.add(temp.left);
            }
        }
        return null;
    }
    
    public void DeleteNode(int data){ //This is the subroutine for the user
        if (this.root == null){  
    //We take into account the possibility that the tree is empty and that only the root exists. In this cases, we return null to avoid problems
        } else {
            if (data == this.root.value && this.root.left == null && this.root.right == null){
                this.root = null;
            } else {
        this.DeleteNode2(root, data); //Otherwise, we simply call the subroutine (explained previously) to delete the node.
        boolean keep = this.RebalanceNeeded(root, data); //After deleting we check if the tree needs to be rebalanced and keep rebalancing it until it's fully balanced.
        while (keep == true){
          this.RebalancingTraverse(root, data);
          keep = this.RebalanceNeeded(root, data);
        }
        }
        }
    }
    
    private Node rotateR(Node x){ //A simple rotation from right to left.
        Node child = x.right;
        x.right = child.left;
        child.left = x;
        return child;
    }
    
    private Node rotateL(Node x){ //A simple rotation from left to right.
        Node child = x.left;
        x.left = child.right;
        child.right = x;
        return child;
    }
    
    private int Height(Node n){ //We look for the given nodes height. If the root is used as argument, we get the tree's height.
        if (n == null){
             return -1; //If the tree is empty, we return -1.
        } else {
            int l = Height(n.left); //We calculate the heigth of the right and left subtrees, and return the highest value of the 2, plus 1.
            int r = Height(n.right);
            if (l > r){
                return l+1; //This way, we offset the -1 into a 0 if the node has no childs. 
            } else {
               return r+1; 
            }
        }
    }
    
    public int GetHeight(Node n){ //a Getter to be used in the JFrame.
        return Height(n);
    }
    
    private int BFactor(Node n){
        return Height(n.right)-Height(n.left); //We look for the balance factor by comparing the right and left subtrees' heights.
    }
    
    private boolean RebalanceNeeded(Node n, int data){ //A simple level traverse in which we look if the tree needs rebalancing using the balance factors (if a single node requires rebalancing then the tree requires rebalancing too).
         Queue<Node> queue = new LinkedList<Node>();
        queue.add(n);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
            if (this.BFactor(temp) > 1 || this.BFactor(temp) < -1){ //We check the balance factors and return if the tree needs balancing or not.
                return true;
            }
            if (temp.right != null && data > temp.value){ //We take advantage of the properties of the search tree to check only the changed subtree.
                queue.add(temp.right);
            }
            if (temp.left != null && data < temp.value){
                queue.add(temp.left);
            }
        }
        return false;
    }
    
    private void RebalancingTraverse(Node n, int data){ //In order to rebalance from bottom to top, we make use of the level traverse explained further ahead.
        for (int level=this.Height(this.root); level >= 1; level--){ 
            this.RecursiveTraverseR(root, level, data);          
        }
        this.root = this.Rebalance(this.root);
        
    }
    
     private void RecursiveTraverseR(Node n, int l, int data){ //A simple level traverse but with the use of recursion (kinda). 
        if (n == null){
            return; //This subroutine will be explained further ahead in RecursiveTraverse.
        } else { 
                if (n.value > data){
                    if (l-1 == 0){
                        if (n.left != null){
                            n.left = this.Rebalance(n.left);
                        }
                    } else { 
                       RecursiveTraverseR(n.left, l-1, data);  
                    }
                   
                } else {
                    if (l-1 == 0){
                        if (n.right != null){
                            n.right = this.Rebalance(n.right);
                        }   
                    } else {
                        RecursiveTraverseR(n.right, l-1, data); 
                    }
                   
                }                
        }
    }
    
    
    
    private Node Rebalance(Node x){ //We rebalance a given node.
        if (this.BFactor(x) == 2){ //First we check if the node needs rebalancing or not., and which side is the one that is unbalanced. 
            if (Height(x.right.right) > Height(x.right.left)){ //After knowing which side of the tree is unbalanced, whe check if the node requires a simple rotation or a double rotation, and balance accordingly.
               x = rotateR(x); 
            } else {
                x.right = rotateL(x.right);
                x = rotateR(x);
            }
        } else if(this.BFactor(x) == -2){
            if (Height(x.left.left) > Height(x.left.right)){
                x = rotateL(x);
            } else {
                x.left = rotateR(x.left);
                x = rotateL(x);
            }
        }
        return x;
    }
    
    public Node SearchNode(Node n, int data){ //A simple level traverse search to return a node with a given value.
        Queue<Node> queue = new LinkedList<Node>();
        if (n == null){
            return null;
        }
        queue.add(n);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
             if (data == temp.value){ //We check if the current node has the given value, if true we return it, else we keep looking.
                return temp;
            }
            if (temp.right != null && data > temp.value){
                queue.add(temp.right);
            }
            if (temp.left != null && data < temp.value){
                queue.add(temp.left);
            } 
        }
        return null; //In case there isn't a node with the given value in the tree.
    }
    
    
    
    private Node SearchGramps(Node n, int data){ //A simple level traverse search
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(n);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
            if (temp.right != null && data > temp.value){ //We look at the child nodes to check if they exist, if they do, we check if they have childs and if they also do, we verify if the value of the grandchild node is the same as the given value.
                if ((temp.right.left != null && temp.right.left.value == data) || (temp.right.right != null && temp.right.right.value == data)){ 
                    return temp; //In case we find the grandchild, we return the grandparent node.
                }
                queue.add(temp.right);
            }
            if (temp.left != null && data < temp.value){
                if ((temp.left.left != null && temp.left.left.value == data) || (temp.left.right != null && temp.left.right.value == data)){
                    return temp;
                }
                queue.add(temp.left);
            }
        }
        return null; //We return null in case we never find a grandchild with the given value.
    }
    
    public Node FindGramps(int data){ //Subroutine destined for the user, takes into account the posibility of an empty tree.
        if (this.root == null){
           return null;
        } else {
             if (SearchGramps(this.root, data) == null){ //Calls the above subroutine, if it returns null we tell the user the node doesn´t have a grandparent, else we print the grandparent node's value.
                return null;
            } else {
                return this.SearchGramps(this.root, data);
            }
        }
    }
    
    private Node SearchUncle(Node n, int data){ //A SIMPle level traverse search (see what I did there :p).
       Queue<Node> queue = new LinkedList<Node>();
        queue.add(n);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
            if (temp.right != null && data > temp.value){ //Same principle used in the SearchGramps subroutine, we look for a grandchild node with the given value.
                if ((temp.right.left != null && temp.right.left.value == data) || (temp.right.right != null && temp.right.right.value == data)){
                    return temp.left; //However, instead of returning the grandparent node, we return the uncle node (if it exists). Essentially, we return the left child if the grandchild node is in the right subtree and viceversa.
                } 
                queue.add(temp.right);
            }
            if (temp.left != null && data < temp.value){
                if ((temp.left.left != null && temp.left.left.value == data) || (temp.left.right != null && temp.left.right.value == data)){
                    return temp.right;
                }
                queue.add(temp.left);
            }
        }
        return null; //In case we don´t find an uncle node, we return null
    }
    
     public Node FindUncle(int data){ //Subroutine destined for the user, takes into account the posibility of an empty tree.
        if (this.root == null){
           return null;
        } else {
             if (SearchUncle(this.root, data) == null){ //Calls the above subroutine, if it returns null we tell the user the node doesn´t have an uncle, else we print the uncle node's value.
                return null;
            } else {
                return this.SearchUncle(this.root, data);
            }
        }
    }
    
    public void NormalTraverse (Node n, int level){ 
        if (n == null){ //We check if the tree is empty
            System.out.println("El árbol está vacío");
        } else {
            if (level > this.Height(this.root)){ //We stop once we print all levels in the tree.
            return;
        } else {
          System.out.println("LEVEL: " + level); //We print by levels using a secondary subroutine.
          this.RecursiveTraverse(root, level);   
        }
        }
        
        NormalTraverse(n, level + 1); //We call the subroutine again (recursion!) to print the next level
    }
    
    private void RecursiveTraverse(Node n, int l){ //A simple level traverse search but with the use of recursion (kinda). 
        if (n == null){ //We check if the node exists
            return;
        } else { //This subroutine only prints the nodes in a given level.
            if (l == 0){ //It only prints the nodes in the given level
                System.out.println("     Valor: " + n.value);
            } else if(l > 0){ //The l, acts as a counter variable, every time we move to the child, we dimish it becasue we diminish the distance in levels between the one we're in and the one we're looking for.
                RecursiveTraverse(n.left, l-1); //Calls itself (see, recursion!) but with the childs to look for all the nodes with the given level in the tree.
                RecursiveTraverse(n.right, l-1);
            }
        }
    }
    
    public int NodeLevel(Node n){  //A simple level traverse search that we use to finde a node's level
        int lvl = 0; //We use a counter variable to count how many times we go down in the tree. It starts at 0 bc its the lowest level. We assume the tree is not empty.
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(this.root);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
            if (temp == n){ //When we find the node with the given value, we return the counter variable.
                return lvl;
            }
            if (temp.right != null && n.value > temp.value){ //In order to avoid measurment issues, we take advantage of the properties of a seacrh tree.
                queue.add(temp.right);
            }
            if (temp.left != null && n.value < temp.value){
                queue.add(temp.left);
            }
            lvl++; //We go down after going down in the tree, regardless if it's left or right.
        }
        return lvl; 
    }
    
    
    }

