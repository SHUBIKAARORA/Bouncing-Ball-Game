import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
public class BouncingBallGame extends JFrame {
    private JPanel playArea;
    private JPanel paddle;
    private JButton addButton;
	private JButton addButton1;
    private Timer timer;
    private Random rand;
    private int paddleX = 200;
    private int paddleWidth = 50;
    private int paddleHeight = 20;
    private int score = 0;
    private boolean gameOver = false;

    public BouncingBallGame() {
        setTitle("Bouncing Ball Game");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        playArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw balls
                for (Ball ball : balls) {
                    ball.draw(g);
                }
                // Draw paddle
                g.setColor(Color.BLUE);
                g.fillRect(paddleX, getHeight() - paddleHeight, paddleWidth, paddleHeight);
                // Draw score
                g.setColor(Color.BLACK);
                g.drawString("Score: " + score, 10, 20);
                // Draw game over message
                if (gameOver) {
                    g.drawString("Game Over!", getWidth() / 2 - 40, getHeight() / 2);
                }
            }
        };
     playArea.setBackground(Color.WHITE);
        playArea.setFocusable(true);
        playArea.requestFocus();
        playArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                int step = 20;                 
		if (key == KeyEvent.VK_LEFT) {
                    paddleX = Math.max(0, paddleX - step);
                } else if (key == KeyEvent.VK_RIGHT) {
                    paddleX = Math.min(playArea.getWidth() - paddleWidth, paddleX + step);
                }
                repaint();
            }
        });


        paddle = new JPanel();
	
       addButton1=new JButton("Restart");
	addButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
		playArea.setVisible(false);
		dispose();
                new BouncingBallGame().setVisible(true);
            }
        });
        addButton = new JButton("Add Ball");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBall();
            }
        });

        add(playArea, BorderLayout.CENTER);
        add(paddle, BorderLayout.SOUTH);
        paddle.add(addButton);
	paddle.add(addButton1);

        rand = new Random();
        timer = new Timer(5, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveBalls();
                checkCollision();
                if (gameOver) {
                    timer.stop();
                }
                playArea.repaint();
            }
        });
        timer.start();
    }

    private void addBall() {
        int x = rand.nextInt(playArea.getWidth() - Ball.SIZE);
        int y = 0;
        int dx = rand.nextInt(5) - 2;
        int dy = rand.nextInt(5) - 2;
        Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        Ball ball = new Ball(x, y, dx, dy, color);
        balls.add(ball);
        playArea.requestFocusInWindow(); // Request focus for the play area panel
    }

    private void moveBalls() {
        for (Ball ball : balls) {
            ball.move(playArea.getWidth(), playArea.getHeight());
        }
    }

    private void checkCollision() {
    for (Ball ball : balls) {
        if (ball.getY() + Ball.SIZE >= playArea.getHeight() - paddleHeight &&
            ball.getY() <= playArea.getHeight() &&
            ball.getX() + Ball.SIZE >= paddleX &&
            ball.getX() <= paddleX + paddleWidth) {
            score++;
        } else if (ball.getY() + Ball.SIZE > playArea.getHeight()) {
            gameOver = true;
        }
    }
}


    private ArrayList<Ball> balls = new ArrayList<>();

    private static class Ball {
        public static final int SIZE = 20;
        private int x, y, dx, dy;
        private Color color;

        public Ball(int x, int y, int dx, int dy, Color color) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.color = color;
        }

        public void draw(Graphics g) {
            g.setColor(color);
            g.fillOval(x, y, SIZE, SIZE);
        }

        public void move(int width, int height) {
            x += dx;
            y += dy;
            if (x < 0 || x + SIZE > width) {
                dx = -dx;
            }
            if (y < 0 || y + SIZE > height) {
                dy = -dy;
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BouncingBallGame().setVisible(true);
            }
        });
    }
}
