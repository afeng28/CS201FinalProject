import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class AirlineQueriesImp implements AirlineDataQueries{
  List<Flights> records;
  @Override
  public int loadDataset(String csvFile) {
    int recordsLoaded = 0; // Track how many records were successfully loaded
    String line;
    String csvSplitBy = ",";
    List<Flights> records = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        // Read/skip first line (headers)
        br.readLine(); // Discard header line (no need to store it)

        // Read each line from the CSV file
        while ((line = br.readLine()) != null) {
            try {
                // Trim and split the line by comma (handles extra whitespace)
                String[] values = line.trim().split(csvSplitBy);

                // Validate that the line has enough columns
                if (values.length < 23) { // Ensure all expected columns exist
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                // Parse values safely (handle NumberFormatException)
                Flights tmp = new Flights(
                    values[0].trim(),       // airportCode
                    values[1].trim(),       // carrierName
                    values[2].trim(),       // airportName
                    Integer.parseInt(values[3].trim()),  // month
                    Integer.parseInt(values[4].trim()),  // year
                    values[11].trim(),      // timeLabel
                    Integer.parseInt(values[15].trim()), // total mins flights
                    Integer.parseInt(values[18].trim()), // total flights
                    Integer.parseInt(values[22].trim())  // flightsDelayed
                );

                records.add(tmp);
                recordsLoaded++; // Increment successful load count
            } catch (NumberFormatException e) {
                System.err.println("Skipping line due to number parsing error: " + line);
            } catch (Exception e) {
                System.err.println("Error processing line: " + line + " | Error: " + e.getMessage());
            }
        }

        // Print the imported data (optional)
        System.out.println("Imported " + recordsLoaded + " records:");
        for (Flights record : records) {
            System.out.println(record);
        }

    } catch (FileNotFoundException e) {
        System.err.println("Error: CSV file not found: " + csvFile);
        return -1; // Indicate failure
    } catch (IOException e) {
        System.err.println("Error reading CSV file: " + e.getMessage());
        return -1;
    }

    return recordsLoaded; // Return the number of successfully loaded records
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
  public List<Flights> delaysGreaterThan(String flightsDelayed, Comparable lowerBound, Comparable upperBound){
    List<Flights> delays = new ArrayList<>();
    for(Flights flight : records){

    }
    return delays;
  }

  public double computeAverageTotalDelay() {
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
            double delayMinutes = Double.parseDouble(flight.getTotalMinutesDelayed());
            sumOfDelays += delayMinutes; // Add to running total
            count++;                     // Increase count of valid flights
        } catch (NumberFormatException e) {
            System.out.println("Skipping invalid delay time: " + flight.getTotalMinutesDelayed());
        }
    }

    return sumOfDelays / count; // Computes the average and returns
}
  
  
}
