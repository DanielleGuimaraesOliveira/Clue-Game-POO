package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.ControladorPartida;

public class JanelaAcusacao extends JDialog {

	public JanelaAcusacao(JFrame janelaPai, ControladorPartida controlador) {
		super(janelaPai, "Fazer Acusação Final", true);

		// Um pouco mais alta para acomodar o aviso de 2 linhas
		setSize(520, 260); 
		setLocationRelativeTo(janelaPai);
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // Pode fechar no 'X' se desistir

		String[] suspeitos = {
			"Sr. Verde",
			"Srta. Scarlet",
			"Coronel Mustard",
			"Professor Plum",
			"Sra. Peacock",
			"Sra. White"
		};

		String[] armas = {
			"Corda",
			"Cano de Chumbo",
			"Faca",
			"Chave Inglesa",
			"Castiçal",
			"Revólver"
		};

		// Na acusação, todos os cômodos ficam disponíveis
		String[] comodos = {
			"Cozinha",
			"Salão de Música",
			"Jardim de Inverno",
			"Sala de Jantar",
			"Sala de Jogos",
			"Biblioteca",
			"Sala de Estar",
			"Entrada",
			"Escritório"
		};

		JComboBox<String> comboSuspeito = new JComboBox<>(suspeitos);
		JComboBox<String> comboArma = new JComboBox<>(armas);
		JComboBox<String> comboComodo = new JComboBox<>(comodos);

		// --- PAINEL DO TOPO ---
		JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER));
		painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		JLabel lblAviso = new JLabel("<html><center><b>CUIDADO!</b> Esta é a Acusação Final.<br>Se você errar, será <b>ELIMINADO</b> do jogo!</center></html>");
		lblAviso.setForeground(Color.RED); // Deixa o alerta vermelho
		painelTopo.add(lblAviso);
		
		// --- PAINEL CENTRAL ---
		JPanel painelForm = new JPanel(new GridLayout(3, 2, 10, 15));
		painelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
		
		painelForm.add(new JLabel("Assassino:", SwingConstants.RIGHT));
		painelForm.add(comboSuspeito);
		
		painelForm.add(new JLabel("Arma:", SwingConstants.RIGHT));
		painelForm.add(comboArma);
		
		painelForm.add(new JLabel("Local:", SwingConstants.RIGHT));
		painelForm.add(comboComodo);
		
		// --- PAINEL DE BAIXO ---
		JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		JButton btnAcusar = new JButton("Confirmar Acusação!");
		JButton btnCancelar = new JButton("Cancelar");
		
		painelBotoes.add(btnAcusar);
		painelBotoes.add(btnCancelar);

		// LÓGICA DOS BOTÕES 
		btnAcusar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//  confirmação extra antes de validar
				int confirmacao = JOptionPane.showConfirmDialog(
					janelaPai, 
					"Tem CERTEZA da acusação?", 
					"Confirmar Acusação Final", 
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE
				);

				if (confirmacao == JOptionPane.YES_OPTION) {
					// chama a verificação no model via controller
					boolean venceu = controlador.realizarAcusacao(
						    (String) comboSuspeito.getSelectedItem(),
						    (String) comboArma.getSelectedItem(),
						    (String) comboComodo.getSelectedItem()
						);

						dispose();

						if (venceu) {

						    String[] solucao =
						        controlador.revelarEnvelope();

						    JanelaVitoria janelaVitoria =
						        new JanelaVitoria(
						            janelaPai,
						            controlador.getJogadorAtual().getNome(),
						            solucao[0],
						            solucao[1],
						            solucao[2],
						            controlador
						        );

						    janelaVitoria.setVisible(true);

						} else {
					JOptionPane.showMessageDialog(
						janelaPai, 
						"Acusação errada e você foi ELIMINADO!", 
						"Eliminado", 
						JOptionPane.ERROR_MESSAGE
					);
					
					controlador.proximoTurno();
					janelaPai.repaint();
					
					// verifica fim de jogo
					if (controlador.todosEliminados()) {
						JOptionPane.showMessageDialog(
							janelaPai,
							"Ninguém acertou o assassino!", 
							"GAME OVER",
							JOptionPane.ERROR_MESSAGE
						);
						
						System.exit(0);
						}
					}
				}
			}
		});

		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Apenas fecha a janela se ele mudar de ideia, sem passar o turno
				dispose();
			}
		});

		// Adicionando tudo à janela principal
		add(painelTopo, BorderLayout.NORTH);
		add(painelForm, BorderLayout.CENTER);
		add(painelBotoes, BorderLayout.SOUTH);
	}
}