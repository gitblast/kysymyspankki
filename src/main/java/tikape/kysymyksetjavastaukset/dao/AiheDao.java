

package tikape.kysymyksetjavastaukset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.kysymyksetjavastaukset.Aihe;
import tikape.kysymyksetjavastaukset.Kysymys;
import tikape.kysymyksetjavastaukset.database.Database;

public class AiheDao implements Dao<Aihe, Integer> {

    private Database database;

    public AiheDao(Database database) {
        this.database = database;
    }
    
    @Override
    public Aihe findOne(Integer key) throws SQLException {       
        return null;
    }
    
    

    @Override
    public List<Aihe> findAll() throws SQLException {
        List<Aihe> aiheet = new ArrayList<>();

        Connection conn = database.getConnection();
        ResultSet rs = conn.prepareStatement("SELECT * FROM Aihe").executeQuery(); 

        while (rs.next()) {
            aiheet.add(new Aihe(rs.getInt("id"), rs.getInt("kurssi_id"), rs.getString("aihe")));
        }
        
        rs.close();
        conn.close();
        

        return aiheet;
    }

    @Override
    public Aihe saveOrUpdate(Aihe object) throws SQLException {
               
        Connection conn = database.getConnection();
        PreparedStatement tarkistus = conn.prepareStatement("SELECT * FROM Aihe");
        ResultSet testi = tarkistus.executeQuery();
        
        while (testi.next()) {
            if (testi.getString("aihe").equals(object.getAihe())) {
                return object;
            }
        }
        
        testi.close();
        tarkistus.close();
        
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Aihe (kurssi_id, aihe) VALUES (?, ?)");
        stmt.setInt(1, object.getKurssiId());
        stmt.setString(2, object.getAihe());
        stmt.executeUpdate();
        
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM Aihe WHERE aihe = ?");
        stmt2.setString(1, object.getAihe());
        ResultSet rs = stmt2.executeQuery();
        
        rs.next();
        Aihe a = new Aihe(rs.getInt("id"), rs.getInt("kurssi_id"), rs.getString("aihe"));

        rs.close();
        stmt2.close();
        stmt.close();
        conn.close();
        
        return a;

    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();        
        
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Aihe WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        
        conn.close();
        
    }


}