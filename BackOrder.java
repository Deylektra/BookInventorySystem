import java.util.*;
public class BackOrder{
  int customer;
  int quantityShort;
  String title;
  String isbn;
  double price;
  String format;

//BackOrder constructor
  public BackOrder(String isbn,int quantityShort, int customer){
    this.isbn = isbn;
    this.customer = customer;
    this.quantityShort = quantityShort;
  }

  public String toString() {
      return String.format("%s %s %s", isbn, quantityShort, customer);
  }

  public String getIsbn() {
      return isbn;
  }

  public int getQuantity(){
      return quantityShort;
  }

  public int getCustomerNumber(){
      return customer;
  }

  public void changeQuantity(int change) {
          quantityShort = quantityShort + change;
  }

}
