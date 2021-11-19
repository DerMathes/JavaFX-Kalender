package de.mathes.backend;

import de.mathes.klassen.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class SpeicherH2 implements Speicher {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    public static String DB_URL = "jdbc:h2:./kalender;AUTO_SERVER=TRUE";

    //  Database credentials
    static final String USER = "mathes";
    static final String PASS = "";
    Connection conn = null;

    public boolean istVerbunden() {
        return conn != null;
    }

    @Override
    public void init() {
        System.out.println(this.getClass());

        Statement stmt = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();

            //Anlegen der Benutzer Tabelle
            String sqlBenutzer = "CREATE TABLE IF NOT EXISTS Benutzer" +
                    "(benutzerId INTEGER auto_increment not NULL, " +
                    " benutzerName VARCHAR(255), " +
                    " benutzerFarbe INTEGER , " +
                    " benutzerAktiv BIT DEFAULT TRUE ," +
                    " PRIMARY KEY ( benutzerId ))";
            stmt.executeUpdate(sqlBenutzer);

            //Anlegen der Projekte Tabelle
            String sqlProjekte = "CREATE TABLE IF NOT EXISTS Projekte" +
                    "(projektId INTEGER auto_increment not NULL, " +
                    " projektName VARCHAR(255), " +
                    " projektFarbe INTEGER ," +
                    " PRIMARY KEY ( projektId ))";
            stmt.executeUpdate(sqlProjekte);

            //Anlegen der Benutzer_Projekte Tabelle
            String sqlBenutzer_Projekte = "CREATE TABLE IF NOT EXISTS Benutzer_Projekte" +
                    "(projektId INTEGER , " +
                    " benutzerId INTEGER , " +
                    " teilnahmeBeginn DATE , " +
                    " teilnahmeEnde DATE ," +
                    " FOREIGN KEY ( projektId) REFERENCES Projekte (projektId)," +
                    " FOREIGN KEY ( benutzerId) REFERENCES Benutzer (benutzerId))";
            stmt.executeUpdate(sqlBenutzer_Projekte);

            //Anlegen der Urlaub Tabelle
            String sqlUrlaub = "CREATE TABLE IF NOT EXISTS Urlaub" +
                    "(urlaubId INTEGER auto_increment not NULL, " +
                    " benutzerId INTEGER ," +
                    " urlaubBeginn DATE , " +
                    " urlaubEnde DATE ," +
                    " FOREIGN KEY (benutzerId) REFERENCES Benutzer (benutzerId)," +
                    " PRIMARY KEY (urlaubId))";
            stmt.executeUpdate(sqlUrlaub);

            System.out.println("Created table in given database...");

            //STEP 3.5 Select

//            ResultSet selectAll = stmt.executeQuery("SELECT * FROM Benutzer");
//            while (selectAll.next()) {
//                System.out.printf("%s, %s, %s, %s \n",
//                        selectAll.getString(1),
//                        selectAll.getString(2),
//                        selectAll.getString(3),
//                        selectAll.getString(4));
//            }

            // STEP 4: Clean-up environment
            stmt.close();

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ignore) {
            }
        } //end try
    }


    // Methode zum Ausführen eines SQL-Befehls
    public long executeSQL(String sql) {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(sql);
            ResultSet generatedKeys = statement.executeQuery("CALL SCOPE_IDENTITY()");
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);

            } else {
                // Throw exception?
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    //Insert in die Datenbank
    @Override
    public long erstelleBenutzer(String benutzerName, int benutzerFarbe) {
        return executeSQL(String.format("Insert INTO Benutzer(benutzerName,benutzerFarbe) VALUES ('%s',%d)", benutzerName, benutzerFarbe));
    }

    @Override
    public long erstelleProjekt(String projektName, int projektFarbe) {
        return executeSQL(String.format("Insert INTO Projekte(projektName,projektFarbe) VALUES ('%s',%d)", projektName, projektFarbe));
    }

    @Override
    public long erstelleBenutzerProjekt(int projektId, int benutzerId, LocalDate teilnahmeBeginn, LocalDate teilnahmeEnde) {
        return executeSQL(String.format("Insert INTO Benutzer_Projekte(projektId, benutzerId, teilnahmeBeginn, teilnahmeEnde) VALUES (%d,%d, '%tF', '%tF')", projektId, benutzerId, teilnahmeBeginn, teilnahmeEnde));
    }

    @Override
    public long erstelleUrlaub(int benutzerId, LocalDate urlaubBeginn, LocalDate urlaubEnde) {
        return executeSQL(String.format("Insert INTO Urlaub(benutzerId, urlaubBeginn, urlaubEnde) VALUES (%d, '%tF', '%tF')", benutzerId, urlaubBeginn, urlaubEnde));
    }


    //Delete in die Datenbank
    @Override
    public long loescheBenutzer(int benutzerId) {
        return executeSQL("DELETE FROM BENUTZER WHERE BENUTZERID =" + benutzerId);
    }

    @Override
    public long loescheProjekt(int projektId) {
        return executeSQL("DELETE FROM PROJEKTE WHERE PROJEKTID =" + projektId);
    }

    @Override
    public long loescheBenutzerProjekt(int benutzerId, int projektId) {
        return executeSQL("DELETE FROM BENUTZER_PROJEKTE WHERE projektId = " + projektId + "AND benutzerId = " + benutzerId);
    }

    @Override
    public long loescheUrlaub(int urlaubId) {
        return executeSQL("DELETE FROM URLAUB WHERE urlaubID = " + urlaubId);
    }


    //Update in die Datenbank

    @Override
    public long aendereBenutzer(int benutzerId, String benutzerName, int benutzerFarbe , boolean benutzerAktiv) {
        return executeSQL(String.format("UPDATE BENUTZER SET benutzerName = '%s', benutzerFarbe = %d, benutzerAktiv = %b WHERE BENUTZERID = %d", benutzerName, benutzerFarbe, benutzerAktiv, benutzerId));
    }

    @Override
    public long aendereProjekt(int projektId, String projektName, int projektFarbe) {
        return executeSQL(String.format("UPDATE PROJEKTE SET projektName = '%s', projektFarbe = %d WHERE PROJEKTID = %d", projektName, projektFarbe, projektId));
    }

    @Override
    public long aendereBenutzer_Projekt(int benutzerId, int projektId, LocalDate teilnahmeBeginn, LocalDate teilnahmeEnde) {
        return executeSQL(String.format("UPDATE BENUTZER_Projekte SET teilnahmeBeginn = '%tF', teilnahmeEnde = '%tF' WHERE projektID = %d AND benutzerID = %d", teilnahmeBeginn, teilnahmeEnde, projektId, benutzerId));
    }

    @Override
    public long aendereUrlaub(int urlaubId, LocalDate urlaubBeginn, LocalDate urlaubEnde) {
        return executeSQL(String.format("UPDATE URLAUB SET urlaubBeginn = '%tF', urlaubEnde = '%tF' WHERE urlaubID = %d", urlaubBeginn, urlaubEnde, urlaubId));
    }


    // Methode zum Ausführen eines Select-Befehls
    public <T> T executeSelect(String sql, Class<T> clz) {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            if (clz.equals(Benutzer.class) && rs.next()) {
                return (T) new Benutzer(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getBoolean(4));
            } else if (clz.equals(Projekte.class) && rs.next()) {
                return (T) new Projekte(rs.getInt(1), rs.getString(2), rs.getInt(3));
            } else if (clz.equals(Benutzer_Projekt.class) && rs.next()) {
                return (T) new Benutzer_Projekt(rs.getInt(1), rs.getInt(2), rs.getDate(3).toLocalDate(), rs.getDate(4).toLocalDate());
            } else if (clz.equals(Urlaub.class) && rs.next()) {
                return (T) new Urlaub(rs.getInt(1), rs.getInt(2), rs.getDate(3).toLocalDate(), rs.getDate(4).toLocalDate());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    //Select in die Datenbank
    @Override
    public Benutzer gebeBenutzer(int benutzerId) {
        return executeSelect("SELECT * FROM Benutzer WHERE benutzerId = " + benutzerId, Benutzer.class);
    }

    @Override
    public List<Benutzer> gebeAlleBenutzer(){
        ResultSet resultSet = executeSelect("Select * From Benutzer");
        List<Benutzer> benutzerList = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                Benutzer benutzer = new Benutzer(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getBoolean(4));
                benutzerList.add(benutzer);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return benutzerList;
    }

    @Override
    public List<Projekte> gebeAlleProjekte() {
        ResultSet resultSet = executeSelect("Select * From Projekte");
        List<Projekte> projektListe = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                Projekte projekte = new Projekte(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3));
                projektListe.add(projekte);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return projektListe;
    }

    @Override
    public int gebeZeilenAnzahl(LocalDate anfangsDatum, LocalDate endDatum) {
        ResultSet resultSet = executeSelect("select bp.TEILNAHMEBEGINN, bp.TEILNAHMEENDE, b.BENUTZERNAME, b.BENUTZERFARBE, p.PROJEKTNAME, p.PROJEKTFARBE\n" +
                "from BENUTZER_PROJEKTE bp\n" +
                "left join BENUTZER B on bp.BENUTZERID = B.BENUTZERID\n" +
                "left join PROJEKTE P on bp.PROJEKTID = P.PROJEKTID\n" +
                "where (TEILNAHMEBEGINN between '"+ anfangsDatum +"' AND '"+ endDatum +"'\n" +
                "  or TEILNAHMEENDE between '"+ anfangsDatum +"' AND '"+ endDatum +"'\n" +
                "  or (TEILNAHMEBEGINN <  '"+ anfangsDatum +"' AND TEILNAHMEENDE > '"+ endDatum +"'))\n" +
                "AND b.BENUTZERAKTIV = true;");
        int size = 0;
        if (resultSet != null)
        {
            try {
                resultSet.last();    // moves cursor to the last row
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                size = resultSet.getRow(); // get row id
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return size;
    }


    @Override
    public List<Benutzer_Projekt> gebeBenutzerProjekteInZeitraum(LocalDate anfangsDatum, LocalDate endDatum) {
        ResultSet resultSet = executeSelect("select bp.TEILNAHMEBEGINN, bp.TEILNAHMEENDE, b.BENUTZERID, p.PROJEKTID\n" +
                "from BENUTZER_PROJEKTE bp\n" +
                "left join BENUTZER B on bp.BENUTZERID = B.BENUTZERID\n" +
                "left join PROJEKTE P on bp.PROJEKTID = P.PROJEKTID\n" +
                "where (TEILNAHMEBEGINN between '"+ anfangsDatum +"' AND '"+ endDatum +"'\n" +
                "  or TEILNAHMEENDE between '"+ anfangsDatum +"' AND '"+ endDatum +"'\n" +
                "  or (TEILNAHMEBEGINN <  '"+ anfangsDatum +"' AND TEILNAHMEENDE > '"+ endDatum +"'))\n" +
                "AND b.BENUTZERAKTIV = true;");
        List<Benutzer_Projekt> benutzerProjektListe = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                Benutzer_Projekt benutzer_projekt = new Benutzer_Projekt(resultSet.getInt(4),resultSet.getInt(3),resultSet.getDate(1).toLocalDate(), resultSet.getDate(2).toLocalDate());
                benutzerProjektListe.add(benutzer_projekt);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return benutzerProjektListe;
    }


    @Override
    public Projekte gebeProjekt(int projektId) {
        return executeSelect("SELECT * FROM PROJEKTE WHERE projektId = " + projektId, Projekte.class);
    }

    @Override
    public Benutzer_Projekt gebeBenutzer_Projekt(int benutzerId, int projektId) {
        return executeSelect("SELECT * FROM BENUTZER_PROJEKTE WHERE projektId = " + projektId + "AND benutzerId = " + benutzerId, Benutzer_Projekt.class);
    }

    @Override
    public Urlaub gebeUrlaub(int urlaubId) {
        return executeSelect("SELECT * FROM URLAUB WHERE urlaubId = " + urlaubId, Urlaub.class);
    }


    @Override
    public void beenden() {
        try {
            conn.close();
            System.out.println("Connection closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ResultSet executeSelect(String sql) {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
