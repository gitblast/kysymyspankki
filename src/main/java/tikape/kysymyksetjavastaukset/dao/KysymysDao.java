

package tikape.kysymyksetjavastaukset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.kysymyksetjavastaukset.Kysymys;
import tikape.kysymyksetjavastaukset.database.Database;

public class KysymysDao implements Dao<Kysymys, Integer>{

    private Database database;

    public KysymysDao(Database database) {
        this.database = database;
    }
    
    public List<String> findAllKurssit() throws SQLException {
        List<String> kurssit = new ArrayList<>();
        
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT kurssi FROM Kysymys");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            kurssit.add(rs.getString("kurssi"));
        }
        
        rs.close();
        stmt.close();
        conn.close();
        
        return kurssit;
    }
    
    public List<String> findAllAiheet(String kurssi) throws SQLException {
        List<String> aiheet = new ArrayList<>();
        
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT aihe FROM Kysymys WHERE kurssi = ?");
        stmt.setString(1, kurssi);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            aiheet.add(rs.getString("aihe"));
        }
        
        rs.close();
        stmt.close();
        conn.close();
        
        return aiheet;
    }

    @Override
    public Kysymys findOne(Integer key) throws SQLException {       
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            stmt.close();
            rs.close();
            conn.close();
            return null;
        }
        
        Kysymys k = new Kysymys(rs.getInt("id"), rs.getInt("aihe_id"), rs.getString("kysymys"));
        
        rs.close();
        stmt.close();
        conn.close();
        
        return k;
    }

    @Override
    public List<Kysymys> findAll() throws SQLException {
        List<Kysymys> kysymykset = new ArrayList<>();

        Connection conn = database.getConnection();
        ResultSet rs = conn.prepareStatement("SELECT * FROM Kysymys").executeQuery(); 

        while (rs.next()) {
            kysymykset.add(new Kysymys(rs.getInt("id"), rs.getInt("aihe_id"), rs.getString("kysymys")));
        }
        
        rs.close();
        conn.close();
        

        return kysymykset;
    }

    @Override
    public Kysymys saveOrUpdate(Kysymys object) throws SQLException {
        
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys (aihe_id, kysymys) VALUES (?, ?)");
        stmt.setInt(1, object.getAiheId());
        stmt.setString(2, object.getKysymys());
        stmt.executeUpdate();
        
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM Kysymys WHERE kysymys = ?");
        stmt2.setString(1, object.getKysymys());
        ResultSet rs = stmt2.executeQuery();
        
        rs.next();
        Kysymys k = new Kysymys(rs.getInt("id"), rs.getInt("aihe_id"), rs.getString("kysymys"));

        rs.close();
        stmt2.close();
        stmt.close();
        conn.close();
        
        return k;

    }
  
    @Override
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
    
    public Integer deleteAndReturnAiheId(Integer key) throws SQLException {
        Connection conn = database.getConnection(); 
        
        PreparedStatement aihe = conn.prepareStatement("SELECT aihe_id FROM Kysymys WHERE id = ?");
        aihe.setInt(1, key);
        ResultSet rs = aihe.executeQuery();
        rs.next();
        int aiheId = rs.getInt("aihe_id");
        
        PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM Vastaus WHERE kysymys_id = ?");
        stmt2.setInt(1, key);
        stmt2.executeUpdate();
        
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        stmt2.close();
        rs.close();
        aihe.close();
        
        conn.close();
        
        return aiheId;
    }


}