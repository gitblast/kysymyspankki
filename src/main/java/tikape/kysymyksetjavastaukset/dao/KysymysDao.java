

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

    @Override
    public Kysymys findOne(Integer key) throws SQLException {       
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        Kysymys k = new Kysymys(rs.getInt("id"), rs.getString("kurssi"), rs.getString("aihe"), rs.getString("kysymys"));
        
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
            kysymykset.add(new Kysymys(rs.getInt("id"), rs.getString("kurssi"), rs.getString("aihe"), rs.getString("kysymys")));
        }
        
        rs.close();
        conn.close();
        

        return kysymykset;
    }

    @Override
    public Kysymys saveOrUpdate(Kysymys object) throws SQLException {
        Kysymys byName = findByQuestion(object.getKysymys());

        if (byName != null) {
            return byName;
        }

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys (kurssi, aihe, kysymys) VALUES (?, ?, ?)");
        stmt.setString(1, object.getKurssi());
        stmt.setString(2, object.getAihe());
        stmt.setString(3, object.getKysymys());
        stmt.executeUpdate();  
        
        stmt.close();
        conn.close();

        return findByQuestion(object.getKysymys());

    }
    
    private Kysymys findByQuestion(String question) throws SQLException {
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
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}