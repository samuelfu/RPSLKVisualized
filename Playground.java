import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.Timer;

/**
 * The Playground class is the main class that provides the applet code. It
 * takes the parameters from the html file Playground.html and uses them to
 * construct the texts that float across the screen. The program can handle a
 * lot of strings, depending on the computer. The strings cannot skip each other
 * unless the strings are 1 unit wide/tall. All strings can be stationary if
 * their direction is set as 0,0. If two strings are the same both in direction
 * and size, then they will cover each other. If there has not been collisions
 * for a while, the applet stops.
 * 
 * @author Samuel Fu ssf2130 and jrk based on cay horstmann
 * 
 *         needs an html file (real or simulated) that looks like:
 * 
 *         <applet code="Playground.class" width="500" height="500">
 *         <param name="throw" value="Paper Rock Scissors Lizard Spock" />
 *         <param name="fontname" value="TimesRoman Courier Helvetica Dialog
 *         Arial" /> <param name="fontsize" value="20" />
 *         <param name="ColorOne" value="255 0 0 255 0" />
 *         <param name="ColorTwo" value="0 255 0 255 255" />
 *         <param name="ColorThree" value="0 0 255 0 255" />
 * 
 *         <param name="delay" value="10" />
 *         <param name="xStart" value = "250 300 500 400 300"/>
 *         <param name="yStart" value="250 300 500 100 0"/>
 *         <param name="horizontal" value="1 1 -1 -1 0"/>
 *         <param name="vertical" value="-1 1 0 1 -1"/> </applet>
 *
 * 
 */
public class Playground extends Applet {
	final private int NUMBEROFSTRINGS = 5;
	private String[] htmlThrowList;
	private int htmlDelay;
	private Font[] throwFont = new Font[NUMBEROFSTRINGS];
	private Rectangle2D[] throwRectangle = new Rectangle2D[NUMBEROFSTRINGS];
	private int[] throwX = new int[NUMBEROFSTRINGS];
	private int[] throwY = new int[NUMBEROFSTRINGS];
	private Color[] colorList = new Color[NUMBEROFSTRINGS];

	private Timer appletTimer;
	private Evaluator evaluator = new Evaluator();
	private Font boom = new Font("TimesRoman", Font.BOLD, 20);
	private Boolean isBoom = false;
	private int count = 0;

	/**
	 * Idioms for applet initialization, mostly to get parameters; use convention
	 * that incoming parameters are prefixed with "html". Creates multiple
	 * Rectangle2Ds with the boundaries of strings. Creates Fonts with colors and
	 * sizes. Starts a timer that moves the rectangles. If the rectangles intersect
	 * then it evaluates which rectangle wins. The winner continues and the loser
	 * disappears. When two things collide the winner is enlarged. A Boom! appears
	 * when they collide and disappears when another collision occurs.
	 */
	public void init() {

		htmlThrowList = getParameter("throw").split(" ");
		String[] htmlFontList = getParameter("fontname").split(" ");
		int[] htmlFontSize = new int[NUMBEROFSTRINGS];

		for (int i = 0; i < throwFont.length; i++) {
			htmlFontSize[i] = Integer.parseInt(getParameter("fontsize"));
			throwFont[i] = new Font(htmlFontList[i], Font.BOLD, htmlFontSize[i]);
			colorList[i] = new Color(Integer.parseInt(getParameter("ColorOne").split(" ")[i]),
					Integer.parseInt(getParameter("ColorTwo").split(" ")[i]),
					Integer.parseInt(getParameter("ColorThree").split(" ")[i]));
		}

		Graphics2D g2D = (Graphics2D) getGraphics();
		FontRenderContext throwContext = g2D.getFontRenderContext();

		for (int i = 0; i < NUMBEROFSTRINGS; i++)
			throwRectangle[i] = throwFont[i].getStringBounds(htmlThrowList[i], throwContext);

		Rectangle2D boomRect = boom.getStringBounds("Boom!", throwContext);

		for (int i = 0; i < NUMBEROFSTRINGS; i++) {
			throwY[i] = Integer.parseInt(getParameter("yStart").split(" ")[i]);
			throwX[i] = Integer.parseInt(getParameter("xStart").split(" ")[i]);
		}

		htmlDelay = Integer.parseInt(getParameter("delay"));

		appletTimer = new Timer(htmlDelay, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < NUMBEROFSTRINGS; i++) {
					if (Integer.parseInt(getParameter("horizontal").split(" ")[i]) == -1)
						throwX[i]--;
					else if (Integer.parseInt(getParameter("horizontal").split(" ")[i]) == 1)
						throwX[i]++;

					if (Integer.parseInt(getParameter("vertical").split(" ")[i]) == -1)
						throwY[i]--;
					else if (Integer.parseInt(getParameter("vertical").split(" ")[i]) == 1)
						throwY[i]++;

					if (throwX[i] + throwRectangle[i].getWidth() < 0)
						throwX[i] = getWidth();
					else if (throwX[i] > getWidth())
						throwX[i] -= getWidth();

					if (throwY[i] + throwRectangle[i].getHeight() < 0)
						throwY[i] = getHeight();
					else if (throwY[i] > getHeight())
						throwY[i] -= getHeight();

					throwRectangle[i] = new Rectangle2D.Double(throwX[i], throwY[i], throwRectangle[i].getWidth(),
							throwRectangle[i].getHeight());

				}
				for (int i = 0; i < NUMBEROFSTRINGS; i++)
					for (int j = i + 1; j < NUMBEROFSTRINGS; j++) {
						if (throwRectangle[i].intersects(throwRectangle[j])) {
							System.out.println("YO");

							int result = evaluator.determineWinnerOfRound(htmlThrowList[i], htmlThrowList[j]);
							if (result == 0) {
								throwRectangle[j] = new Rectangle2D.Double(0, 0, 0, 0);
								throwFont[j] = new Font(htmlFontList[j], Font.BOLD, 0);
								htmlFontSize[i] += 20;
								throwFont[i] = new Font(htmlFontList[i], Font.BOLD, htmlFontSize[i]);
								throwRectangle[i] = throwFont[i].getStringBounds(htmlThrowList[i], throwContext);
							} else if (result == 1) {
								throwRectangle[i] = new Rectangle2D.Double(0, 0, 0, 0);
								throwFont[i] = new Font(htmlFontList[i], Font.BOLD, 0);
								htmlFontSize[j] += 20;
								throwFont[j] = new Font(htmlFontList[j], Font.BOLD, htmlFontSize[j]);
								throwRectangle[j] = throwFont[j].getStringBounds(htmlThrowList[j], throwContext);
							}

							if (isBoom)
								isBoom = false;
							else
								isBoom = true;
							count = 0;
						} else {
							count++;
							if (count == 5000)
								stop();
						}
					}
				repaint();
			}
		});
	}

	/**
	 * Starts timer
	 */
	public void start() {
		appletTimer.start();
	}

	/**
	 * Paints the text onto the screen/applet
	 */
	public void paint(Graphics g) {
		for (int i = 0; i < NUMBEROFSTRINGS; i++) {
			g.setFont(throwFont[i]);
			g.setColor(colorList[i]);
			g.drawString(htmlThrowList[i], throwX[i], throwY[i]);
		}
		if (isBoom) {
			g.setFont(boom);
			g.setColor(new Color(0, 0, 0));
			g.drawString("Boom!", 30, 30);

		}
	}

	/**
	 * Stops the timer
	 */
	public void stop() {
		appletTimer.stop();
	}

	/**
	 * Reclaims applet
	 */
	public void destroy() {
	}

}
