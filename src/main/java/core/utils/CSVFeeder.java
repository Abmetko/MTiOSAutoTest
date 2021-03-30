package core.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;


public class CSVFeeder {

    private static CSVReader csvReader;

    private CSVFeeder(){ }

    public static CSVReader getCsvReaderInstance(){
        if (csvReader == null) {
            {
                try {
                    csvReader = new CSVReaderBuilder(new FileReader("src/main/resources/projectData.CSV"))
                            .withSkipLines(1).build(); //skip line with headers
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return csvReader;
    }

    public static String extractData(String packageName){
        String extractedData;
        try {
            for(String[] line:getCsvReaderInstance().readAll()){
                if(line[0].equals(packageName)){
                    extractedData = line[1];
                    getCsvReaderInstance().close();
                    return extractedData;
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        try {
            getCsvReaderInstance().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}