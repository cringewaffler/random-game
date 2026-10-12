import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("WASD Movement");
            PlayerPanel game = new PlayerPanel();

            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setContentPane(game);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }

    private static class PlayerPanel extends JPanel {
        private static final int PLAYER_SIZE = 25;
        private static final int MOVE_AMOUNT = 30;

        private int playerX = 960;
        private int playerY = 540;
        private final MovementManager movementManager = new MovementManager();
        private final Timer gameLoop;

        PlayerPanel() {
            setPreferredSize(new Dimension(1920, 1080));
            setBackground(Color.WHITE);

            bindMovement("W", KeyEvent.VK_W);
            bindMovement("A", KeyEvent.VK_A);
            bindMovement("S", KeyEvent.VK_S);
            bindMovement("D", KeyEvent.VK_D);
                
            //  The Lambda version
            gameLoop = new Timer(16, e -> movementManager.updateMovement());

            gameLoop.start();
        }
        private void bindMovement(String key, int keyCode) {
            InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = getActionMap();

            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), key + "Press");
            actionMap.put(key + "Press", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    movementManager.setKeyPressed(keyCode, true);
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), key + "Release");
            actionMap.put(key + "Release", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    movementManager.setKeyPressed(keyCode, false);
                }
            });
        }
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            graphics.setColor(new Color(128, 130, 177));
            graphics.fillOval(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
        }
        private class MovementManager {
            private final Set<Integer> pressedKeys = new HashSet<>();

            public void setKeyPressed(int keyCode, boolean isPressed) {
                if (isPressed) {
                    pressedKeys.add(keyCode);
                } 
                else {
                    pressedKeys.remove(keyCode);
                }
                updateMovement();
            }

            private void updateMovement() {
                int dx = 0;
                int dy = 0;

                if (pressedKeys.contains(KeyEvent.VK_W)) dy -= MOVE_AMOUNT;
                if (pressedKeys.contains(KeyEvent.VK_S)) dy += MOVE_AMOUNT;
                if (pressedKeys.contains(KeyEvent.VK_A)) dx -= MOVE_AMOUNT;
                if (pressedKeys.contains(KeyEvent.VK_D)) dx += MOVE_AMOUNT;

                playerX = Math.max(0, Math.min(playerX + dx, getWidth() - PLAYER_SIZE));
                playerY = Math.max(0, Math.min(playerY + dy, getHeight() - PLAYER_SIZE));

                repaint();
            }
        }
    }
}
