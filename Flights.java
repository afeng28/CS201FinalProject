public static class Flights {
  private String airportCode;
  private String carrierName;

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
