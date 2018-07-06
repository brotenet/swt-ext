package org.eclipse.swt.reporting.jasper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.environment.SystemDialogs;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Spinner;

import com.lowagie.text.pdf.codec.Base64;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.ExcelDataSource;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.data.JRHibernateAbstractDataSource;
import net.sf.jasperreports.engine.data.JRHibernateIterateDataSource;
import net.sf.jasperreports.engine.data.JRHibernateListDataSource;
import net.sf.jasperreports.engine.data.JRHibernateScrollDataSource;
import net.sf.jasperreports.engine.data.JRJpaDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleDocxExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOdsExporterConfiguration;
import net.sf.jasperreports.export.SimpleOdtExporterConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleRtfExporterConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxExporterConfiguration;

@SuppressWarnings("serial")
public class JasperReportView extends Canvas {
	
	private JasperReport jasper_report;
	private JasperPrint jasper_print;
	private String report_resource_url = "";
	private Double zoom_scale = 1.0;

	private Collection<RefreshListener> refreshListeners = new ArrayList<RefreshListener>();
	
	private CLabel page;
	private Composite cmpPage;
	private ScrolledComposite cmp_page_container;
	private Composite cmp_content;
	private MenuItem mntmExportPdf;
	private MenuItem mntmExportOdt;
	private MenuItem mntmExportOds;
	private MenuItem mntmExportDocx;
	private MenuItem mntmExportXlsx;
	private MenuItem mntmExportHtml;
	private MenuItem mntmExportCsv;
	private MenuItem mntmExportRtf;
	private Button btnExport;
	private Button btnPageFirst;
	private Spinner btnPageSelect;
	private Button btnPageLast;
	private Label place_holder_left_01;
	private Label place_holder_right_02;
	private MenuItem btnZoomIn;
	private MenuItem btnZoomOut;
	private MenuItem btnActualSize;

	public JasperReportView(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(7, false));

		btnExport = new Button(this, SWT.NONE);
		btnExport.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnExport.setImage(new Image(null,
				getClass().getResourceAsStream("/org/eclipse/swt/reporting/jasper/JasperReportsView.ExportMenu.png")));

		
		Menu menu = new Menu(btnExport);
		btnExport.setMenu(menu);

