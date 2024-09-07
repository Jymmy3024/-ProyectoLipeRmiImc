package jimmychaverra.seminario.imc.rmi.vistas;

import jimmychaverra.seminario.imc.rmi.lib.DatosImc;
import jimmychaverra.seminario.imc.rmi.lib.IRemotaCalculoImc;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class VentanaPrincipal extends JFrame{
    private JPanel panelFondo;
    private JTabbedPane tabbedPane1;
    private JTextField campoIpServidor;
    private JLabel txtEstado;
    private JButton btnIniciar;
    private JTextField campoPuertoServidor;
    private JTextField txtMensaje;
    private JTextField campoPeso;
    private JTextField campoAltura;
    private JLabel txtResultado;
    private JButton btnCalcular;

    CallHandler invocadorRemoto;
    String ipServidor = "localhost";
    int puerto = 9007;
    IRemotaCalculoImc calculoImcRemoto;
    Client cliente;

    public VentanaPrincipal() {
        iniciarForma();
        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleIniciar();
            }


        });

        //formopened();
        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularImc();
            }
        });
    }

   /* private void formopened() {
        String ip;
        try{
            ip = InetAddress.getLocalHost().getHostAddress();
            campoIpServidor.setText(ip);
        }catch(UnknownHostException e){
            JOptionPane.showMessageDialog(panelFondo, "Falla en la conexion   ");
        }
    }*/

    private void iniciarForma(){
        setContentPane(panelFondo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900,700);

        //setLocationRelativeTo(null);
    }

    private void toggleIniciar() {
        try{
            if(btnIniciar.getText().equalsIgnoreCase("Conectar")){
                puerto = Integer.parseInt(campoPuertoServidor.getText());
                ipServidor = campoIpServidor.getText();
                invocadorRemoto = new CallHandler();
                cliente = new Client(ipServidor, puerto, invocadorRemoto);
                calculoImcRemoto = (IRemotaCalculoImc)
                        cliente.getGlobal(IRemotaCalculoImc.class);
                btnIniciar.setText("Desconectar");
                btnIniciar.setForeground(Color.RED);
                txtEstado.setText("Conectado");
                txtEstado.setForeground(Color.GREEN);
            }else if(btnIniciar.getText().equalsIgnoreCase("Desconectar")){
                cliente.close();
                btnIniciar.setText("Conectar");
                txtEstado.setText("Desconectado");
                btnIniciar.setForeground(Color.GREEN);
                txtEstado.setForeground(Color.RED);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(panelFondo, "ERROR AL CONECTAR");
            e.printStackTrace();
        }

    }

    private void calcularImc() {
        float peso = Float.parseFloat(campoPeso.getText());
        float altura = Float.parseFloat(campoAltura.getText());
        Thread hilo = new Thread(){
            @Override
            public void run() {
                try{
                    System.out.println("Peso: " + peso);
                    System.out.println("Altura: " + altura);
                    DatosImc datos = new DatosImc();
                    datos.setAltura(altura);
                    datos.setPeso(peso);
                    System.out.println("Enviados los datos\nEsperando respuesta");
                    datos = calculoImcRemoto.calcularImc(datos);
                    System.out.println("IMC: " + datos.getResultado()+"\nMensaje: "+datos.getInterpretacion());
                    txtResultado.setText(datos.getResultado()+"");
                    txtMensaje.setText(datos.getInterpretacion());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelFondo, "Error con el cliente "+ex.getMessage());
                    System.out.println("Error con el cliente"+ex.getMessage());
                    ex.printStackTrace();
                }
            }
        };
        hilo.start();
    }

    public JLabel getTxtEstado() {
        return txtEstado;
    }

    public JButton getBtnIniciar() {
        return btnIniciar;
    }
}
