package monitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HealthMonitor {
	public static void main(String[] args) {

		// SHOW USER MENU
		System.out.println("=== SERVER HEALTH MONITOR ===");
		System.out.println("1. Run Health Check");
		System.out.println("2. Generate Report");
		System.out.println("Please enter your choice (1 or 2): ");

		// READ USER INPUT
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int choice = scanner.nextInt();

		if (choice == 1) {
			runHealthCheck();
		} else if (choice == 2) {
			generateReport();
		} else {
			System.out.println("Invalid choice!");
		}

		scanner.close();
	}

	public static void runHealthCheck() {
		String apiUrl = "https://jsonplaceholder.typicode.com/posts/1";

		try {
			// MAKE A CONNECTION TO API URL
			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			// LOAD API AND CHECK THE RESPONSE
			int responseCode = connection.getResponseCode();
			System.out.println("Response Code: " + responseCode + "\n");

			// IF GET IS 200 - DO AS FOLLOW
			if (responseCode == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String line;
				StringBuilder response = new StringBuilder();

				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();

				// PARSE JSON RESPONSE
				String jsonResponse = response.toString();
				// EXTRACT VALUES FROM JSON
				String userId = extractValue(jsonResponse, "userId");
				String id = extractValue(jsonResponse, "id");
				String title = extractValue(jsonResponse, "title");

				// FORMAT OUTPUT
				String status = "OK - Response: " + responseCode + " | User ID: " + userId + " | Post ID: " + id
						+ " | Title: " + title;

				System.out.println("=== API HEALTH CHECK ===");
				System.out.println("Response Code: " + responseCode);
				System.out.println("Status: OK");
				System.out.println("User ID: " + userId);
				System.out.println("Post ID: " + id);
				System.out.println("Title: " + title);

				// SAVE TO FILE
				saveToLog(status);
				System.out.println("\n✓ Log saved to health_log.txt");

			} else {
				String status = "DOWN - Response Code: " + responseCode;

				System.out.println("=== API HEALTH CHECK ===");
				System.out.println("Status: DOWN");
				System.out.println("Response Code: " + responseCode);

				saveToLog(status);
				System.out.println("\n✓ Log saved to health_log.txt");
			}

		} catch (Exception e) {
			System.out.println("=== API HEALTH CHECK ===");
			System.out.println("Status: ERROR");
			System.out.println("Error: " + e.getMessage());
		}
	}

	public static void generateReport() {
		File logFile = new File("health_log.txt");

		// CHECK, IF FILE EXISTS
		if (!logFile.exists()) {
			System.out.println("\n=== HEALTH REPORT ===");
			System.out.println("No log file found. Run health check first!");
			return;
		}

		int totalChecks = 0;
		int okCount = 0;
		int downCount = 0;
		int errorCount = 0;
		String lastCheck = "";

		try (BufferedReader reader = new BufferedReader(new java.io.FileReader(logFile))) {

			String line;
			while ((line = reader.readLine()) != null) {
				totalChecks++;
				lastCheck = line; // LAST LINE == LAST CHECK

				// SUM UP STATUS
				if (line.contains("OK - Response")) {
					okCount++;
				} else if (line.contains("DOWN")) {
					downCount++;
				} else if (line.contains("ERROR")) {
					errorCount++;
				}
			}

			// COUNT UPTIME PERCENTAGE
			double uptimePercent = 0;
			if (totalChecks > 0) {
				uptimePercent = (okCount * 100.0) / totalChecks;
			}

			// CREATE A REPORT
			System.out.println("\n=== HEALTH REPORT ===");
			System.out.println("Total Checks: " + totalChecks);
			System.out.println("OK: " + okCount);
			System.out.println("DOWN: " + downCount);
			System.out.println("ERROR: " + errorCount);
			System.out.println(String.format("Uptime: %.2f%%", uptimePercent));
			System.out.println("\nLast Check:");
			System.out.println(lastCheck);

		} catch (IOException e) {
			System.out.println("Error reading log file: " + e.getMessage());
		}
	}

	public static void saveToLog(String message) {
		File logFile = new File("health_log.txt");

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
			// true = append

			// TIMESTAMP
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String timestamp = now.format(formatter);

			// SAVE
			writer.write("[" + timestamp + "] " + message);
			writer.newLine();

		} catch (IOException e) {
			System.out.println("Error saving to log: " + e.getMessage());
		}
	}

	// HELPER METHOD - MANUAL JSON PARSING
	public static String extractValue(String json, String key) {
		try {
			int start = json.indexOf("\"" + key + "\":") + key.length() + 3;
			int end = json.indexOf(",", start);
			if (end == -1)
				end = json.indexOf("}", start);

			String value = json.substring(start, end).replace("\"", "").trim();
			return value;

		} catch (Exception e) {
			return "N/A";
		}
	}

}