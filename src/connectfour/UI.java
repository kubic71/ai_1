package connectfour;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import minimax.*;

class View extends JPanel {
    private static final long serialVersionUID = 0;

    static final Color[] colors = { Color.WHITE, Color.YELLOW, Color.RED };

    ConnectFour game;

    public View(ConnectFour game) {
        this.game = game;

        setPreferredSize(new Dimension(800, 700));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(50, 50, 700, 600);

        int w = game.winner();
        if (w >= 1) {
            g.setColor(Color.GREEN);
            for (int i = 0 ; i < 4 ; ++i) {
                int x = game.win_x + i * game.win_dx;
                int y = game.win_y + i * game.win_dy;
                g.fillOval(50 + 100 * x, 50 + 100 * y, 100, 100);
            }
        }

        for (int x = 0; x < game.width(); ++x)
            for (int y = 0; y < game.height(); ++y) {
                g.setColor(colors[game.at(x, y)]);
                g.fillOval(50 + 100 * x + 10, 50 + 100 * y + 10, 80, 80);
            }
    }
}

public class UI extends JFrame implements KeyListener, MouseListener {
    private static final long serialVersionUID = 0;

    ConnectFour game;
    ArrayList<Strategy<ConnectFour, Integer>> players = new ArrayList<>();

    public UI(ConnectFour game) {
        super("Connect Four");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.game = game;
        add(new View(game));
        pack();
        setLocationRelativeTo(null);

        addKeyListener(this);
        addMouseListener(this);

        players.add(null);      // player 0 = dummy entry
    }

    public void addPlayer(Strategy<ConnectFour, Integer> strategy) {
        players.add(strategy);
    }

    public void addHuman() {
        players.add(null);
    }

    Strategy<ConnectFour, Integer> currentStrategy() {
        return players.get(game.turn());
    }

    void computerMove() {
        int move = currentStrategy().action(game);
        if (!game.move(move))
            throw new Error("strategy chose illegal move!");
    }

    void move(int x) {
        if (game.winner() >= 0)
                System.exit(0);

        if (currentStrategy() != null)  // computer's turn
            computerMove();
        else {    // human's turn
            if (!game.move(x))
                return;

            if (game.winner() >= 0)
                return;     // human won

            if (currentStrategy() != null)  // computer's turn
                computerMove();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == ' ')
            move(-1);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = (e.getX() - 50) / 100;
        move(x);
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }
}
