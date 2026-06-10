import javax.swing.SwingUtilities;

import model.GerenciadorDePartida;
import view.GerenciadorInterface;
import controller.ControladorPartida;


class Main {

	public static void main(String[] args) {
		GerenciadorDePartida gerenciadorPartida = GerenciadorDePartida.getInstance();
		
		 SwingUtilities.invokeLater(new Runnable() {

	            @Override
	            public void run() {

					ControladorPartida controlador = new ControladorPartida(gerenciadorPartida);
					GerenciadorInterface.iniciar(controlador);

	            }
	        });
		
	}

}

