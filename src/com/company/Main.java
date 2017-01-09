package com.company;

import com.sun.org.apache.xpath.internal.operations.Mod;
import javafx.collections.ObservableArray;
import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    //public static HashMap<String, DateLog> dailyLog = new HashMap<>();
    //public static ArrayList<Observations> observations = new ArrayList<>();


    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);


        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {

                    String dayLogged = request.queryParams("monthEntered, dayEntered");

                    if (dayLogged == null) {

                    }

                   HashMap thatOne = new HashMap();

                    ArrayList<Observations> logs = selectObservs(conn, get(DateLog.id));


                    Session session = request.session();
                    String dataEntered = session.attribute("dataEntry");

                    //DateLog logger = selectObservs(Connection, conn);


                    thatOne.put("observations", logs);
                    thatOne.put("dateEntered", dataEntered);

                    return new ModelandView(logs, "home.html");
                }),
                new MustacheTemplateEngine()
        );


        Spark.post(
                "/login",
                ((request, response) ->

                {

                    String dateEntered = request.queryParams("dateEntry");
                    String[] correctInput = dateEntered.split("/");
                    String monthEntered = correctInput[0];
                    String dayEntered = correctInput[1];

                    //int mEntry = Integer.parseInt(monthEntered);
                    //int dEntry = Integer.parseInt(dayEntered);


                    if (monthEntered == null || monthEntered.length() > 2 || dayEntered == null || dateEntered.length() > 2) {
                        throw new Exception("No valid date entered");
                    }

                    int mEntry = Integer.parseInt(monthEntered);
                    int dEntry = Integer.parseInt(dayEntered);

                    DateLog datelog = selectDay(conn, mEntry, dEntry);
                    if (datelog == null) {
                        insertDay(conn, mEntry, dEntry);

                    }
                    Session session = request.session();
                    session.attribute("active", dateEntered);
                    response.redirect("/");

                    return "";

                }));


        Spark.post(
                "/logout",
                ((request, response) -> {

                    Session session = request.session();
                    session.invalidate();

                    response.redirect("/");
                    return "";

                })
        );


        Spark.post(
                "/data-entry",
                ((request, response) -> {

                    String dataEntered = request.queryParams("dateEntry");

                    String[] correctInput = dataEntered.split("/");
                    String monthEntered = correctInput[0];
                    String dayEntered = correctInput[1];

                    int mEntry = Integer.parseInt(monthEntered);
                    int dEntry = Integer.parseInt(dayEntered);


                    //int id = observationsOfDay.getInt("Observations.id");
                    String bl = request.queryParams("bloodData");
                    int bloodEntry = Integer.parseInt(bl);
                    String energyL = request.queryParams("energyData");
                    String mealE = request.queryParams("mealData");
                    String noteS = request.queryParams("notesData");

                    //Observations observations = new Observations(id, bloodEntry, energyL, mealE, noteS)
                    //Observations observations = insertData(conn, id, bloodEntry, mealE, noteS);


                    if (bl == null || energyL == null || mealE == null || noteS == null) {
                        throw new Exception("All entries have not been made");
                    }



                    DateLog dateLog = selectDay(conn, mEntry, dEntry);

                    insertData(conn, bloodEntry, energyL, mealE, noteS);


                    //ArrayList<Observations> entOb = new ArrayList<>();

                    response.redirect(request.headers("Referer"));
                    return "";


                }));


        Spark.post(
                "/delete-entry",
                ((request, response) -> {
                    //Session session = request.session();
                    //String dataEntered = session.attribute("dateEntry");

                    String alteredData = request.queryParams("deleteEntry");

                    String[] correctInput = alteredData.split("/");
                    String monthEntered = correctInput[0];
                    String dayEntered = correctInput[1];

                    //int mEntry = Integer.parseInt(monthEntered);
                    //int dEntry = Integer.parseInt(dayEntered);


                    if (monthEntered == null || dayEntered == null) {
                        throw new Exception("no data given");
                    }

                    int mEntry = Integer.parseInt(monthEntered);
                    int dEntry = Integer.parseInt(dayEntered);


                    DateLog.remove(mEntry, dEntry);

                    return "";
                })
        );

        Spark.post(
                "/update-entry",
                ((request, response) -> {
                    String entryUpdate = request.queryParams("updateEntry");
                    String[] correctInput = dateEntered.split("/");
                    String monthEntered = correctInput[0];
                    String dayEntered = correctInput[1];

                    int mEntry = Integer.parseInt(monthEntered);
                    int dEntry = Integer.parseInt(dayEntered);

                    if(logg == null) {
                        throw new Exception("no data selected");
                    }

                    String dataE = request.queryParams("")

                })
        );





        /*Spark.post(
                "/meal-entry",
                ((request, response) -> {
                    Session session = request.session();
                    String dataEntered = session.attribute("dataEntered");
                    if(dataEntered == null) {
                        throw new Exception("No data given");
                    }






                })



        ); */

        /*Spark.post(
                "/note-entry",
                ((request, response) -> {
                    Session session = request.session();
                    String dataEntered = session.attribute("dataEntered");
                    if(dataEntered == null) {
                        throw new Exception("No data given");
                    }






                })


        );*/


    }

    public static void createTables(Connection conn) throws SQLException { //identity is
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS dayInput (id IDENTITY, month INT, day INT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS observationsOfDay (id IDENTITY, bloodSugarLevel INT, energyLevel VARCHAR, mealEaten VARCHAR, notes VARCHAR, dayInput_id INT)");
    }


    public static DateLog selectDay(Connection conn, int month, int day) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM days WHERE month = ? AND day = ?)");
        stmt.setInt(1, month);
        stmt.setInt(2, day);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            int id = results.getInt("dateLog.id");
            ArrayList<Observations> aryOb = selectObservs(conn, id);
            return new DateLog(id, month, day, aryOb);

        }


    }

    public static void insertDay(Connection conn, int month, int day) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO days VALUES (NULL, ?, ?)");
        stmt.setInt(1, month);
        stmt.setInt(2, day);
        stmt.execute();
    }

    /*public static Observations selectData(Connection conn, int dayInput_id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM observations WHERE dateInput_id = ?)");
        stmt.setInt(1, dayInput_id);
        ResultSet results = stmt.executeQuery();
        if(results.next()) {
            int idNote = results.getInt("observations.id");
            int sugle = results.getInt("observations.bloodSugarLevel");
            String energyLev = results.getString("observations.energy");
            String meaten = results.getString("observations.mealEaten");
            String noters = results.getString("observations.notes");
            return new Observations(idNote, bloodSugarLevel, energyLevel, mealEaten, notes);

        }


    } */

    public static void insertData(Connection conn, int bloodSugarLevel, String energyLevel, String mealEaten, String notes) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO observationsOfDay VALUES (NULL, ?, ?, ?, ?, ?)");
        //stmt.setInt(1,id);
        stmt.setInt(1, bloodSugarLevel);
        stmt.setString(2, energyLevel);
        stmt.setString(3, mealEaten);
        stmt.setString(4, notes);

        stmt.execute();
    }

    public static ArrayList<Observations> selectObservs(Connection conn, int id) throws SQLException {
        ArrayList<Observations> observat = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM observationsOfDay INNER JOIN dateLogs ON observations.dateLog_id = dateLogs.id WHERE observations.dateLog_id= ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int obId = results.getInt("observations.id");
            int suL = results.getInt("observations.bloodSugarLevel");
            String ene = results.getString("observations.energyLevel");
            String mel = results.getString("observations.mealEaten");
            String not = results.getString("observations.notes");

            Observations observs = new Observations(obId, suL, ene, mel, not);
            observat.add(observs);

            //return observat; //(conn, id, bloodSugarLevel, energyLevel, mealEaten, notes);


        }

        return observat;


    }


}



