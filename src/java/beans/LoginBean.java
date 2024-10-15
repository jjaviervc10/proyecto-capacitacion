/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

/**
 *
 * @author Blueweb
 */
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import javax.faces.context.FacesContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.NamingException;
import java.sql.SQLException;
import data.PoolDB;
import sesiones.Sesion;
import javax.faces.view.ViewScoped;
import model.Login; 
import java.security.NoSuchAlgorithmException;
import beans.HexDigest;
import java.io.IOException;
import javax.faces.application.FacesMessage;

@ManagedBean(name = "loginBean")
@ViewScoped
public class LoginBean implements Serializable {

    private Login login = new Login(); // Instanciamos el modelo Login

    public String login() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Manejo de NoSuchAlgorithmException
            String encryptedPassword;
            try {
                encryptedPassword = HexDigest.hexDigest(login.getPass());
            } catch (NoSuchAlgorithmException e) {
                FacesContext.getCurrentInstance().addMessage(null, new javax.faces.application.FacesMessage("Error al encriptar la contraseña"));
                e.printStackTrace();
                return null;
            }

            connection = PoolDB.getConnection("MyDB");

            // Asegúrate de que el nombre de la tabla y las columnas sean correctos
            String sql = "SELECT * FROM sUsuario WHERE usuario = ? AND pass = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, login.getUsuario());
            ps.setString(2, encryptedPassword);

            rs = ps.executeQuery();

            if (rs.next()) {
                String nombreCompleto = rs.getString("nombreCompleto");
                int idUsuario = rs.getInt("idUsuario");

               
                Sesion.guardarEnSesion("nombreCompleto", nombreCompleto);
                Sesion.guardarEnSesion("idUsuario", idUsuario);

                FacesContext facesContext = FacesContext.getCurrentInstance();
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/inicio.xhtml?faces-redirect=true");
                } catch (IOException e) {
                    FacesContext.getCurrentInstance().addMessage(null, new javax.faces.application.FacesMessage("Error en la redirección"));
                    e.printStackTrace();
                    return null;
                }
                return null;

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new javax.faces.application.FacesMessage("Usuario o contraseña incorrectos"));
                return null;
            }

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new javax.faces.application.FacesMessage("Error de base de datos"));
            return null;

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sesión cerrada", "Has cerrado sesión exitosamente."));

        try {
            
            String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/login.xhtml");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void resetSessionTimeout() {
       
        FacesContext.getCurrentInstance().getExternalContext().getSession(false);
      
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sesión extendida."));
    }

    
    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getContextPath() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }
}
