// FlappyBirdGame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlappyBirdGame {
    private JFrame frame;
    private GamePanel gamePanel;

    public FlappyBirdGame() {
        frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);

        gamePanel = new GamePanel();
        frame.add(gamePanel);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new FlappyBirdGame();
    }
}

// Bird.java
import java.awt.*;

public class Bird {
    private int x, y;
    private int width, height;
    private int velocity;

    public Bird(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocity = 0;
    }

    public void flap() {
        velocity = -10;
    }

    public void update() {
        velocity += 1;
        y += velocity;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

// Pipe.java
import java.awt.*;

public class Pipe {
    private int x, y;
    private int width, height;

    public Pipe(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

// GamePanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {
    private Bird bird;
    private List<Pipe> pipes;
    private Timer timer;

    public GamePanel() {
        bird = new Bird(100, 200, 30, 30);
        pipes = new ArrayList<>();
        timer = new Timer(16, this);
        timer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                bird.flap();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.YELLOW);
        g.fillRect(bird.getX(), bird.getY(), bird.getWidth(), bird.getHeight());

        for (Pipe pipe : pipes) {
            g.setColor(Color.GREEN);
            g.fillRect(pipe.getX(), pipe.getY(), pipe.getWidth(), pipe.getHeight());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        bird.update();

        // Add new pipes
        if (Math.random() < 0.1) {
            pipes.add(new Pipe(getWidth(), (int) (Math.random() * getHeight()), 30, 30));
        }

        // Update pipes
        for (Pipe pipe : pipes) {
            pipe.setX(pipe.getX() - 2);
        }

        // Check collisions
        for (Pipe pipe : pipes) {
            if (bird.getBounds().intersects(pipe.getBounds())) {
                // Game over
                System.out.println("Game Over!");
                timer.stop();
            }
        }

        repaint();
    }
}