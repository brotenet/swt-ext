package org.eclipse.swt.containers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

public class ExpandingComposite extends Canvas {
	private Composite top;
	private Shell null_shell = new Shell();
	private Composite container_top;
	private Composite container_center;
	private Composite container_right;
	private Composite container_bottom;
	private Composite content_top;
	private Composite center;
	private CLabel btn_top;
	private Composite container_left;
	private CLabel btn_left;
	private Composite content_right;
	private Composite right;
	private CLabel btn_right;
	private Composite content_bottom;
	private Composite bottom;
	private CLabel btn_bottom;
	private Composite content_left;
	private Composite left;
	private CLabel btn_right_top;
	private CLabel btn_right_bottom;
	private CLabel btn_left_top;
	private CLabel btn_left_bottom;
	
	
	public ExpandingComposite(Composite parent, int style) {
		super(parent, style);
		populate();
	}
	
	public ExpandingComposite(Shell parent, int style) {
		super(parent, style);
		populate();
	}
	
	private void populate() {
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		container_top = new Composite(this, SWT.NONE);
		GridLayout gl_container_top = new GridLayout(1, false);
		gl_container_top.verticalSpacing = 0;
		gl_container_top.horizontalSpacing = 0;
		gl_container_top.marginHeight = 0;
		gl_container_top.marginWidth = 0;
		container_top.setLayout(gl_container_top);
		container_top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		
		content_top = new Composite(container_top, SWT.NONE);
		content_top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_content_top = new GridLayout(1, false);
		gl_content_top.verticalSpacing = 0;
		gl_content_top.horizontalSpacing = 0;
		gl_content_top.marginHeight = 0;
		gl_content_top.marginWidth = 0;
		content_top.setLayout(gl_content_top);
		
		top = new Composite(content_top, SWT.NONE);
		top.setLayout(new FillLayout(SWT.HORIZONTAL));
		top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		top.setBounds(0, 0, 64, 64);
		
		btn_top = new CLabel(container_top, SWT.CENTER);
		btn_top.setBottomMargin(0);
		btn_top.setLeftMargin(0);
		btn_top.setRightMargin(0);
		btn_top.setTopMargin(0);
		btn_top.setImage(new Image(null, ExpandingComposite.class.getResourceAsStream("/org/eclipse/swt/containers/up.png")));
		btn_top.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent event) {
				setTopOpen(!isTopOpen());
			}

		});
		
		btn_top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btn_top.setBounds(0, 0, 81, 27);
		btn_top.setText("");
		
		container_left = new Composite(this, SWT.NONE);
		GridLayout gl_container_left = new GridLayout(3, false);
		gl_container_left.verticalSpacing = 0;
		gl_container_left.horizontalSpacing = 0;
		gl_container_left.marginHeight = 0;
		gl_container_left.marginWidth = 0;
		container_left.setLayout(gl_container_left);
		container_left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		new Label(container_left, SWT.NONE);
		
		content_left = new Composite(container_left, SWT.NONE);
		GridLayout gl_content_left = new GridLayout(1, false);
		gl_content_left.verticalSpacing = 0;
		gl_content_left.horizontalSpacing = 0;
		gl_content_left.marginHeight = 0;
		gl_content_left.marginWidth = 0;
		content_left.setLayout(gl_content_left);
		content_left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		
		left = new Composite(content_left, SWT.NONE);
		left.setLayout(new FillLayout(SWT.HORIZONTAL));
		left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		left.setBounds(0, 0, 64, 64);
		
		btn_left_top = new CLabel(container_left, SWT.NONE);
		btn_left_top.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				setLeftOpen(!isLeftOpen());
			}
		});
		btn_left_top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		btn_left_top.setText("");
		new Label(container_left, SWT.NONE);
		
		btn_left = new CLabel(container_left, SWT.HORIZONTAL | SWT.CENTER);
		btn_left.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				setLeftOpen(!isLeftOpen());
			}
		});
		btn_left.setImage(new Image(null, ExpandingComposite.class.getResourceAsStream("/org/eclipse/swt/containers/left.png")));
		btn_left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		new Label(container_left, SWT.NONE);
		
		btn_left_bottom = new CLabel(container_left, SWT.NONE);
		btn_left_bottom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				setLeftOpen(!isLeftOpen());
			}
		});
		btn_left_bottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		btn_left_bottom.setText("");
		
		container_center = new Composite(this, SWT.NONE);
		container_center.setLayout(new FillLayout(SWT.HORIZONTAL));
		container_center.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		center = new Composite(container_center, SWT.NONE);
		center.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		container_right = new Composite(this, SWT.NONE);
		GridLayout gl_container_right = new GridLayout(3, false);
		gl_container_right.verticalSpacing = 0;
		gl_container_right.horizontalSpacing = 0;
		gl_container_right.marginHeight = 0;
		gl_container_right.marginWidth = 0;
		container_right.setLayout(gl_container_right);
		container_right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		btn_right_top = new CLabel(container_right, SWT.NONE);
		btn_right_top.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				setRightOpen(!isRightOpen());
			}
		});
		btn_right_top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		btn_right_top.setText("");
		new Label(container_right, SWT.NONE);
		
		content_right = new Composite(container_right, SWT.NONE);
		GridLayout gl_content_right = new GridLayout(2, false);
		gl_content_right.verticalSpacing = 0;
		gl_content_right.horizontalSpacing = 0;
		gl_content_right.marginHeight = 0;
		gl_content_right.marginWidth = 0;
		content_right.setLayout(gl_content_right);
		content_right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		
		right = new Composite(content_right, SWT.NONE);
		right.setLayout(new FillLayout(SWT.HORIZONTAL));
		right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		right.setBounds(0, 0, 64, 64);
		new Label(content_right, SWT.NONE);
		
		btn_right = new CLabel(container_right, SWT.HORIZONTAL | SWT.CENTER);
		btn_right.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				setRightOpen(!isRightOpen());
			}
		});
		btn_right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btn_right.setImage(new Image(null, ExpandingComposite.class.getResourceAsStream("/org/eclipse/swt/containers/right.png")));
		new Label(container_right, SWT.NONE);
		
		btn_right_bottom = new CLabel(container_right, SWT.NONE);
		btn_right_bottom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				setRightOpen(!isRightOpen());
			}
		});
		btn_right_bottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		btn_right_bottom.setText("");
		new Label(container_right, SWT.NONE);
		
		container_bottom = new Composite(this, SWT.NONE);
		GridLayout gl_container_bottom = new GridLayout(1, false);
		gl_container_bottom.verticalSpacing = 0;
		gl_container_bottom.horizontalSpacing = 0;
		gl_container_bottom.marginHeight = 0;
		gl_container_bottom.marginWidth = 0;
		container_bottom.setLayout(gl_container_bottom);
		container_bottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		
		btn_bottom = new CLabel(container_bottom, SWT.CENTER);
		btn_bottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		btn_bottom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				setBottomOpen(!isBottomOpen());
			}
		});
		btn_bottom.setSize(16, 17);
		btn_bottom.setImage(new Image(null, ExpandingComposite.class.getResourceAsStream("/org/eclipse/swt/containers/down.png")));
		
		content_bottom = new Composite(container_bottom, SWT.NONE);
		GridLayout gl_content_bottom = new GridLayout(1, false);
		gl_content_bottom.verticalSpacing = 0;
		gl_content_bottom.horizontalSpacing = 0;
		gl_content_bottom.marginHeight = 0;
		gl_content_bottom.marginWidth = 0;
		content_bottom.setLayout(gl_content_bottom);
		content_bottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		bottom = new Composite(content_bottom, SWT.NONE);
		bottom.setLayout(new FillLayout(SWT.HORIZONTAL));
		bottom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		setLeftOpen(false);
		setRightOpen(false);
		setTopOpen(false);
		setBottomOpen(false);
		setLeftButtonVisible(true);
		setRightButtonVisible(true);
		setTopButtonVisible(true);
		setBottomButtonVisible(true);
	}
	
	@Override
	public void setBackground(Color background) {
		super.setBackground(background);
		top.setBackground(background);
		container_top.setBackground(background);
		container_center.setBackground(background);
		container_right.setBackground(background);
		container_bottom.setBackground(background);
		content_top.setBackground(background);
		center.setBackground(background);
		btn_top.setBackground(background);
		container_left.setBackground(background);
		btn_left.setBackground(background);
		content_right.setBackground(background);
		right.setBackground(background);
		btn_right.setBackground(background);
		content_bottom.setBackground(background);
		bottom.setBackground(background);
		btn_bottom.setBackground(background);
		content_left.setBackground(background);
		left.setBackground(background);
		btn_right_top.setBackground(background);
		btn_right_bottom.setBackground(background);
		btn_left_top.setBackground(background);
		btn_left_bottom.setBackground(background);
	}
	
	public void rebuild() {
		this.layout(true, true);
	}
	
	public Composite getCenter() {
		return center;
	}

	public void setCenter(Composite center) {
		this.center = center;
	}

	public Composite getTop() {
		return top;
	}

	public void setTop(Composite top) {
		this.top = top;
	}
	
	public void setTopButtonVisible(Boolean visible) {
		if(visible == true) {
			btn_top.setParent(container_top);
		}else {
			btn_top.setParent(null_shell);
		}
		rebuild();
	}
	
	public Boolean isTopButtonVisible() {
		return (btn_top.getParent() == container_top);
	}

	public Composite getLeft() {
		return left;
	}

	public void setLeft(Composite left) {
		this.left = left;
	}

	public void setLeftButtonVisible(Boolean visible) {
		if(visible == true) {
			btn_left_top.setParent(container_left);
			btn_left.setParent(container_left);
			btn_left_bottom.setParent(container_left);
		}else {
			btn_left_top.setParent(null_shell);
			btn_left.setParent(null_shell);
			btn_left_bottom.setParent(null_shell);
		}
		rebuild();
	}
	
	public Boolean isButtonLeftVisible() {
		return (btn_left.getParent() == container_left);
	}
	
	public Composite getRight() {
		return right;
	}

	public void setRight(Composite right) {
		this.right = right;
	}
	
	public void setRightButtonVisible(Boolean visible) {
		if(visible == true) {
			btn_right_top.setParent(container_right);
			btn_right.setParent(container_right);
			btn_right_bottom.setParent(container_right);
		}else {
			btn_right_top.setParent(null_shell);
			btn_right.setParent(null_shell);
			btn_right_bottom.setParent(null_shell);
		}
		rebuild();
	}
	
	public Boolean isButtonRightVisible() {
		return (btn_right.getParent() == container_right);
	}

	public Composite getBottom() {
		return bottom;
	}

	public void setBottom(Composite bottom) {
		this.bottom = bottom;
	}
	
	public void setBottomButtonVisible(Boolean visible) {
		if(visible == true) {
			btn_bottom.setParent(container_bottom);
		}else {
			btn_bottom.setParent(null_shell);
		}
		rebuild();
	}
	
	public Boolean isButtonButtonVisible() {
		return (btn_bottom.getParent() == container_bottom);
	}

	public void setTopOpen(boolean open) {
		if(open == true) {
			top.setParent(content_top);
			btn_top.setImage(new Image(null, ExpandingComposite.class.getResourceAsStream("/org/eclipse/swt/containers/up.png")));
		}else {
			top.setParent(null_shell);
			btn_top.setImage(new Image(null, ExpandingComposite.class.getResourceAsStream("/org/eclipse/swt/containers/down.png")));
		}
		rebuild();
	}

	public boolean isTopOpen() {
		return (getTop().getParent() == content_top);
	}
	
	public void setLeftOpen(boolean open) {
		if(open == true) {
			left.setParent(content_left);
			btn_left.setImage(new Image(null, ExpandingComposite.class.getResourceAsStream("/org/eclipse/swt/containers/left.png")));
		}else {
			left.setParent(null_shell);
			btn_left.setImage(new Image(null, ExpandingComposite.class.getResourceAsStream("/org/eclipse/swt/containers/right.png")));
		}
		rebuild();
	}
	
	public boolean isLeftOpen() {
		return (getLeft().getParent() == content_left);
	}
	
	public void setRightOpen(boolean open) {
		if(open == true) {
			right.setParent(content_right);
			btn_right.setImage(new Image(null, ExpandingComposite.class.getResourceAsStream("/org/eclipse/swt/containers/right.png")));
		}else {
			right.setParent(null_shell);
			btn_right.setImage(new Image(null, ExpandingComposite.class.getResourceAsStream("/org/eclipse/swt/containers/left.png")));
		}
		rebuild();
	}
	
	public boolean isRightOpen() {
		return (getRight().getParent() == content_right);
	}
	
	public void setBottomOpen(boolean open) {
		if(open == true) {
			bottom.setParent(content_bottom);
			btn_bottom.setImage(new Image(null, ExpandingComposite.class.getResourceAsStream("/org/eclipse/swt/containers/down.png")));
		}else {
			bottom.setParent(null_shell);
			btn_bottom.setImage(new Image(null, ExpandingComposite.class.getResourceAsStream("/org/eclipse/swt/containers/up.png")));
		}
		rebuild();
	}
	
	public boolean isBottomOpen() {
		return (getBottom().getParent() == content_bottom);
	}

	
}
