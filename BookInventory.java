//Julianne Sem
//Book inventory
//CS 145

/*
  BookInventory manages the info from the two inputted text files (Books and Transactions).
  This program prints out the output while using the classes Book, BackOrder, and Total to keep track of
  and manipulate the data in the text files.
*/

import java.util.*;
import java.io.*;


public class BookInventory{

  static File bookFile;
  static File transactionFile;
  static CS145LinkedList<Book> inventory = new CS145LinkedList<Book>();
  static ArrayList<BackOrder> ListOfBackOrders = new ArrayList<BackOrder>();
  static Total revenue = new Total(0.0);


  public static void main(String[] args) throws FileNotFoundException {
    if(handleArguments(args)){
          Scanner book = new Scanner(bookFile);
          Scanner backOrders = new Scanner(transactionFile);
          processBooks(book);
          processTransactions(backOrders);
          totalValue(revenue);
    }
  }


  //Checks that there are only 2 command line arguments and that the files
  //inputed are readable, if it's readable, it returns true

  public static boolean handleArguments(String[] args){
      if(args.length != 2){
        System.out.println("ERROR: Wrong number of command line arguments, need Book text file and Transactions text file");
        return false;
      }
     bookFile = new File(args[0]);
     transactionFile = new File(args[1]);

     if (!bookFile.canRead()) {
        System.out.println("The file " + args[0] + " cannot be opened for input.");
        return false;
     }else if (!transactionFile.canRead()) {
        System.out.println("The file " + args[1] + " cannot be opened for input.");
        return false;
     }
      return true;
  }


  //This method processes the book file and calls the convertToBook method
  //to convert each line of the file into a book object
  public static void processBooks(Scanner bookFile){
      while(bookFile.hasNextLine()){
        String line = bookFile.nextLine();
        String[] bookFileArray= line.split(",");
        convertToBook(bookFileArray);
      }
    }

  //converts an array of strings into a book object
  public static void convertToBook(String[] bookArray){
      Book currentBook = new Book(bookArray[0], bookArray[1], Double.parseDouble(bookArray[2]), bookArray[3]);
      currentBook.changeStock(Integer.parseInt(bookArray[4]));
      addBook(currentBook);
  }

  //adds a book to the LinkedList called inventory, it also prints out
  //the attributes of the book
  public static void addBook(Book currentBook){
    if(inventory.isEmpty()){
      inventory.add(currentBook);
      System.out.print("Book: " + currentBook + " Price: ");
      System.out.printf("%.2f", currentBook.getPrice());
      System.out.println(", Stock: " + currentBook.getStock() + ".");

      return;
    }
    int index = findBook(currentBook.getIsbn());
    if(index == -1){
       inventory.add(currentBook);
       System.out.print("Book: " + currentBook + " Price: ");
       System.out.printf("%.2f", currentBook.getPrice());
       System.out.println(", Stock: " + currentBook.getStock() + ".");

    }else{
       Book current = inventory.get(index);
       current.changeStock(currentBook.getStock());
    }
  }

  //This method uses the isbn to find the index of the Book in the
  //LinkedList inventory, it returns -1 if the Book was not found
  public static int findBook(String isbn){
      for(int i = 0; i < inventory.size(); i++){
          Book current = inventory.get(i);
          if(current.getIsbn().equals(isbn)){
            return i;
          }
      }return -1;
  }


  //This method processes the Transaction File and calls a certain method
  //depending on the first word in the line
  public static void processTransactions(Scanner transactionFile){// change back order to transaction
    boolean secondTime = false;
      while(transactionFile.hasNextLine()){
          String line = transactionFile.nextLine();
          String[] transactionFileArray = line.split(" ");
          if(transactionFileArray[0].equals("SHOW")){
            if(secondTime == false){
              System.out.println();
            }else{
              show();
            }
          }else if(transactionFileArray[0].equals("STOCK")){
              addStock(transactionFileArray);
              secondTime = true;
          }else if(transactionFileArray[0].equals("ORDER")){
              order(transactionFileArray);
              secondTime = true;
          }
      }
  }

  //this method adds stock to a Book and then calls the orderBackOrder
  //to check if any backorders can be fulfilled
  public static void addStock(String[] transactionArray){
      int index = findBook(transactionArray[1]);
      Book current = inventory.get(index);
      int currentStock = current.getStock();
      current.changeStock(Integer.parseInt(transactionArray[2]));
      System.out.println("Stock for book " + current + " increased from " + currentStock + " to " + current.getStock() + ".");
      orderBackOrder(current);
  }

