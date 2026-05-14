package view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class PainelDeFundo extends JPanel {
    private Image imagem;

    public PainelDeFundo(Image imagem) {
        this.imagem = imagem;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
       
        Graphics2D g2d = (Graphics2D) g;
        
        if (imagem != null) {
            
            g2d.drawImage(imagem, 0, 0, getWidth(), getHeight(), this);
        }
    }
}