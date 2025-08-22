import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JPanel;

public class DataVisualizationGenerator extends JPanel {

    private String data;
    private SecretKey secretKey;

    public DataVisualizationGenerator(String data) throws NoSuchAlgorithmException {
        this.data = data;
        this.secretKey = generateSecretKey();
    }

    private SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }

    private String hashData(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());
        byte[] byteData = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte aByte : byteData) {
            String hex = Integer.toHexString(0xff & aByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        String hashedData = hashData(data);
        int[] colors = new int[hashedData.length()];
        for (int i = 0; i < hashedData.length(); i++) {
            colors[i] = Integer.parseInt(hashedData.substring(i, i + 6), 16);
        }

        int width = getWidth();
        int height = getHeight();
        int rectWidth = width / hashedData.length();
        for (int i = 0; i < hashedData.length(); i++) {
            g2d.setColor(new Color(colors[i]));
            g2d.fillRect(i * rectWidth, 0, rectWidth, height);
        }

        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(Color.BLACK);
        g2d.drawString(data, 10, 30);
    }
}