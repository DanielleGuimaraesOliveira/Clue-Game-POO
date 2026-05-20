import javax.swing.SwingUtilities;

import model.GerenciadorDePartida;
import view.GerenciadorInterface;


class Main {

	public static void main(String[] args) {
		GerenciadorDePartida gerenciadorPartida = GerenciadorDePartida.getInstance();
		
		 SwingUtilities.invokeLater(new Runnable() {

	            @Override
	            public void run() {

	                GerenciadorInterface.iniciar(gerenciadorPartida);

	            }
	        });
		
	}

}

