package View;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controller.ImageController;

public class ImageView extends JFrame {
	/*
	 * Primary view class Extends the JFrame and holds all the individual panels
	 * Makes calls to the controller to retrieve functionality
	 */
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	GridBagConstraints c;
	ImageController controller;

	JPanel imagePanels;
	JPanel infoControlPanel;

	JTextField dirBox;
	JTextField sizeBox;
	JButton copyNameButton;
	JButton copyPictureButton;
	JButton cycleReset;
	JButton undo;
	JButton reset;

	public ImageView(ImageController controller) {
		this.controller = controller;
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		this.setResizable(false);
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setVisible(true);

		setupImagePanels();
		setupInfoControllerPanel();
	}

	private void setupImagePanels() {
		imagePanels = controller.getGreaterImagesPanel();
		int imagePanelsHeight = (int) imagePanels.getPreferredSize().getHeight();
		int imagePanelsWidth = (int) imagePanels.getPreferredSize().getWidth();

		Dimension panelResolution;
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		Scanner sc = new Scanner(controller.getLayout());
		sc.useDelimiter("x");
		int x = Integer.parseInt(sc.next());
		int y = Integer.parseInt(sc.next());
		sc.close();

		for (int j = 0; j < y; j++) {
			for (int i = 0; i < x; i++) {
				panelResolution = new Dimension(imagePanelsWidth / x, imagePanelsHeight / y);
				c.weightx = 1;
				c.gridx = i;
				c.gridy = j;
				imagePanels.add(controller.getNewImagePanel(panelResolution), c);
			}
		}

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		this.add(imagePanels, c);
	}

	public void setupInfoControllerPanel() {
		infoControlPanel = new JPanel();
		infoControlPanel.setLayout(new GridBagLayout());

		JPanel firstRow = new JPanel();
		JPanel secondRow = new JPanel();
		firstRow.setLayout(new GridBagLayout());
		secondRow.setLayout(new GridBagLayout());

		undo = controller.getButton("Undo", 100);
		this.setUndoButton(false);

		dirBox = new JTextField(200);
		dirBox.setEditable(false);
		dirBox.setHorizontalAlignment(JTextField.RIGHT);

		sizeBox = new JTextField(208);
		sizeBox.setSize(10, 26);
		sizeBox.setEditable(false);
		sizeBox.setHorizontalAlignment(JTextField.RIGHT);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		firstRow.add(controller.getButton("Copy Name", 150), c);

		c.weightx = 1;
		c.gridx = 1;
		c.gridy = 0;
		firstRow.add(controller.getButton("Copy Picture", 150), c);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		secondRow.add(undo, c);

		c.weightx = 1;
		c.gridx = 1;
		c.gridy = 0;
		secondRow.add(controller.getButton("Reset", 100), c);

		c.weightx = 1;
		c.gridx = 2;
		c.gridy = 0;
		secondRow.add(controller.getButton("Return to the first images", 100), c);

		c.weightx = 1;
		c.gridx = 2;
		c.gridy = 0;
		firstRow.add(dirBox, c);

		c.weightx = 1;
		c.gridx = 3;
		c.gridy = 0;
		secondRow.add(sizeBox, c);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		infoControlPanel.add(firstRow, c);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		infoControlPanel.add(secondRow, c);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		this.add(infoControlPanel, c);
	}

	public void close() {
		this.removeAll();
		this.setVisible(false);
	}

	public void setUndoButton(boolean b) {
		undo.setEnabled(b);
	}

	public String getDirBox() {
		return dirBox.getText();
	}

	public void setDirBox(String s) {
		dirBox.setText(s);
	}

	public void setSizeBox(String s) {
		sizeBox.setText(s);
	}
}