public class Main{
  public static void main(String[] args){
    //create object of implementation class
    AirlineQueriesImp b = new AirlineQueriesImp();
    String filename = "airlines.csv";
    b.loadDataset(filename);
    //do other functions
  }
}
