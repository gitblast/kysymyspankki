

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

public class AiheDao {

    private Database database;

    public AiheDao(Database database) {
        this.database = database;
    }
    
    
    public Kysymys findOne(Integer key) throws SQLException {       
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }
        
        Kysymys k = new Kysymys(rs.getInt("id"), rs.getString("kurssi"), rs.getString("aihe"), rs.getString("kysymys"));
        
        rs.close();
        stmt.close();
        conn.close();
        
        return k;
    }
    
    

    
    public List<Aihe> findAll() throws SQLException {
        List<Aihe> aiheet = new ArrayList<>();

        Connection conn = database.getConnection();
        ResultSet rs = conn.prepareStatement("SELECT * FROM AiheTest").executeQuery(); 

        while (rs.next()) {
            aiheet.add(new Aihe(rs.getInt("id"), rs.getString("aihe")));
        }
        
        rs.close();
        conn.close();
        

        return aiheet;
    }

    public Aihe saveOrUpdate(Aihe object) throws SQLException {
        
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO AiheTest (aihe) VALUES (?)");
        stmt.setString(1, object.getAihe());
        stmt.executeUpdate();
        
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM AiheTest WHERE aihe = ?");
        stmt2.setString(1, object.getAihe());
        ResultSet rs = stmt2.executeQuery();
        
        rs.next();
        Aihe a = new Aihe(rs.getInt("id"), rs.getString("aihe"));

        rs.close();
        stmt2.close();
        stmt.close();
        conn.close();
        
        return a;

    }
    
    /*private Kysymys findByQuestion(String question) throws SQLException {
    Connection conn = database.getConnection();
    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE kysymys = ?");
    stmt.setString(1, question);
    
    ResultSet rs = stmt.executeQuery();
    if (!rs.next()) {
    return null;
    }
    
    rs.close();
    stmt.close();
    conn.close();
    
    return new Kysymys(rs.getInt("id"), rs.getString("kurssi"), rs.getString("aihe"), rs.getString("kysymys"));
    }*/
    
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();        
        
        PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM Vastaus WHERE kysymys_id = ?");
        stmt2.setInt(1, key);
        stmt2.executeUpdate();
        
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        stmt2.close();
        
        conn.close();
        
    }


}