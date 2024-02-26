import java.util.*;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;

public class SnakeGame extends JPanel implements ActionListener 
{

    private final int width;
    private final int height;
    private final int cellSize;
    private final Random random = new Random();
    private static final int FRAME_RATE = 20;
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private int highScore;
    private GamePoint food;
    private Direction direction = Direction.RIGHT;
    private Direction newDirection = Direction.RIGHT;
    private final LinkedList<GamePoint> snake = new LinkedList<>();

    public SnakeGame(final int width, final int height) 
    {
        this.width = width;
        this.height = height;
        this.cellSize = width / (FRAME_RATE * 2);
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
    }

    public void startGame() 
    {
        resetGameData();
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() 
        {
            @Override
            public void keyPressed(final KeyEvent e) 
            {
                handleKeyEvent(e.getKeyCode());
            }
        }
        );
        new Timer(1000 / FRAME_RATE, this).start();
    }

    private void handleKeyEvent(final int keyCode) 
    {
        if (!gameStarted) 
        {
            if (keyCode == KeyEvent.VK_SPACE) 
                gameStarted = true;
            
        } 
        else if (!gameOver) 
        {
            switch (keyCode) 
            {
                case KeyEvent.VK_UP:
                    if (direction != Direction.DOWN) 
                        newDirection = Direction.UP;
                    break;
                case KeyEvent.VK_W:
                if (direction != Direction.DOWN) 
                    newDirection = Direction.UP;
                break;
                case KeyEvent.VK_DOWN:
                    if (direction != Direction.UP) 
                        newDirection = Direction.DOWN;
                    break;
                case KeyEvent.VK_S:
                if (direction != Direction.UP) 
                    newDirection = Direction.DOWN;
                break;
                case KeyEvent.VK_RIGHT:
                    if (direction != Direction.LEFT) 
                        newDirection = Direction.RIGHT;
                    break;
                case KeyEvent.VK_D:
                if (direction != Direction.LEFT) 
                    newDirection = Direction.RIGHT;
                break;
                case KeyEvent.VK_LEFT:
                    if (direction != Direction.RIGHT) 
                        newDirection = Direction.LEFT;
                    break;
                case KeyEvent.VK_A:
                if (direction != Direction.RIGHT) 
                    newDirection = Direction.LEFT;
                break;
            }
        } 
        else if (keyCode == KeyEvent.VK_SPACE) 
        {
            gameStarted = false;
            gameOver = false;
            resetGameData();
        }
    }

    private void resetGameData() 
    {
        snake.clear();
        snake.add(new GamePoint(width / 2, height / 2));
        generateFood();
    }

    private void generateFood() 
    {
        do 
        {
            food = new GamePoint(random.nextInt(width / cellSize) * cellSize,
                    random.nextInt(height / cellSize) * cellSize);
        } 
        while (snake.contains(food));
    }

    @Override
    protected void paintComponent(final Graphics graphics) 
    {
        super.paintComponent(graphics);

        if (!gameStarted) 
        {
            printMessage1(graphics, "Press Space Bar to Begin Game");
            printMessage2(graphics, "Feed The Snake"  + "\n➡");
            printMessage3(graphics, "🐍");
            printMessage4(graphics, " 🍅");
        } 
        else 
        {
            graphics.setColor(Color.red);
            graphics.fillOval(food.x, food.y, cellSize, cellSize);

            Color snakeColor = Color.GREEN;
            for (final var point : snake) 
            {
                graphics.setColor(snakeColor);
                graphics.fillRect(point.x, point.y, cellSize, cellSize);
                final int newGreen = (int) Math.round(snakeColor.getGreen() * (0.95));
                snakeColor = new Color(100, newGreen, 50);
            }

            if (gameOver) 
            {
                final int currentScore = snake.size()-1;
                if (currentScore > highScore) 
                    highScore = currentScore;
                
                printMessage5(graphics, "Game Over!");   
                printMessage6(graphics, "🎊 Your Score: " + currentScore
                        + "\n⌛  Highest Score: " + highScore);
                printMessage1(graphics, "Press Space Bar to Play Again 🔃");
            }
        }
        if(gameStarted && !gameOver)
        {
            graphics.setColor(Color.green);
            graphics.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
            graphics.drawString("Your Score: " + String.valueOf(snake.size()-1), cellSize-16, cellSize);
        }
    }
    

    private void printMessage1(final Graphics graphics1, final String message) 
    {
        graphics1.setColor(Color.gray);
        graphics1.setFont(graphics1.getFont().deriveFont(30F));
        int currentHeight = height-200;
        final var graphics2D = (Graphics2D) graphics1;
        final var frc = graphics2D.getFontRenderContext();
        for (final var line : message.split("\n")) 
        {
            final var layout = new TextLayout(line, graphics1.getFont(), frc);
            final var bounds = layout.getBounds();
            final var targetWidth = (float) (width - bounds.getWidth()) / 2;
            layout.draw(graphics2D, targetWidth, currentHeight);
            currentHeight += graphics1.getFontMetrics().getHeight();
        }
    }
    private void printMessage2(final Graphics graphics2, final String message) 
    {
        graphics2.setColor(Color.white);
        graphics2.setFont(graphics2.getFont().deriveFont(60F));
        int currentHeight = height/3 ;
        final var graphics2D = (Graphics2D) graphics2;
        final var frc = graphics2D.getFontRenderContext();
        for (final var line : message.split("\n")) 
        {
            final var layout = new TextLayout(line, graphics2.getFont(), frc);
            final var bounds = layout.getBounds();
            final var targetWidth = (float) (width - bounds.getWidth()) / 2;
            layout.draw(graphics2D, targetWidth, currentHeight);
            currentHeight += graphics2.getFontMetrics().getHeight();
        }
    }
    private void printMessage3(final Graphics graphics3, final String message) 
    {
        graphics3.setColor(Color.green);
        graphics3.setFont(graphics3.getFont().deriveFont(60F));
        int currentHeight = height-315 ;
        final var graphics2D = (Graphics2D) graphics3;
        final var frc = graphics2D.getFontRenderContext();
        for (final var line : message.split("\n")) 
        {
            final var layout = new TextLayout(line, graphics3.getFont(), frc);
            final var bounds = layout.getBounds();
            final var targetWidth = (float) (width - bounds.getWidth()) -450;
            layout.draw(graphics2D, targetWidth, currentHeight);
            currentHeight += graphics3.getFontMetrics().getHeight();
        }
    }
    private void printMessage4(final Graphics graphics4, final String message) 
    {
        graphics4.setColor(Color.red);
        graphics4.setFont(graphics4.getFont().deriveFont(60F));
        int currentHeight = height-320 ;
        final var graphics2D = (Graphics2D) graphics4;
        final var frc = graphics2D.getFontRenderContext();
        for (final var line : message.split("\n")) 
        {
            final var layout = new TextLayout(line, graphics4.getFont(), frc);
            final var bounds = layout.getBounds();
            final var targetWidth = (float) (width - bounds.getWidth())-300;
            layout.draw(graphics2D, targetWidth, currentHeight);
            currentHeight += graphics4.getFontMetrics().getHeight();
        }
    }
    private void printMessage5(final Graphics graphics5, final String message) 
    {
        graphics5.setColor(Color.red);
        graphics5.setFont(graphics5.getFont().deriveFont(70F));
        int currentHeight = height/3;
        final var graphics2D = (Graphics2D) graphics5;
        final var frc = graphics2D.getFontRenderContext();
        for (final var line : message.split("\n")) 
        {
            final var layout = new TextLayout(line, graphics5.getFont(), frc);
            final var bounds = layout.getBounds();
            final var targetWidth = (float) (width - bounds.getWidth()) / 2;
            layout.draw(graphics2D, targetWidth, currentHeight);
            currentHeight += graphics5.getFontMetrics().getHeight();
        }
    }
    private void printMessage6(final Graphics graphics6, final String message) 
    {
        graphics6.setColor(Color.yellow);
        graphics6.setFont(graphics6.getFont().deriveFont(40F));
        int currentHeight = height-300;
        final var graphics2D = (Graphics2D) graphics6;
        final var frc = graphics2D.getFontRenderContext();
        for (final var line : message.split("\n")) 
        {
            final var layout = new TextLayout(line, graphics6.getFont(), frc);
            final var bounds = layout.getBounds();
            final var targetWidth = (float) (width - bounds.getWidth()) / 2;
            layout.draw(graphics2D, targetWidth, currentHeight);
            currentHeight += graphics6.getFontMetrics().getHeight();
        }
    }

    private void move() 
    {
        direction = newDirection;

        final GamePoint head = snake.getFirst();
        final GamePoint newHead = switch (direction) 
        {
            case UP -> new GamePoint(head.x, head.y - cellSize);
            case DOWN -> new GamePoint(head.x, head.y + cellSize);
            case LEFT -> new GamePoint(head.x - cellSize, head.y);
            case RIGHT -> new GamePoint(head.x + cellSize, head.y);
        };
        snake.addFirst(newHead);

        if (newHead.equals(food)) 
            generateFood();
        else if (isCollision()) 
        {
            gameOver = true;
            snake.removeFirst();
        } 
        else 
            snake.removeLast();
    }

    private boolean isCollision() 
    {
        final GamePoint head = snake.getFirst();
        final var invalidWidth = (head.x < 0) || (head.x >= width);
        final var invalidHeight = (head.y < 0) || (head.y >= height);
        if (invalidWidth || invalidHeight) 
            return true;

        return snake.size() != new HashSet<>(snake).size();
    }

    @Override
    public void actionPerformed(final ActionEvent e) 
    {
        if (gameStarted && !gameOver)
            move();
        repaint();
    }

    private record GamePoint(int x, int y) {}

    private enum Direction 
    {
        UP, DOWN, RIGHT, LEFT
    }
}