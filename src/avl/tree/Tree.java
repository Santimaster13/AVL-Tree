/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avl.tree;

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
                System.out.println("Ya existe un nodo con ese valor");
            } else {
            if (data < n.value){ //We take advantage of the properties of a search tree to minimize the iterations required.
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
        this.AddNode2(root, data); //We 
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
    
    private void DeletingTraverse(Node n){
        Node temp = n;
        boolean stop = false;
        while (stop == false){ //Once we know that there is a valid node to delete, we traverse the tree until we get to the bottom.
            if (temp.right == null && temp.left == null){ //Once we get to the last level and find the value we're looking for, we stop the iterations.
              stop = true;
          } else {
         if (temp == this.root){ //In case the root is going to be deleted, we know it must have at least one child (as we'll see in other subroutine).
             if (temp.left == null){ //Because of this, we check if one of the subtrees is empty and if it's true, we go to the non-empty subtree of the root.
                 temp = temp.right;
             } else  if (temp.right == null){
                 temp = temp.left;
             } else {
            if (this.Height(temp.left) < this.Height(temp.right) && this.Height(temp.left) != 0){ //If we can choose between subtrees (none is empty), we traverse to the subtree with the lowest height to minimize iterations.
                temp = temp.left;
            } else if (this.Height(temp.right) < this.Height(temp.left) && this.Height(temp.right) != 0){
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
        System.out.println(newval);
        if (SearchParent(this.root, newval).right == temp){ //Once we find the parent, we delete the node.
            SearchParent(this.root, newval).right = null;
        } else {
            SearchParent(this.root, newval).left = null; 
        }
        n.value = newval; //At last, we assign the node with the value given by the user the value we stored previously.
        //This way, rather than deleting the node with the value given, we swap the value with the one from a node in the bottom of the tree and delete that node instead for simplicity.
        //In order for this to work, we search for the lowest value of the right subtree, or the highest value of the left subtree. That way we keep the number balance.
    }
    
    private Node SearchParent(Node n, int data){ //A simple level traverse using the properties of a search tree. With every iterarion, we check look for the node with a child with the given value and return it.
         Queue<Node> queue = new LinkedList<Node>(); //We assume that the node we're looking for exists because we know that the child node exists and the node that's going to be deleted isn't the root.
        queue.add(n);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
            if (temp.left != null){
                if (temp.left.value == data){
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
            System.out.println("No hay nodos que eliminar"); //We take into account the possibility that the tree is empty and that only the root exists. In this cases, we can solve the problem without caling other subroutines.
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
    
    private int BFactor(Node n){
        return Height(n.right)-Height(n.left); //We look for the balance factor.
    }
    
    private boolean RebalanceNeeded(Node n, int data){ //A simple level traverse search in which we look if the tree needs rebalancing using the balance factors (if a single node requires rebalancing then the tree requires rebalancing too).
         Queue<Node> queue = new LinkedList<Node>();
        queue.add(n);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
            if (this.BFactor(temp) > 1 || this.BFactor(temp) < -1){
                return true;
            }
            if (temp.right != null && data > temp.value){
                queue.add(temp.right);
            }
            if (temp.left != null && data < temp.value){
                queue.add(temp.left);
            }
        }
        return false;
    }
    
    private void RebalancingTraverse(Node n, int data){ //A simple level traverse in which we rebalance all nodes in the tree (In the Rebalance subroutine we check if the rebalance is needed or not, so we can call that subroutine to every node in the tree without issues).
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(n);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
            this.root = this.Rebalance(this.root);
            if (temp.right != null && data > temp.value){
                temp.right = this.Rebalance(temp.right);
                queue.add(temp.right);
            }
            if (temp.left != null && data < temp.value){
                temp.left = this.Rebalance(temp.left);
                queue.add(temp.left);
            }
        }
    }
    
    private Node FocusedTraverse(Node n, int data){ //A simple level traverse search in which we take advantage of the properties of a search tree to minimize iterations.
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(n);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
            if (data == temp.value){
                return temp; //In this specif case, we return the node we're looking for.
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
    
    private Node SearchNode(Node n, int data){ //A simple level traversal search to return a node with a given value.
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(n);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
            if (data == temp.value){
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
    
    public void SearchNode(int data){ //Subroutine destined for the user that takes into account the cases in which the tree is empty.
        if (this.root == null){
            System.out.println("No hay nodos en el árbol binario");
        } else {
            if (SearchNode(root, data) == null){ //In case the tree is not empty, we use the above subroutine to look for the node with the given value.
                System.out.println("No existe un nodo con el valor dado");
            } else {
                System.out.println("Existe un nodo con el valor dado");
            }
        }
    }
    
    private Node SearchGramps(Node n, int data){
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(n);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
            if (temp.right != null && data > temp.value){
                if ((temp.right.left != null && temp.right.left.value == data) || (temp.right.right != null && temp.right.right.value == data)){
                    return temp;
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
        return null;
    }
    
    public void FindGramps(int data){
        if (this.root == null){
           System.out.println("No hay nodos en el árbol binario"); 
        } else {
             if (SearchGramps(root, data) == null){
                System.out.println("El nodo no tiene un abuelo");
            } else {
                System.out.println("El nodo tiene un abuelo con valor "+ this.SearchGramps(root, data).value);
            }
        }
    }
    
    private Node SearchUncle(Node n, int data){
       Queue<Node> queue = new LinkedList<Node>();
        queue.add(n);
        while (queue.isEmpty()==false){
            Node temp = queue.poll();
            if (temp.right != null && data > temp.value){
                if ((temp.right.left != null && temp.right.left.value == data) || (temp.right.right != null && temp.right.right.value == data)){
                    return temp.left;
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
        return null; 
    }
    
     public void FindUncle(int data){
        if (this.root == null){
           System.out.println("No hay nodos en el árbol binario"); 
        } else {
             if (SearchUncle(root, data) == null){
                System.out.println("El nodo no tiene un abuelo");
            } else {
                System.out.println("El nodo tiene un abuelo con valor "+ this.SearchUncle(root, data).value);
            }
        }
    }
    
    public void NormalTraverse (Node n){ 
        for (int level=0; level <= this.Height(this.root); level++){ //Calls RecursiveTraverse in order of levels (A bit of a trick to make the recursion level traverse work).
            System.out.println("LEVEL: " + level);
            this.RecursiveTraverse(root, level); //This is the best I could find in recursion, most answers were that using recursion for this is useless (I agree :p).
            
        }
    }
    
    private void RecursiveTraverse(Node n, int l){ //A simple level traverse search but with the use of recursion (kinda). 
        if (n == null){
            return;
        } else {
            if (l == 0){ //It only prints the nodes in the given level
                System.out.println("     Valor: " + n.value);
            } else if(l > 0){
                RecursiveTraverse(n.left, l-1); //Calls itself (see, recursion!) but with the childs to look for all the nodes with the given level in the tree
                RecursiveTraverse(n.right, l-1);
            }
        }
    }
}
