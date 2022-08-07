This is a personal project I have been working on, on and off.  Other image viewers don't allow you to view and quickly skim through multiple images at the same time, so I created this.  It's capable of showing 1, 2 or 4 images at a time through split screen.  Various functions are permitted through mouse clicks and keyboard input.  At all times, the escape button will close the current window.

You start by inserting a folder path into the text field, select the x by y layout and clicking on 'submit' (or pressing Enter).

The application will load image panels based on the layout parameters provided. 

Available functions on the main application's image panels:
- left-click will allow you to 'select' the image and the selected image's data will be shown at the bottom
- middle-click will move on to the next picture in the folder
- right-click will delete the current picture from your hard drive and then move on to the next picture (be careful, there is no delete warning!)
- right-arrow on the keyboard will load a full set of new images in every panel 

While left-clicking once will display its respective data, left-clicking a second time will open up the image in a pop-out window

Available functions on the pop up window:
- left-click will allow you to drag the image around
- middle-click will force zoom the image to fit your screen (note that currently, this part of the code is fixed  to fit a screen size of 1680x1050)
- right-click will close the pop out window
- mouse wheel scroll up will zoom to shrink the image by 50%
- mouse wheel scroll down will zoom to increase the image by 2x.
- double clicking on the image will zoom into that particular location (be careful about zooming in too much!)

The undo button will undo the deletion of the last image (up to a maximum of -one- image back).  Upon click, a popout window will display the image that was previously delete.

The reset button simply resets the program.


---
Written by James Tran