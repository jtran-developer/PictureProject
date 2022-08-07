package View;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class PopUpWindow extends JDialog {
	/*
	 * This class handles the pop up window function of the application The image
	 * (as an ImageIcon) is given to this class and the image is put into its own
	 * pop up window
	 */
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	ImageIcon originalImage;
	ImageIcon icon;
	JLabel jl;
	JScrollPane jsp;
	JScrollBar jsbh;
	JScrollBar jsbv;

	ImageIcon icon2;

	int currentX;
	int currentY;
	double zoom;
	boolean doubleClickMove;

	public PopUpWindow() {
		icon = new ImageIcon();
		jl = new JLabel(icon);
		jsp = new JScrollPane(jl);
		jsbh = jsp.getHorizontalScrollBar();
		jsbv = jsp.getVerticalScrollBar();
		addKeyboardMouseListener();

		this.add(jsp);
	}

	/*
	 * Used to create a new instance of the image pop up window. The main
	 * application sends this method an image as its parameter.
	 */
	public void createPopUp(ImageIcon ic) {
		originalImage = ic;
		this.zoom = 1;
		this.setVisible(true);
		updatePopUp(1);
	}

	/*
	 * Calculate all necessary parameters before showing the image, including zoom
	 * and conforming to screen size
	 */
	private void updatePopUp(double zoom) {
		this.zoom = this.zoom * zoom;
		int width = (int) (originalImage.getIconWidth() * this.zoom);
		int height = (int) (originalImage.getIconHeight() * this.zoom);

		icon.setImage(
				new ImageIcon(originalImage.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH))
						.getImage());
		jl.revalidate();

		this.setSize(new Dimension(width + 17 > screenSize.width + 13 ? screenSize.width + 13 : width + 17,
				height + 40 > screenSize.height + 6 ? screenSize.height + 6 : height + 40));

		setLocation(-6, 0);

		if (this.doubleClickMove) {
			int zoomX = currentX * 2 - (this.getWidth() / 2);
			int zoomY = currentY * 2 - (this.getHeight() / 2);

			if (zoomX < 0)
				zoomX = 0;

			if (zoomY < 0)
				zoomY = 0;

			jsbh.setValue(zoomX);
			jsbv.setValue(zoomY);

			this.doubleClickMove = false;
		} else {
			jsbh.setValue(0);
			jsbv.setValue(0);
		}
	}

	/*
	 * Enable the controls to the pop up window, specifically around zoom and
	 * closing of the window.
	 */
	public void addKeyboardMouseListener() {
		jsp.setWheelScrollingEnabled(false);
		jsp.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				currentX = e.getX();
				currentY = e.getY();
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.getClickCount() == 2) {
						currentX += jsbh.getValue();
						currentY += jsbv.getValue();
						doubleClickMove = true;
						updatePopUp(2);
					}
				} else if (e.getButton() == MouseEvent.BUTTON2) {
					// fix zoom to full image of screen size with middle button
					double zoom = (screenSize.getHeight() - 33) / icon.getIconHeight();
					if (screenSize.getWidth() < (icon.getIconWidth() * zoom)) {
						zoom = (screenSize.getWidth() - 19) / icon.getIconWidth();
					}
					updatePopUp(zoom);
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					// close pop-out window with right-click
					setVisible(false);
				}
			}
		});

		jsp.addMouseMotionListener(new MouseMotionAdapter() {
			JScrollBar jsbh = jsp.getHorizontalScrollBar();
			JScrollBar jsbv = jsp.getVerticalScrollBar();

			public void mouseDragged(MouseEvent e) {
				int diffX = e.getX() - currentX;
				int diffY = e.getY() - currentY;

				jsbh.setValue(jsbh.getValue() - diffX);
				jsbv.setValue(jsbv.getValue() - diffY);

				currentX = e.getX();
				currentY = e.getY();
			}
		});

		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				// close window with escape key
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setVisible(false);
				}
			}
		});

		this.addMouseWheelListener(new MouseAdapter() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() == -1) {
					updatePopUp(.5);

				} else if (e.getWheelRotation() == 1) {
					updatePopUp(2);
				}
			}
		});
	}
}
