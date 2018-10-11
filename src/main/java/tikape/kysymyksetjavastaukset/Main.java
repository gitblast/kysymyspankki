
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
            List<String> lista = new ArrayList<>();
            
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT text FROM test");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String text = rs.getString("text");
                lista.add(text);
            }
            
            stmt.close();
            conn.close();
            
            HashMap map = new HashMap<>();
            
            map.put("lista", lista);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());    
        
        Spark.post("/", (req, res) -> {
            Connection conn = getConnection();
            
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO test (text) VALUES (?)");
            stmt.setString(1, req.queryParams("kysymys"));
            stmt.executeUpdate();
            
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