package tikape.kysymyksetjavastaukset;


public class Aihe {
    private Integer id;
    private String aihe;

    public Aihe(Integer id, String aihe) {
        this.id = id;
        this.aihe = aihe;
    }

    public String getAihe() {
        return aihe;
    }

    public Integer getId() {
        return id;
    }
    
    
}
