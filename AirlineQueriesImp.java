import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class AirlineQueriesImp implements AirlineDataQueries{
  private List<Flights> records;
  private Map<String, List<Flights>> airportCodeIndex = new HashMap<>();
  @Override
public int loadDataset(String csvFile) {
    int recordsLoaded = 0;
    records = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        br.readLine(); // Skip header

        String line;
        while ((line = br.readLine()) != null) {
            try {
                List<String> values = parseCsvLine(line);
                
                if (values.size() < 23) {
                    System.err.println("Skipping malformed line (not enough columns): " + line);
                    continue;
                }

                Flights tmp = new Flights(
                    values.get(0).trim(),  // airportCode
                    values.get(1).trim(),  // airportName
                    values.get(2),         // timeLabel
                    Integer.parseInt(values.get(3)), // month
                    Integer.parseInt(values.get(5)), // year
                    values.get(11).trim(), // carrierName
                    Integer.parseInt(values.get(19)), // flightsDelayed
                    Integer.parseInt(values.get(22)), // totalFlights
                    Integer.parseInt(values.get(23)) // totalMinsDelayed
                );

                records.add(tmp);
                airportCodeIndex.computeIfAbsent(tmp.getAirportCode(), k -> new ArrayList<>()).add(tmp);
                recordsLoaded++;
            } catch (NumberFormatException e) {
                System.err.println("Skipping line due to number parsing error: " + line);
            } catch (Exception e) {
                System.err.println("Error processing line: " + line + " | Error: " + e.getMessage());
            }
        }

        System.out.println("Imported " + recordsLoaded + " records:");
    } catch (IOException e) {
        System.err.println("Error reading file: " + e.getMessage());
        return -1;
    }
    return recordsLoaded;
}

/**
 * Parses a CSV line, handling quoted fields with commas
 */
private List<String> parseCsvLine(String line) {
    List<String> values = new ArrayList<>();
    StringBuilder currentValue = new StringBuilder();
    boolean inQuotes = false;

    for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);
        
        if (c == '"') {
            // Toggle quote state
            inQuotes = !inQuotes;
            
            // Handle double quotes (escaped quotes in CSV)
            if (inQuotes && i > 0 && line.charAt(i-1) == '"') {
                currentValue.append('"');
            }
        } else if (c == ',' && !inQuotes) {
            // End of field
            values.add(currentValue.toString());
            currentValue = new StringBuilder();
        } else {
            // Regular character
            currentValue.append(c);
        }
    }
    
    // Add the last field
    values.add(currentValue.toString());
    
    return values;
}
  @Override  
  public List<Flights> flightsFromAirport(String attribute, Object value) {
    if(!"Airport.Code".equals(attribute) || value  == null) {
      return Collections.emptyList();
    }
    String code = value.toString();
    return airportCodeIndex.getOrDefault(code, Collections.emptyList());
  }

    @Override
    public List<Flights> delaysGreaterThan(String attribute, Comparable lowerBound, Comparable upperBound) {
        List<Flights> result = new ArrayList<>();
        
        // Validate input
        if (!"Flights.Delayed".equals(attribute) ){
            return Collections.emptyList();
        }
        
        // Iterate through all flights
        for (Flights flight : records) {
            try {
                int delay = flight.getFlightsDelayed(); // 
                
                // Check if delay is within bounds
                boolean satisfiesLower = (lowerBound == null) || (lowerBound.compareTo(delay) <= 0);
                boolean satisfiesUpper = (upperBound == null) || (upperBound.compareTo(delay) >= 0);
                
                if (satisfiesLower && satisfiesUpper) {
                    result.add(flight);
                }
            } catch (Exception e) {
                System.err.println("Error processing flight: " + flight + " | Error: " + e.getMessage());
            }
        }
        
        return result;
    }

  @Override  
  public double averageTimeDelayed(String attribute, String startTime,
  String endTime) {
    // If no records are loaded, return 0 to avoid division by zero
    if (records.isEmpty()) {
        return 0.0;
    }

    double sumOfDelays = 0.0;  // To accumulate total delay minutes
    int count = 0;             // To count valid records

    // Loop through all flight records
    for (Flights flight : records) {
        try {
            // Parse the total minutes delayed as a double
            double delayMinutes = flight.getTotalMinsDelayed();
            sumOfDelays += delayMinutes; // Add to running total
            count++;                     // Increase count of valid flights
        } catch (NumberFormatException e) {
            System.out.println("Skipping invalid delay time: " + flight.getTotalMinsDelayed());
        }
    }

    return sumOfDelays / count; // Computes the average and returns
}
  
  
}
