import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
public class AirlineQueriesImp implements AirlineDataQueries{
  
  private List<Flights> records = new ArrayList<>();
  private Map<String, List<Flights>> airportCodeIndex = new HashMap<>();
  
  public int loadDataset(String csvFile) {
    String line;
    String csvSplitBy = ",";
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            //Read/skip first line (headers)
            line = br.readLine();
            // Read each line from the CSV file
            while ((line = br.readLine()) != null) {

                // Split the line by comma and add to records
                String[] values = line.split(csvSplitBy);
                //replace with correct values here
               Flights tmp = new Flights(
                values[2],  // airportCode
                values[6],  // carrierName
                values[3],  // airportName
                Integer.parseInt(values[18]),  // totalMinsDelayed
                Integer.parseInt(values[7]),   // totalFlights
                values[21],  // timeLabel
                Integer.parseInt(values[4]),   // month
                Integer.parseInt(values[5]),   // year
                Integer.parseInt(values[8])    // flightsDelayed
);
                records.add(tmp);
            }

            // Print the imported data
            System.out.println("Imported CSV data:");
            for (Flights record : records) {
                System.out.println(record);
            }
  }

  public List<Flights> flightsFromAirport(String attribute, Object value) {
    if(!"Airport.Code".equals(attribute) || value  == null) {
      return Collections.emptyList();
    }
    String code = value.toString();
    return airportCodeIndex.getOrDefault(code, Collections.emptyList());
  }
  
}
