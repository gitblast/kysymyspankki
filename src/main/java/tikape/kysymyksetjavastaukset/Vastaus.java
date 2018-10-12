

package tikape.kysymyksetjavastaukset;


public class Vastaus {
    private int id;
    private String vastaus;
    private boolean oikein;
    private int kysymysId;

    public Vastaus(int id, String vastaus, boolean oikein, int kysymysId) {
        this.id = id;
        this.vastaus = vastaus;
        this.oikein = oikein;
        this.kysymysId = kysymysId;
    }

    public int getId() {
        return id;
    }

    public int getKysymysId() {
        return kysymysId;
    }

    public String getVastaus() {
        return vastaus;
    }

    public boolean isOikein() {
        return oikein;
    }
    
    
}