import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Destination {
    String name;
    LocalDate arrivalDate;
    LocalDate departureDate;
    double estimatedBudget;
    ArrayList<Activity> activities;
    WeatherForecast weatherForecast;

    public Destination(String name) {
        this.name = name;
        this.activities = new ArrayList<>();
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }
}

class Activity {
    String name;
    LocalDate date;
    double estimatedCost;
    String category;

    public Activity(String name, LocalDate date, double estimatedCost, String category) {
        this.name = name;
        this.date = date;
        this.estimatedCost = estimatedCost;
        this.category = category;
    }
}

class WeatherForecast {
    String condition;
    double temperature;
    double precipitation;

    public WeatherForecast(String condition, double temperature, double precipitation) {
        this.condition = condition;
        this.temperature = temperature;
        this.precipitation = precipitation;
    }
}

class TravelItineraryPlanner {
    private ArrayList<Destination> destinations;
    private Scanner scanner;
    private double totalBudget;

    public TravelItineraryPlanner() {
        destinations = new ArrayList<>();
        scanner = new Scanner(System.in);
        totalBudget = 0;
    }

    public void addDestination() {
        System.out.print("Enter destination name: ");
        String name = scanner.nextLine();
        
        Destination destination = new Destination(name);

        System.out.print("Enter arrival date (YYYY-MM-DD): ");
        LocalDate arrivalDate = LocalDate.parse(scanner.nextLine());
        destination.arrivalDate = arrivalDate;

        System.out.print("Enter departure date (YYYY-MM-DD): ");
        LocalDate departureDate = LocalDate.parse(scanner.nextLine());
        destination.departureDate = departureDate;

        System.out.print("Enter estimated budget for this destination: $");
        double estimatedBudget = scanner.nextDouble();
        scanner.nextLine();
        destination.estimatedBudget = estimatedBudget;
        totalBudget += estimatedBudget;

        destination.weatherForecast = generateMockWeatherForecast();

        addActivities(destination);

        destinations.add(destination);
    }

    private void addActivities(Destination destination) {
        while (true) {
            System.out.print("Add an activity (Y/N)? ");
            String choice = scanner.nextLine();
            
            if (choice.equalsIgnoreCase("N")) break;

            System.out.print("Activity name: ");
            String activityName = scanner.nextLine();

            System.out.print("Activity date (YYYY-MM-DD): ");
            LocalDate activityDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Estimated cost: $");
            double activityCost = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("Activity category (Sightseeing/Food/Accommodation/Transport): ");
            String category = scanner.nextLine();

            Activity activity = new Activity(activityName, activityDate, activityCost, category);
            destination.addActivity(activity);
        }
    }

    private WeatherForecast generateMockWeatherForecast() {
        String[] conditions = {"Sunny", "Cloudy", "Rainy", "Partly Cloudy"};
        String condition = conditions[(int)(Math.random() * conditions.length)];
        double temperature = 15 + Math.random() * 20;
        double precipitation = Math.random() * 100;

        return new WeatherForecast(condition, temperature, precipitation);
    }

    public void displayItinerary() {
        if (destinations.isEmpty()) {
            System.out.println("No destinations added yet.");
            return;
        }

        System.out.println("\n=== COMPREHENSIVE TRAVEL ITINERARY ===");
        
        for (Destination dest : destinations) {
            System.out.println("\nDestination: " + dest.name);
            System.out.println("Arrival: " + dest.arrivalDate);
            System.out.println("Departure: " + dest.departureDate);
            System.out.println("Trip Duration: " + 
                ChronoUnit.DAYS.between(dest.arrivalDate, dest.departureDate) + " days");
            
            System.out.println("\nWeather Forecast:");
            System.out.printf("Condition: %s\n", dest.weatherForecast.condition);
            System.out.printf("Temperature: %.1f°C\n", dest.weatherForecast.temperature);
            System.out.printf("Precipitation: %.1f%%\n", dest.weatherForecast.precipitation);

            System.out.println("\nActivities:");
            double destTotalCost = 0;
            HashMap<String, Double> categoryCosts = new HashMap<>();

            for (Activity activity : dest.activities) {
                System.out.printf("- %s on %s: $%.2f (Category: %s)\n", 
                    activity.name, activity.date, activity.estimatedCost, activity.category);
                
                destTotalCost += activity.estimatedCost;
                categoryCosts.put(activity.category, 
                    categoryCosts.getOrDefault(activity.category, 0.0) + activity.estimatedCost);
            }

            System.out.printf("\nDestination Budget: $%.2f\n", dest.estimatedBudget);
            System.out.printf("Total Activities Cost: $%.2f\n", destTotalCost);
            
            System.out.println("\nCategory Cost Breakdown:");
            categoryCosts.forEach((category, cost) -> 
                System.out.printf("%s: $%.2f\n", category, cost));
        }

        System.out.printf("\nTotal Trip Budget: $%.2f\n", totalBudget);
    }

    public void run() {
        while (true) {
            System.out.println("\n--- Travel Itinerary Planner ---");
            System.out.println("1. Add Destination");
            System.out.println("2. View Itinerary");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addDestination();
                    break;
                case "2":
                    displayItinerary();
                    break;
                case "3":
                    System.out.println("Exiting Travel Planner...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        TravelItineraryPlanner planner = new TravelItineraryPlanner();
        planner.run();
    }
}
