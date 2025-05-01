import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class AirlineDataQueriesImplTest {
    private AirlineDataQueries queryEngine;
    private List<Flights> testDataset;

    @Before
    public void setUp() {
        // Create a test dataset
        testDataset = new ArrayList<>();
        // Add sample flight records to the test dataset
        testDataset.add(new Flights("JFK", "Delta", "John F Kennedy Intl", 
            1, 2023, "January 2023", 15, 100, 120));
        testDataset.add(new Flights("LAX", "American", "Los Angeles Intl", 
            1, 2023, "January 2023", 25, 150, 180));
        testDataset.add(new Flights("JFK", "United", "John F Kennedy Intl", 
            2, 2023, "February 2023", 10, 80, 90));
        testDataset.add(new Flights("ORD", "Delta", "Chicago O'Hare Intl", 
            2, 2023, "February 2023", 30, 120, 200));

        // Initialize the query engine with the test dataset
        queryEngine = new AirlineQueriesImp(testDataset);
    }

    @Test
    public void testFlightsFromAirport() {
        // Test query for flights from JFK
        List<Flights> results = queryEngine.flightsFromAirport("Airport.Code", "JFK");
        
        // Assert that 2 flights are returned
        assertEquals(2, results.size());
        
        // Assert that all results have JFK as airport code
        for (Flights flight : results) {
            assertEquals("JFK", flight.getAirportCode());
        }
    }

    @Test
    public void testDelaysGreaterThan() {
        // Test range query for flights with 15 <= delays <= 25
        List<Flights> results = queryEngine.delaysGreaterThan("Flights.Delayed", 15, 25);
        
        // Assert that 2 flights are returned
        assertEquals(2, results.size());
        
        // Assert that all results are within the expected range
        for (Flights flight : results) {
            int delays = flight.getFlightsDelayed();
            assertTrue(delays >= 15 && delays <= 25);
        }
    }

    @Test
    public void testComputeAverageTotalDelay() {
        // Test average delay calculation
        double average = queryEngine.computeAverageTotalDelay();
        
        // Expected average = (120 + 180 + 90 + 200) / 4 = 147.5
        assertEquals(147.5, average, 0.001);
    }

    @Test
    public void testFlightsFromAirportWithNoResults() {
        // Test query for non-existent airport
        List<Flights> results = queryEngine.flightsFromAirport("Airport.Code", "SFO");
        
        // Assert that no results are returned
        assertTrue(results.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlightsFromAirportWithUnsupportedAttribute() {
        // Test query with unsupported attribute
        queryEngine.flightsFromAirport("unsupported_attribute", "value");
    }
}
