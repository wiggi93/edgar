package application;

import models.EdgarEntry;

import java.io.*;
import java.net.URL;
import java.util.HashMap;

public class EdgarMain {

    static String folderPath = "";
    static String outputPath = "";
    static HashMap<String, EdgarEntry> lol = new HashMap<String, EdgarEntry>();
    static String baseUrl = "https://www.sec.gov/Archives/";
    private static String currentSIC = "";
    private static String currentCIK = "";
    private static ExcelHandler excelHandler;

    public static void main(String[] args) {

        if (setPaths(args)){
            parseFolder();
            exportToExcel();
        }

    }

    private static void exportToExcel() {
        excelHandler = new ExcelHandler(outputPath);
        try {
            excelHandler.createExcel(lol);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean setPaths(String[] args) {
        if (args.length > 0) {
            if (args[0].length() > 1) {
                folderPath = args[0];
                if (!folderPath.endsWith("\\")) {
                    folderPath = folderPath + "\\";
                }
                System.out.println(folderPath);
            }

            if (args[1].length() > 1) {
                outputPath = args[1];
                if (!outputPath.endsWith("\\")) {
                    outputPath = outputPath + "\\";
                }
                System.out.println(outputPath);
            }
            return true;
        } else {
            System.out.println("Please set input / output folder as arguments on execution");
            return false;
        }
    }

    public static void parseFolder() {

        if (!folderPath.isEmpty()) {
            File folder = new File(folderPath);
            File[] listOfFiles = folder.listFiles();
            int index = 0;
            for (File i : listOfFiles) {
                try {
                    parseFile(i.getPath());
                    index++;
                    System.out.println("File " + index + " done");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void parseFile(String tsvPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(tsvPath));
        String row = "";
        String currentCompany = "";

        while ((row = br.readLine()) != null) {
            currentCompany = row.split("\\|")[1];

            if (!lol.containsKey(currentCompany)) {
                setSICAndCIK(row.split("\\|")[4]);

                if (currentSIC != null) {
                    EdgarEntry entry = new EdgarEntry(currentCompany, currentCIK, currentSIC);
                    lol.put(currentCompany, entry);
                    if (lol.size() % 50 == 0)
                        System.out.println(lol.size() + "companies added");
                    currentSIC = null;
                }
            }
        }
        br.close();
    }

    private static void setSICAndCIK(String linkToCompanyTxt) throws IOException {

        URL url = new URL(baseUrl + linkToCompanyTxt);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = br.readLine()) != null)
            if (inputLine.split("STANDARD INDUSTRIAL CLASSIFICATION:").length >= 2) {
                currentSIC = inputLine.split("STANDARD INDUSTRIAL CLASSIFICATION:")[1].split("\\[")[1].substring(0, 4);

            } else if (inputLine.split("CENTRAL INDEX KEY:").length >= 2) {
                currentCIK = inputLine.split("CENTRAL INDEX KEY:")[1].substring(3, 13);
            }
        br.close();

    }
}