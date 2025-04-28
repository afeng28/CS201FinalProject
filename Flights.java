public static class Flights {
  private String airportCode;
  private String carrierName;
  private String airportName;
  private int totalMinsDelayed;
  private int totalFlights;
  private String timeLabel;
  private int month;
  private int year;
  private int flightsDelayed;
  

  public Flights (String airportCode, String carrierName) {
    this.airportCode = airportCode;
    this.carrierName = carrierName;
  }

  public String getAirportCode() {
    return airportCode;
  }

  public String getCarrierName() {
    return carrierName;
  }
  public String getAirportName() {
    return airportName;
  }
  public int getTotalMinsDelayed() {
    return totalMinsDelayed;
  }
  public string getTime(){
    return timeLabel;
  }
  public int getMonth() {
    return month;
  }
  public int getYear() {
    return year;
  }
  private int getFlightsDelayed(){
    return flightsDelayed;
  }
  

  
}
