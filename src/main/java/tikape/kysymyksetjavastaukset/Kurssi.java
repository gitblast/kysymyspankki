package tikape.kysymyksetjavastaukset;


public class Kurssi {
    private Integer id;
    private String kurssi;

    public Kurssi(Integer id, String kurssi) {
        this.id = id;
        this.kurssi = kurssi;
    }

    public Integer getId() {
        return id;
    }

    public String getKurssi() {
        return kurssi;
    }
    
    
}
