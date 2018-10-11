
package tikape.kysymyksetjavastaukset;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) {
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        System.out.println("yo");
        
        Spark.get("/", (req, res) -> {
            List<Kysymys> lista = new ArrayList<>();
            
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Kysymys k = new Kysymys(rs.getInt("id"), rs.getString("kurssi"),
                        rs.getString("aihe"), rs.getString("kysymys"));
                lista.add(k);
            }
            
            stmt.close();
            conn.close();
            
            HashMap map = new HashMap<>();
            
            map.put("lista", lista);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());    
        
        Spark.post("/", (req, res) -> {
            Connection conn = getConnection();
            
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys (kurssi, aihe, kysymys) VALUES (?, ?, ?)");
            stmt.setString(1, req.queryParams("kurssi"));
            stmt.setString(2, req.queryParams("aihe"));
            stmt.setString(3, req.queryParams("kysymys"));
            
            //MUISTILISTA: tällä hetkellä voi lisätä tyhjän kentän. pitää muokata tätä allaolevaa
            if (!req.queryParams("kysymys").equals(null) && !req.queryParams("kysymys").equals("")) {
                stmt.executeUpdate();
            }
            
            stmt.close();
            conn.close();
            
            res.redirect("/");
            return "";
        });
        
    }
    
    
    
    public static Connection getConnection() throws Exception {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }

        return DriverManager.getConnection("jdbc:sqlite:questions.db");
    }

}