import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    // Game constants
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int GROUND = 100;
    private static final int PIPE_WIDTH = 60;
    private static final int PIPE_GAP = 150;
    private static final int BIRD_SIZE = 30;
    private static final int GRAVITY = 1;
    private static final int JUMP_STRENGTH = -12;
    private static final int PIPE_SPEED = 4;

    // Game state variables
    private int birdY = HEIGHT / 2;
    private int birdVelocity = 0;
    private ArrayList<Rectangle> pipes;
    private int score = 0;
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private Timer timer;

    public FlappyBird() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(135, 206, 250));
        setFocusable(true);
        addKeyListener(this);

        pipes = new ArrayList<>();
        timer = new Timer(20, this);
        timer.start();

        addPipe(true);
        addPipe(true);
        addPipe(true);
        addPipe(true);
    }

    // Add a new pipe to the screen
    private void addPipe(boolean start) {
        int space = PIPE_GAP;
        int height = 50 + new Random().nextInt(HEIGHT - GROUND - space - 50);
        if (start) {
            pipes.add(new Rectangle(WIDTH + pipes.size() * 200, HEIGHT - height - GROUND, PIPE_WIDTH, height));
            pipes.add(new Rectangle(WIDTH + pipes.size() * 200, 0, PIPE_WIDTH, HEIGHT - height - space - GROUND));
        } else {
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + 200, HEIGHT - height - GROUND, PIPE_WIDTH, height));
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x, 0, PIPE_WIDTH, HEIGHT - height - space - GROUND));
        }
    }

    // Move pipes and bird
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted) {
            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                pipe.x -= PIPE_SPEED;
            }

            if (!gameOver) {
                birdVelocity += GRAVITY;
                birdY += birdVelocity;
            }

            // Remove off-screen pipes
            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                if (pipe.x + pipe.width < 0) {
                    pipes.remove(pipe);
                    if (pipe.y == 0) addPipe(false);
                }
            }

            // Collision detection
            for (Rectangle pipe : pipes) {
                if (pipe.intersects(new Rectangle(WIDTH / 2 - BIRD_SIZE / 2, birdY, BIRD_SIZE, BIRD_SIZE))) {
                    gameOver = true;
                    break;
                }
            }

            // Ground and ceiling collision
            if (birdY > HEIGHT - GROUND - BIRD_SIZE || birdY < 0) {
                gameOver = true;
            }

            // Score
            for (Rectangle pipe : pipes) {
                if (pipe.y > 0 && pipe.x + PIPE_WIDTH / 2 == WIDTH / 2 - BIRD_SIZE / 2) {
                    score++;
                }
            }
        }

        repaint();
    }

    // Draw everything
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw ground
        g.setColor(new Color(222, 184, 135));
        g.fillRect(0, HEIGHT - GROUND, WIDTH, GROUND);

        // Draw pipes
        g.setColor(new Color(0, 200, 0));
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        // Draw bird
        g.setColor(Color.YELLOW);
        g.fillOval(WIDTH / 2 - BIRD_SIZE / 2, birdY, BIRD_SIZE, BIRD_SIZE);

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        if (!gameStarted) {
            String msg = "Press SPACE to Start";
            g.drawString(msg, WIDTH / 2 - g.getFontMetrics().stringWidth(msg) / 2, HEIGHT / 2 - 50);
        }

        if (gameOver) {
            String msg = "Game Over!";
            g.drawString(msg, WIDTH / 2 - g.getFontMetrics().stringWidth(msg) / 2, HEIGHT / 2 - 50);
            String scoreMsg = "Score: " + score;
            g.drawString(scoreMsg, WIDTH / 2 - g.getFontMetrics().stringWidth(scoreMsg) / 2, HEIGHT / 2);
            String restartMsg = "Press R to Restart";
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString(restartMsg, WIDTH / 2 - g.getFontMetrics().stringWidth(restartMsg) / 2, HEIGHT / 2 + 40);
        } else if (gameStarted) {
            g.drawString(String.valueOf(score), WIDTH / 2 - 10, 100);
        }
    }

    // Key events
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            if (!gameStarted) {
                gameStarted = true;
            }
            if (!gameOver) {
                birdVelocity = JUMP_STRENGTH;
            }
        }

        if (key == KeyEvent.VK_R && gameOver) {
            // Reset game
            pipes.clear();
            birdY = HEIGHT / 2;
            birdVelocity = 0;
            score = 0;
            gameOver = false;
            gameStarted = false;
            addPipe(true);
            addPipe(true);
            addPipe(true);
            addPipe(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    // Main method to run the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
