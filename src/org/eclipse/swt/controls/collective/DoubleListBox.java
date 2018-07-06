package org.eclipse.swt.controls.collective;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.environment.WindowManager;
import org.eclipse.swt.environment.WindowManager.HorizontalAlignments;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DoubleListBox extends Canvas {
	private Label lblLeft;
	private Label lblRight;
	private List lstLeft;
	private List lstRight;
	private Composite header_right;
	private CLabel sp1;
	private CLabel sp2;
	private CLabel sp3;
	private Composite header_left;
	
	private String textLeft = "Left";
	private String textRight = "Right";
	private HorizontalAlignments textLeftAlignment = HorizontalAlignments.LEFT;
	private HorizontalAlignments textRightAlignment = HorizontalAlignments.RIGHT;
	private Color background;
	private Color foreground;
	private Color listBackground = new Color(null, 255,255,255);
	private Color listForeground = new Color(null, 33,33,33);
	private Font textFont = Display.getDefault().getSystemFont();
	private Font listFont = Display.getDefault().getSystemFont();
	
	private String[] itemsLeft = new String[] {};
	private String[] itemsRight = new String[] {};

	public DoubleListBox(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(3, false);
		background = super.getBackground();
		foreground = super.getForeground();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		header_left = new Composite(this, SWT.NONE);
		header_left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		header_left.setLayout(new GridLayout(1, false));
		
		lblLeft = new Label(header_left, SWT.NONE);
		lblLeft.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblLeft.setBackground(new Color(null, 0,0,0,0));
		
		sp1 = new CLabel(this, SWT.NONE);
		sp1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2));
		sp1.setText("");
		
		header_right = new Composite(this, SWT.NONE);
		header_right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		header_right.setLayout(new GridLayout(1, false));
		
		lblRight = new Label(header_right, SWT.NONE);
		lblRight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblRight.setBackground(new Color(null, 0,0,0,0));
		lblRight.setText("");
		
		lstLeft = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		lstLeft.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 7));
		
		lstRight = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		lstRight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 7));
		
		Button btnAddRight = new Button(this, SWT.NONE);
		btnAddRight.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				moveItemsRight(getSelectionIdecesLeft());
			}
		});
		btnAddRight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddRight.setText(">");
		
		Button btnAddAllRight = new Button(this, SWT.NONE);
		btnAddAllRight.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				moveAllItemsRight();
			}
		});
		btnAddAllRight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddAllRight.setText(">>");
		
		sp2 = new CLabel(this, SWT.NONE);
		sp2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		sp2.setText("");
		
		Button btnAddAllLeft = new Button(this, SWT.NONE);
		btnAddAllLeft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				moveAllItemsLeft();
			}
		});
		btnAddAllLeft.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddAllLeft.setText("<<");
		
		Button btnAddLeft = new Button(this, SWT.NONE);
		btnAddLeft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				moveItemsLeft(getSelectionIdecesLeft());
			}
		});
		btnAddLeft.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddLeft.setText("<");
		
		sp3 = new CLabel(this, SWT.NONE);
		sp3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		sp3.setText("");
		
		render();
	}
	
	private void render() {
		super.setBackground(this.background);
		header_left.setBackground(this.background);
		header_right.setBackground(this.background);
		sp1.setBackground(this.background);
		sp2.setBackground(this.background);
		sp3.setBackground(this.background);
		lstLeft.setBackground(this.listBackground);
		lstRight.setBackground(this.listBackground);
		
		lblLeft.setForeground(this.foreground);
		lblRight.setForeground(this.foreground);
		lstLeft.setForeground(this.listForeground);
		lstRight.setForeground(this.listForeground);
		
		lblLeft.setText(this.textLeft);
		lblRight.setText(this.textRight);
		
		lblLeft.setAlignment(WindowManager.AlignmetToInt(this.textLeftAlignment));
		lblRight.setAlignment(WindowManager.AlignmetToInt(this.textRightAlignment));
		
		lblLeft.setFont(this.textFont);
		lblRight.setFont(this.textFont);
		lstLeft.setFont(this.listFont);
		lstRight.setFont(this.listFont);
		
		setItemsLeft(this.itemsLeft);
	}
	
	public void addItemLeft(String item) {
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(this.itemsLeft));
		list.add(item);
		this.itemsLeft = list.toArray(new String[this.itemsLeft.length]);
		setItemsLeft(this.itemsLeft);
	}
	
	public void addItemLeft(int index, String item) {
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(this.itemsLeft));
		list.add(index, item);
		this.itemsLeft = list.toArray(new String[this.itemsLeft.length]);
		setItemsLeft(this.itemsLeft);
	}
	
	public void setItemsLeft(String[] items) {
		if(items.length > 0) {
			this.itemsLeft = items;
			this.lstLeft.removeAll();
			for(String item : items) {
				this.lstLeft.add(item);
			}
		}
	}
	
	public void removeItemLeft(int index) {
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(this.itemsLeft));
		list.remove(index);
		this.itemsLeft = list.toArray(new String[list.size()]);
		setItemsLeft(this.itemsLeft);
	}
	
	public void removeItemsLeft(int[] indeces) {
		for (int index : indeces) {
			if(index < this.itemsLeft.length) {
				removeItemLeft(index);
			}
		}
	}
	
	public void removeItemsLeft(String[] values) {
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(this.itemsLeft));
		for(int i = 0; i < list.size(); i++) {
			if(Arrays.asList(values).contains(list.get(i))) {
				list.remove(i);
			}
		}
		this.itemsLeft = list.toArray(new String[this.itemsLeft.length]);
		setItemsLeft(this.itemsLeft);
	}
	
	public void removeItemLeft(String value) {
		removeItemsLeft(new String[] {value});
	}
	
	public void removeAllItemsLeft() {
		this.itemsLeft = new String[] {};
		this.lstLeft.removeAll();
	}
	
	public String[] getItemsLeft() {
		return this.itemsLeft;
	}
	
	public void addItemRight(String item) {
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(this.itemsRight));
		list.add(item);
		this.itemsRight = list.toArray(new String[this.itemsRight.length]);
		setItemsRight(this.itemsRight);
	}
	
	public void addItemRight(int index, String item) {
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(this.itemsRight));
		list.add(index, item);
		this.itemsRight = list.toArray(new String[itemsRight.length]);
		setItemsRight(this.itemsRight);
	}
	
	public void setItemsRight(String[] items) {
		if(items.length > 0) {
			this.itemsRight = items;
			this.lstRight.removeAll();
			for(String item : items) {
				this.lstRight.add(item);
			}
		}
	}
	
	public String[] getItemsRight() {
		return this.itemsRight;
	}
	
	public void removeItemRight(int index) {
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(this.itemsRight));
		list.remove(index);
		this.itemsRight = list.toArray(new String[list.size()]);
		setItemsRight(this.itemsRight);
	}
	
	public void removeItemsRight(int[] indeces) {
		for (int index : indeces) {
			if(index < this.itemsRight.length) {
				removeItemRight(index);
			}
		}
	}
	
	public void removeItemsRight(String[] values) {
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(this.itemsRight));
		for(int i = 0; i < list.size(); i++) {
			if(Arrays.asList(values).contains(list.get(i))) {
				list.remove(i);
			}
		}
		this.itemsRight = list.toArray(new String[this.itemsRight.length]);
		setItemsRight(this.itemsRight);
	}
	
	public void removeItemRight(String value) {
		removeItemsRight(new String[] {value});
	}
	
	public void removeAllItemsRight() {
		this.itemsRight = new String[] {};
		this.lstRight.removeAll();
	}

	public void moveItemsRight(int[] indeces) {
		for(int index : indeces) {
			if(index < this.itemsLeft.length) {
				removeItemRight(getItemLeft(index));
				addItemRight(getItemLeft(index));
				removeItemLeft(index);
			}
		}
	}
	
	public void moveItemRight(int index) {
		moveItemsRight(new int[] {index});
	}
	
	public void moveAllItemsRight() {
		for(int i= 0; i < this.itemsLeft.length; i++) {
			removeItemRight(getItemLeft(i));
			addItemRight(getItemLeft(i));
			removeItemLeft(i);
		}
	}
	
	public void moveItemsLeft(int[] indeces) {
		for(int index : indeces) {
			if(index < this.itemsRight.length) {
				removeItemLeft(getItemRight(index));
				addItemLeft(getItemRight(index));
				removeItemRight(index);
			}
		}
	}
	
	public void moveItemLeft(int index) {
		moveItemsLeft(new int[] {index});
	}
	
	public void moveAllItemsLeft() {
		for(int i= 0; i < this.itemsRight.length; i++) {
			removeItemLeft(getItemRight(i));
			addItemLeft(getItemRight(i));
			removeItemRight(i);
		}
	}
	
	public String getItemLeft(int index) {
		if(index < this.itemsLeft.length) {
			return this.itemsLeft[index];
		}else {
			return null;
		}		
	}
	
	public String[] getItemsLeft(int[] indeces) {
		ArrayList<String> list = new ArrayList<String>();
		for(int index : indeces) {
			if(getItemLeft(index) != null) {
				list.add(getItemLeft(index));
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	public int[] getSelectionIdecesLeft() {
		return this.lstLeft.getSelectionIndices();
	}
	
	public String[] getSelectionLeft() {
		return getItemsLeft(getSelectionIdecesLeft());
	}
	
	public String getItemRight(int index) {
		if(index < this.itemsRight.length) {
			return this.itemsRight[index];
		}else {
			return null;
		}		
	}
	
	public String[] getItemsRight(int[] indeces) {
		ArrayList<String> list = new ArrayList<String>();
		for(int index : indeces) {
			if(getItemRight(index) != null) {
				list.add(getItemRight(index));
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	public int[] getSelectionIdecesRight() {
		return this.lstRight.getSelectionIndices();
	}
	
	public String[] getSelectionRight() {
		return getItemsRight(getSelectionIdecesRight());
	}
	
	public String getTextLeft() {
		return textLeft;
	}

	public void setTextLeft(String textLeft) {
		this.textLeft = textLeft;
		render();
	}

	public String getTextRight() {
		return textRight;
	}

	public void setTextRight(String textRight) {
		this.textRight = textRight;
		render();
	}

	public HorizontalAlignments getTextLeftAlignment() {
		return textLeftAlignment;
	}

	public void setTextLeftAlignment(HorizontalAlignments textLeftAlignment) {
		this.textLeftAlignment = textLeftAlignment;
		render();
	}

	public HorizontalAlignments getTextRightAlignment() {
		return textRightAlignment;
	}

	public void setTextRightAlignment(HorizontalAlignments textRightAlignment) {
		this.textRightAlignment = textRightAlignment;
		render();
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
		render();
	}

	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
		render();
	}

	public Color getListBackground() {
		return listBackground;
	}

	public void setListBackground(Color listBackground) {
		this.listBackground = listBackground;
		render();
	}

	public Color getListForeground() {
		return listForeground;
	}

	public void setListForeground(Color listForeground) {
		this.listForeground = listForeground;
		render();
	}

	public Font getTextFont() {
		return textFont;
	}

	public void setTextFont(Font textFont) {
		this.textFont = textFont;
		render();
	}

	public Font getListFont() {
		return listFont;
	}

	public void setListFont(Font listFont) {
		this.listFont = listFont;
		render();
	}
	
	
	
}
