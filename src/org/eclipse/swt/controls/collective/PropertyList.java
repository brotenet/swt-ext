package org.eclipse.swt.controls.collective;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class PropertyList extends Canvas {
	
	private ArrayList<HashMap<String, Object>> components = new ArrayList<>();
	private HashMap<String, Object> controls_memory = new HashMap<String, Object>();
	private HashMap<String, Object> labels_memory = new HashMap<String, Object>();
	
	public static String TYPE_TITLE = "TYPE_TITLE";
	public static String TYPE_SEPARATOR = "TYPE_SEPARATOR";
	public static String TYPE_NOTE = "TYPE_NOTE";
	public static String TYPE_TEXT = "TYPE_TEXT";
	public static String TYPE_HIDDEN_TEXT = "TYPE_HIDDEN_TEXT";
	public static String TYPE_SELECT = "TYPE_SELECT";
	public static String TYPE_CHECK = "TYPE_CHECK";
	public static String TYPE_NUMERIC = "TYPE_NUMERIC";
	public static String TYPE_DATE = "TYPE_DATE";
	public static String TYPE_TIME = "TYPE_TIME";
	
	private Composite cmp_root;
	private ScrolledComposite scroll_root;
	private Image action_image = null;
	
	public PropertyList(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		scroll_root = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		scroll_root.setExpandHorizontal(true);
		scroll_root.setExpandVertical(true);
		depopulate();
	}

	private static boolean checkControType(String source_type, String reference_type) {
		return (source_type.trim().equalsIgnoreCase(reference_type.trim()) || source_type.trim().equalsIgnoreCase("TYPE_" + reference_type.trim()));
	}
	
	/**
	 * Depopulate property list and remove all previously added inputs
	 */
	public void depopulate() {
		cmp_root = new Composite(scroll_root, SWT.NONE);
		GridLayout gl_cmp_root = new GridLayout(1, false);
		gl_cmp_root.marginBottom = 10;
		gl_cmp_root.marginRight = 10;
		gl_cmp_root.marginTop = 10;
		gl_cmp_root.marginLeft = 10;
		cmp_root.setLayout(gl_cmp_root);
		
		CLabel lblNewLabel = new CLabel(cmp_root, SWT.BORDER);
		lblNewLabel.setImage(new Image(null, PropertyList.class.getResourceAsStream("/org/eclipse/swt/controls/collective/PropertyList.Action.png")));
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblNewLabel.setText("");
		scroll_root.setContent(cmp_root);
		cmp_root.layout(true);
		scroll_root.setMinSize(cmp_root.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		redraw();
	}
	
	/**
	 * Populate property list with added inputs
	 */
	@SuppressWarnings("unchecked")
	public void populate() {
		cmp_root = new Composite(scroll_root, SWT.NONE);
		cmp_root.setBackground(super.getBackground());
		GridLayout gl_cmp_root = new GridLayout(3, false);
		gl_cmp_root.marginBottom = 10;
		gl_cmp_root.marginRight = 10;
		gl_cmp_root.marginTop = 10;
		gl_cmp_root.marginLeft = 10;
		cmp_root.setLayout(gl_cmp_root);
		scroll_root.setContent(cmp_root);

		for(Object data_object : components.toArray()) {
			HashMap<String, Object> data = (HashMap<String, Object>) data_object;
			if(checkControType((String) data.get("type"), TYPE_TITLE)) {
				buildTittle(data);
			}else if(checkControType((String) data.get("type"), TYPE_SEPARATOR)) {
				buildSeparator(data);
			}else if(checkControType((String) data.get("type"), TYPE_NOTE)) {
				buildNote(data);
			}else if(checkControType((String) data.get("type"), TYPE_CHECK)) {
				buildCheck(data);
			}else if(checkControType((String) data.get("type"), TYPE_TEXT)) {
				buildText(data);
			}else if(checkControType((String) data.get("type"), TYPE_HIDDEN_TEXT)) {
				buildHiddenText(data);
			}else if(checkControType((String) data.get("type"), TYPE_NUMERIC)) {
				buildNumeric(data);
			}else if(checkControType((String) data.get("type"), TYPE_DATE)) {
				buildDate(data);
			}else if(checkControType((String) data.get("type"), TYPE_TIME)) {
				buildTime(data);
			}else if(checkControType((String) data.get("type"), TYPE_SELECT)) {
				buildSelect(data);
			}
		}		
		cmp_root.layout(true);
		scroll_root.setMinSize(cmp_root.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		redraw();
	}
	
	/**
	 * Set the icon image of action buttons
	 * @return
	 */
	public Image getActionImage() {
		return action_image;
	}

	/**
	 * Get the icon image of action buttons
	 * @param actionImage
	 */
	public void setActionImage(Image actionImage) {
		action_image = actionImage;
	}

	/**
	 * Add a listener for when a action button is selected<br>
	 * The event data are returned as a HashMap with the following base keys:<br>
	 * - id: The input id as a String<br>
	 * - type: The input type as a String<br>
	 * - value: The input value as an Object<br>
	 * @param listener
	 */
	public void addActionSelectListener (Listener listener) {
		checkWidget ();
		addListener (SWT.Selection,listener);
	}
	
	private GridData $LAYOUT_DATA(int span, boolean expand, int alignment) {
		return new GridData(alignment, SWT.CENTER, expand, false, span, 1);
	}
	
	private GridData $LAYOUT_DATA(int span) {
		return $LAYOUT_DATA(span, true, SWT.FILL);
	}
	
	private Object $CTL(HashMap<String, Object> data) {
		return controls_memory.get((String) data.get("id"));
	}
	
	private Object $CTL(String id) {
		return controls_memory.get(id);
	}
	
	private Object $LBL(HashMap<String, Object> data) {
		return labels_memory.get((String) data.get("id"));
	}
	
	private Object $LBL(String id) {
		return labels_memory.get(id);
	}
	
	private Object $PROP(HashMap<String, Object> data, String property) {
		return data.get(property);
	}
	
	/**
	 * Get value of input for a given id as an int
	 * @param id
	 * @return
	 */
	public int getInt(String id) {
		return (int) get(id);
	}
	
	/**
	 * Get value of input for a given id as a boolean
	 * @param id
	 * @return
	 */
	public boolean getBoolean(String id) {
		return (boolean) get(id);
	}
	
	/**
	 * Get value of input for a given id as a String
	 * @param id
	 * @return
	 */
	public String getString(String id) {
		return String.valueOf(get(id));
	}
	
	/**
	 * Get value of input for a given id as a Calendar date
	 * @param id
	 * @return
	 */
	public Calendar getDate(String id) {
		int[] value =  (int[]) get(id);
		Calendar output = new GregorianCalendar(value[0], value[1], value[2], 0, 0 ,0);
		return output;
	}
	
	/**
	 * Get value of input for a given id as a formated Calendar date String
	 * @param id
	 * @return
	 */
	public String getDate(String id, String format) {
		if(format != null) {
			SimpleDateFormat date_format = new SimpleDateFormat(format);
			return date_format.format(getDate(id));
		}else {
			SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MMM-dd");
			return date_format.format(getDate(id).getTime());
		}		
	}
	
	/**
	 * Get value of input for a given id as a Calendar time
	 * @param id
	 * @return
	 */
	public Calendar getTime(String id) {
		int[] value =  (int[]) get(id);
		Calendar output = new GregorianCalendar(1, 1, 1, value[0], value[1], value[2]);
		return output;
	}
	
	/**
	 * Get value of input for a given id as a formated Calendar time String
	 * @param id
	 * @return
	 */
	public String getTime(String id, String format) {
		if(format != null) {
			SimpleDateFormat date_format = new SimpleDateFormat(format);
			return date_format.format(getTime(id));
		}else {
			SimpleDateFormat date_format = new SimpleDateFormat("HH:mm:ss");
			return date_format.format(getTime(id).getTime());
		}
	}
	
	/**
	 * Get value of input for a given id as Object
	 * @param id
	 * @return
	 */
	public Object get(String id) {
		if($CTL(id).getClass() == Button.class) {
			return ((Button) $CTL(id)).getSelection();
		}else if($CTL(id).getClass() == Text.class){
			return ((Text) $CTL(id)).getText();
		}else if($CTL(id).getClass() == Spinner.class) {
			return ((Spinner) $CTL(id)).getSelection();
		}else if($CTL(id).getClass() == DateTime.class) {
			if(((DateTime) $CTL(id)).getData("type") == TYPE_DATE) {
				return new int[] {((DateTime) $CTL(id)).getYear(), ((DateTime) $CTL(id)).getMonth(), ((DateTime) $CTL(id)).getDay()};
			}else {
				return new int[] {((DateTime) $CTL(id)).getHours(), ((DateTime) $CTL(id)).getMinutes(), ((DateTime) $CTL(id)).getSeconds()};
			}
		}else if ($CTL(id).getClass() == CCombo.class) {
			return ((CCombo) $CTL(id)).getText();
		}else {
			return null;
		}
	}
	
	/**
	 * Get all ID keys for inputs added
	 * @return
	 */
	public String[] keySet() {
		return controls_memory.keySet().toArray(new String[controls_memory.keySet().size()]);
	}
	
	/**
	 * Get all input values as key-value map
	 * @return
	 */
	public HashMap<String, Object> values(){
		HashMap<String, Object> output = new HashMap<String, Object>();
		for (String key : keySet()) {
			output.put(key, get(key));
		}
		return output;
	}

	private void buildTittle(HashMap<String, Object> data) {
		labels_memory.put((String) data.get("id"), new CLabel(cmp_root, SWT.NONE));
		((CLabel) $LBL(data)).setLayoutData($LAYOUT_DATA(3));
		((CLabel) $LBL(data)).setText((String) $PROP(data, "value"));
		if($PROP(data, "background_color") != null) {
			((CLabel) $LBL(data)).setBackground((Color) $PROP(data, "background_color"));
		}else {
			((CLabel) $LBL(data)).setBackground(super.getBackground());
		}
		if($PROP(data, "foreground_color") != null) {
			((CLabel) $LBL(data)).setForeground((Color) $PROP(data, "foreground_color"));
		}
		if(((int) $PROP(data, "font_height")) > 0) {
			FontData font_data[] = ((CLabel) $LBL(data)).getFont().getFontData();
			font_data[0].setHeight((int) $PROP(data, "font_height"));
			((CLabel) $LBL(data)).setFont(new Font(null, font_data));
		}
	}
	
	/**
	 * Add a (non-input) title to the property list
	 * @param value
	 */
	public void addTittle(String value) {
		addTittle(value, null, null, -1);
	}
	
	/**
	 * Add a (non-input) title to the property list
	 * @param value
	 * @param background_color
	 * @param foreground_color
	 * @param font_height
	 */
	public void addTittle(String value, Color background_color, Color foreground_color, int font_height) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("type", TYPE_TITLE);
		data.put("id", UUID.randomUUID().toString());
		data.put("value", value);
		data.put("foreground_color", foreground_color);
		data.put("background_color", background_color);
		data.put("font_height", font_height);
		components.add(data);
	}
	
	private void buildSeparator(HashMap<String, Object> data) {
		labels_memory.put((String) data.get("id"), new Label(cmp_root, SWT.SEPARATOR | SWT.HORIZONTAL));
		((Label) $LBL(data)).setLayoutData($LAYOUT_DATA(3));
		((Label) $LBL(data)).setBackground(super.getBackground());
	}
	
	/**
	 * Add a (non-input) separator to the property list
	 */
	public void addSeparator() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("type", TYPE_SEPARATOR);
		data.put("id", UUID.randomUUID().toString());
		components.add(data);
	}
	
	private void buildNote(HashMap<String, Object> data) {
		labels_memory.put((String) data.get("id"), new Browser(cmp_root, SWT.BORDER));
		((Browser) $LBL(data)).setLayoutData($LAYOUT_DATA(3));
		((Browser) $LBL(data)).setText((String) $PROP(data, "value"));
		((GridData) ((Browser) $LBL(data)).getLayoutData()).heightHint = ((int) $PROP(data, "height"));
	}
	
	/**
	 * Add a (non-input) notes text box to the property list
	 * @param value
	 */
	public void addNote(String value) {
		addNote(value, 70, 8);
	}
	
	/**
	 * Add a (non-input) notes text box to the property list
	 * @param value
	 * @param height
	 * @param font_scale
	 */
	public void addNote(String value, int height, int font_scale) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("type", TYPE_NOTE);
		data.put("id", UUID.randomUUID().toString());
		if(font_scale < 1) {
			font_scale = 2;
		}
		data.put("value", "<body><font size=\"" + font_scale + "\">" + value + "</font></body>");
		data.put("height", height);
		components.add(data);
	}
	
	private void buildCheck(HashMap<String, Object> data) {
		labels_memory.put((String) data.get("id"), new CLabel(cmp_root, SWT.NONE));
		((CLabel) $LBL((String) data.get("id"))).setLayoutData($LAYOUT_DATA(1, false, SWT.RIGHT));
		((CLabel) $LBL((String) data.get("id"))).setText((String) $PROP(data, "label"));
		((CLabel) $LBL((String) data.get("id"))).setBackground(super.getBackground());
		
		if(((boolean) data.get("enable_action")) == true) {
			controls_memory.put((String) data.get("id"), new Button(cmp_root, SWT.CHECK));
			((Button) $CTL(data)).setLayoutData($LAYOUT_DATA(1));
			((Button) $CTL(data)).setSelection((boolean) $PROP(data, "value"));
			((Button) $CTL(data)).setData("type", $PROP(data, "type"));
			
			controls_memory.put((String) data.get("id") + "_action", new Button(cmp_root, SWT.PUSH));
			((Button) $CTL((String) data.get("id") + "_action")).setLayoutData($LAYOUT_DATA(1, false, SWT.FILL));
			if(action_image != null) {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(action_image);
			}else {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(new Image(null, PropertyList.class.getResourceAsStream("/org/eclipse/swt/controls/collective/PropertyList.Action.png")));
			}			
			((Button) $CTL((String) data.get("id") + "_action")).addSelectionListener(new SelectionAdapter() {
				@SuppressWarnings("unchecked")
				@Override
				public void widgetSelected(SelectionEvent event) {
					Event post_event = new Event();
					post_event.data = data;
					((HashMap<String, Object>) post_event.data).put("value", get(String.valueOf(data.get("id"))));
					notifyListeners(SWT.Selection, post_event);
				}
			});
		}else {
			controls_memory.put((String) data.get("id"), new Button(cmp_root, SWT.CHECK));
			((Button) $CTL(data)).setLayoutData($LAYOUT_DATA(2));
			((Button) $CTL(data)).setSelection((boolean) $PROP(data, "value"));
			((Button) $CTL(data)).setData("type", $PROP(data, "type"));
		}
	}
	
	/**
	 * Add a check box input to the property list
	 * @param id
	 * @param label
	 * @param value
	 * @param enable_action
	 */
	public void addCheck(String id, String label, Boolean value, boolean enable_action) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("type", TYPE_CHECK);
		data.put("id", id);
		data.put("value", value);
		data.put("label", label);
		data.put("enable_action", enable_action);
		components.add(data);
	}
	
	private void buildText(HashMap<String, Object> data) {
		labels_memory.put((String) data.get("id"), new CLabel(cmp_root, SWT.NONE));
		((CLabel) $LBL((String) data.get("id"))).setLayoutData($LAYOUT_DATA(1, false, SWT.RIGHT));
		((CLabel) $LBL((String) data.get("id"))).setText((String) $PROP(data, "label"));
		((CLabel) $LBL((String) data.get("id"))).setBackground(super.getBackground());
		
		if(((boolean) data.get("enable_action")) == true) {
			controls_memory.put((String) data.get("id"), new Text(cmp_root, SWT.BORDER));
			((Text) $CTL(data)).setLayoutData($LAYOUT_DATA(1));
			((Text) $CTL(data)).setText((String) $PROP(data, "value"));
			((Text) $CTL(data)).setData("type", $PROP(data, "type"));
			
			controls_memory.put((String) data.get("id") + "_action", new Button(cmp_root, SWT.PUSH));
			((Button) $CTL((String) data.get("id") + "_action")).setLayoutData($LAYOUT_DATA(1, false, SWT.FILL));
			if(action_image != null) {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(action_image);
			}else {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(new Image(null, PropertyList.class.getResourceAsStream("/org/eclipse/swt/controls/collective/PropertyList.Action.png")));
			}
			((Button) $CTL((String) data.get("id") + "_action")).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					Event post_event = new Event();
					post_event.data = data;
					((HashMap<String, Object>) post_event.data).put("value", get(String.valueOf(data.get("id"))));
					notifyListeners(SWT.Selection, post_event);
				}
			});
		}else {
			controls_memory.put((String) data.get("id"), new Text(cmp_root, SWT.BORDER));
			((Text) $CTL(data)).setLayoutData($LAYOUT_DATA(2));
			((Text) $CTL(data)).setText((String) $PROP(data, "value"));
			((Text) $CTL(data)).setData("type", $PROP(data, "type"));
		}
	}
	
	/**
	 * Add a text input to the property list
	 * @param id
	 * @param label
	 * @param value
	 * @param enable_action
	 */
	public void addText(String id, String label, String value, boolean enable_action) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("type", TYPE_TEXT);
		data.put("id", id);
		data.put("value", value);
		data.put("label", label);
		data.put("enable_action", enable_action);
		components.add(data);
	}
	
	private void buildHiddenText(HashMap<String, Object> data) {
		labels_memory.put((String) data.get("id"), new CLabel(cmp_root, SWT.NONE));
		((CLabel) $LBL((String) data.get("id"))).setLayoutData($LAYOUT_DATA(1, false, SWT.RIGHT));
		((CLabel) $LBL((String) data.get("id"))).setText((String) $PROP(data, "label"));
		((CLabel) $LBL((String) data.get("id"))).setBackground(super.getBackground());
		
		if(((boolean) data.get("enable_action")) == true) {
			controls_memory.put((String) data.get("id"), new Text(cmp_root, SWT.BORDER | SWT.PASSWORD));
			((Text) $CTL(data)).setLayoutData($LAYOUT_DATA(1));
			((Text) $CTL(data)).setText((String) $PROP(data, "value"));
			((Text) $CTL(data)).setData("type", $PROP(data, "type"));
			
			controls_memory.put((String) data.get("id") + "_action", new Button(cmp_root, SWT.PUSH));
			((Button) $CTL((String) data.get("id") + "_action")).setLayoutData($LAYOUT_DATA(1, false, SWT.FILL));
			if(action_image != null) {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(action_image);
			}else {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(new Image(null, PropertyList.class.getResourceAsStream("/org/eclipse/swt/controls/collective/PropertyList.Action.png")));
			}
			((Button) $CTL((String) data.get("id") + "_action")).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					Event post_event = new Event();
					post_event.data = data;
					((HashMap<String, Object>) post_event.data).put("value", get(String.valueOf(data.get("id"))));
					notifyListeners(SWT.Selection, post_event);
				}
			});
		}else {
			controls_memory.put((String) data.get("id"), new Text(cmp_root, SWT.BORDER | SWT.PASSWORD));
			((Text) $CTL(data)).setLayoutData($LAYOUT_DATA(2));
			((Text) $CTL(data)).setText((String) $PROP(data, "value"));
			((Text) $CTL(data)).setData("type", $PROP(data, "type"));
		}
	}
	
	/**
	 * Add a hidden text input to the property list
	 * @param id
	 * @param label
	 * @param value
	 * @param enable_action
	 */
	public void addHidenText(String id, String label, String value, boolean enable_action) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("type", TYPE_HIDDEN_TEXT);
		data.put("id", id);
		data.put("value", value);
		data.put("label", label);
		data.put("enable_action", enable_action);
		components.add(data);
	}
	
	private void buildNumeric(HashMap<String, Object> data) {
		labels_memory.put((String) data.get("id"), new CLabel(cmp_root, SWT.NONE));
		((CLabel) $LBL((String) data.get("id"))).setLayoutData($LAYOUT_DATA(1, false, SWT.RIGHT));
		((CLabel) $LBL((String) data.get("id"))).setText((String) $PROP(data, "label"));
		((CLabel) $LBL((String) data.get("id"))).setBackground(super.getBackground());
		
		if(((boolean) data.get("enable_action")) == true) {
			controls_memory.put((String) data.get("id"), new Spinner(cmp_root, SWT.BORDER));
			((Spinner) $CTL(data)).setLayoutData($LAYOUT_DATA(1));
			((Spinner) $CTL(data)).setSelection((int) $PROP(data, "value"));
			((Spinner) $CTL(data)).setMaximum((int) $PROP(data, "maximum"));
			((Spinner) $CTL(data)).setMinimum((int) $PROP(data, "minimum"));
			((Spinner) $CTL(data)).setDigits((int) $PROP(data, "digits"));
			((Spinner) $CTL(data)).setIncrement((int) $PROP(data, "increment"));
			((Spinner) $CTL(data)).setPageIncrement((int) $PROP(data, "increment"));
			((Spinner) $CTL(data)).setData("type", $PROP(data, "type"));
			
			controls_memory.put((String) data.get("id") + "_action", new Button(cmp_root, SWT.PUSH));
			((Button) $CTL((String) data.get("id") + "_action")).setLayoutData($LAYOUT_DATA(1, false, SWT.FILL));
			if(action_image != null) {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(action_image);
			}else {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(new Image(null, PropertyList.class.getResourceAsStream("/org/eclipse/swt/controls/collective/PropertyList.Action.png")));
			}
			((Button) $CTL((String) data.get("id") + "_action")).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					Event post_event = new Event();
					post_event.data = data;
					((HashMap<String, Object>) post_event.data).put("value", get(String.valueOf(data.get("id"))));
					notifyListeners(SWT.Selection, post_event);
				}
			});
		}else {
			controls_memory.put((String) data.get("id"), new Spinner(cmp_root, SWT.BORDER));
			((Spinner) $CTL(data)).setLayoutData($LAYOUT_DATA(2));
			((Spinner) $CTL(data)).setSelection((int) $PROP(data, "value"));
			((Spinner) $CTL(data)).setMaximum((int) $PROP(data, "maximum"));
			((Spinner) $CTL(data)).setMinimum((int) $PROP(data, "minimum"));
			((Spinner) $CTL(data)).setDigits((int) $PROP(data, "digits"));
			((Spinner) $CTL(data)).setIncrement((int) $PROP(data, "increment"));
			((Spinner) $CTL(data)).setPageIncrement((int) $PROP(data, "increment"));
			((Spinner) $CTL(data)).setData("type", $PROP(data, "type"));
		}
		
	}

	/**
	 * Add a numeric input to the property list
	 * @param id
	 * @param label
	 * @param value
	 * @param maximum
	 * @param minimum
	 * @param digits
	 * @param increment
	 * @param enable_action
	 */
	public void addNumeric(String id, String label, int value, int maximum, int minimum, int digits, int increment, boolean enable_action) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("type", TYPE_NUMERIC);
		data.put("id", id);
		data.put("label", label);
		data.put("value", value);
		data.put("maximum", maximum);
		data.put("minimum", minimum);
		data.put("value", value);
		data.put("digits", digits);
		data.put("increment", increment);
		data.put("enable_action", enable_action);
		components.add(data);
	}
	
	private void buildDate(HashMap<String, Object> data) {
		labels_memory.put((String) data.get("id"), new CLabel(cmp_root, SWT.NONE));
		((CLabel) $LBL((String) data.get("id"))).setLayoutData($LAYOUT_DATA(1, false, SWT.RIGHT));
		((CLabel) $LBL((String) data.get("id"))).setText((String) $PROP(data, "label"));
		((CLabel) $LBL((String) data.get("id"))).setBackground(getBackground());
		
		if(((boolean) data.get("enable_action")) == true) {
			controls_memory.put((String) data.get("id"), new DateTime(cmp_root, SWT.BORDER | SWT.DATE));
			((DateTime) $CTL(data)).setLayoutData($LAYOUT_DATA(1));
			((DateTime) $CTL(data)).setDay((int) $PROP(data, "day"));
			((DateTime) $CTL(data)).setMonth((int) $PROP(data, "month"));
			((DateTime) $CTL(data)).setYear((int) $PROP(data, "year"));
			((DateTime) $CTL(data)).setData("type", $PROP(data, "type"));
			
			controls_memory.put((String) data.get("id") + "_action", new Button(cmp_root, SWT.PUSH));
			((Button) $CTL((String) data.get("id") + "_action")).setLayoutData($LAYOUT_DATA(1, false, SWT.FILL));
			if(action_image != null) {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(action_image);
			}else {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(new Image(null, PropertyList.class.getResourceAsStream("/org/eclipse/swt/controls/collective/PropertyList.Action.png")));
			}
			((Button) $CTL((String) data.get("id") + "_action")).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					Event post_event = new Event();
					post_event.data = data;
					((HashMap<String, Object>) post_event.data).put("value", get(String.valueOf(data.get("id"))));
					notifyListeners(SWT.Selection, post_event);
				}
			});
		}else {
			controls_memory.put((String) data.get("id"), new DateTime(cmp_root, SWT.BORDER | SWT.DATE));
			((DateTime) $CTL(data)).setLayoutData($LAYOUT_DATA(2));
			((DateTime) $CTL(data)).setDay((int) $PROP(data, "day"));
			((DateTime) $CTL(data)).setMonth((int) $PROP(data, "month"));
			((DateTime) $CTL(data)).setYear((int) $PROP(data, "year"));
			((DateTime) $CTL(data)).setData("type", $PROP(data, "type"));
		}		
	}
	
	/**
	 * Add a date input to the property list
	 * @param id
	 * @param label
	 * @param day
	 * @param month
	 * @param year
	 */
	public void addDate(String id, String label, int day, int month, int year, boolean enable_action) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("type", TYPE_DATE);
		data.put("id", id);
		data.put("label", label);
		data.put("day", day);
		data.put("month", month);
		data.put("year", year);
		data.put("enable_action", enable_action);
		components.add(data);
	}
	
	/**
	 * Add a date input to the property list
	 * @param id
	 * @param label
	 * @param date
	 * @param enable_action
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void addDate(String id, String label, Calendar date, boolean enable_action) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("type", TYPE_DATE);
		data.put("id", id);
		data.put("label", label);
		data.put("day", date.get(Calendar.DAY_OF_MONTH));
		data.put("month", date.get(Calendar.MONTH));
		data.put("year", data.get(Calendar.YEAR));
		data.put("enable_action", enable_action);
		components.add(data);
	}
	
	private void buildTime(HashMap<String, Object> data) {
		labels_memory.put((String) data.get("id"), new CLabel(cmp_root, SWT.NONE));
		((CLabel) $LBL((String) data.get("id"))).setLayoutData($LAYOUT_DATA(1, false, SWT.RIGHT));
		((CLabel) $LBL((String) data.get("id"))).setText((String) $PROP(data, "label"));
		((CLabel) $LBL((String) data.get("id"))).setBackground(getBackground());
		
		if(((boolean) data.get("enable_action")) == true) {
			controls_memory.put((String) data.get("id"), new DateTime(cmp_root, SWT.BORDER | SWT.TIME));
			((DateTime) $CTL(data)).setLayoutData($LAYOUT_DATA(1));
			((DateTime) $CTL(data)).setHours((int) $PROP(data, "hours"));
			((DateTime) $CTL(data)).setMinutes((int) $PROP(data, "minutes"));
			((DateTime) $CTL(data)).setSeconds((int) $PROP(data, "seconds"));
			((DateTime) $CTL(data)).setData("type", $PROP(data, "type"));
			
			controls_memory.put((String) data.get("id") + "_action", new Button(cmp_root, SWT.PUSH));
			((Button) $CTL((String) data.get("id") + "_action")).setLayoutData($LAYOUT_DATA(1, false, SWT.FILL));
			if(action_image != null) {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(action_image);
			}else {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(new Image(null, PropertyList.class.getResourceAsStream("/org/eclipse/swt/controls/collective/PropertyList.Action.png")));
			}
			((Button) $CTL((String) data.get("id") + "_action")).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					Event post_event = new Event();
					post_event.data = data;
					((HashMap<String, Object>) post_event.data).put("value", get(String.valueOf(data.get("id"))));
					notifyListeners(SWT.Selection, post_event);
				}
			});
		}else {
			controls_memory.put((String) data.get("id"), new DateTime(cmp_root, SWT.BORDER | SWT.TIME));
			((DateTime) $CTL(data)).setLayoutData($LAYOUT_DATA(2));
			((DateTime) $CTL(data)).setHours((int) $PROP(data, "hours"));
			((DateTime) $CTL(data)).setMinutes((int) $PROP(data, "minutes"));
			((DateTime) $CTL(data)).setSeconds((int) $PROP(data, "seconds"));
			((DateTime) $CTL(data)).setData("type", $PROP(data, "type"));
		}
	}
	
	/**
	 * Add a time input to the property list
	 * @param id
	 * @param label
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @param enable_action
	 */
	public void addTime(String id, String label, int hours, int minutes, int seconds, boolean enable_action) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("type", TYPE_TIME);
		data.put("id", id);
		data.put("label", label);
		data.put("hours", hours);
		data.put("minutes", minutes);
		data.put("seconds", seconds);
		data.put("enable_action", enable_action);
		components.add(data);
	}
	
	/**
	 * Add a time input to the property list
	 * @param id
	 * @param label
	 * @param time
	 * @param enable_action
	 */
	public void addTime(String id, String label, Calendar time, boolean enable_action) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("type", TYPE_TIME);
		data.put("id", id);
		data.put("label", label);
		data.put("hours", time.get(Calendar.HOUR_OF_DAY));
		data.put("minutes", time.get(Calendar.MINUTE));
		data.put("seconds", time.get(Calendar.SECOND));
		data.put("enable_action", enable_action);
		components.add(data);
	}
	
	private void buildSelect(HashMap<String, Object> data) {
		labels_memory.put((String) data.get("id"), new CLabel(cmp_root, SWT.NONE));
		((CLabel) $LBL((String) data.get("id"))).setLayoutData($LAYOUT_DATA(1, false, SWT.RIGHT));
		((CLabel) $LBL((String) data.get("id"))).setText((String) $PROP(data, "label"));
		((CLabel) $LBL((String) data.get("id"))).setBackground(getBackground());
		
		if(((boolean) data.get("enable_action")) == true) {
			controls_memory.put((String) data.get("id"), new CCombo(cmp_root, SWT.BORDER));
			((CCombo) $CTL(data)).setLayoutData($LAYOUT_DATA(1));
			((CCombo) $CTL(data)).setText((String) $PROP(data, "value"));
			((CCombo) $CTL(data)).setItems((String[]) $PROP(data, "items"));
			((CCombo) $CTL(data)).setData("type", $PROP(data, "type"));
			
			controls_memory.put((String) data.get("id") + "_action", new Button(cmp_root, SWT.PUSH));
			((Button) $CTL((String) data.get("id") + "_action")).setLayoutData($LAYOUT_DATA(1, false, SWT.FILL));
			if(action_image != null) {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(action_image);
			}else {
				((Button) $CTL((String) data.get("id") + "_action")).setImage(new Image(null, PropertyList.class.getResourceAsStream("/org/eclipse/swt/controls/collective/PropertyList.Action.png")));
			}
			((Button) $CTL((String) data.get("id") + "_action")).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					Event post_event = new Event();
					post_event.data = data;
					((HashMap<String, Object>) post_event.data).put("value", get(String.valueOf(data.get("id"))));
					notifyListeners(SWT.Selection, post_event);
				}
			});
		}else {
			controls_memory.put((String) data.get("id"), new CCombo(cmp_root, SWT.BORDER));
			((CCombo) $CTL(data)).setLayoutData($LAYOUT_DATA(2));
			((CCombo) $CTL(data)).setText((String) $PROP(data, "value"));
			((CCombo) $CTL(data)).setItems((String[]) $PROP(data, "items"));
			((CCombo) $CTL(data)).setData("type", $PROP(data, "type"));
		}
	}
	
	/**
	 * Add a combo-box select input to the property list
	 * @param id
	 * @param label
	 * @param value
	 * @param items
	 * @param enable_action
	 */
	public void addSelect(String id, String label, String value, String[] items, boolean enable_action) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("type", TYPE_SELECT);
		data.put("id", id);
		data.put("label", label);
		data.put("value", value);
		data.put("items", items);
		data.put("enable_action", enable_action);
		components.add(data);
	}
	
	@Override
	public void setBackground(Color background) {
		super.setBackground(background);
		cmp_root.setBackground(background);
	}
	
	@Override
	public Color getBackground() {
		return cmp_root.getBackground();
	}
	
}
