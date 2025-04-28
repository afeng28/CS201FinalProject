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
  

 public Flights (String airportCode, String timeLabel, int month, int year, String carrierName, int flightsDelayed, int totalMinsDelayed, int totalFlights) {
    this.airportCode = airportCode;
    this.airportName = airportName;
    
    this.timeLabel = timeLabel;
    this.month = month;
    this.year = year;
    this.carrierName = carrierName;
    this.flightsDelayed = flightsDelayed;
    this.totalMinsDelayed = totalMinsDelayed;
    this.totalFlights = totalFlights;
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
  
  @Override
  public String toString() {
      return "AirportStats{" +
              "airportCode='" + airportCode + '\'' +
              ", carrierName='" + carrierName + '\'' +
              ", airportName='" + airportName + '\'' +
              ", totalMinsDelayed=" + totalMinsDelayed +
              ", totalFlights=" + totalFlights +
              ", timeLabel='" + timeLabel + '\'' +
              ", month=" + month +
              ", year=" + year +
              ", flightsDelayed=" + flightsDelayed +
              '}';
}
  
}