		mntmExportPdf = new MenuItem(menu, SWT.NONE);
		mntmExportPdf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				try {
					File file = File.createTempFile(UUID.randomUUID() + "-" + UUID.randomUUID(), ".pdf");
					JRPdfExporter exporter = new JRPdfExporter();
					SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
					exporter.setConfiguration(configuration);
					exporter.setExporterInput(new SimpleExporterInput(jasper_print));
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file.getAbsolutePath()));
					exporter.exportReport();
					SystemDialogs.SaveFile(getShell(), "Save Report", null, "report.pdf", file);					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		mntmExportPdf.setImage(new Image(null, getClass()
				.getResourceAsStream("/org/eclipse/swt/reporting/jasper/JasperReportsView.ExportMenu.PDF.png")));
		mntmExportPdf.setText("Export PDF");

		mntmExportOdt = new MenuItem(menu, SWT.NONE);
		mntmExportOdt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				try {
					File file = File.createTempFile(UUID.randomUUID() + "-" + UUID.randomUUID(), ".odt");
					JROdtExporter exporter = new JROdtExporter();
					SimpleOdtExporterConfiguration configuration = new SimpleOdtExporterConfiguration();
					exporter.setConfiguration(configuration);
					exporter.setExporterInput(new SimpleExporterInput(jasper_print));
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file.getAbsolutePath()));
					exporter.exportReport();
					SystemDialogs.SaveFile(getShell(), "Save Report", null, "report.odt", file);					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		mntmExportOdt.setImage(new Image(null, getClass()
				.getResourceAsStream("/org/eclipse/swt/reporting/jasper/JasperReportsView.ExportMenu.ODT.png")));
		mntmExportOdt.setText("Export ODT");

		mntmExportOds = new MenuItem(menu, SWT.NONE);
		mntmExportOds.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				try {
					File file = File.createTempFile(UUID.randomUUID() + "-" + UUID.randomUUID(), ".ods");
					JROdsExporter exporter = new JROdsExporter();
					SimpleOdsExporterConfiguration configuration = new SimpleOdsExporterConfiguration();
					exporter.setConfiguration(configuration);
					exporter.setExporterInput(new SimpleExporterInput(jasper_print));
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file.getAbsolutePath()));
					exporter.exportReport();
					SystemDialogs.SaveFile(getShell(), "Save Report", null, "report.ods", file);					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		mntmExportOds.setImage(new Image(null, getClass()
				.getResourceAsStream("/org/eclipse/swt/reporting/jasper/JasperReportsView.ExportMenu.ODS.png")));
		mntmExportOds.setText("Export ODS");

		mntmExportDocx = new MenuItem(menu, SWT.NONE);
		mntmExportDocx.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				try {
					File file = File.createTempFile(UUID.randomUUID() + "-" + UUID.randomUUID(), ".docx");
					JRDocxExporter exporter = new JRDocxExporter();
					SimpleDocxExporterConfiguration configuration = new SimpleDocxExporterConfiguration();
					exporter.setConfiguration(configuration);
					exporter.setExporterInput(new SimpleExporterInput(jasper_print));
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file.getAbsolutePath()));
					exporter.exportReport();
					SystemDialogs.SaveFile(getShell(), "Save Report", null, "report.docx", file);					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		mntmExportDocx.setImage(new Image(null, getClass()
				.getResourceAsStream("/org/eclipse/swt/reporting/jasper/JasperReportsView.ExportMenu.DOCx.png")));
		mntmExportDocx.setText("Export DOCx");

		mntmExportXlsx = new MenuItem(menu, SWT.NONE);
		mntmExportXlsx.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				try {
					File file = File.createTempFile(UUID.randomUUID() + "-" + UUID.randomUUID(), ".xlsx");
					JRXlsxExporter exporter = new JRXlsxExporter();
					SimpleXlsxExporterConfiguration configuration = new SimpleXlsxExporterConfiguration();
					exporter.setConfiguration(configuration);
					exporter.setExporterInput(new SimpleExporterInput(jasper_print));
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file.getAbsolutePath()));
					exporter.exportReport();
					SystemDialogs.SaveFile(getShell(), "Save Report", null, "report.xlsx", file);					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		mntmExportXlsx.setImage(new Image(null, getClass()
				.getResourceAsStream("/org/eclipse/swt/reporting/jasper/JasperReportsView.ExportMenu.XLSx.png")));
		mntmExportXlsx.setText("Export XLSx");

		mntmExportHtml = new MenuItem(menu, SWT.NONE);
		mntmExportHtml.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				try {
					File file = File.createTempFile(UUID.randomUUID() + "-" + UUID.randomUUID(), ".html");
					HtmlExporter exporter = new HtmlExporter();
					SimpleHtmlExporterConfiguration configuration = new SimpleHtmlExporterConfiguration();
					exporter.setConfiguration(configuration);
					exporter.setExporterInput(new SimpleExporterInput(jasper_print));
					SimpleHtmlExporterOutput exporter_output = new SimpleHtmlExporterOutput(file.getAbsolutePath());
					exporter.setExporterOutput(exporter_output);
					exporter_output.setImageHandler(new HtmlResourceHandler() {
						Map<String, String> images = new HashMap<>();
						@Override
			            public void handleResource(String id, byte[] data) {
			                System.err.println("id" + id);
			                images.put(id, "data:image/jpg;base64," + Base64.encodeBytes(data));
			            }

			            @Override
			            public String getResourcePath(String id) {
			                return images.get(id);
			            }
					});
					exporter.exportReport();
					SystemDialogs.SaveFile(getShell(), "Save Report", null, "report.html", file);					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		mntmExportHtml.setImage(new Image(null, getClass()
				.getResourceAsStream("/org/eclipse/swt/reporting/jasper/JasperReportsView.ExportMenu.HTML.png")));
		mntmExportHtml.setText("Export HTML");

		mntmExportCsv = new MenuItem(menu, SWT.NONE);
		mntmExportCsv.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				try {
					File file = File.createTempFile(UUID.randomUUID() + "-" + UUID.randomUUID(), ".csv");
					JRCsvExporter exporter = new JRCsvExporter();
					SimpleCsvExporterConfiguration configuration = new SimpleCsvExporterConfiguration();
					exporter.setConfiguration(configuration);
					exporter.setExporterInput(new SimpleExporterInput(jasper_print));
					exporter.setExporterOutput(new SimpleWriterExporterOutput(file.getAbsolutePath()));
					exporter.exportReport();
					SystemDialogs.SaveFile(getShell(), "Save Report", null, "report.csv", file);					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		mntmExportCsv.setImage(new Image(null, getClass()
				.getResourceAsStream("/org/eclipse/swt/reporting/jasper/JasperReportsView.ExportMenu.CSV.png")));
		mntmExportCsv.setText("Export CSV");

		mntmExportRtf = new MenuItem(menu, SWT.NONE);
		mntmExportRtf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				try {
					File file = File.createTempFile(UUID.randomUUID() + "-" + UUID.randomUUID(), ".rtf");
					JRRtfExporter exporter = new JRRtfExporter();
					SimpleRtfExporterConfiguration configuration = new SimpleRtfExporterConfiguration();
					exporter.setConfiguration(configuration);
					exporter.setExporterInput(new SimpleExporterInput(jasper_print));
					exporter.setExporterOutput(new SimpleWriterExporterOutput(file.getAbsolutePath()));
					exporter.exportReport();
					SystemDialogs.SaveFile(getShell(), "Save Report", null, "report.rtf", file);					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		mntmExportRtf.setImage(new Image(null, getClass()
				.getResourceAsStream("/org/eclipse/swt/reporting/jasper/JasperReportsView.ExportMenu.RTF.png")));
		mntmExportRtf.setText("Export RTF");

		btnExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				btnExport.getMenu().setLocation(btnExport.toDisplay(1, btnExport.getBounds().height));
				btnExport.getMenu().setVisible(true);
			}
		});

		place_holder_left_01 = new Label(this, SWT.NONE);
		place_holder_left_01.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		btnPageFirst = new Button(this, SWT.NONE);
		btnPageFirst.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnPageFirst.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				btnPageSelect.setSelection(btnPageSelect.getMinimum());
				renderPage(btnPageSelect.getSelection());
			}
		});
		btnPageFirst.setImage(new Image(null,
				getClass().getResourceAsStream("/org/eclipse/swt/reporting/jasper/JasperReportsView.Page.First.png")));
		btnPageSelect = new Spinner(this, SWT.BORDER);
		btnPageSelect.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnPageSelect.setPageIncrement(1);
		btnPageSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				renderPage(btnPageSelect.getSelection());
			}
		});
		btnPageSelect.setMaximum(1);
		btnPageSelect.setMinimum(1);
		btnPageSelect.setSelection(1);
		btnPageLast = new Button(this, SWT.NONE);
		btnPageLast.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnPageLast.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				btnPageSelect.setSelection(btnPageSelect.getMaximum());
				renderPage(btnPageSelect.getSelection());
			}
		});
		btnPageLast.setImage(new Image(null,
				getClass().getResourceAsStream("/org/eclipse/swt/reporting/jasper/JasperReportsView.Page.Last.png")));

		place_holder_right_02 = new Label(this, SWT.NONE);
		place_holder_right_02.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		Button btnPageRefresh = new Button(this, SWT.NONE);
		btnPageRefresh.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnPageRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				for(RefreshListener listener : refreshListeners) {
					RefreshEvent refresh_event = new RefreshEvent(event.getSource());
					listener.refreshInitialized(refresh_event);
				}
				renderPage(btnPageSelect.getSelection());
			}
		});
		btnPageRefresh.setImage(new Image(null, getClass()
				.getResourceAsStream("/org/eclipse/swt/reporting/jasper/JasperReportsView.Page.Refresh.png")));
		cmp_content = new Composite(this, SWT.NONE);
		cmp_content.setLayout(new FillLayout(SWT.HORIZONTAL));
		cmp_content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));
		cmp_page_container = new ScrolledComposite(cmp_content, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		cmp_page_container.setExpandHorizontal(true);
		cmp_page_container.setExpandVertical(true);

		cmpPage = new Composite(cmp_page_container, SWT.NONE);
		cmpPage.setLayout(new FillLayout(SWT.HORIZONTAL));
		page = new CLabel(cmpPage, SWT.CENTER);
		page.setAlignment(SWT.CENTER);
		page.setText("");
		
		Menu menu_1 = new Menu(page);
		page.setMenu(menu_1);
		
		btnZoomIn = new MenuItem(menu_1, SWT.NONE);
		btnZoomIn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				renderPage(btnPageSelect.getSelection(), 0.2);
			}
		});
		btnZoomIn.setImage(new Image(null, JasperReportView.class.getResourceAsStream("/org/eclipse/swt/reporting/jasper/zoom_in.png")));
		btnZoomIn.setText("Zoom In");
		
		btnZoomOut = new MenuItem(menu_1, SWT.NONE);
		btnZoomOut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				renderPage(btnPageSelect.getSelection(), -0.2);
			}
		});
		btnZoomOut.setImage(new Image(null, JasperReportView.class.getResourceAsStream("/org/eclipse/swt/reporting/jasper/zoom_out.png")));
		btnZoomOut.setText("Zoom Out");
		
		btnActualSize = new MenuItem(menu_1, SWT.NONE);
		btnActualSize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				setZoomScale(1.0);
				renderPage(btnPageSelect.getSelection());
			}
		});
		btnActualSize.setImage(new Image(null, JasperReportView.class.getResourceAsStream("/org/eclipse/swt/reporting/jasper/zoom.png")));
		btnActualSize.setText("Zoom 1:1");
		cmp_page_container.setContent(cmpPage);
		cmp_page_container.setMinSize(cmpPage.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void load(Map<String, Object> parameters) throws JRException {
		jasper_report = JasperCompileManager.compileReport(getClass().getResourceAsStream(getReportResourceURL()));
		jasper_print = JasperFillManager.fillReport(jasper_report, parameters, new JREmptyDataSource());
		btnPageSelect.setMaximum(jasper_print.getPages().size());
		setPage(1);
	}
	
	public void load(Map<String, Object> parameters, String[] column_names, String csv_file_path) throws JRException, FileNotFoundException {
		load(parameters, column_names, true, "\r\n", null, null, null, null, null, new File(csv_file_path));
	}
	
	public void load(Map<String, Object> parameters, String[] column_names, File csv_file) throws JRException, FileNotFoundException {
		load(parameters, column_names, true, "\r\n", null, null, null, null, null, csv_file);
	}
	
	public void load(Map<String, Object> parameters, String[] column_names, Boolean first_row_as_header, String record_delimiter, Character field_delimiter, File csv_file) throws JRException, FileNotFoundException {
		load(parameters, column_names, first_row_as_header, record_delimiter, field_delimiter, null, null, null, null, csv_file);
	}
	
	public void load(Map<String, Object> parameters, String[] column_names, Boolean first_row_as_header, String record_delimiter, Character field_delimiter, String csv_file_path) throws JRException, FileNotFoundException {
		load(parameters, column_names, first_row_as_header, record_delimiter, field_delimiter, null, null, null, null, new File(csv_file_path));
	}
	
	public void load(Map<String, Object> parameters, String[] column_names, Boolean first_row_as_header, String record_delimiter, Character field_delimiter, String date_pattern, String time_zone_id, String number_pattern, String locale, String csv_file_path) throws FileNotFoundException, JRException {
		load(parameters, column_names, first_row_as_header, record_delimiter, field_delimiter, date_pattern, time_zone_id, number_pattern, locale, new File(csv_file_path));
	}
	
	public void load(Map<String, Object> parameters, String[] column_names, Boolean first_row_as_header, String record_delimiter, Character field_delimiter, String date_pattern, String time_zone_id, String number_pattern, String locale, File csv_file) throws JRException, FileNotFoundException {
		JRCsvDataSource data_source = new JRCsvDataSource(csv_file);
		data_source.setColumnNames(column_names);
		if(first_row_as_header != null) {
			data_source.setUseFirstRowAsHeader(first_row_as_header);
		}else {
			data_source.setUseFirstRowAsHeader(true);
		}
		if(record_delimiter != null) {
			data_source.setRecordDelimiter(record_delimiter);
		}
		if(field_delimiter != null) {
			data_source.setFieldDelimiter(field_delimiter);
		}
		if(date_pattern != null) {
			data_source.setDatePattern(date_pattern);
		}
		if(time_zone_id != null) {
			data_source.setTimeZone(time_zone_id);
		}
		if(number_pattern != null) {
			data_source.setNumberPattern(number_pattern);
		}
		if(locale != null) {
			data_source.setLocale(locale);
		}		
		load(parameters, data_source);
	}
	
	public void load(Map<String, Object> parameters, JRCsvDataSource data_source) throws JRException {
		jasper_report = JasperCompileManager.compileReport(getClass().getResourceAsStream(getReportResourceURL()));
		jasper_print = JasperFillManager.fillReport(jasper_report, parameters, data_source);
		btnPageSelect.setMaximum(jasper_print.getPages().size());
		setPage(1);
	}
	
	public void load(Map<String, Object> parameters, String sheet, String[] column_names, int[] column_indexes, Boolean first_row_as_header, String date_pattern, String time_zone_id, String number_pattern, String locale, File excel_file) throws JRException, IOException {
		ExcelDataSource data_source = new ExcelDataSource(excel_file);
		if(column_names != null) {
			data_source.setColumnNames(column_names, column_indexes);
		}else {
			String[] names_from_indexes = new String[column_indexes.length];
			for(int i = 0; i < column_indexes.length; i++) {
				names_from_indexes[i] = "col_" + i;
			}
			data_source.setColumnNames(names_from_indexes, column_indexes);
		}
		if(sheet != null) {
			data_source.setSheetSelection(sheet);
		}
		if(date_pattern != null) {
			data_source.setDatePattern(date_pattern);
		}
		if(time_zone_id != null) {
			data_source.setTimeZone(time_zone_id);
		}
		if(number_pattern != null) {
			data_source.setNumberPattern(number_pattern);
		}
		if(locale != null) {
			data_source.setLocale(locale);
		}
		if(first_row_as_header != null) {
			data_source.setUseFirstRowAsHeader(first_row_as_header);
		}
		load(parameters, data_source);
	}
	
	public void load(Map<String, Object> parameters, ExcelDataSource data_source) throws JRException {
		jasper_report = JasperCompileManager.compileReport(getClass().getResourceAsStream(getReportResourceURL()));
		jasper_print = JasperFillManager.fillReport(jasper_report, parameters, data_source);
		btnPageSelect.setMaximum(jasper_print.getPages().size());
		setPage(1);
	}
	
	public void load(Map<String, Object> parameters, String sheet, String[] column_names, int[] column_indexes, Boolean first_row_as_header, File excel_file) throws JRException, IOException {
		load(parameters, sheet, column_names, column_indexes, first_row_as_header, null, null, null, null, excel_file);
	}
	
	public void load(Map<String, Object> parameters, String sheet, String[] column_names, int[] column_indexes, File excel_file) throws JRException, IOException {
		load(parameters, sheet, column_names, column_indexes, null, null, null, null, null, excel_file);
	}
	
	public void load(Map<String, Object> parameters, ResultSet result_set) throws JRException {
		JRResultSetDataSource data_source = new JRResultSetDataSource(result_set);
		load(parameters, data_source);		
	}
	
	public void load(Map<String, Object> parameters, JRResultSetDataSource data_source) throws JRException {
		jasper_report = JasperCompileManager.compileReport(getClass().getResourceAsStream(getReportResourceURL()));
		jasper_print = JasperFillManager.fillReport(jasper_report, parameters, data_source);
		btnPageSelect.setMaximum(jasper_print.getPages().size());
		setPage(1);
	}
	
	public void load(Map<String, Object> parameters, String jdbc_driver, String url, String user, String password, String sql) throws ClassNotFoundException, SQLException, JRException {
		Class.forName(jdbc_driver);
		Connection connection = DriverManager.getConnection(url, user, password);
		ResultSet result_set = connection.createStatement().executeQuery(sql);
		JRResultSetDataSource data_source = new JRResultSetDataSource(result_set);
		load(parameters, data_source);
	}
	
	public void load(Map<String, Object> parameters, Class<?> jdbc_driver, String url, String user, String password, String sql) throws ClassNotFoundException, SQLException, JRException {
		Class.forName(jdbc_driver.getName());
		Connection connection = DriverManager.getConnection(url, user, password);
		ResultSet result_set = connection.createStatement().executeQuery(sql);
		JRResultSetDataSource data_source = new JRResultSetDataSource(result_set);
		load(parameters, data_source);
	}
	
	public void load(Map<String, Object> parameters, JRXmlDataSource data_source) throws JRException {
		jasper_report = JasperCompileManager.compileReport(getClass().getResourceAsStream(getReportResourceURL()));
		jasper_print = JasperFillManager.fillReport(jasper_report, parameters, data_source);
		btnPageSelect.setMaximum(jasper_print.getPages().size());
		setPage(1);
	}
	
	public void load(Map<String, Object> parameters, JRJpaDataSource data_source) throws JRException {
		jasper_report = JasperCompileManager.compileReport(getClass().getResourceAsStream(getReportResourceURL()));
		jasper_print = JasperFillManager.fillReport(jasper_report, parameters, data_source);
		btnPageSelect.setMaximum(jasper_print.getPages().size());
		setPage(1);
	}
	
	public void load(Map<String, Object> parameters, JRHibernateAbstractDataSource data_source) throws JRException {
		jasper_report = JasperCompileManager.compileReport(getClass().getResourceAsStream(getReportResourceURL()));
		jasper_print = JasperFillManager.fillReport(jasper_report, parameters, data_source);
		btnPageSelect.setMaximum(jasper_print.getPages().size());
		setPage(1);
	}
	
	public void load(Map<String, Object> parameters, JRHibernateIterateDataSource data_source) throws JRException {
		jasper_report = JasperCompileManager.compileReport(getClass().getResourceAsStream(getReportResourceURL()));
		jasper_print = JasperFillManager.fillReport(jasper_report, parameters, data_source);
		btnPageSelect.setMaximum(jasper_print.getPages().size());
		setPage(1);
	}
	
	public void load(Map<String, Object> parameters, JRHibernateListDataSource data_source) throws JRException {
		jasper_report = JasperCompileManager.compileReport(getClass().getResourceAsStream(getReportResourceURL()));
		jasper_print = JasperFillManager.fillReport(jasper_report, parameters, data_source);
		btnPageSelect.setMaximum(jasper_print.getPages().size());
		setPage(1);
	}
	
	public void load(Map<String, Object> parameters, JRHibernateScrollDataSource data_source) throws JRException {
		jasper_report = JasperCompileManager.compileReport(getClass().getResourceAsStream(getReportResourceURL()));
		jasper_print = JasperFillManager.fillReport(jasper_report, parameters, data_source);
		btnPageSelect.setMaximum(jasper_print.getPages().size());
		setPage(1);
	}
	
	public void load(Map<String, Object> parameters, String date_pattern, String time_zone, String locale, String number_format, File json_file) throws JRException, FileNotFoundException {
		JsonDataSource data_source = new JsonDataSource(json_file);
		if(date_pattern != null) {
			data_source.setDatePattern(date_pattern);
		} else {
			data_source.setDatePattern("dd-MM-yyyy");
		}
		if(time_zone != null) {
			data_source.setTimeZone(time_zone);
		} else {
			data_source.setTimeZone(TimeZone.getDefault());
		}
		if(locale != null) {
			data_source.setLocale(locale);
		} else {
			data_source.setLocale(Locale.ENGLISH);
		}
		if(number_format != null) {
			data_source.setNumberPattern(number_format);
		} else {
			data_source.setNumberPattern("#,##0.##");
		}
		jasper_report = JasperCompileManager.compileReport(getClass().getResourceAsStream(getReportResourceURL()));
		jasper_print = JasperFillManager.fillReport(jasper_report, parameters, data_source);
		btnPageSelect.setMaximum(jasper_print.getPages().size());
		setPage(1);
	}
	
	public void load(Map<String, Object> parameters, JsonDataSource data_source) throws JRException {
		jasper_report = JasperCompileManager.compileReport(getClass().getResourceAsStream(getReportResourceURL()));
		jasper_print = JasperFillManager.fillReport(jasper_report, parameters, data_source);
		btnPageSelect.setMaximum(jasper_print.getPages().size());
		setPage(1);
	}
	
	private void setPageContent(Image image) {
		page.setImage(image);
		cmp_page_container.setMinSize(cmpPage.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void renderPage(int page) {
		try {
			setPageContent(new Image(null, getImageData(JasperPrintManager.printPageToImage(jasper_print,page-1,new Float(zoom_scale)))));
		} catch (JRException exception) {
			exception.printStackTrace();
		}
	}
	
	private void renderPage(int page, Double zoom_modifier) {
		setZoomScale(getZoomScale() + zoom_modifier);
		renderPage(page);
	}

	public String getReportResourceURL() {
		return report_resource_url;
	}

	public void setReportResourceURL(String report_resource_url) {
		this.report_resource_url = report_resource_url;
	}
	
	public int getPage() {
		return btnPageSelect.getSelection(); 
	}

	public void setPage(int page_index) {
		renderPage(page_index);
	}	

	public Double getZoomScale() {
		return zoom_scale;
	}

	public void setZoomScale(Double zoom_scale) {
		this.zoom_scale = zoom_scale;
	}

	public boolean getExportingEnabled() {
		return btnExport.isEnabled();
	}

	public void setExportingEnabled(boolean enabled) {
		btnExport.setEnabled(enabled);
	}

	public boolean getEnableExporCSV() {
		return mntmExportCsv.getEnabled();
	}

	public void setEnableExporCSV(boolean enabled) {
		mntmExportCsv.setEnabled(enabled);
	}

	public boolean getEnableExportDOCx() {
		return mntmExportDocx.getEnabled();
	}

	public void setEnableExportDOCx(boolean enabled) {
		mntmExportDocx.setEnabled(enabled);
	}

	public boolean getEnableExportHTML() {
		return mntmExportHtml.isEnabled();
	}

	public void setEnableExportHTML(boolean enabled) {
		mntmExportHtml.setEnabled(enabled);
	}

	public boolean getEnableExportODS() {
		return mntmExportOds.isEnabled();
	}

	public void setEnableExportODS(boolean enabled) {
		mntmExportOds.setEnabled(enabled);
	}

	public boolean getEnableExportODT() {
		return mntmExportOdt.isEnabled();
	}

	public void setEnableExportODT(boolean enabled) {
		mntmExportOdt.setEnabled(enabled);
	}

	public boolean getEnableExportPDF() {
		return mntmExportPdf.isEnabled();
	}

	public void setEnableExportPDF(boolean enabled) {
		mntmExportPdf.setEnabled(enabled);
	}

	public boolean getEnableExportRTF() {
		return mntmExportRtf.isEnabled();
	}

	public void setEnableExportRTF(boolean enabled) {
		mntmExportRtf.setEnabled(enabled);
	}

	public boolean getEnableExportXLSx() {
		return mntmExportXlsx.isEnabled();
	}

	public void setEnableExportXLSx(boolean enabled) {
		mntmExportXlsx.setEnabled(enabled);
	}

	private static ImageData getImageData(java.awt.Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Null 'image' argument.");
        }
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        if (w == -1 || h == -1) {
            return null;
        }
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics g = bi.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return convertToSWT(bi);
    }
	
	private static ImageData convertToSWT(java.awt.image.BufferedImage bufferedImage) {
        if (bufferedImage.getColorModel() instanceof java.awt.image.DirectColorModel) {
        	java.awt.image.DirectColorModel colorModel
                    = (java.awt.image.DirectColorModel) bufferedImage.getColorModel();
            PaletteData palette = new PaletteData(colorModel.getRedMask(),
                    colorModel.getGreenMask(), colorModel.getBlueMask());
            ImageData data = new ImageData(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), colorModel.getPixelSize(),
                    palette);
            java.awt.image.WritableRaster raster = bufferedImage.getRaster();
            int[] pixelArray = new int[3];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    raster.getPixel(x, y, pixelArray);
                    int pixel = palette.getPixel(new RGB(pixelArray[0],
                            pixelArray[1], pixelArray[2]));
                    data.setPixel(x, y, pixel);
                }
            }
            return data;
        }
        else if (bufferedImage.getColorModel() instanceof java.awt.image.IndexColorModel) {
        	java.awt.image.IndexColorModel colorModel = (java.awt.image.IndexColorModel)
                    bufferedImage.getColorModel();
            int size = colorModel.getMapSize();
            byte[] reds = new byte[size];
            byte[] greens = new byte[size];
            byte[] blues = new byte[size];
            colorModel.getReds(reds);
            colorModel.getGreens(greens);
            colorModel.getBlues(blues);
            RGB[] rgbs = new RGB[size];
            for (int i = 0; i < rgbs.length; i++) {
                rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF,
                        blues[i] & 0xFF);
            }
            PaletteData palette = new PaletteData(rgbs);
            ImageData data = new ImageData(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), colorModel.getPixelSize(),
                    palette);
            data.transparentPixel = colorModel.getTransparentPixel();
            java.awt.image.WritableRaster raster = bufferedImage.getRaster();
            int[] pixelArray = new int[1];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    raster.getPixel(x, y, pixelArray);
                    data.setPixel(x, y, pixelArray[0]);
                }
            }
            return data;
        }
        return null;
    }
	
	public class RefreshEvent extends EventObject {

		public RefreshEvent(Object source) {
			super(source);
		}
		
		public int getSelectedPage() {
			return btnPageSelect.getSelection();
		}
	}
	
	public interface RefreshListener extends EventListener{
		public void refreshInitialized(RefreshEvent event);
	}
	
	public void addRefreshListener(RefreshListener listener) {
		refreshListeners.add(listener);
	}
	
	@Override
	protected void checkSubclass() {
	}
}
