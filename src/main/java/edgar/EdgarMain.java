package edgar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

public class EdgarMain {

	static String tsvPath = "C:\\Users\\phili\\Desktop\\2010-QTR1.tsv";
	static HashMap<String, String> lol = new HashMap<String, String>();
	static String baseUrl = "https://www.sec.gov/Archives/";
	
	public static void main(String[] args) {
		
		System.out.println(tsvPath);
		try {
			parseFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void parseFile() throws IOException {
		        BufferedReader br = new BufferedReader(new FileReader(tsvPath));
		        String row = "";
		        String currentCompany = "";
		        String currentSIC = "";
		        while ((row = br.readLine()) != null){
		        		currentCompany = row.split("\\|")[1];
		        		if (!lol.containsKey(currentCompany)) {
		        		currentSIC = getSIC(row.split("\\|")[4]);
		        			if (currentSIC != null) {
				        		lol.put(currentCompany, currentSIC);
				        		System.out.println(currentCompany + " = " +lol.get(currentCompany));
				        		currentSIC = null;
		        			}
		        		}
		        }
		        br.close();
	}

	private static String getSIC(String currentCompany) throws IOException {
			URL url = new URL(baseUrl+currentCompany);
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
