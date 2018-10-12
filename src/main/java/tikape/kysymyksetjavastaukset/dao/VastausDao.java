

package tikape.kysymyksetjavastaukset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.kysymyksetjavastaukset.Vastaus;
import tikape.kysymyksetjavastaukset.database.Database;


public class VastausDao implements Dao<Vastaus, Integer> {
    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }
    
   

    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {
        List<Vastaus> vastaukset = new ArrayList();
        
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            vastaukset.add(new Vastaus(rs.getInt("id"), rs.getString("vastaus"), rs.getBoolean("oikein"), rs.getInt("kysymys_id")));
        }
        
        rs.close();
        stmt.close();
        conn.close();
        
        return vastaukset;
    }

    @Override
    public Vastaus saveOrUpdate(Vastaus object) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Vastaus (kysymys_id, vastaus, oikein) VALUES (?, ?, ?)");
        stmt.setInt(1, object.getKysymysId());
        stmt.setString(2, object.getVastaus());
        stmt.setBoolean(3, object.isOikein());
        stmt.executeUpdate();
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM Vastaus WHERE vastaus = ?");
        ResultSet rs = stmt2.executeQuery();
        rs.next();
        
        Vastaus v = new Vastaus(rs.getInt("id"), rs.getString("vastaus"), rs.getBoolean("oikein"), rs.getInt("kysymys_id"));
        
        rs.close();
        stmt2.close();
        stmt.close();
        conn.close();
        
        return v;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastaus WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

}