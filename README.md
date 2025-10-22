# Server Health Monitor

A Java-based application that monitors API health, logs results, and generates uptime reports.

## Features

✅ **Health Check** - Monitors API endpoint status  
✅ **Automatic Logging** - Saves results with timestamps to `health_log.txt`  
✅ **Report Generation** - Analyzes logs and calculates uptime percentage  
✅ **Simple CLI Menu** - User-friendly command-line interface  

## Technologies Used

- Java (Core)
- HttpURLConnection (API calls)
- BufferedReader/Writer (File I/O)
- Manual JSON parsing

## Skills Demonstrated (DevOps Focus)

- ✅ File I/O operations (logging, reading)
- ✅ Exception handling (try-catch, try-with-resources)
- ✅ HTTP API calls (REST)
- ✅ Data parsing (manual JSON)
- ✅ CLI applications
- ✅ Code organization (methods, clean structure)
- ✅ Git version control

## How to Run

1. Clone the repository
2. Open in IDE (Eclipse/IntelliJ or compile via terminal):

   javac monitor/HealthMonitor.java
   java monitor.HealthMonitor

3. Choose option:
   - `1` - Run health check
   - `2` - Generate report

## Sample Output

```
=== HEALTH REPORT ===
Total Checks: -|-
OK: -|-
DOWN: -|-
ERROR: -|-
Uptime: -|-%

```

## Use Cases

- DevOps automatisation monitoring 
- API uptime tracking
- Learning Java I/O and HTTP connections

## Author

Piotr Matynia - Aspiring DevOps Engineer

## Future Improvements

- [ ] Support multiple API endpoints
- [ ] Email/Slack notifications on downtime
- [ ] Configurable check intervals
- [ ] JSON library integration