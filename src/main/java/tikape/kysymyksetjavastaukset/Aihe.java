package tikape.kysymyksetjavastaukset;


public class Aihe {
    private Integer id;
    private Integer kurssiId;
    private String aihe;

    public Aihe(Integer id, Integer kurssiId, String aihe) {
        this.id = id;
        this.kurssiId = kurssiId;
        this.aihe = aihe;
    }

    public String getAihe() {
        return aihe;
    }

    public Integer getKurssiId() {
        return kurssiId;
    }
    
    

    public Integer getId() {
        return id;
    }
    
    
}
