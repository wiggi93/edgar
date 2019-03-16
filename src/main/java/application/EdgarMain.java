package application;

import models.EdgarEntry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

        setPaths(args);
        parseFolder();
        exportToExcel();
    }

    private static void exportToExcel() {
        excelHandler = new ExcelHandler(outputPath);
        try {
            excelHandler.createExcel(lol);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setPaths(String[] args) {
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
        } else {
            folderPath = "C:\\Users\\phili\\Desktop\\tsv\\";
            outputPath = "C:\\Users\\phili\\Desktop\\";
            System.out.println("No Input & Output folder selected, using default Paths");
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
        String filing = "";

        while ((row = br.readLine()) != null) {
            currentCIK = row.split("\\|")[0];
            currentCompany = row.split("\\|")[1];
            filing = row.split("\\|")[2];

            if (!lol.containsKey(currentCompany) && (filing.equals("S-1") || filing.equals("S-1/A"))) {
                setSIC(row.split("\\|")[4]);
                if (currentSIC != null) {
                    EdgarEntry entry = new EdgarEntry(currentCompany, currentCIK, currentSIC);
                    lol.put(currentCompany, entry);
                    if (lol.size() % 20 == 0) {
                        System.out.println(lol.size() + "companies added");
                        System.out.println("last entry : " + entry.toString());
                    }
                    currentSIC = null;
                }
            }
        }
        br.close();
    }

    private static void setSIC(String linkToCompanyTxt) throws IOException {

        String url2 = baseUrl + linkToCompanyTxt;

        Document doc = Jsoup.connect(url2).get();
        String text = doc.text();
        if (text.split("STANDARD INDUSTRIAL CLASSIFICATION:").length >= 2) {
            currentSIC = text.split("STANDARD INDUSTRIAL CLASSIFICATION:")[1].split("\\[")[1].substring(0, 4);
            if (text.split("CENTRAL INDEX KEY:").length >= 2) {
                currentCIK = text.split("CENTRAL INDEX KEY:")[1].substring(1, 11);
            }
        }
    }
}