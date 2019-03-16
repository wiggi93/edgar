package models;

public class EdgarEntry {

    String companyName;
    String cik;
    String sic;

    public EdgarEntry(){
        this.companyName = "";
        this.cik = "";
        this.sic = "";
    }

    @Override
    public String toString() {
        return "EdgarEntry{" +
                "companyName='" + companyName + '\'' +
                ", cik='" + cik + '\'' +
                ", sic='" + sic + '\'' +
                '}';
    }

    public EdgarEntry(String companyName, String cik, String sic){
        this.companyName = companyName;
        this.cik = cik;
        this.sic = sic;
    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCik() {
        return cik;
    }

    public void setCik(String cik) {
        this.cik = cik;
    }

    public String getSic() {
        return sic;
    }

    public void setSic(String sic) {
        this.sic = sic;
    }
}
