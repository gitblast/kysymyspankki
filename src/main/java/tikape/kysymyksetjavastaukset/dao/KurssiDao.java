

package tikape.kysymyksetjavastaukset.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.kysymyksetjavastaukset.Kurssi;
import tikape.kysymyksetjavastaukset.database.Database;

public class KurssiDao implements Dao<Kurssi, Integer> {

    private Database database;

    public KurssiDao(Database database) {
        this.database = database;
    }
    
    @Override
    public Kurssi findOne(Integer key) throws SQLException {       
        return null;
    }
    
    

    @Override
    public List<Kurssi> findAll() throws SQLException {
        List<Kurssi> kurssit = new ArrayList<>();

        Connection conn = database.getConnection();
        ResultSet rs = conn.prepareStatement("SELECT * FROM Kurssi").executeQuery(); 

        while (rs.next()) {
            kurssit.add(new Kurssi(rs.getInt("id"), rs.getString("kurssi")));
        }
        
        rs.close();
        conn.close();
        

        return kurssit;
    }
    
    public Kurssi findByQuestion(Integer questionId) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kurssi INNER JOIN Aihe ON Aihe.kurssi_id = Kurssi.id "
                + "INNER JOIN Kysymys ON Kysymys.aihe_id = Aihe.id AND Kysymys.id = ?");
        stmt.setInt(1, questionId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }
        
        return new Kurssi(rs.getInt("id"), rs.getString("kurssi"));
    }

    @Override
    public Kurssi saveOrUpdate(Kurssi object) throws SQLException {
               
        Connection conn = database.getConnection();
        PreparedStatement tarkistus = conn.prepareStatement("SELECT * FROM Kurssi");
        ResultSet testi = tarkistus.executeQuery();
        
        while (testi.next()) {
            if (testi.getString("kurssi").equals(object.getKurssi())) {
                return new Kurssi(testi.getInt("id"), testi.getString("kurssi"));
            }
        }
        
        testi.close();
        tarkistus.close();
        
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kurssi (kurssi) VALUES (?)");
        stmt.setString(1, object.getKurssi());
        stmt.executeUpdate();
        
        
        PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM Kurssi WHERE kurssi = ?");
        stmt2.setString(1, object.getKurssi());
        ResultSet rs = stmt2.executeQuery();
        
        rs.next();
        Kurssi a = new Kurssi(rs.getInt("id"), rs.getString("kurssi"));

        rs.close();
        stmt2.close();
        stmt.close();
        conn.close();
        
        return a;

    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();        
        
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kurssi WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        
        conn.close();
        
    }


}