package Controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import View.ImagePanel;
import View.ImageView;
import View.PopUpWindow;

public class ImageController {
	/*
	 * Primary controller class. Provides functionality to the view class.
	 */
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	int numDeleted = 0;
	int recordIndex = -1;
	int index = -1;
	int deletedIndex = -1;
	int[] indexShift;

	String deletedName;
	String directory;
	String layout;
	String[] children;

	InputWindow iw;
	PopUpWindow p;
	GridBagConstraints c;
	ParseRecords pr;

	ArrayList<ImagePanel> alOfPanels;
	ArrayList<Integer> currentDirectoryIndices;

	ImageView viewer;
	ImagePanel selectedPanel;
	ImagePanel tempPanel;
	JButton tempButton;

	/*
	 * Controller constructor
	 */
	public ImageController(InputWindow iw) throws FileNotFoundException {
		this.iw = iw;
		setupProgram();
		start();
	}

	/*
	 * Initialize all necessary variables and procedures
	 */
	private void setupProgram() throws FileNotFoundException {
		this.pr = new ParseRecords();
		this.p = new PopUpWindow();
		this.alOfPanels = new ArrayList<ImagePanel>();
		this.directory = iw.getDirectory();
		this.currentDirectoryIndices = pr.getRecords(this.directory);
		this.layout = iw.getLayout();
		this.viewer = new ImageView(this);
		addMouseListenerToPanel();

	}

