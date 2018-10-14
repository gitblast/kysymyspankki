
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
import tikape.kysymyksetjavastaukset.dao.AiheDao;
import tikape.kysymyksetjavastaukset.dao.KurssiDao;
import tikape.kysymyksetjavastaukset.dao.KysymysDao;
import tikape.kysymyksetjavastaukset.dao.VastausDao;
import tikape.kysymyksetjavastaukset.database.Database;

public class Main {
    //todo: ehkä pitää poistaa kaikki vastaukset kun poistetaan kysymys...?

    public static void main(String[] args) throws ClassNotFoundException {
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database database = new Database();
        KysymysDao kDao = new KysymysDao(database);
        VastausDao vDao = new VastausDao(database);
        AiheDao aDao = new AiheDao(database);
        KurssiDao kurssiDao = new KurssiDao(database);
        
        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kurssit", kurssiDao.findAll());
            map.put("kysymykset", kDao.findAll());
            map.put("aiheet", aDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());    
        
        Spark.post("/", (req, res) -> {
            Kurssi k = kurssiDao.saveOrUpdate(new Kurssi(-1, req.queryParams("kurssi")));
            Aihe a = aDao.saveOrUpdate(new Aihe(-1, k.getId(), req.queryParams("aihe")));
            kDao.saveOrUpdate(new Kysymys(-1, a.getId(), req.queryParams("kysymys")));
            
            res.redirect("/");
            return "";
        });
        
        Spark.get("/kysymykset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer kysymysId = Integer.parseInt(req.params(":id"));
            
            map.put("kysymys", kDao.findOne(kysymysId));
            map.put("vastaukset", vDao.findAllByQuestion(kysymysId));
            
            return new ModelAndView(map, "kysymys");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/kysymykset/:id", (req, res) -> {
            kDao.delete(Integer.parseInt(req.params(":id")));
            
            res.redirect("/");
            return "";
        });
        
        Spark.post("/kysymykset/poista/:id", (req, res) -> {
            int kysymysId = vDao.deleteAndReturnQid(Integer.parseInt(req.params(":id")));
            res.redirect("/kysymykset/" + String.valueOf(kysymysId));
            return "";
        });
        
        Spark.post("/kysymykset/lisaa/:id", (req, res) -> {
            boolean oikein = false;
            if (req.queryParams("oikein") != null) {
                oikein = true;
            }
            
            vDao.saveOrUpdate(new Vastaus(-1, req.queryParams("vastaus"), oikein, Integer.parseInt(req.params(":id"))));
                       
            
            res.redirect("/kysymykset/" + req.params(":id"));
            return "";
        });
        
    }
    
    
    
    

}