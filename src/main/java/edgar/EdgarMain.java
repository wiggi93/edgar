package edgar;

import java.io.*;
import java.net.URL;
import java.util.HashMap;

public class EdgarMain {

    static String folderPath = "C:\\Users\\phili\\Desktop\\tsv";
    static File folder = new File(folderPath);
    static File[] listOfFiles = folder.listFiles();
    static HashMap<String, String> lol = new HashMap<String, String>();
    static String baseUrl = "https://www.sec.gov/Archives/";

    public static void main(String[] args) {

        parseFolder();

    }

    public static void parseFolder() {
        for (File i : listOfFiles) {
            try {
                parseFile(i.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void parseFile(String tsvPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(tsvPath));
        String row = "";
        String currentCompany = "";
        String currentSIC = "";
        while ((row = br.readLine()) != null) {
            currentCompany = row.split("\\|")[1];
            if (!lol.containsKey(currentCompany)) {
                currentSIC = getSIC(row.split("\\|")[4]);
                if (currentSIC != null) {
                    lol.put(currentCompany, currentSIC);
                    System.out.println(currentCompany + " = " + lol.get(currentCompany));
                    currentSIC = null;
                }
            }
        }
        br.close();
    }

    private static String getSIC(String currentCompany) throws IOException {
        URL url = new URL(baseUrl + currentCompany);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = br.readLine()) != null)
            if (inputLine.split("STANDARD INDUSTRIAL CLASSIFICATION:").length >= 2) {
                return inputLine.split("STANDARD INDUSTRIAL CLASSIFICATION:")[1].split("\\[")[1].substring(0, 4);
            }
        br.close();
        return null;
    }

}
