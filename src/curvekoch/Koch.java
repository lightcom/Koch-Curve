package curvekoch;

/**
 *
 * @author lightcom
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author 
 */
public class Koch extends JComponent {

    private int N;
    Dimension dim;
    
    public Koch(int N) {
        this.N = N;
        this.dim = new Dimension(400, 400);
        setPreferredSize(this.dim);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                fillFigure(e.getX(), e.getY(), Color.BLACK);
                Color currentPixelColor = getColorOfPixel(e.getX(), e.getY());
                repaint();
            }
        });
    }
    
    //задается количество итераций
    public void setN(int N){
        this.N = N;
        graphics2D.clearRect(0, 0, getSize().width, getSize().height);
        repaint();
    }
    
    //заливка фигуры по координатам
    public void fillFigure(int x, int y, Color targetColor) {
        int x1 = x, x2 = x;
        //ищем по х границы до первого черного пикселя и до края
        while (this.dim.width >= x1 + 1 && getColorOfPixel(x1 + 1, y).getRGB() != targetColor.getRGB()) {
            x1++;
        }
        while (x2 - 1 >= 0 && getColorOfPixel(x2 - 1, y).getRGB() != targetColor.getRGB()) {
            x2--;
        }
        
        // рисуем линию по найденым х-ам
        graphics2D.drawLine(x2, y, x1, y);
        
        int i = x2;
        //выполняем текующую функцию изменяя координату y и находясь в рамках найденных x-ов
        while (i >= x2 && i <= x1) {
            Color c1 = getColorOfPixel(i, y + 1);
            if(c1!=null){
                if (this.dim.height >= y + 1 && c1.getRGB() != targetColor.getRGB()) {
                    fillFigure(i, y + 1, targetColor);
                }
            }
            Color c2 = getColorOfPixel(i, y - 1);
            if(c2!=null){
                if (y - 1 > 0 && c2.getRGB() != targetColor.getRGB()) {
                    fillFigure(i, y - 1, targetColor);
                }
            }
            i++;
        }

    }
    // получаем цвет пикселя рисунка по координатам
    private Color getColorOfPixel(int x, int y) {
        try {
            return new Color(image.getRGB(x, y));
        } catch (Exception e) {
            return null;
        }
    }
    
    BufferedImage image;
    //this is gonna be your image that you draw on
    Graphics2D graphics2D;
    
    // функция прорисовки
    @Override
    public void paint(Graphics g) {
        //создаем элемент image
        if (image == null) {
            Image i = createImage(getSize().width, getSize().height);
            image = (BufferedImage) i;
            graphics2D = (Graphics2D) image.getGraphics();
            //graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //clear();
        }
        g.drawImage(image, 0, 0, null);
        g.setColor(Color.BLACK);
        
        //четыре основные точки первоначального квадрата по Axiom = F+F+F+F
        Point a = new Point(70, 70);
        Point b = new Point(250, 70);
        Point c = new Point(250, 250);
        Point d = new Point(70, 250);
        // 90 degree = Pi/2
        double d90 = Math.PI / 2;
        // Axiom = F+F+F+F
        drawKochLine(graphics2D, a, b, 0, N); // F
        drawKochLine(graphics2D, b, c, d90, N); // +F   // d90 т.к. один раз повернули на 90 от первоначальной прямой
        drawKochLine(graphics2D, c, d, 2*d90, N); // +F   // 2*d90 т.к. два раз повернули на 90 от первоначальной прямой
        drawKochLine(graphics2D, d, a, 3*d90, N); // +F   // 3*d90 т.к. три раз повернули на 90 от первоначальной прямой
        
        repaint();
    }
    
    //разбивает линию по правилу или рисует линию, если итерации закончились
    public void drawKochLine(Graphics g, Point a, Point b, double angle, int n) {
        if (n > 0) { //еще остались итерации
            double lineLength = Math.sqrt(Math.pow(b.y - a.y, 2)
                    + Math.pow(b.x - a.x, 2));
            
            double d90 = Math.PI / 2; // 90 градусов
            double lineLengthOf4 = lineLength / 4; //длина отрезка F
            
            //7 точкек по правилу F => F+F-F-FF+F+F-F
            Point newA1 = new Point(a.x + (int) Math.round((lineLengthOf4 * Math.cos(angle))),
                    a.y + (int) Math.round((lineLengthOf4 * Math.sin(angle))));
            
            Point newA2 = new Point(newA1.x + (int) Math.round((lineLengthOf4 * Math.cos(angle-d90))),
                    newA1.y + (int) Math.round((lineLengthOf4 * Math.sin(angle-d90))));

            Point newA3 = new Point(newA2.x + (int) Math.round((lineLengthOf4 * Math.cos(angle))),
                    newA2.y + (int) Math.round((lineLengthOf4 * Math.sin(angle))));
            
            Point c = new Point(newA3.x + (int) Math.round((lineLengthOf4 * Math.cos(angle + d90))),
                    newA3.y + (int) Math.round((lineLengthOf4 * Math.sin(angle + d90))));
            
            Point newB3 = new Point(c.x + (int) Math.round((lineLengthOf4 * Math.cos(angle + d90))),
                    c.y + (int) Math.round((lineLengthOf4 * Math.sin(angle + d90))));

            Point newB2 = new Point(newB3.x + (int) Math.round((lineLengthOf4 * Math.cos(angle))),
                    newB3.y + (int) Math.round((lineLengthOf4 * Math.sin(angle))));
            
            Point newB1 = new Point(newB2.x + (int) Math.round((lineLengthOf4 * Math.cos(angle-d90))),
                    newB2.y + (int) Math.round((lineLengthOf4 * Math.sin(angle-d90))));
            
            n--;
            // правило F => F+F-F-FF+F+F-F
            drawKochLine(graphics2D, a, newA1, angle, n); // F
            drawKochLine(graphics2D, newA1, newA2, angle - d90, n); // +F
            drawKochLine(graphics2D, newA2, newA3, angle, n); // -F
            drawKochLine(graphics2D, newA3, c, angle + d90, n); // -F
            drawKochLine(graphics2D, c, newB3, angle + d90, n); // F
            drawKochLine(graphics2D, newB3, newB2, angle, n); // +F
            drawKochLine(graphics2D, newB2, newB1, angle - d90, n); // +F
            drawKochLine(graphics2D, newB1, b, angle, n); // -F
            
        } else {
            g.drawLine(a.x, a.y, b.x, b.y);
        }
    }
}