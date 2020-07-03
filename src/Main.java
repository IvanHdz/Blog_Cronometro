
/**
 *
 * @author Ivan
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Main extends JApplet {

    Thread hilo;
    PanelSegundos pc;

    @Override
    public void init() {
        pc = new PanelSegundos(this);
        hilo = new Thread(pc);
        add(pc);
        JPanel panelAbajo = new JPanel();
        panelAbajo.setBackground(Color.WHITE);
        JButton iniciar = new JButton("Iniciar");
        iniciar.addActionListener((ActionEvent arg0) -> {
            hilo.start();
            pc.setLive(true);
            pc.setGo(true);

            iniciar.setEnabled(false);
        });
        JButton reanudar = new JButton("Reanudar");
        reanudar.addActionListener((ActionEvent arg0) -> {
            pc.start();
        });
        JButton parar = new JButton("Detener");
        parar.addActionListener((ActionEvent arg0) -> {
            pc.stop();
        });

        panelAbajo.add(iniciar);
        panelAbajo.add(reanudar);
        panelAbajo.add(parar);
        add(panelAbajo, BorderLayout.SOUTH);
        repaint();
    }

    /**
     * ******************************************************
     */
    class PanelSegundos extends JPanel implements Runnable {

        ImageIcon numeros[];
        int indiceseg = 0;
        int indiceseg2 = 0;
        int indicemin = 0;
        int indicemin2 = 0;
        private boolean go, live;
        //boolean sw = true;
        Main c;

        public PanelSegundos(Main cron) {
            c = cron;
            numeros = new ImageIcon[10];
            for (int i = 0; i < 10; i++) {
                numeros[i] = new ImageIcon(getClass().getResource("Imagenes/imagennumero" + i + ".jpg"));
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponents(g);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 800, 800);
            g.drawImage(numeros[indicemin2].getImage(), 50, 100, this);
            g.drawImage(numeros[indicemin].getImage(), 100, 100, this);
            g.setColor(Color.RED);
            g.fillOval(140, 150, 5, 5);
            g.drawImage(numeros[indiceseg].getImage(), 200, 100, this);
            g.drawImage(numeros[indiceseg2].getImage(), 150, 100, this);
        }

        @Override
        public void run() {
            while (isLive()) {
                synchronized (this) {
                    while (!isGo()) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                    if (indiceseg < 9) {
                        indiceseg++;
                    } else {
                        if (indiceseg2 < 5) {
                            indiceseg2++;
                        } else {
                            indiceseg2 = 0;
                            if (indicemin < 9) {
                                indicemin++;
                            } else {
                                indicemin = 0;
                                if (indicemin < 5) {
                                    indicemin2++;
                                } else {
                                    indicemin = 0;
                                }
                            }
                        }
                        indiceseg = 0;
                    }
                    repaint();
                    c.repaint();
                } catch (InterruptedException e) {
                }
            }
        }

        public void stop() {
            //sw = false;
            setGo(false);
        }

        public synchronized void start() {
            setGo(true);
            notify();
        }

        //********** MÉTODOS SET Y GET DE LAS VARIABLES DE TIPO BOOLEAN e INT ************
        /**
         * @return the live
         */
        public boolean isLive() {
            return live;
        }

        /**
         * @param live the live to set
         */
        public void setLive(boolean live) {
            this.live = live;
        }

        /**
         * @return the go
         */
        public boolean isGo() {
            return go;
        }

        /**
         * @param go the go to set
         */
        public void setGo(boolean go) {
            this.go = go;
        }
    }
}
