//This class is used to find the revenue of books ordered
public class Total{
  private double revenue;
  public Total(double revenue){
    this.revenue = revenue;
  }

  public void add(double value){
    revenue += value;
  }

  public String toString(){
    return String.format("%s", revenue);
  }
}
