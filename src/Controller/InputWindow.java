package Controller;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputWindow {
	/*
	 * This class controls the initial window upon first running the program
	 */
	JFrame window;
	JPanel inputPanel;

	JTextField folderPathTextField;
	JTextField x;
	JTextField y;
	ButtonGroup bg;

	public InputWindow() {
		populateInputPanel();
		createJFrame();
	}

	private void createJFrame() {
		window = new JFrame();
		window.setResizable(false);
		window.setLocation(-10, 0);
		window.setLayout(new GridBagLayout());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(inputPanel);
		window.setVisible(true);
		window.pack();
		addKeyListenersToComponents();
		addFocusListenersToComponents();
		addMouseListenersToComponents();
	}

	private void populateInputPanel() {
		inputPanel = new JPanel();
		this.addFolderPathTextField();
		this.addPasteButton();
		this.addRadioButtons();
		this.addSubmitButton();
		this.addExitButton();
	}

	/*
	 * Input field for the folder path.
	 */
	private void addFolderPathTextField() {
		folderPathTextField = new JTextField(50);
		folderPathTextField.setName("folderPathTextField");
		inputPanel.add(folderPathTextField);
		folderPathTextField.setText("Test Images (Press Enter)");
		folderPathTextField.selectAll();
	}

	/*
	 * The Paste Button allows for quick copy of the contents of the clipboard into
	 * the folderPathTextField.
	 */
	private void addPasteButton() {
		JButton pasteButton = new JButton("Paste");
		pasteButton.setName("Paste");
		inputPanel.add(pasteButton);
	}

	/*
	 * Select the desired layout of the application Options are: 1x1, 2x1 and 2x2
	 */
	private void addRadioButtons() {
		x = new JTextField(3);
		x.setHorizontalAlignment(JTextField.CENTER);
		x.setName("x");
		x.setText("3");

		y = new JTextField(3);
		y.setHorizontalAlignment(JTextField.CENTER);
		y.setName("y");
		y.setText("2");

		JLabel label = new JLabel("x");
		label.setName("label");

		inputPanel.add(x);
		inputPanel.add(label);
		inputPanel.add(y);

	}

	/*
	 * Proceed towards initializing the main application
	 */
	private void addSubmitButton() {
		JButton submit = new JButton("Submit");
		submit.setName("Submit");
		inputPanel.add(submit);
	}

	/*
	 * Close and exit the application
	 */
	private void addExitButton() {
		JButton exit = new JButton("Exit");
		exit.setName("Exit");
		inputPanel.add(exit);
	}

	/*
	 * Consolidate all the key listeners to one method
	 */
	private void addKeyListenersToComponents() {
		for (Component c : inputPanel.getComponents()) {
			c.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
					if (key == KeyEvent.VK_ENTER && (c.getName().equals("folderPathTextField")
							|| c.getName().equals("x") || c.getName().equals("y"))) {
						startProgram();
					} else if ((key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE) && c.getName().equals("Paste")) {
						pasteText();
					} else if ((key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE) && c.getName().equals("Submit")) {
						startProgram();
					} else if (key == KeyEvent.VK_ESCAPE
							|| ((key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE) && c.getName().equals("Exit"))) {
						System.exit(0);
					}
				}
			});
		}
	}

	/*
	 * Consolidate all the focus listeners to one method
	 */
	private void addFocusListenersToComponents() {
		for (Component c : inputPanel.getComponents()) {
			if (c.getName().equals("folderPathTextField") || c.getName().equals("x") || c.getName().equals("y")) {
				x.addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent e) {
						((JTextField) c).selectAll();
					}

					@Override
					public void focusLost(FocusEvent e) {
					}
				});
			}
		}
	}

	/*
	 * Consolidate all the mouse listeners to one method
	 */
	private void addMouseListenersToComponents() {
		for (Component c : inputPanel.getComponents()) {
			c.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (c.getName().equals("Paste")) {
						pasteText();
					} else if (c.getName().equals("Submit")) {
						startProgram();
					} else if (c.getName().equals("Exit")) {
						System.exit(0);
					}
				}
			});
		}
	}

	/*
	 * Initialize the main application if the folder path is valid
	 */
	private void startProgram() {
		if (new File(folderPathTextField.getText()).exists()) {
			window.setVisible(false);
			try {
				new ImageController(this);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("File does not exist");
		}
	}

	/*
	 * Paste text from clipboard to the folder path textfield
	 */
	public void pasteText() {
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable t = c.getContents(this);
		if (t == null)
			return;
		try {
			folderPathTextField.setText((String) t.getTransferData(DataFlavor.stringFlavor));
		} catch (Exception error) {
			System.err.println("Probably inapplicable in pasting from clipboard");
		}
	}

	/*
	 * Reactivate this window
	 */
	public void restart() {
		window.setVisible(true);
		folderPathTextField.selectAll();
	}

	public String getDirectory() {
		return folderPathTextField.getText();
	}

	public String getLayout() {
		// return "3x3";
		// return bg.getSelection().getActionCommand();
		return x.getText() + "x" + y.getText();
	}
}
