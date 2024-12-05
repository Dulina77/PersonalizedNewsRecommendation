package com.example.cw1;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;

public class CSVconverter {
    public String[][] ArticleCollector() {
        String csvFile = "merged_data.csv";
        String[][] data = null;
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] header = reader.readNext();
            int rowCount = 0;
            while (reader.readNext() != null) {
                rowCount++;
            }

            reader.close();
            CSVReader readerAgain = new CSVReader(new FileReader(csvFile));
            readerAgain.readNext();

            data = new String[rowCount][3];
            int rowIndex = 0;

            String[] row;
            while ((row = readerAgain.readNext()) != null) {
                if (row.length >= 3) {
                    data[rowIndex][0] = row[0];

                    data[rowIndex][1] = row[2];
                    data[rowIndex][2] = row[1];
                }
                rowIndex++;
            }

            readerAgain.close();

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