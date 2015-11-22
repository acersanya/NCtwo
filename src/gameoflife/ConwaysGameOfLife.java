package gameoflife;

import java.awt.*;
import model.*;
import model.Record.ImportanceLevel;

import java.awt.event.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;

import javax.swing.*;
import java.util.List;
/**
 * Conway's game of life is a cellular automaton devised by the
 * mathematician John Conway.
 * Clicking on Help -> Journal Events
 * Will display you all information about cells
 * Journal will show you, what cells were born 
 * and what cells are already dead 
 */
public class ConwaysGameOfLife extends JFrame implements ActionListener {
    public static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 600);
    public static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(400, 400);
    private static final int BLOCK_SIZE = 10;
 
    private JMenuBar mb_menu;
    private JMenu m_file, m_game, m_help;
    private JMenuItem mi_file_options, mi_file_exit;
    private JMenuItem mi_game_autofill, mi_game_play, mi_game_stop, mi_game_reset;
    private JMenuItem mi_help_about, mi_help_source;
    private int i_movesPerSecond = 3;
    private GameBoard gb_gameBoard;
    private Thread game;
    private static ArrayList<CellRecord> allCells = new ArrayList<>();
 ;
 
   
    

 
    public ConwaysGameOfLife() {
        // Setup menu
        mb_menu = new JMenuBar();
        setJMenuBar(mb_menu);
        m_file = new JMenu("File");
        mb_menu.add(m_file);
        m_game = new JMenu("Game");
        mb_menu.add(m_game);
        m_help = new JMenu("Help");
        mb_menu.add(m_help);
        mi_file_options = new JMenuItem("Options");
        mi_file_options.addActionListener(this);
        mi_file_exit = new JMenuItem("Exit");
        mi_file_exit.addActionListener(this);
        m_file.add(mi_file_options);
        m_file.add(new JSeparator());
        m_file.add(mi_file_exit);
        mi_game_autofill = new JMenuItem("Autofill");
        mi_game_autofill.addActionListener(this);
        mi_game_play = new JMenuItem("Play");
        mi_game_play.addActionListener(this);
        mi_game_stop = new JMenuItem("Stop");
        mi_game_stop.setEnabled(false);
        mi_game_stop.addActionListener(this);
        mi_game_reset = new JMenuItem("Reset");
        mi_game_reset.addActionListener(this);
        m_game.add(mi_game_autofill);
        m_game.add(new JSeparator());
        m_game.add(mi_game_play);
        m_game.add(mi_game_stop);
        m_game.add(mi_game_reset);
        mi_help_about = new JMenuItem("Journal Events");
        mi_help_about.addActionListener(this);
      
        m_help.add(mi_help_about);
        // Setup game board
        gb_gameBoard = new GameBoard();
        add(gb_gameBoard);
    }
 
    public void setGameBeingPlayed(boolean isBeingPlayed) {
        if (isBeingPlayed) {
            mi_game_play.setEnabled(false);
            mi_game_stop.setEnabled(true);
            game = new Thread(gb_gameBoard);
            game.start();
        } else {
            mi_game_play.setEnabled(true);
            mi_game_stop.setEnabled(false);
            game.interrupt();
        }
    }
 
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(mi_file_exit)) {

            System.exit(0);
        } else if (ae.getSource().equals(mi_file_options)) {
            // Put up an options panel to change the number of moves per second
            final JFrame f_options = new JFrame();
            f_options.setTitle("Options");
            f_options.setSize(300,60);
            f_options.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_options.getWidth())/2, 
                (Toolkit.getDefaultToolkit().getScreenSize().height - f_options.getHeight())/2);
            f_options.setResizable(false);
            JPanel p_options = new JPanel();
            p_options.setOpaque(false);
            f_options.add(p_options);
            p_options.add(new JLabel("Number of moves per second:"));
            Integer[] secondOptions = {1,2,3,4,5,10,15,20};
            final JComboBox cb_seconds = new JComboBox(secondOptions);
            p_options.add(cb_seconds);
            cb_seconds.setSelectedItem(i_movesPerSecond);
            cb_seconds.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    i_movesPerSecond = (Integer)cb_seconds.getSelectedItem();
                    f_options.dispose();
                }
            });
            f_options.setVisible(true);
        } else if (ae.getSource().equals(mi_game_autofill)) {
            final JFrame f_autoFill = new JFrame();
            f_autoFill.setTitle("Autofill");
            f_autoFill.setSize(360, 60);
            f_autoFill.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_autoFill.getWidth())/2, 
                (Toolkit.getDefaultToolkit().getScreenSize().height - f_autoFill.getHeight())/2);
            f_autoFill.setResizable(false);
            JPanel p_autoFill = new JPanel();
            p_autoFill.setOpaque(false);
            f_autoFill.add(p_autoFill);
            p_autoFill.add(new JLabel("What percentage should be filled? "));
            Object[] percentageOptions = {"Select",5,10,15,20,25,30,40,50,60,70,80,90,95};
            final JComboBox cb_percent = new JComboBox(percentageOptions);
            p_autoFill.add(cb_percent);
            cb_percent.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (cb_percent.getSelectedIndex() > 0) {
                        gb_gameBoard.resetBoard();
                        gb_gameBoard.randomlyFillBoard((Integer)cb_percent.getSelectedItem());
                        f_autoFill.dispose();
                    }
                }
            });
            f_autoFill.setVisible(true);
        } else if (ae.getSource().equals(mi_game_reset)) {
            gb_gameBoard.resetBoard();
            gb_gameBoard.repaint();
        } else if (ae.getSource().equals(mi_game_play)) {
            setGameBeingPlayed(true);
        } else if (ae.getSource().equals(mi_game_stop)) {
            setGameBeingPlayed(false);
        }else if (ae.getSource().equals(mi_help_about)) {
        	JOptionPane.showMessageDialog(null, allCells.toString().replaceAll("^\\[", ""));
            JOptionPane.showMessageDialog(null, "End");
        }
    }
 
    private class GameBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, Runnable {
    
        private Dimension d_gameBoardSize = null;
        private ArrayList<Point> point = new ArrayList<Point>(0);
      
        
        public GameBoard() {
           
            // Add resizing listener
            addComponentListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
        }
 
        private void updateArraySize() {
            ArrayList<Point> removeList = new ArrayList<Point>(0);
            for (Point current : point) {
                if ((current.x > d_gameBoardSize.width-1) || (current.y > d_gameBoardSize.height-1)) {
                    removeList.add(current);
                }
            }
            point.removeAll(removeList);
            repaint();
        }
 
        public void addPoint(int x, int y) {
            if (!point.contains(new Point(x,y))) {
                point.add(new Point(x,y));
            } 
            repaint();
        }
 
        public void addPoint(MouseEvent me) {
            int x = me.getPoint().x/BLOCK_SIZE-1;
            int y = me.getPoint().y/BLOCK_SIZE-1;
            if ((x >= 0) && (x < d_gameBoardSize.width) && (y >= 0) && (y < d_gameBoardSize.height)) {
                addPoint(x,y);
            }
        }
 

        public void resetBoard() {
            point.clear();
        }
 
        /**
         * Random fill with cells
         * @param percent of board filling
         */
        public void randomlyFillBoard(int percent) {
            for (int i=0; i<d_gameBoardSize.width; i++) {
                for (int j=0; j<d_gameBoardSize.height; j++) {
                    if (Math.random()*100 < percent) {
                        addPoint(i,j);
                    }
                }
            }
        }
 
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                for (Point newPoint : point) {
                    // Draw new point
                    g.setColor(Color.blue);
                    g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*newPoint.x), BLOCK_SIZE + (BLOCK_SIZE*newPoint.y), BLOCK_SIZE, BLOCK_SIZE);
                }
            } catch (ConcurrentModificationException cme) {}
            // Setup grid
            g.setColor(Color.BLACK);
            for (int i=0; i<=d_gameBoardSize.width; i++) {
                g.drawLine(((i*BLOCK_SIZE)+BLOCK_SIZE), BLOCK_SIZE, (i*BLOCK_SIZE)+BLOCK_SIZE, BLOCK_SIZE + (BLOCK_SIZE*d_gameBoardSize.height));
            }
            for (int i=0; i<=d_gameBoardSize.height; i++) {
                g.drawLine(BLOCK_SIZE, ((i*BLOCK_SIZE)+BLOCK_SIZE), BLOCK_SIZE*(d_gameBoardSize.width+1), ((i*BLOCK_SIZE)+BLOCK_SIZE));
            }
        }
 
        @Override
        public void componentResized(ComponentEvent e) {
            // Setup the game board size with proper boundries
            d_gameBoardSize = new Dimension(getWidth()/BLOCK_SIZE-2, getHeight()/BLOCK_SIZE-2);
            updateArraySize();
        }
        @Override
        public void componentMoved(ComponentEvent e) {}
        @Override
        public void componentShown(ComponentEvent e) {}
        @Override
        public void componentHidden(ComponentEvent e) {}
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {
            // Mouse was released (user clicked)
            addPoint(e);
        }
        @Override
        public void mouseEntered(MouseEvent e) {}
 
        @Override
        public void mouseExited(MouseEvent e) {}
 
        @Override
        public void mouseDragged(MouseEvent e) {
            // Mouse is being dragged, user wants multiple selections
            addPoint(e);
        }
        @Override
        public void mouseMoved(MouseEvent e) {}
 
        /**
         * Here Journal is used!!!
         */
        @Override
        public void run() {
            boolean[][] gameBoard = new boolean[d_gameBoardSize.width+2][d_gameBoardSize.height+2];
            for (Point current : point) {
                gameBoard[current.x+1][current.y+1] = true;
            }
            List<CellRecord>cells = new ArrayList<>();
            ArrayList<Point> survivingCells = new ArrayList<Point>(0);
            // Iterate through the array, follow game of life rules
            for (int i=1; i<gameBoard.length-1; i++) {
                for (int j=1; j<gameBoard[0].length-1; j++) {
                    int surrounding = 0;
                    if (gameBoard[i-1][j-1]) { surrounding++; }
                    if (gameBoard[i-1][j])   { surrounding++; }
                    if (gameBoard[i-1][j+1]) { surrounding++; }
                    if (gameBoard[i][j-1])   { surrounding++; }
                    if (gameBoard[i][j+1])   { surrounding++; }
                    if (gameBoard[i+1][j-1]) { surrounding++; }
                    if (gameBoard[i+1][j])   { surrounding++; }
                    if (gameBoard[i+1][j+1]) { surrounding++; }
                    if (gameBoard[i][j]) {
                        // Cell is alive, Can the cell live? (2-3)
                        if ((surrounding == 2) || (surrounding == 3)) {
                            survivingCells.add(new Point(i-1,j-1));
                            cells.add(new CellRecord(new Date(), "Conway game", ImportanceLevel.LEVEL_ONE, "Born cell", i - 1, j - 1));
                        } 
                        else if(surrounding > 3){
                        	cells.add(new CellRecord(new Date(), "Conway game", ImportanceLevel.LEVEL_FOUR, "Dead cell", i, j));
                        }
                    } else {
                        // Cell is dead, will the cell be given birth? (3)
                        if (surrounding == 3) {
                        	cells.add(new CellRecord(new Date(), "Conway game", ImportanceLevel.LEVEL_ONE, "Dead Cell", i , j ));
                            survivingCells.add(new Point(i-1,j-1));
                            cells.add(new CellRecord(new Date(), "Conway game", ImportanceLevel.LEVEL_ONE, "Dead Cell", i -1 , j - 1));
                        }         
                    }
                }
            }
            resetBoard();
            point.addAll(survivingCells);
            allCells.addAll(cells);
            
            repaint();
            try {
                Thread.sleep(1000/i_movesPerSecond);
                run();
            } catch (InterruptedException ex) {}
        }
    }
    
    public boolean checkExist(int i, int j){
    	for(CellRecord index: allCells){
    		if((index.getCoordinateX() == i) && (index.getCoordinateY() == j)){
    			return true;
    		}
    	}
    	return false;
    }
    
        
}
 