package org.fabnun.senu.vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Esta clase permite mostrar una pantalla de inicio mientras carga la aplicacion y los datos
 * @author fabnun
 */
public abstract class AbstractSplashScreenLoader {

    private float progress = 0;
    private String state = "";

    /**Este metodo se debe implementar para realizar la carga*/
    public abstract void load();

    /**Este metodo se debe implementar y es invocado despues de la carga
    con el fin de iniciar la aplicacion*/
    public abstract void start();

    /**Retorna el texto que describe el estado de la carga*/
    public String getState() {
        return state;
    }

    /**Establece el texto que describe el estado de la carga*/
    public void setState(String state) {
        this.state = state;
    }

    /**Retorna el progreso de la carga*/
    public float getProgress() {
        return progress;
    }

    /**Establece el progreso de la carga (0f-1f)*/
    public void setProgress(float progress) {
        this.progress = progress;
    }

    /**Este Metodo muestra la pantalla de inicio y realiza la carga*/
    public void SplashScreen(String imageFilename, String title, long minTime, final int x, final int y, final Color fontColor,
            final Color startColor, final Color endColor) throws IOException {
        new SplashScreen(imageFilename, title, minTime, x, y, fontColor, startColor, endColor, this);
    }

    private class SplashScreen extends JFrame implements Runnable {

        private Image image = null;
        private float progress = 0;
        private long minTime = 0;
        private int width = 0;
        private int height = 0;
        private JPanel panel = null;
        private AbstractSplashScreenLoader loader;

        private Color mixColor(Color c0, Color c1, float factor) {
            float r0 = (float) c0.getRed() / 255f;
            float g0 = (float) c0.getGreen() / 255f;
            float b0 = (float) c0.getBlue() / 255f;
            float r1 = (float) c1.getRed() / 255f;
            float g1 = (float) c1.getGreen() / 255f;
            float b1 = (float) c1.getBlue() / 255f;
            float r = r0 * (1f - factor) + r1 * (factor);
            float g = g0 * (1f - factor) + g1 * (factor);
            float b = b0 * (1f - factor) + b1 * (factor);
            return new Color(r, g, b);
        }

        private SplashScreen(String imageFilename, String title, long minTime, final int x, final int y, final Color fontColor,
                final Color startColor, final Color endColor, final AbstractSplashScreenLoader loader) throws IOException {
            image = ImageIO.read(new File(imageFilename));
            this.loader = loader;
            this.minTime = minTime;
            width = image.getWidth(null);
            height = image.getHeight(null);
            setTitle(title);
            panel = new JPanel() {

                @Override
                public void paint(Graphics g) {
                    g.drawImage(image, 0, 0, null);
                    int end = 10 + (int) ((float) (width - 20) * progress);
                    g.setColor(fontColor);
                    g.drawString(loader.getState(), x, y);
                    for (int x = 10; x < end; x++) {
                        g.setColor(mixColor(startColor, endColor, ((float) x - 10f) / ((float) width - 10f)));
                        g.drawLine(x, height - 10, x, height - 6);
                    }
                }
            };
            this.add(panel);
            setSize(width, height);
            setLocationRelativeTo(null);
            setAlwaysOnTop(true);
            setResizable(false);
            setUndecorated(true);
            Thread thread = new Thread(this);
            new Thread(new Runnable() {

                public void run() {
                    loader.load();
                    loader.setProgress(1);
                }
            }).start();

            thread.start();
        }

        public void run() {
            setVisible(true);
            long t0 = System.currentTimeMillis();
            long t = t0;
            while (progress < 1) {
                t = System.currentTimeMillis();
                panel.repaint();

                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                }
                progress = Math.min(calcProgress((t - t0)), loader.getProgress());
            }
            dispose();
            loader.start();
        }

        private float calcProgress(long l) {
            return Math.min(1f, ((float) l + ((float) minTime / 10f)) / (float) minTime);
        }
    }
}
