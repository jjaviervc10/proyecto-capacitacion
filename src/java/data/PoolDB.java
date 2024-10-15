/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Blueweb
 */
package data;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Carlos
 */

//Creates a new instance of PoolDB
public class PoolDB {

//Creates a new instance of PoolDB
    public static Connection getConnection(String nName) throws SQLException, NamingException {
        InitialContext cxt = new InitialContext();

        DataSource ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/" + nName);

        if (ds == null) {
            throw new SQLException("Origen de Datos " + nName + " no Encontrado!");

        }

        Connection conn = ds.getConnection();
        conn.setAutoCommit(true);
        System.out.println("Conexión a la base de datos establecida correctamente.");

        return conn;
    }

    public static void main(String[] args) {
        try {
            Connection conn = getConnection("MyDB");
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }
}
