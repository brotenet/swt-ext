package org.eclipse.swt.environment;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.environment.Environment.Session;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class SystemDialogs {
	
	public static boolean SaveFile(Shell parent, String title, String extensions, String default_file_name, File file) {
		try {
			FileDialog dialog = new FileDialog(parent, SWT.SAVE);
			dialog.setText(title);
			if(extensions != null) {
				dialog.setFilterExtensions(extensions.split(","));
			}
			if(default_file_name != null) {
				dialog.setFileName(default_file_name);
			}
			dialog.open();
			Files.copy(Paths.get(file.getAbsolutePath()), new FileOutputStream(dialog.getFilterPath() + Session.FileSeparator() + dialog.getFileName()));
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}		
	}
}
