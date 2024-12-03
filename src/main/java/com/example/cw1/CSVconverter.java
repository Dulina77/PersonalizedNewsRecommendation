package com.example.cw1;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;

public class CSVconverter {
    public String[][] ArticleCollector() {
        String csvFile = "merged_data.csv"; // Path to your CSV file
        String[][] data = null;
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            // Skip the header
            String[] header = reader.readNext();

            // Count rows for array size
            int rowCount = 0;
            while (reader.readNext() != null) {
                rowCount++;
            }

            // Reinitialize the reader to parse data again
            reader.close();
            CSVReader readerAgain = new CSVReader(new FileReader(csvFile));
            readerAgain.readNext(); // Skip the header

            // Create a 2D array for columns 1 and 3
            data = new String[rowCount][3];
            int rowIndex = 0;

            String[] row;
            while ((row = readerAgain.readNext()) != null) {
                if (row.length >= 3) { // Ensure at least 3 columns exist
                    data[rowIndex][0] = row[0]; // Column 1 (e.g., headline)

                    data[rowIndex][1] = row[2]; // Column 3 (e.g., content)
                    data[rowIndex][2] = row[1];
                }
                rowIndex++;
            }

            readerAgain.close();

            // Print the extracted data
            System.out.println("Extracted data (Column 1 ,2, 3):");
            for (String[] r : data) {
                System.out.println("Heading: " + r[0] + " | content: " + r[1]  + " | category: " + r[2]);
            }


        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return data;
    }


}