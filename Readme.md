# UBN Crosschecker

UBN Crosschecker is a Java-based tool designed to analyze and process QSO (contact) data for amateur radio operators. It provides utilities for calculating error rates, best hourly rates, and other statistics.

## Features

-   **Error Analysis**: Visualize error percentages by operator using pie charts.
-   **Rate Statistics**: Calculate the best hourly QSO rates for each operator.
-   **Data Processing**: Group and analyze QSO data by various criteria.

## Technologies Used

-   **Java**: Core programming language.
-   **JavaFX**: For creating interactive visualizations (e.g., pie charts).
-   **Maven**: Dependency management and build automation.

## Installation

1.  Clone the repository:
    ```bash
    git clone [https://github.com/ddimunzio/ubn-crosschecker.git](https://github.com/ddimunzio/ubn-crosschecker.git)
    cd ubn-crosschecker
    ```
2.  Build the project using Maven:
    ```bash
    mvn clean install
    ```
3.  Run the application:
    ```bash
    mvn javafx:run
    ```

## Usage

-   **Error Percentage Visualization**: Displays a pie chart showing error percentages by operator.
-   **Best Hourly Rate Calculation**: Processes QSO data to determine the highest hourly QSO rate for each operator.

## Code Structure

-   `src/main/java/org/lw5hr/tool/utils/RateStats.java`: Contains logic for calculating best hourly rates.
-   `src/main/java/org/lw5hr/model/Qso.java`: Represents QSO data.
-   `src/main/java/org/lw5hr/tool/ui`: Handles UI components and visualizations.

## Contributing

Contributions are welcome! Please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix.
3.  Commit your changes and push the branch.
4.  Open a pull request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

For questions or feedback, please contact ddimunzio.

![Screenshot 2025-06-18 115339.png](images/Screenshot%202025-06-18%20115339.png)