/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respuestas;

import java.util.List;
import objetos.Empresas;


public class RespuestaEmpresa extends Respuesta {
    
    private List<Empresas> listaEmpresa;

 
    public RespuestaEmpresa(int id, String mensaje, List<Empresas> listaEmpresa) {
        super(id, mensaje);  // Llama al constructor de la clase base
        this.listaEmpresa = listaEmpresa;
    }


    public RespuestaEmpresa(int id, String mensaje) {
        super(id, mensaje);
    }



    
    public List<Empresas> getListaEmpresa() {
        return listaEmpresa;
    }

    public void setListaEmpresa(List<Empresas> listaEmpresa) {
        this.listaEmpresa = listaEmpresa;
    }
}
