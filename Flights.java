public static class Flights {
  private String airportCode;
  private String carrierName;
  private String airportName;
  private int totalMinsDelayed;
  

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
}
