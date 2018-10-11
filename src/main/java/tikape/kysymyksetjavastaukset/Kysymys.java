
package tikape.kysymyksetjavastaukset;

public class Kysymys {
    private Integer id;
    private String kurssi;
    private String aihe;
    private String kysymys;

    public Kysymys(Integer id, String kurssi, String aihe, String kysymys) {
        this.id = id;
        this.kurssi = kurssi;
        this.aihe = aihe;
        this.kysymys = kysymys;
    }

    public String getAihe() {
        return aihe;
    }


    public String getKurssi() {
        return kurssi;
    }

    public String getKysymys() {
        return kysymys;
    }

    
}