
package tikape.kysymyksetjavastaukset;

public class Kysymys {
    private Integer id;
    private Integer aiheId;
    private String kysymys;

    public Kysymys(Integer id, Integer aiheId, String kysymys) {
        this.id = id;
        this.aiheId = aiheId;
        this.kysymys = kysymys;
    }

    public Integer getId() {
        return id;
    }

    public Integer getAiheId() {
        return aiheId;
    }    
    
    public String getKysymys() {
        return kysymys;
    }

    
}