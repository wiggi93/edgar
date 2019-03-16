package application;

import models.EdgarEntry;

import java.io.*;
import java.net.URL;
import java.util.HashMap;

public class EdgarMain {

    static String folderPath = "C:\\Users\\phili\\Desktop\\tsv";
    static String outputPath = "";
    static File folder = new File(folderPath);
    static File[] listOfFiles = folder.listFiles();
    static HashMap<String, EdgarEntry> lol = new HashMap<String, EdgarEntry>();
    static String baseUrl = "https://www.sec.gov/Archives/";
    private static String currentSIC = "";
    private static String currentCIK = "";
    private static application.ExcelHandler excelHandler;

    public static void main(String[] args){

        if (args.length>0){
            if (args[0].length()>1){
                folderPath = args[0];
            }

            if (args[1].length()>1){
                outputPath = args[1];
            }
        }

        parseFolder();
        excelHandler = new ExcelHandler(outputPath);
        try {
            excelHandler.createExcel(lol);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void parseFolder() {
        int index = 0;
        for (File i : listOfFiles) {
            try {
                parseFile(i.getPath());
                index++;
                System.out.println("File "+index+" done");

            } catch (IOException e) {
                e.printStackTrace();
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