  //This method prints every Book and current backorders
  public static void show(){
      System.out.println();
      for(int i = 0; i < inventory.size(); i++){
          Book current = inventory.get(i);
          int index = searchBackOrders(current.getIsbn());
          System.out.print("Book: " + current + " Price: ");
          System.out.printf("%.2f", current.getPrice());
          System.out.println(", Stock: " + current.getStock() + ".");

          BackOrder bookBackOrders;
          for(int k = 0; k < ListOfBackOrders.size(); k++){
              bookBackOrders = ListOfBackOrders.get(k);
              if(bookBackOrders.getIsbn().equals(current.getIsbn())){
                System.out.println("Backorders:");
                System.out.println("  customer: " + bookBackOrders.getCustomerNumber() + ", amount: " + bookBackOrders.getQuantity());
              }
          }
      }
      System.out.println();
  }


  //This method orders Books and figures out whether a backorder will need to
  //be created. It also adds the total of revenue from the order
  public static void order(String[] transactionArray){
    int index = findBook(transactionArray[1]);
    Book current = inventory.get(index);
    int currentStock = current.getStock();
    int orderedAmount = Integer.parseInt(transactionArray[2]);
    if((currentStock - orderedAmount) < 0){
      if(currentStock > 0){
        current.changeStock(-currentStock);
        transactionArray[2] = ""+(orderedAmount - currentStock);
        System.out.println("Order filled for customer " + Integer.parseInt(transactionArray[3]) + " for " + (currentStock) + " copies of book " + current + ".");
        revenue.add(currentStock * current.getPrice());
        addBackOrder(transactionArray);
      }else if(currentStock==0){
          System.out.print("");
          addBackOrder(transactionArray);
      }
    }
    else{
      current.changeStock(-orderedAmount);
      System.out.println("Order filled for customer " + Integer.parseInt(transactionArray[3]) + " for " + (Integer.parseInt(transactionArray[2])) + " copies of book " + current + ".");
      revenue.add((Integer.parseInt(transactionArray[2])) * current.getPrice());
    }
  }


  //This creates a new BackOrder and prints out information about the
  //backorder
  public static void addBackOrder(String[] transactionArray){
    BackOrder current = new BackOrder(transactionArray[1], Integer.parseInt(transactionArray[2]), Integer.parseInt(transactionArray[3]));
    ListOfBackOrders.add(current);
    int index = findBook(transactionArray[1]);
    Book bookInfo = inventory.get(index);
    System.out.println("Back order for customer " + transactionArray[3] + " for " + transactionArray[2] + " copies of book " + bookInfo);
  }


  //This method searches through the ArrayList to find the backorder, it
  //returns the index that it is at. If it was not found, then it will return -1
  public static int searchBackOrders(String isbn){
      for(int i =0; i < ListOfBackOrders.size(); i++){
           BackOrder current = ListOfBackOrders.get(i);
           if(current.getIsbn().equals(isbn)){
               return i;
           }
      } return -1;
  }

  //This method fulfilles as many backorders as possible for a given Book object
  //It prints out information about the backorder when at least part of the
  //order has been fulfilled.
  public static void orderBackOrder(Book bookBeingOrdered){
      int index = searchBackOrders(bookBeingOrdered.getIsbn());// while index doesn not equal -1 or there are books left
      int inStock = bookBeingOrdered.getStock();
      while(index != -1 && inStock > 0){
          BackOrder current = ListOfBackOrders.get(index);
          int orderedAmount = current.getQuantity();
          if(inStock - orderedAmount < 0){
              current.changeQuantity(-inStock);
              bookBeingOrdered.changeStock(-inStock);
              System.out.println("Back order filled for customer " + current.getCustomerNumber() + " for " + inStock + " copies of book " + bookBeingOrdered);
              index = -1;
              revenue.add(inStock * bookBeingOrdered.getPrice());
          }else{
              System.out.println("Back order filled for customer " + current.getCustomerNumber() + " for " + orderedAmount + " copies of book " + bookBeingOrdered);
              revenue.add(orderedAmount * bookBeingOrdered.getPrice());
              ListOfBackOrders.remove(index);
              bookBeingOrdered.changeStock(-orderedAmount);
          }
          inStock = bookBeingOrdered.getStock();
          index = searchBackOrders(bookBeingOrdered.getIsbn());
      }
  }

  //Prints out a string of the total revenue from books ordered.
  public static void totalValue(Total revenue){
    System.out.println();
    System.out.println("Total value of orders filled is $" + revenue);
  }

}
