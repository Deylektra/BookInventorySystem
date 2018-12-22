//Julianne Sem
//CS 145
/*
   This program is a singly linked list that I made in CS 145.
*/

public class CS145LinkedList<E> {

   /* The ListNode class for this list. */
   private class ListNode {
      E data;
      ListNode next;

      /* Construct a ListNode containing data. */
      ListNode(E data) {
         this.data = data;
         next = null;
      }

      /* Construct a ListNode containing data, setting the
       * next. */
      ListNode(E data, ListNode next) {
         this.data = data;
         this.next = next;
      }
   }

   /* The first ListNode in the List. */
   private ListNode front;

   /* Construct an empty list object. */
   public CS145LinkedList() {
      front = null;
   }

   //This method returns the size of the list
   public int size() {
      int length = 0;
      ListNode current = this.front;
      while(current != null){
         length += 1;
         current = current.next;
      }
      return length;
   }

   //This method returns true if the list is empty
   public boolean isEmpty() {
      if(this.size() == 0){
         return true;
      }else
         return false;
   }

   /* Add the given element, value, to the end of the list. */
   public void add(E value) {
      if (front == null) {
         front = new ListNode(value);
      } else {
         ListNode current = front;
         while (current.next != null) {
            current = current.next;
         }
         current.next = new ListNode(value);
      }
   }

   //This method adds the value given in the parameter to the list at
   //a given index
   public void add(int index, E value) {
      if (index == 0) {
         front = new ListNode(value, front);
      }else {
         ListNode current = front;
         for (int i = 0; i < index - 1; i++) {
            current = current.next;
         }
         current.next = new ListNode(value,
         current.next);
      }
   }

   //This method returns the value at the given index when the
   // index if greater than 0 and is less than the size of the list
   public E get(int index) {
      if(0 <= index || index < this.size()){
         ListNode current = this.front;
         current = this.front;
         int currentIndex = 0;
         while(currentIndex < index && current != null){
            current = current.next;
            currentIndex += 1;
         }
      if(currentIndex < index - 1 && current == null){
         throw new IndexOutOfBoundsException("Index is out of range");
      }
      return current.data;
      }
      return null;
   }

   //This method removes the first element(value) from the index
   //and returns the value that was removed
   public E remove() {
      ListNode node = this.front;
      if (front.data == null) {
         return null;
      } else {
         E result = node.data;
         front = node.next;
         return result;
      }
   }

   //This method removes a element(value) at the given index and it returns
   //the value that was removed
   public E remove(int index) {
      ListNode node = this.front;
      if (index == 0) {
         front = front.next;
         return node.data;
      } else {
         ListNode current = this.front;
         for (int i = 0; i < index - 1; i++) {
            current = current.next;
         }
         node = current.next;
         current.next = current.next.next;
         return node.data;
      }
   }
}
