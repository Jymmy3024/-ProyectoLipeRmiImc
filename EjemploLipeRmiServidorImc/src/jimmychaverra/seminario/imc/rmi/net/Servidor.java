package jimmychaverra.seminario.imc.rmi.net;

import jimmychaverra.seminario.imc.rmi.lib.IRemotaCalculoImc;
import net.sf.lipermi.exception.LipeRMIException;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Server;

import java.io.IOException;

public class Servidor {

    private int puerto = 9007;
    private CallHandler invocador;
    private Server servidor;
    private CalculoRmiImcImplem calculoImc;
    private IRemotaCalculoImc calculoImcRemoto;

    public Servidor() {
        invocador = new CallHandler();
        servidor = new Server();
        calculoImc = new CalculoRmiImcImplem();
    }

    public void iniciar() throws Exception {
        try{
            invocador.registerGlobal(IRemotaCalculoImc.class, calculoImc);
            servidor.bind(puerto, invocador);
            System.out.println("Servidor iniciado");
        }catch(LipeRMIException ex){
            throw new Exception("Error: No es posible invocar metodos remotos");
        }catch(IOException ex){
            throw new Exception("Error: I/O");
        }
    }

    public void detener(){
        servidor.close();
    }
}
