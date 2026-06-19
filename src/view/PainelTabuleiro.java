package view;

import javax.swing.JPanel;

import Interfaces.ICasa;
import Interfaces.IJogador;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;

import controller.ControladorPartida;
import model.Observador;

class PainelTabuleiro extends JPanel implements Observador {

    private Image imagem;
    private JanelaJogo janelaPai;
    private ControladorPartida controlador;

    // AJUSTE FINO DO GRID
    private final int OFFSET_X = 14;
    private final int OFFSET_Y = 12;

    // tamanho REAL visual de cada quadrado
    private final int TAMANHO_CASA = 24;

    public PainelTabuleiro(
        Image imagem,
        ControladorPartida controlador,
        JanelaJogo janelaPai
    ) {

        this.imagem = imagem;
        this.controlador = controlador;
        this.janelaPai = janelaPai;

        addMouseListener(
            new ManipuladorCliqueTabuleiro(
                controlador,
                janelaPai,
                this
            )
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        desenharTabuleiro(g2d);
        desenharMovimentosValidos(g2d);
        desenharPecas(g2d);
    }

    private void desenharTabuleiro(Graphics2D g2d) {

        if (imagem != null) {

            g2d.drawImage(
                imagem,
                0,
                0,
                this
            );
        }
    }

    private void desenharMovimentosValidos(Graphics2D g2d) {

        if (janelaPai == null || controlador == null) {
            return;
        }

        int valorDados = janelaPai.getValorSimuladoDados();

        if (valorDados <= 0 || controlador.getJogadorAtual() == null) {
            return;
        }

        List<ICasa> casas =
            controlador.mapearCasas(valorDados);

        for (ICasa c : casas) {

            int px = OFFSET_X + (c.getX() * TAMANHO_CASA);

            int py = OFFSET_Y + (c.getY() * TAMANHO_CASA);

            boolean isComodo = !c.getTipo().equals("1")
            		&& !c.getTipo().equals("p") 
            		&& !c.getTipo().equals("0");
            
            if (isComodo) {
            	// COR PARA CÔMODO: Verde mais escuro
                g2d.setColor(new java.awt.Color(0, 100, 0, 150)); // Preenchimento escuro e transparente
                g2d.fillRect(px, py, TAMANHO_CASA, TAMANHO_CASA);

                g2d.setColor(new java.awt.Color(0, 80, 0)); // Borda mais escura
                g2d.drawRect(px, py, TAMANHO_CASA - 1, TAMANHO_CASA - 1);
            }
            else {
            	// COR PARA CORREDOR/PORTA: Verde claro original
            	g2d.setColor(new java.awt.Color(0, 255, 0, 100));
            	g2d.fillRect(px, py, TAMANHO_CASA, TAMANHO_CASA);

                g2d.setColor(new java.awt.Color(0, 200, 0));
                g2d.drawRect(px, py, TAMANHO_CASA - 1, TAMANHO_CASA - 1);
            }
            
        }
    }

    private void desenharPecas(Graphics2D g2d) {

        if (
            controlador == null ||
            controlador.getJogadores() == null
        ) {
            return;
        }

        int tamanhoPiao =
            (int) (TAMANHO_CASA * 0.7);

        int margemX =
            (TAMANHO_CASA - tamanhoPiao) / 2;

        int margemY =
            (TAMANHO_CASA - tamanhoPiao) / 2;

        for (IJogador j : controlador.getJogadores()) {

            ICasa posicao =
                j.getPosicaoAtual();

            if (posicao == null) {
                continue;
            }

            int xPixelBase =
                OFFSET_X +
                (posicao.getX() * TAMANHO_CASA);

            int yPixelBase =
                OFFSET_Y +
                (posicao.getY() * TAMANHO_CASA);

            int xFinal =
                xPixelBase + margemX;

            int yFinal =
                yPixelBase + margemY;

            g2d.setColor(
                CorJogador.getCor(
                    j.getPersonagem().getNome()
                )
            );

            g2d.fillOval(
                xFinal,
                yFinal,
                tamanhoPiao,
                tamanhoPiao
            );
        }
    }
    

    @Override
    public void atualizar() {

        repaint();
    }
}