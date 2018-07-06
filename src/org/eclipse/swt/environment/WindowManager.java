package org.eclipse.swt.environment;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class WindowManager {
	
	public static enum HorizontalAlignments {LEFT, CENTER, RIGHT};
	public static enum VerticalAlignments {TOP, MIDDLE, BOTTOM};
	public static enum Alignments {LEFT, CENTER, RIGHT, TOP, MIDDLE, BOTTOM};
	
	
	public static int LOCATION_TOP_CENTER_TO_DISPLAY = 121;
	public static int LOCATION_TOP_CENTER_TO_PARENT = 122;
	public static int LOCATION_TOP_LEFT_TO_DISPLAY = 123;
	public static int LOCATION_TOP_LEFT_TO_PARENT = 124;
	public static int LOCATION_TOP_RIGHT_TO_DISPLAY = 125;
	public static int LOCATION_TOP_RIGHT_TO_PARENT = 126;
	public static int LOCATION_MIDDLE_CENTER_TO_DISPLAY = 131;
	public static int LOCATION_MIDDLE_CENTER_TO_PARENT = 132;
	public static int LOCATION_MIDDLE_LEFT_TO_DISPLAY = 133;
	public static int LOCATION_MIDDLE_LEFT_TO_PARENT = 134;
	public static int LOCATION_MIDDLE_RIGHT_TO_DISPLAY = 135;
	public static int LOCATION_MIDDLE_RIGHT_TO_PARENT = 136;
	public static int LOCATION_BOTTOM_CENTER_TO_DISPLAY = 141;
	public static int LOCATION_BOTTOM_CENTER_TO_PARENT = 142;
	public static int LOCATION_BOTTOM_LEFT_TO_DISPLAY = 143;
	public static int LOCATION_BOTTOM_LEFT_TO_PARENT = 144;
	public static int LOCATION_BOTTOM_RIGHT_TO_DISPLAY = 145;
	public static int LOCATION_BOTTOM_RIGHT_TO_PARENT = 146;
	
	public static int MOUSE_BUTTON_ANY = 0;
	public static int MOUSE_BUTTON_LEFT = 1;
	public static int MOUSE_BUTTON_MIDDLE = 2;
	public static int MOUSE_BUTTON_RIGHT = 3;
	
	
	
	/**
	 * Create a new borderless shell that is attached to a parent shell
	 * @param parent : sets the parent of the shell (Shell)
	 * @param modal : defines if the shell is modal (Boolean)
	 * @return
	 */
	public static Shell newBorderlessShell(Shell parent, boolean modal) {
		int style = SWT.NO_TRIM;
		if (modal) {
			style = style | SWT.APPLICATION_MODAL;
		}
		Shell output = new Shell(parent, style);
		setLocation(output, LOCATION_MIDDLE_CENTER_TO_PARENT);
		output.setImage(Environment.Resources.getImageFromResource("/org/eclipse/swt/environment/arrow_down_gray.png"));
		output.setLayout(new FillLayout(SWT.HORIZONTAL));
		return output;
	}
	
	/**
	 * Create a new borderless shell that is attached to a display
	 * @param display : sets the display of the shell (Display)
	 * @param modal : defines if the shell is modal (Boolean)
	 * @return
	 */
	public static Shell newBorderlessShell(Display display) {
		int style = SWT.NO_TRIM;
		Shell output = new Shell(display, style);
		setLocation(output, LOCATION_MIDDLE_CENTER_TO_DISPLAY);
		output.setImage(Environment.Resources.getImageFromResource("/org/eclipse/swt/environment/arrow_down_gray.png"));
		output.setLayout(new FillLayout(SWT.HORIZONTAL));
		return output;
	}
	
	/**
	 * Create a new borderless shell that is not attached to a parent
	 * @param modal : defines if the shell is modal (Boolean)
	 * @return
	 */
	public static Shell newBorderlessShell() {
		return newBorderlessShell(new Display());
	}
	
	/**
	 * Create a new shell that is not attached to a parent
	 * @param parent : sets the parent of the shell (Shell)
	 * @param resizable : defines if the shell is resizable (Boolean)
	 * @param maximizable : defines if the shell is maximizable (Boolean)
	 * @param modal : defines if the shell is modal (Boolean)
	 * @return
	 */
	public static Shell newShell(boolean resizable, boolean maximizable) {
		return newShell(new Display(), resizable, maximizable);
	}
	
	/**
	 * Create a new shell that is attached to a parent shell
	 * @param parent : sets the parent of the shell (Shell)
	 * @param resizable : defines if the shell is resizable (Boolean)
	 * @param maximizable : defines if the shell is maximizable (Boolean)
	 * @param modal : defines if the shell is modal (Boolean)
	 * @return
	 */
	public static Shell newShell(Shell parent, boolean resizable, boolean maximizable, boolean modal) {
		int style = SWT.DIALOG_TRIM;
		if (modal) {
			style = style | SWT.APPLICATION_MODAL;
		}
		if (resizable) {
			style = style | SWT.RESIZE;
		}
		if (maximizable) {
			style = style | SWT.MAX;
		}		
		Shell output = new Shell(parent, style);
		setLocation(output, LOCATION_MIDDLE_CENTER_TO_PARENT);
		output.setImage(Environment.Resources.getImageFromResource("/org/eclipse/swt/environment/arrow_down_gray.png"));
		output.setLayout(new FillLayout(SWT.HORIZONTAL));
		return output;
	}
	
	/**
	 * Create a new shell that is attached to a display
	 * @param display : sets the display of the shell (Display)
	 * @param resizable : defines if the shell is resizable (Boolean)
	 * @param maximizable : defines if the shell is maximizable (Boolean)
	 * @return
	 */
	public static Shell newShell(Display display, boolean resizable, boolean maximizable) {
		int style = SWT.DIALOG_TRIM;
		if (resizable) {
			style = style | SWT.RESIZE;
		}
		if (maximizable) {
			style = style | SWT.MAX;
		}		
		Shell output = new Shell(display, style);
		setLocation(output, LOCATION_MIDDLE_CENTER_TO_DISPLAY);
		output.setImage(Environment.Resources.getImageFromResource("/org/eclipse/swt/environment/arrow_down_gray.png"));
		output.setLayout(new FillLayout(SWT.HORIZONTAL));
		return output;
	}
	
	/**
	 * Opens a given shell
	 * @param shell : the shell to open (Shell)
	 */
	public static void open(Shell shell) {
		Display display = shell.getDisplay();
		if(display == null) {
			display = Display.getDefault();
		}
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	/**
	 * Set the position of a given shell in relative mode using location constants.
	 * @param shell : target shell for positioning (Shell)
	 * @param display : target display for position coordinates reference (Display)
	 * @param location_constant : window positioning constant (int)
	 * <br/><br/>
	 * <b>Available location constants:</b><br/>
	 * LOCATION_TOP_CENTER_TO_DISPLAY<br/>
	 * LOCATION_TOP_CENTER_TO_PARENT<br/>
	 * LOCATION_TOP_LEFT_TO_DISPLAY<br/>
	 * LOCATION_TOP_LEFT_TO_PARENT<br/>
	 * LOCATION_TOP_RIGHT_TO_DISPLAY<br/>
	 * LOCATION_TOP_RIGHT_TO_PARENT<br/>
	 * LOCATION_MIDDLE_CENTER_TO_DISPLAY<br/>
	 * LOCATION_MIDDLE_CENTER_TO_PARENT<br/>
	 * LOCATION_MIDDLE_LEFT_TO_DISPLAY<br/>
	 * LOCATION_MIDDLE_LEFT_TO_PARENT<br/>
	 * LOCATION_MIDDLE_RIGHT_TO_DISPLAY<br/>
	 * LOCATION_MIDDLE_RIGHT_TO_PARENT<br/>
	 * LOCATION_BOTTOM_CENTER_TO_DISPLAY<br/>
	 * LOCATION_BOTTOM_CENTER_TO_PARENT<br/>
	 * LOCATION_BOTTOM_LEFT_TO_DISPLAY<br/>
	 * LOCATION_BOTTOM_LEFT_TO_PARENT<br/>
	 * LOCATION_BOTTOM_RIGHT_TO_DISPLAY<br/>
	 * LOCATION_BOTTOM_RIGHT_TO_PARENT<br/>
	 * <br/>
	 * <b>Example:</b><br/>
	 * ShellManager.setLocation(some_shell, some_display, ShellManager.LOCATION_TOP_CENTER_TO_DISPLAY);
	 * <br/><br/>
	 */
	public static void setLocation(Shell shell, Display display, int location_constant) {
		setLocation(shell, display, location_constant, 0, 0);
	}
	
	/**
	 * Set the position of a given shell in relative mode using location constants.
	 * @param shell : target shell for positioning (Shell)
	 * @param location_constant : window positioning constant (int)
	 * <br/><br/>
	 * <b>Available location constants:</b><br/>
	 * LOCATION_TOP_CENTER_TO_DISPLAY<br/>
	 * LOCATION_TOP_CENTER_TO_PARENT<br/>
	 * LOCATION_TOP_LEFT_TO_DISPLAY<br/>
	 * LOCATION_TOP_LEFT_TO_PARENT<br/>
	 * LOCATION_TOP_RIGHT_TO_DISPLAY<br/>
	 * LOCATION_TOP_RIGHT_TO_PARENT<br/>
	 * LOCATION_MIDDLE_CENTER_TO_DISPLAY<br/>
	 * LOCATION_MIDDLE_CENTER_TO_PARENT<br/>
	 * LOCATION_MIDDLE_LEFT_TO_DISPLAY<br/>
	 * LOCATION_MIDDLE_LEFT_TO_PARENT<br/>
	 * LOCATION_MIDDLE_RIGHT_TO_DISPLAY<br/>
	 * LOCATION_MIDDLE_RIGHT_TO_PARENT<br/>
	 * LOCATION_BOTTOM_CENTER_TO_DISPLAY<br/>
	 * LOCATION_BOTTOM_CENTER_TO_PARENT<br/>
	 * LOCATION_BOTTOM_LEFT_TO_DISPLAY<br/>
	 * LOCATION_BOTTOM_LEFT_TO_PARENT<br/>
	 * LOCATION_BOTTOM_RIGHT_TO_DISPLAY<br/>
	 * LOCATION_BOTTOM_RIGHT_TO_PARENT<br/>
	 * <br/>
	 * <b>Example:</b><br/>
	 * ShellManager.setLocation(some_shell, ShellManager.LOCATION_TOP_CENTER_TO_DISPLAY);
	 * <br/><br/>
	 */
	public static void setLocation(Shell shell, int location_constant) {
		setLocation(shell, location_constant, 0, 0);
	}
	
	/**
	 * Set the position of a given shell in relative mode using location constants as well as absolute x/y location offsets.
	 * @param shell : target shell for positioning (Shell)
	 * @param location_constant : window positioning constant (int)
	 * @param x_offset : horiszontal offset (int)
	 * @param y_offset : vertical offest (int)
	 * <br/><br/>
	 * <b>Available location constants:</b><br/>
	 * LOCATION_TOP_CENTER_TO_DISPLAY<br/>
	 * LOCATION_TOP_CENTER_TO_PARENT<br/>
	 * LOCATION_TOP_LEFT_TO_DISPLAY<br/>
	 * LOCATION_TOP_LEFT_TO_PARENT<br/>
	 * LOCATION_TOP_RIGHT_TO_DISPLAY<br/>
	 * LOCATION_TOP_RIGHT_TO_PARENT<br/>
	 * LOCATION_MIDDLE_CENTER_TO_DISPLAY<br/>
	 * LOCATION_MIDDLE_CENTER_TO_PARENT<br/>
	 * LOCATION_MIDDLE_LEFT_TO_DISPLAY<br/>
	 * LOCATION_MIDDLE_LEFT_TO_PARENT<br/>
	 * LOCATION_MIDDLE_RIGHT_TO_DISPLAY<br/>
	 * LOCATION_MIDDLE_RIGHT_TO_PARENT<br/>
	 * LOCATION_BOTTOM_CENTER_TO_DISPLAY<br/>
	 * LOCATION_BOTTOM_CENTER_TO_PARENT<br/>
	 * LOCATION_BOTTOM_LEFT_TO_DISPLAY<br/>
	 * LOCATION_BOTTOM_LEFT_TO_PARENT<br/>
	 * LOCATION_BOTTOM_RIGHT_TO_DISPLAY<br/>
	 * LOCATION_BOTTOM_RIGHT_TO_PARENT<br/>
	 * <br/>
	 * <b>Example:</b><br/>
	 * ShellManager.setLocation(some_shell, ShellManager.LOCATION_TOP_CENTER_TO_DISPLAY, 10, 8);
	 * <br/><br/>
	 */
	public static void setLocation(Shell shell, int location_constant, int x_offset, int y_offset) {
		setLocation(shell, Display.getCurrent(), location_constant, x_offset, y_offset);
	}
	
	/**
	 * Set the position of a given shell in relative mode using location constants as well as absolute x/y location offsets.
	 * @param shell : target shell for positioning (Shell)
	 * @param display : target display for position coordinates reference (Display)
	 * @param location_constant : window positioning constant (int)
	 * @param x_offset : horiszontal offset (int)
	 * @param y_offset : vertical offest (int)
	 * <br/><br/>
	 * <b>Available location constants:</b><br/>
	 * LOCATION_TOP_CENTER_TO_DISPLAY<br/>
	 * LOCATION_TOP_CENTER_TO_PARENT<br/>
	 * LOCATION_TOP_LEFT_TO_DISPLAY<br/>
	 * LOCATION_TOP_LEFT_TO_PARENT<br/>
	 * LOCATION_TOP_RIGHT_TO_DISPLAY<br/>
	 * LOCATION_TOP_RIGHT_TO_PARENT<br/>
	 * LOCATION_MIDDLE_CENTER_TO_DISPLAY<br/>
	 * LOCATION_MIDDLE_CENTER_TO_PARENT<br/>
	 * LOCATION_MIDDLE_LEFT_TO_DISPLAY<br/>
	 * LOCATION_MIDDLE_LEFT_TO_PARENT<br/>
	 * LOCATION_MIDDLE_RIGHT_TO_DISPLAY<br/>
	 * LOCATION_MIDDLE_RIGHT_TO_PARENT<br/>
	 * LOCATION_BOTTOM_CENTER_TO_DISPLAY<br/>
	 * LOCATION_BOTTOM_CENTER_TO_PARENT<br/>
	 * LOCATION_BOTTOM_LEFT_TO_DISPLAY<br/>
	 * LOCATION_BOTTOM_LEFT_TO_PARENT<br/>
	 * LOCATION_BOTTOM_RIGHT_TO_DISPLAY<br/>
	 * LOCATION_BOTTOM_RIGHT_TO_PARENT<br/>
	 * <br/>
	 * <b>Example:</b><br/>
	 * ShellManager.setLocation(some_shell, some_display, ShellManager.LOCATION_TOP_CENTER_TO_DISPLAY, 10, 8);
	 * <br/><br/>
	 */
	public static void setLocation(Shell shell, Display display, int location_constant, int x_offset, int y_offset){
		if(location_constant == LOCATION_BOTTOM_CENTER_TO_DISPLAY){
			Rectangle monitor_bounds = display.getPrimaryMonitor().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation((monitor_bounds.x + (monitor_bounds.width - shell_bounds.width) / 2) + x_offset, (monitor_bounds.height - shell_bounds.height) - y_offset);
		}else if(location_constant == LOCATION_BOTTOM_CENTER_TO_PARENT){
			Rectangle parent_bounds = shell.getParent().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation((parent_bounds.x + (parent_bounds.width - shell_bounds.width) / 2) + x_offset, (parent_bounds.y + (parent_bounds.height - shell_bounds.height)) - y_offset);
		}else if (location_constant == LOCATION_BOTTOM_LEFT_TO_DISPLAY){
			Rectangle monitor_bounds = display.getPrimaryMonitor().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation(x_offset, (monitor_bounds.height - shell_bounds.height) - y_offset);
		}else if(location_constant == LOCATION_BOTTOM_LEFT_TO_PARENT){
			Rectangle parent_bounds = shell.getParent().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation(parent_bounds.x + x_offset, (parent_bounds.y + (parent_bounds.height - shell_bounds.height)) - y_offset);
		}else if(location_constant == LOCATION_BOTTOM_RIGHT_TO_DISPLAY){
			Rectangle monitor_bounds = display.getPrimaryMonitor().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation(monitor_bounds.width - (shell_bounds.width + x_offset), (monitor_bounds.height - shell_bounds.height) - y_offset);
		}else if(location_constant == LOCATION_BOTTOM_RIGHT_TO_PARENT){
			Rectangle parent_bounds = shell.getParent().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation((parent_bounds.x + parent_bounds.width) - (shell_bounds.width + x_offset), (parent_bounds.y + (parent_bounds.height - shell_bounds.height)) - y_offset);
		}else if (location_constant == LOCATION_MIDDLE_CENTER_TO_DISPLAY){
			Rectangle monitor_bounds = display.getPrimaryMonitor().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation((monitor_bounds.x + (monitor_bounds.width - shell_bounds.width) / 2) + x_offset, (monitor_bounds.y + (monitor_bounds.height - shell_bounds.height) / 2) + y_offset);
		}else if(location_constant == LOCATION_MIDDLE_CENTER_TO_PARENT){
			Rectangle parent_bounds = shell.getParent().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation((parent_bounds.x + (parent_bounds.width - shell_bounds.width) / 2) + x_offset, (parent_bounds.y + (parent_bounds.height - shell_bounds.height) / 2) + y_offset);
		}else if(location_constant == LOCATION_MIDDLE_LEFT_TO_DISPLAY){
			Rectangle monitor_bounds = display.getPrimaryMonitor().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation(x_offset, (monitor_bounds.y + (monitor_bounds.height - shell_bounds.height) / 2) + y_offset);
		}else if(location_constant == LOCATION_MIDDLE_LEFT_TO_PARENT){
			Rectangle parent_bounds = shell.getParent().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation(parent_bounds.x + x_offset, (parent_bounds.y + (parent_bounds.height - shell_bounds.height) / 2) + y_offset);
		}else if (location_constant == LOCATION_MIDDLE_RIGHT_TO_DISPLAY){
			Rectangle monitor_bounds = display.getPrimaryMonitor().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation((monitor_bounds.width - shell_bounds.width) - x_offset, (monitor_bounds.y + (monitor_bounds.height - shell_bounds.height) / 2) + y_offset);
		}else if (location_constant == LOCATION_MIDDLE_RIGHT_TO_PARENT){
			Rectangle parent_bounds = shell.getParent().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation((parent_bounds.x + parent_bounds.width - shell_bounds.width) - x_offset, (parent_bounds.y + (parent_bounds.height - shell_bounds.height) / 2) + y_offset);
		}else if (location_constant == LOCATION_TOP_CENTER_TO_DISPLAY){
			Rectangle monitor_bounds = display.getPrimaryMonitor().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation((monitor_bounds.x + (monitor_bounds.width - shell_bounds.width) / 2) + x_offset, y_offset);
		}else if (location_constant == LOCATION_TOP_CENTER_TO_PARENT){
			Rectangle parent_bounds = shell.getParent().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation((parent_bounds.x + (parent_bounds.width - shell_bounds.width) / 2) + x_offset, parent_bounds.y + y_offset);
		}else if (location_constant == LOCATION_TOP_LEFT_TO_DISPLAY){
			shell.setLocation(x_offset, y_offset);
		}else if (location_constant == LOCATION_TOP_LEFT_TO_PARENT){
			Rectangle parent_bounds = shell.getParent().getBounds();
			shell.setLocation(parent_bounds.x + x_offset, parent_bounds.y + y_offset);
		}else if (location_constant == LOCATION_TOP_RIGHT_TO_DISPLAY){
			Rectangle monitor_bounds = display.getPrimaryMonitor().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation((monitor_bounds.width - shell_bounds.width) - x_offset, y_offset);
		}else if (location_constant == LOCATION_TOP_RIGHT_TO_PARENT){
			Rectangle parent_bounds = shell.getParent().getBounds();
			Rectangle shell_bounds = shell.getBounds();
			shell.setLocation((parent_bounds.x + parent_bounds.width - shell_bounds.width) - x_offset, parent_bounds.y + y_offset);
		}
		else{}
	}
	
	/**
	 * Return the X/Y coordinates of a control in relation to its parent or its display
	 * @param control : the control to locate (Control)
	 * @param from_parent : defines if the X/Y coordinates are related to the parent (boolean)
	 * @return the measured X/Y coordinates (Point)
	 */
	public static Point locate(Control control, boolean from_parent) {
		if(from_parent == true){
			return new Point(control.toDisplay(1,1).x - control.getParent().toDisplay(1,1).x, control.toDisplay(1,1).y - control.getParent().toDisplay(1,1).y);
		}else{
			return control.toDisplay(1, 1);
		}		
	}
	
	/**
	 * Return the X/Y coordinates of a control in reference to a given target control
	 * @param control : the control to locate (Control)
	 * @param refenece : the control to reference (Control)
	 * @return the measured X/Y coordinates (Point)
	 */
	public static Point locate(Control control, Control refenece) {
		return(new Point(control.toDisplay(1,1).x - refenece.toDisplay(1,1).x, control.toDisplay(1,1).y - refenece.toDisplay(1,1).y));
	}
	
	/**
	 * Convert a WindowManager alignment constant to an SWT int constant
	 * @param alignment
	 * @return
	 */
	public static int AlignmetToInt(HorizontalAlignments alignment) {
		if(alignment == HorizontalAlignments.LEFT) {
			return SWT.LEFT;
		}else if(alignment == HorizontalAlignments.CENTER) {
			return SWT.CENTER;
		}else if(alignment == HorizontalAlignments.RIGHT){
			return SWT.RIGHT;
		}else {
			return -1;
		}
	}
	
	/**
	 * Convert a WindowManager alignment constant to an SWT int constant
	 * @param alignment
	 * @return
	 */
	public static int AlignmetToInt(VerticalAlignments alignment) {
		if(alignment == VerticalAlignments.TOP) {
			return SWT.TOP;
		}else if(alignment == VerticalAlignments.MIDDLE) {
			return SWT.CENTER;
		}else if(alignment == VerticalAlignments.BOTTOM){
			return SWT.BOTTOM;
		}else {
			return -1;
		}
	}
	
	/**
	 * Convert a WindowManager alignment constant to an SWT int constant
	 * @param alignment
	 * @return
	 */
	public static int AlignmetToInt(Alignments alignment) {
		if(alignment == Alignments.TOP) {
			return SWT.TOP;
		}else if(alignment == Alignments.MIDDLE) {
			return SWT.CENTER;
		}else if(alignment == Alignments.BOTTOM){
			return SWT.RIGHT;
		}else if(alignment == Alignments.TOP) {
			return SWT.TOP;
		}else if(alignment == Alignments.MIDDLE) {
			return SWT.CENTER;
		}else if(alignment == Alignments.BOTTOM){
			return SWT.BOTTOM;
		}else {
			return -1;
		}
	}
}
