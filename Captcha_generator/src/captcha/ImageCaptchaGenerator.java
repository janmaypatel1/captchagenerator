package captcha;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Random;

public class ImageCaptchaGenerator extends Canvas {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 50;
    private static final int NUM_CHARACTERS = 6;
    private static final char[] CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private String captchaText;

    public ImageCaptchaGenerator() {
        captchaText = generateCaptchaText();
    }

    private String generateCaptchaText() {
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < NUM_CHARACTERS; i++) {
            captcha.append(CHARACTERS[(int) (Math.random() * CHARACTERS.length)]);
        }
        return captcha.toString();
    }

    public String getCaptchaText() {
        return captchaText;
    }

    public void refreshCaptcha() {
        captchaText = generateCaptchaText();
    }

    private Color getRandomColor() {
        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        return new Color(r, g, b);
    }

    private BufferedImage createBlurredImage(String text, Font font, Color color) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setFont(font);
        g2.setColor(color);
        g2.drawString(text, 0, 30);
        g2.dispose();

        float[] matrix = {
            1/9f, 1/9f, 1/9f,
            1/9f, 1/9f, 1/9f,
            1/9f, 1/9f, 1/9f
        };
        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(img, null);
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Set font properties
        Font font = new Font("Arial", Font.BOLD, 30);
        g2d.setFont(font);

        // Draw each character in a different color with blur
        for (int i = 0; i < captchaText.length(); i++) {
            Color color = getRandomColor();
            BufferedImage charImage = createBlurredImage(String.valueOf(captchaText.charAt(i)), font, color);
            int charX = 20 + (i * 30); // Adjust position for each character
            int charY = 5; // Adjust y position to align text
            g2d.drawImage(charImage, charX, charY, null);
        }

        // Add a box around captcha
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        g2d.dispose();

        // Draw the image on the canvas
        g.drawImage(image, 0, 0, this);
    }
}

