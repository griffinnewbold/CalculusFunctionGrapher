package project.griffin.newbold.calculus;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import project.griffin.newbold.calculus.Function;

import javax.swing.JButton;
import javax.swing.JPanel;


public class Window extends JPanel implements MouseWheelListener, KeyListener, Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 650;

	private BufferedImage buff;
	private Graphics2D g2d;
	
	private ExpressionParser parser;
	private Function function;
	private ExpressionParser parsergx;
	private Function functiongx;
	
	private double windowX, windowY, windowWidth, windowHeight;
	private Point mousePt;

	private String textBox;
	private String textBoxGx;
	private boolean typedFace = false;
	private int numDrawn = 0;
	
	public Window() {
		addMouseWheelListener(this);
		addKeyListener(this);
		this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                repaint();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - mousePt.x;
                int dy = e.getY() - mousePt.y;
                windowX -= dx / (double)WIDTH * windowWidth;
                windowY += dy / (double)HEIGHT * windowHeight;
                mousePt = e.getPoint();
                repaint();
            }
        });
		setFocusable(true);
		requestFocusInWindow();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		
		buff = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g2d = buff.createGraphics();
		
		parser = new ExpressionParser();
		textBox = "";
		function = parser.parse(textBox);
		
		//second equation
		parsergx = new ExpressionParser();
		textBoxGx = "";
		functiongx = parsergx.parse(textBoxGx);
		
		windowX = 0.0;
		windowY = 0.0;
		windowHeight = 2.0;
		windowWidth = windowHeight * WIDTH / HEIGHT;
	}
	
	// Time variables
	private double yVar = 0.0;	// Constantly increasing
	private double zVar = 0.0;	// Cycles smoothly from -1 to 1
	private synchronized void updateDT(double dt) {
		yVar += dt;
		zVar = Math.sin(yVar);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color background = Color.GRAY;
		g2d.setColor(background);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);

		synchronized (this) {
			//first func begins
			List<Double> xs = new ArrayList<>();
			List<Double> ys = new ArrayList<>();
			
			for (int x = 0; x < WIDTH; x++) {
				double xx = toRealX(x);
				
				double yy = 0.0;
				if (function != null) yy = function.evaluateAt(xx, yVar, zVar);
				
				double scaledX = x;
				double scaledY = toScreenY(yy);
				scaledY = Math.min(Math.max(scaledY, -5), HEIGHT + 5);
				
				xs.add(scaledX);
				ys.add(scaledY);
			}
			
			int[] xa = new int[xs.size()];
			int[] ya = new int[ys.size()];
			for (int i = 0; i < xa.length; i++) {
				xa[i] = xs.get(i).intValue();
			}
			for (int i = 0; i < ya.length; i++) {
				ya[i] = ys.get(i).intValue();
			}
			//first func ends
			//second func begins
			List<Double> xs2 = new ArrayList<>();
			List<Double> ys2 = new ArrayList<>();
			
			for (int x = 0; x < WIDTH; x++) {
				double xx2 = toRealX(x);
				
				double yy2 = 0.0;
				if (function != null) yy2 = functiongx.evaluateAt(xx2, yVar, zVar);
				
				double scaledX2 = x;
				double scaledY2 = toScreenY(yy2);
				scaledY2 = Math.min(Math.max(scaledY2, -5), HEIGHT + 5);
				
				xs2.add(scaledX2);
				ys2.add(scaledY2);
			}
			
			int[] xa2 = new int[xs2.size()];
			int[] ya2 = new int[ys2.size()];
			for (int i = 0; i < xa.length; i++) {
				xa2[i] = xs2.get(i).intValue();
			}
			for (int i = 0; i < ya.length; i++) {
				ya2[i] = ys2.get(i).intValue();
			}
			//second func ends
			
			g2d.setColor(Color.BLACK);
			int xAxisY = toScreenY(0.0);
			g2d.drawLine(0, xAxisY, WIDTH, xAxisY);
			int yAxisX = toScreenX(0.0);
			
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(new Color(255, 0, 0));
			g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
			g2d.drawPolyline(xa, ya, xa.length);
			
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(new Color(0, 0, 255));
			g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
			g2d.drawPolyline(xa2, ya2, xa2.length);

			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("courier new", Font.ITALIC, 40));
			g2d.drawLine(yAxisX, 0, yAxisX, HEIGHT);
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(0, HEIGHT - g2d.getFontMetrics().getHeight()-50, WIDTH, HEIGHT);
			g2d.setColor(Color.BLACK);
			if(!typedFace) {
				g2d.drawString("g(x) = " + textBox, 25.0f, HEIGHT - 10.0f);
				g2d.setColor(Color.RED);
				g2d.drawString("f(x) = " + textBoxGx, 25.0f, HEIGHT-60.0f);
			}else {
				g2d.setColor(Color.RED);
				g2d.drawString("g(x) = " + textBox, 25.0f, HEIGHT - 15.0f);
				g2d.setColor(Color.BLACK);
				g2d.drawString("f(x) = " + textBoxGx, 25.0f, HEIGHT-60.0f);
			}
			
			//sideinfo
			/*
			g2d.setFont(new Font("courier new", Font.ITALIC, 30));
			g2d.drawString("Value of Integral for f(x): " + textBoxGx, 50f, 50f);
			g2d.setFont(new Font("courier new", Font.ITALIC, 30));
			g2d.drawString("Value of Integral for g(x): " + textBox, 50f, 80f);
			
			g2d.drawString("x", 0, xAxisY - 10);
			g2d.drawString("y", yAxisX + 10, g2d.getFontMetrics().getHeight() - 20);
			*/
		}
		
		g.drawImage(buff, 0, 0, null);
	}
	
	@Override
	public void run() {
		boolean running = true;
		
		long oldTime = 0;
		double dt = 0.0;
		
		while (running) {
			
			long newTime = System.nanoTime();
			dt = (newTime - oldTime) / 1000000000.0;
			oldTime = newTime;
			
			updateDT(dt);
			repaint();
			
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			typedFace = !typedFace; 
			//System.out.print("hi");
			return;
		}
		if(typedFace == true) {
			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				if (textBox.length() > 0) {
					textBox = textBox.substring(0, textBox.length() - 1);
				}
			} else if (Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyChar() == '^' || e.getKeyChar() == '-' ||
					e.getKeyChar() == '+' || e.getKeyChar() == '*' || e.getKeyChar() == '/' || e.getKeyChar() == '(' ||
					e.getKeyChar() == ')' || e.getKeyChar() == '%' || e.getKeyChar() == ',' || e.getKeyChar() == '.') {
				textBox += e.getKeyChar();
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				typedFace = !typedFace;
				function = parser.parse(textBox);
				if (function == null) {
					textBox = "";
				}
			}
		}else {
			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				if (textBoxGx.length() > 0) {
					textBoxGx = textBoxGx.substring(0, textBoxGx.length() - 1);
				}
			} else if (Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyChar() == '^' || e.getKeyChar() == '-' ||
					e.getKeyChar() == '+' || e.getKeyChar() == '*' || e.getKeyChar() == '/' || e.getKeyChar() == '(' ||
					e.getKeyChar() == ')' || e.getKeyChar() == '%' || e.getKeyChar() == ',' || e.getKeyChar() == '.') {
				textBoxGx += e.getKeyChar();
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				typedFace = !typedFace;
				functiongx = parser.parse(textBoxGx);
				if (functiongx == null) {
					textBoxGx = "";
				}
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	private double bottom() {
		return windowY - halfWindowHeight();
	}
	
	private double right() {
		return windowX - halfWindowWidth();
	}
	
	private double toRealX(int screenX) {
		return screenX / (double)WIDTH * windowWidth + right();
	}
	
	private double toRealY(int screenY) {
		return (HEIGHT - screenY) / (double)HEIGHT * windowHeight + bottom();
	}
	
	private int toScreenX(double realX) {
		return (int) ((realX - right()) / windowWidth * WIDTH);
	}
	
	private int toScreenY(double realY) {
		return HEIGHT - (int) ((realY - bottom()) / windowHeight * HEIGHT);
	}
	
	private double halfWindowWidth() {
		return windowWidth / 2.0;
	}
	
	private double halfWindowHeight() {
		return windowHeight / 2.0;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double scale = Math.pow(1.15, e.getPreciseWheelRotation());
		double mxReal = toRealX(e.getX());
		double myReal = toRealY(e.getY());
		double sx = (windowX - mxReal) / windowWidth;
		double sy = (windowY - myReal) / windowHeight;
		windowWidth *= scale;
		windowHeight *= scale;
		windowX = mxReal + sx * windowWidth;
		windowY = myReal + sy * windowHeight;
	}
}