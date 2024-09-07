package jimmychaverra.seminario.imc.rmi;

import carlosolivera.seminario.imc.rmi.net.Servidor;

public class Principal {
    public static void main(String[] args) {
        Servidor servicio = new Servidor();
        try{
            servicio.iniciar();
        }catch(Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
