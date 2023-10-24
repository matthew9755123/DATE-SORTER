package dateorganizer;

import java.util.*;

/**
 * This class describes a priority min-queue that uses an array-list-based min
 * binary heap
 * that implements the PQueueAPI interface. The array holds objects that
 * implement the
 * parameterized Comparable interface.
 * 
 * @author Duncan, Matthew Benfield
 * @param <E> the priority queue element type.
 * @since 9-24-23
 */
public class PQueue<E extends Comparable<E>> implements PQueueAPI<E> {

   /**
    * A complete tree stored in an array list representing the
    * binary heap
    */
   private ArrayList<E> tree;
   /**
    * A comparator lambda function that compares two elements of this
    * heap when rebuilding it; cmp.compare(x,y) gives 1. negative when x less than
    * y
    * 2. positive when x greater than y 3. 0 when x equal y
    */
   private Comparator<? super E> cmp;

   /**
    * Constructs an empty PQueue using the compareTo method of its data type as the
    * comparator
    */
   public PQueue() {
      cmp = Comparator.naturalOrder();
      tree = new ArrayList<>();
   }

   /**
    * A parameterized constructor that uses an externally defined comparator
    * 
    * @param fn - a trichotomous integer value comparator function
    */
   public PQueue(Comparator<? super E> fn) {
      cmp = fn;
      tree = new ArrayList<>();
   }

   public boolean isEmpty() {
      return tree.isEmpty();
   }

   public void insert(E obj) {
      tree.add(obj);
      int child = tree.size() - 1;
      int parent = (child - 1) / 2;

      while (child > 0 && cmp.compare(tree.get(child), tree.get(parent)) < 0) {
         swap(child, parent);
         child = parent;
         parent = (child - 1) / 2;
      }
   }

   public E remove() throws PQueueException {
      if (isEmpty()) {
         throw new PQueueException("Priority queue is empty");
      }

      E root = tree.get(0);

      if (tree.size() == 1) {
         return tree.remove(0);
      } else {
         tree.set(0, tree.remove(tree.size() - 1));
         rebuild(0);
      }
      return root;
   }

   public E peek() throws PQueueException {
      if (isEmpty()) {
         throw new PQueueException("Priority queue is empty");
      }
      return tree.get(0);
   }

   public int size() {
      return tree.size();
   }

   /**
    * Swaps a parent and child elements of this heap at the specified indices
    * 
    * @param place  an index of the child element on this heap
    * @param parent an index of the parent element on this heap
    */
   private void swap(int place, int parent) {
      E temp = tree.get(place);
      tree.set(place, tree.get(parent));
      tree.set(parent, temp);
   }

   /**
    * Rebuilds the heap to ensure that the heap property of the tree is preserved.
    * 
    * @param root the root index of the subtree to be rebuilt
    */
   private void rebuild(int root) {
      int child = 2 * root + 1;

      if (child < tree.size()) {

         int rightChild = child + 1;

         if (rightChild < tree.size() && cmp.compare(tree.get(rightChild), tree.get(child)) < 0) {

            child = rightChild;

         }

         if (cmp.compare(tree.get(child), tree.get(root)) < 0) {

            swap(child, root);

            rebuild(child);

         }

      }

   }

}