	/*
	 * Start the application after all initializations are complete
	 */
	private void start() {
		File dir = new File(this.directory);
		// children = dir.list();

		File[] files = dir.listFiles();

		/*
		 * Uncomment to sort by file size first   
		 */
		//Arrays.sort(files, (f1, f2) -> {return new Long(f1.length()).compareTo(new Long(f2.length()));});

		this.children = new String[files.length];
		
		for (int i = 0; i < files.length; i++) {
			children[i] = files[i].getName();
		}

		if (children.length == 0) {
			viewer.setDirBox("Empty folder");
		}

		indexShift = new int[children.length];
		for (int ii = 0; ii < children.length; ii++) {
			indexShift[ii] = 0;
		}

		if (currentDirectoryIndices == null) {
			currentDirectoryIndices = new ArrayList<Integer>();
		}
		if (currentDirectoryIndices.size() < alOfPanels.size()) {
			for (int i = 0; i < alOfPanels.size(); i++) {
				try {
					currentDirectoryIndices.get(i);
				} catch (IndexOutOfBoundsException e) {
					currentDirectoryIndices.add(-1);
				}
			}
		}

		loadFullSetofFourImages();

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					// load all new images with the right arrow key
					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						newFullSetofFourImages();
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						// exits program
						if (!p.isVisible()) {
							// System.exit(0);
							resetProgram();
						}
					}
				}
				return false;
			}
		});
	}

	/*
	 * Load 4 new images if the directory is not found in the records
	 */
	public void newFullSetofFourImages() {
		for (ImagePanel currentPanel : alOfPanels) {
			findNextImageIndex(currentPanel);
		}
	}

	/*
	 * Load 4 images based on the indexes provided in the int[]
	 */
	public void loadFullSetofFourImages() {
		for (int ii = 0; ii < alOfPanels.size(); ii++) {
			recordIndex = currentDirectoryIndices.get(ii);
			if (recordIndex == -1) {
				findNextImageIndex(alOfPanels.get(ii));
			} else {
				if (recordIndex >= children.length) {
					if (ii == 0) {
						recordIndex = 0;
					} else {
						recordIndex = currentDirectoryIndices.get(ii) + 1;
					}
				}
				displayImage(alOfPanels.get(ii), recordIndex);
				if (recordIndex > index) {
					index = recordIndex;
				}
			}
		}
		// index = max; // *** use case: index loop back around ***
	}

	/*
	 * First step of two in order to load the next image. This method seeks the
	 * index of the next image that should be loaded
	 */
	public void findNextImageIndex(ImagePanel j) {
		try {
			// set file association
			ArrayList<String> alOfFileTypes = new ArrayList<String>();
			alOfFileTypes.add(".jpg");
			alOfFileTypes.add(".jpeg");
			alOfFileTypes.add(".png");
			alOfFileTypes.add(".bmp");
			alOfFileTypes.add(".gif");

			boolean fileNotFound = true;
			do {
				index++;
				if (index == children.length) {
					index = 0;
				}
				for (String s : alOfFileTypes) {
					if (children[index].toLowerCase().endsWith(s) && new File(directory + "\\" + children[index])
							.canRead()) { /* canRead is to make sure the file still exists */
						fileNotFound = false;
					}
				}
				if (fileNotFound) {
				}
			} while (fileNotFound);
		} catch (Exception e) {
			System.err.println("Error in findNextImage");
			e.printStackTrace();
		}

		displayImage(j, index);
	}

	/*
	 * Second step of two in order to load the next image After the correct index is
	 * found, it is recorded then executed.
	 */
	public void displayImage(final ImagePanel j, int ind) {
		for (int i = 0; i < alOfPanels.size(); i++) {
			if (alOfPanels.get(i) == j) {
				currentDirectoryIndices.set(i, ind);
			}
		}
		pr.updateAndWriteRecord(indexShift, currentDirectoryIndices);
		j.setImage(directory + "\\" + children[ind], ind);
	}

	/*
	 * Method that handles deleting images
	 */
	public void deleteFile(String path) {
		try {
			File del = new File("./temp");
			del.delete();
		} catch (Throwable t) {
			System.err.println("Error in deleteFile");
		}
		File toBeDeletedFile = new File(path);
		deletedName = toBeDeletedFile.getName();
		toBeDeletedFile.renameTo(new File("./temp"));
		toBeDeletedFile.delete();
		viewer.setUndoButton(true);
		for (int ii = selectedPanel.getIndex(); ii < children.length; ii++) {
			indexShift[ii] = indexShift[ii] + 1;
		}
		this.deletedIndex = selectedPanel.getIndex();
		numDeleted++;
	}

	/*
	 * Called by the viewer to obtain the greater image panel
	 */
	public JPanel getGreaterImagesPanel() {
		int imagePanelsHeight = (int) screenSize.getHeight() - 22 - 52 + 22; // 22 is title height, 52 is
																				// infocontrolpanel height, +22 is from
																				// removing the title
		int imagePanelsWidth = (int) screenSize.getWidth();
		JPanel imagePanels = new JPanel();
		imagePanels.setBackground(Color.ORANGE);
		imagePanels.setPreferredSize(new Dimension(imagePanelsWidth, imagePanelsHeight));
		imagePanels.setLayout(new GridBagLayout());
		return imagePanels;
	}

	/*
	 * Called by the viewer to obtain individual image panels
	 */
	public ImagePanel getNewImagePanel(Dimension d) {
		tempPanel = new ImagePanel(d);
		alOfPanels.add(tempPanel);
		return tempPanel;
	}

	/*
	 * Called by this class. Adds mouse functionality to each individual image panel
	 */
	private void addMouseListenerToPanel() {
		for (ImagePanel currentPanel : alOfPanels) {
			currentPanel.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						if (selectedPanel == null || !viewer.getDirBox().equals(currentPanel.getFullPath())) {
							selectedPanel = currentPanel;
							viewer.setDirBox(currentPanel.getFullPath());
							viewer.setSizeBox(currentPanel.getFileSize() + "  " + currentPanel.getOriginalDimension()
									+ "  (" + (1 + currentPanel.getIndex()) + " of " + (children.length) + ")");
						} else {
							p = new PopUpWindow();
							p.createPopUp(currentPanel.getImage());
						}
					} else if (e.getButton() == MouseEvent.BUTTON2) {
						findNextImageIndex(currentPanel);
					} else if (e.getButton() == MouseEvent.BUTTON3) {
						selectedPanel = currentPanel;
						deleteFile(currentPanel.getFullPath());
						findNextImageIndex(currentPanel);
					}
				}
			});
		}
	}

	/*
	 * Called by the viewer, retrieves requested JButton including its functionality
	 */
	public JButton getButton(String s, int length) {
		tempButton = new JButton(s);
		tempButton.setSize(new Dimension(length, 26));
		try {
			Method method = this.getClass().getMethod(s.replaceAll(" ", "".toLowerCase()), null);
			method.invoke(this, null);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return tempButton;
	}

	/*
	 * Copy Name button functionality
	 */
	public void CopyName() {
		tempButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					StringSelection selection = new StringSelection(children[selectedPanel.getIndex()]);
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
				} catch (NullPointerException n) {
					JOptionPane.showMessageDialog(null, "No image selected", "Copy Name Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
		});
	}

	/*
	 * Copy Picture button functionality
	 */
	public void CopyPicture() {
		tempButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					ImageSelection imgSel = new ImageSelection(selectedPanel.getImage().getImage());
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null);
				} catch (NullPointerException n) {
					JOptionPane.showMessageDialog(null, "No image selected", "Copy Image Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/*
	 * Undo button functionality
	 */
	public void Undo() {

		tempButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				File f = new File("./temp");
				f.renameTo(new File(directory, deletedName));
				viewer.setUndoButton(false);
				for (int ii = deletedIndex; ii < children.length; ii++) {
					indexShift[ii] = indexShift[ii] - 1;
				}
				pr.updateAndWriteRecord(indexShift, currentDirectoryIndices);
				p.createPopUp(new ImageIcon(directory + "//" + deletedName));
			}
		});
	}

	/*
	 * Reset button functionality
	 */
	public void Reset() {
		tempButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				resetProgram();
			}
		});
	}

	/*
	 * Return to the first images button functionality
	 */
	public void Returntothefirstimages() {
		tempButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				for (int i = 0; i < currentDirectoryIndices.size(); i++) {
					currentDirectoryIndices.set(i, -1);
				}
				index = -1;
				loadFullSetofFourImages();
			}
		});
	}

	/*
	 * Resets the application
	 */
	public void resetProgram() {
		if (this.viewer != null) {
			viewer.close();
			viewer = null;
			iw.restart();
		}
	}

	/*
	 * Called by the viewer, used to send layout to the view Layout needs to be
	 * known by the view in order to know where to put each image panel.
	 */
	public String getLayout() {
		return this.layout;
	}
}
