package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	/*
	 * The ImagePanel sits on top of a greater JPanel in the main controller class
	 * contains the image, manages the image information and sits on top of a
	 * greater JPanel in the main window.
	 */
	ImageIcon image;
	String name;
	String fullPath;
	String type;
	int imageWidth;
	int imageHeight;
	double size;
	int index;
	JLabel label;
	Dimension panelDimension;

	public ImagePanel(Dimension d) {
		this.panelDimension = d;
		this.setPreferredSize(d);
		this.setBorder(BorderFactory.createLineBorder(Color.black));

		this.label = new JLabel();
		setLayout(new BorderLayout());
	}

	public void setImage(String fullPath, int index) {
		this.removeAll();
		this.fullPath = fullPath;
		this.index = index;
		this.image = new ImageIcon(fullPath);
		this.label = new JLabel(this.image);
		this.type = fullPath.substring(fullPath.length() - 3, fullPath.length());
		this.setBackground(new Color((int) (Math.random() * 0x1000000)));
		this.imageWidth = image.getIconWidth();
		this.imageHeight = image.getIconHeight();
		resizeImageSize();

		File f = new File(fullPath);
		long length = f.length();
		this.size = ((double) ((int) (100 * (length * .9765625) / 1000))) / 100;

		add(new JLabel(new ImageIcon(this.image.getImage().getScaledInstance(this.imageWidth, this.imageHeight,
				java.awt.Image.SCALE_SMOOTH))));
		this.revalidate();
		this.repaint();
	}

	private void resizeImageSize() {
		double ratio = this.panelDimension.getHeight() / this.imageHeight;
		this.imageHeight = (int) (image.getIconHeight() * ratio);
		this.imageWidth = (int) (image.getIconWidth() * ratio);

		if (this.imageWidth > this.panelDimension.getWidth()) {
			ratio = this.panelDimension.getWidth() / this.imageWidth;
			this.imageHeight = (int) (this.imageHeight * ratio);
			this.imageWidth = (int) (this.imageWidth * ratio);
		}
	}

	public ImageIcon getImage() {
		return image;
	}

	public String getName() {
		return name;
	}

	public String getFullPath() {
		return fullPath;
	}

	public String getOriginalDimension() {
		return image.getIconWidth() + " x " + image.getIconHeight();
	}

	public String getFileSize() {
		return size + "KB";
	}

	public String getType() {
		return type;
	}

	public int getIndex() {
		return index;
	}

	public void delete() {
		image = null;
	}
}