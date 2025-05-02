import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AirlineQueriesImp implements AirlineDataQueries {
    private List<Flights> records;
    private Map<String, List<Flights>> airportCodeIndex = new HashMap<>();
    private Map<YearMonth, List<Flights>> dateIndex = new HashMap<>();
    private TreeMap<Integer, List<Flights>> delayTree = new TreeMap<>();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM");

    public AirlineQueriesImp(List<Flights> dataset) {
        records = dataset;
        buildIndices();
    }

    public AirlineQueriesImp() {
        records = new ArrayList<>();
    }

    private void buildIndices() {
        airportCodeIndex.clear();
        dateIndex.clear();
        delayTree.clear();
        
        for (Flights flight : records) {
            // Build airport code index
            airportCodeIndex.computeIfAbsent(flight.getAirportCode(), k -> new ArrayList<>()).add(flight);
            
            // Build date index
            YearMonth date = YearMonth.of(flight.getYear(), flight.getMonth());
            dateIndex.computeIfAbsent(date, k -> new ArrayList<>()).add(flight);
            
            // Build delay tree index
            delayTree.computeIfAbsent(flight.getFlightsDelayed(), k -> new ArrayList<>()).add(flight);
        }
    }

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
                    recordsLoaded++;
                } catch (NumberFormatException e) {
                    System.err.println("Skipping line due to number parsing error: " + line);
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line + " | Error: " + e.getMessage());
                }
            }

            // Build indices after loading all records
            buildIndices();
            System.out.println("Imported " + recordsLoaded + " records:");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return -1;
        }
        return recordsLoaded;
    }

    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
                if (inQuotes && i > 0 && line.charAt(i-1) == '"') {
                    currentValue.append('"');
                }
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }
        
        values.add(currentValue.toString());
        return values;
    }

    @Override  
    public List<Flights> flightsFromAirport(String attribute, Object value) {
        if(!"Airport.Code".equals(attribute) || value == null) {
            throw new IllegalArgumentException("Invalid arguments: attribute must be 'Airport.Code' and value cannot be null");
        }
        String code = value.toString();
        return new ArrayList<>(airportCodeIndex.getOrDefault(code, Collections.emptyList()));
    }

    @Override
    public List<Flights> delaysGreaterThan(String attribute, Comparable lowerBound, Comparable upperBound) {
        List<Flights> result = new ArrayList<>();
        
        if (!"Flights.Delayed".equals(attribute)) {
            return Collections.emptyList();
        }
        
        // Convert bounds to Integer
        Integer min = (lowerBound != null) ? (Integer) lowerBound : null;
        Integer max = (upperBound != null) ? (Integer) upperBound : null;
        
        // Use the TreeMap's subMap method for efficient range query
        NavigableMap<Integer, List<Flights>> subMap;
        
        if (min != null && max != null) {
            subMap = delayTree.subMap(min, true, max, true);
        } else if (min != null) {
            subMap = delayTree.tailMap(min, true);
        } else if (max != null) {
            subMap = delayTree.headMap(max, true);
        } else {
            // No bounds provided, return all flights
            return new ArrayList<>(records);
        }
        
        // Collect all flights in the range
        for (List<Flights> flights : subMap.values()) {
            result.addAll(flights);
        }
        
        return result;
    }

    @Override  
    public double averageTimeDelayed(String attribute, String startTime, String endTime) {
        if (!"Average.Delay".equals(attribute)) {
            return 0.0;
        }

        try {
            YearMonth startDate = YearMonth.parse(startTime, DATE_FORMAT);
            YearMonth endDate = YearMonth.parse(endTime, DATE_FORMAT);
            
            double sumOfDelays = 0.0;
            int count = 0;
            
            // Iterate through all months in the range
            for (YearMonth date = startDate; !date.isAfter(endDate); date = date.plusMonths(1)) {
                List<Flights> monthlyFlights = dateIndex.getOrDefault(date, Collections.emptyList());
                for (Flights flight : monthlyFlights) {
                    sumOfDelays += flight.getTotalMinsDelayed();
                    count++;
                }
            }
            
            return count > 0 ? sumOfDelays / count : 0.0;
        } catch (Exception e) {
            System.err.println("Error calculating average delay: " + e.getMessage());
            return 0.0;
        }
    }
}
  
  

