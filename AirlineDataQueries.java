//interface for data queries
public interface AirlineDataQueries{
  //load dataset
  public int loadDataset(String filePath) throws IOException
  //exact match query
  /**
  * Returns all records that exactly match the specified
  criteria.
  * @param attribute The attribute/field to query on
  * @param value The exact value to match
  * @return A collection of records matching the criteria
  */
  List<Flights> flightsFromAirport(String attribute, Object value);
  /**
  * Returns all records where the specified attribute falls
  within the given range.
  * @param attribute The attribute/field to query on
  * @param lowerBound The lower bound of the range (inclusive)
  * @param upperBound The upper bound of the range (inclusive)
  * @return A collection of records matching the criteria
  */
  List<Flights> delaysGreaterThan(String attribute, Comparable lowerBound);
  
  
  * Returns the average value of the specified attribute during
  a given time frame
  * @param attribute The attribute/field
  * @param startTime The start time of the period (inclusive)
  * @param endTime The end time of the period (inclusive)
  * @return The statistic calculated */
  List<Flights> averageTimeDelayed(String attribute, timeStamp startTime,
  timeStamp endTime);

}
