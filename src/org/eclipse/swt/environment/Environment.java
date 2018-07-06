package org.eclipse.swt.environment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.eclipse.swt.graphics.Image;

public class Environment {
	
	public static class Session{
		/**
		 * Character that separates components of a file path. This is "/" on UNIX and "\" on Windows.
		 */
		public static String FileSeparator() {return getProperty("file.separator");};
		/**
		 * Sequence used by operating system to separate lines in text files
		 */
		public static String LineSeparator() {return getProperty("line.separator");};
		/**
		 * Path used to find directories and JAR archives containing class files. Elements of the class path are separated by a platform-specific character specified in the path.separator property.
		 */
		public static String JavaClassPath() {return getProperty("java.class.path");};
		/**
		 * Path separator character used in java.class.path
		 */
		public static String PathSeparator() {return getProperty("path.separator");};
		/**
		 * Installation directory for Java Runtime Environment (JRE)
		 */
		public static String JavaHome() {return getProperty("java.home");};
		/**
		 * JRE vendor name
		 */
		public static String JavaVendor() {return getProperty("java.vendor");};
		/**
		 * JRE vendor URL
		 */
		public static String JavaVendorURL() {return getProperty("java.vendor.url");};
		/**
		 * JRE version number
		 */
		public static String JavaVersion() {return getProperty("java.version");};
		/**
		 * Operating system architecture
		 */
		public static String OperatingSystemArchitecture() {return getProperty("os.arch");};
		/**
		 * Operating system name
		 */
		public static String OperatingSystemName() {return getProperty("os.name");};
		/**
		 * Operating system version
		 */
		public static String OperatingSystemVersion() {return getProperty("os.version");};
		/**
		 * User working directory (application execution directory)
		 */
		public static String UserDirectory() {return getProperty("user.dir");};
		/**
		 * User home directory
		 */
		public static String UserHome() {return getProperty("user.home");};
		/**
		 * User account name
		 */
		public static String UserName() {return getProperty("user.name");};
		
		public static void putProperty(String key, String value) {
			System.getProperties().put(key, value);
		}
		
		public static String getProperty(String key) {
			return System.getProperty(key);
		}
	}
	
	public static class FileSystem {
		
		/**
		 * Get a file array using an array of paths (returns null if no files are found -or- null items in the array if some files are not found)
		 * @param paths: file-system paths of files
		 * @return File objects
		 */
		public static File[] get(String[] paths) {
			File[] output = null;
			if(paths.length > 0) {
				output = new File[paths.length];
				for(int i = 0; i < paths.length; i++) {
					output[i] = get(paths[i].trim());
				}
			}
			return output;
		}
		
		/**
		 * Get a file using path (returns null if not found)
		 * @param path: file-system path of the file
		 * @return File object
		 */
		public static File get(String path) {
			if(check(path)) {
				return new File(path);
			}else {
				return null;
			}
		}
		
		/**
		 * Check if a file/directory exists
		 * @param path: file-system path of the file/directory
		 * @return true = exists, false = does not exist
		 */
		public static boolean check(String path) {
			return Files.exists(Paths.get(path));
		}
		
		/**
		 * Check if a file exists
		 * @param path: file-system path of the file/directory
		 * @return true = exists, false = does not exist
		 */
		public static boolean checkFile(String path) {
			if(check(path)) {
				return !Files.isDirectory(Paths.get(path));
			} else {
				return false;
			}
		}
		
		/**
		 * Check if a directory exists
		 * @param path: file-system path of the file/directory
		 * @return true = exists, false = does not exist
		 */
		public static boolean checkDir(String path) {
			if(check(path)) {
				return Files.isDirectory(Paths.get(path));
			} else {
				return false;
			}
		}
		
		/**
		 * Read the contents of a file to a String
		 * @param path: file-system path of the file
		 * @return File contents
		 */
		public static String cat(String path) {
			try {
				return new String(Files.readAllBytes(Paths.get(path)));
			} catch (Exception ignore) {
				return null;
			}
		}
		
		/**
		 * Delete a directory or file
		 * @param path: file-system path of the directory
		 * @return returns 'true' if directory/file was deleted or does not exist
		 * @throws IOException 
		 */
		public static void delete(String path) throws IOException {
			if(check(path)) {
				Files.delete(Paths.get(path));
			}
		}
		
		/**
		 * Create a directory if it does not exist
		 * @param path: file-system path of the directory
		 * @param force: create any parent directories necessary to reach full path 
		 * @return returns 'true' if directory was create or already exists 
		 * @throws IOException 
		 */
		public static void mkDir(String path, boolean force) throws IOException {
			if(check(path)) {
				if(force == true) {
					delete(path);
					Files.createDirectory(Paths.get(path));
				}			
			} else {
				Files.createDirectory(Paths.get(path));
			}
		}
		
		/**
		 * Create a directory if it does not exist
		 * @param path: file-system path of the directory
		 * @return returns 'true' if directory was create or already exists 
		 * @throws IOException 
		 */
		public static void mkDir(String path) throws IOException {
			mkDir(path, true);
		}
		
		/**
		 * Create or update a file if it does not exist
		 * @param path: file-system path of the file
		 * @param content: File contents
		 * @param force: re-create even if file exists
		 * @throws IOException 
		 */
		public static void touch(String path, String content, Boolean force) throws IOException {
			boolean check = false;
			if(check(path)) {
				if(force == true) {
					delete(path);
					check = true;
					
				}
			}else {
				check = true;
			}
			if(check == true) {
				if(Paths.get(path).getParent() != null) {
					Files.createDirectories(Paths.get(path).getParent());
				}			
				Files.createFile(Paths.get(path));
				if(content == null) {
					content = "";
				}
				PrintWriter writer = new PrintWriter(new File(path));
				writer.print(content);
				writer.close();
			}
		}
		
		/**
		 *  Create or update a file if it does not exist
		 * @param path: file-system path of the file
		 * @param content: the resource input stream of file contents
		 * @throws IOException
		 */
		public static void touch(String path, InputStream content) throws IOException {
			touch(path, content, false);
		}
		
		/**
		 * Create or update a file if it does not exist
		 * @param path: path: file-system path of the file
		 * @param content: File contents
		 * @throws IOException 
		 */
		public static void touch(String path, String content) throws IOException {
			touch(path, content, false);
		}
		
		/**
		 * Create a blank file if it does not exist
		 * @param : path: file-system path of the file
		 * @throws IOException 
		 */
		public static void touch(String path) throws IOException {
			touch(path, "", null);
		}
		
		/**
		 *  Create or update a file if it does not exist
		 * @param path: file-system path of the file
		 * @param content: the resource input stream of file contents
		 * @param force: re-create even if file exists
		 * @throws IOException
		 */
		public static void touch(String path, InputStream content, Boolean force) throws IOException {
			boolean check = false;
			if(check(path)) {
				if(force == true) {
					delete(path);
					check = true;
					
				}
			}else {
				check = true;
			}
			if(check == true) {
				if(Paths.get(path).getParent() != null) {
					Files.createDirectories(Paths.get(path).getParent());
				}			
				Files.copy(content, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		
		/**
		 * Copy a file. Creates target directory path if it does not exist. Overrides file if it already exists to destination.
		 * @param : source_file_path: source file path
		 * @param : source_file_path: target file path
		 */
		public static void copy(String source_file_path, String target_file_path) {
			try {
				if(check(source_file_path)) {
					touch(target_file_path);
					Files.copy(Paths.get(source_file_path), Paths.get(target_file_path), StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (Exception ignore) {
			}			
		}
		
		/**
		 * Returns an array of SWT images for a given array of image file paths
		 * @param resource_path : the resource path for the source image
		 * @return An SWT image object
		 */
		public static Image[] getImagesFromFilePaths(String paths[]) {
			Image[] output = null;
			if(paths.length > 0) {
				output = new Image[paths.length];
				for(int i = 0; i < paths.length; i++) {
					try {
						output[i] = getImageFromFilePath(paths[i]);
					} catch (Exception ignore) {
						output[i] = null;
					}
				}
			}
			return output;
		}
		
		/**
		 * Returns an SWT image for a given image file path
		 * @param resource_path : the resource path for the source image
		 * @return An SWT image object
		 */
		public static Image getImageFromFilePath(String path) {
			Image output = null;
			try {
				output = new Image(null, new FileInputStream(path));
			} catch (Exception ignore) {}
			return output;
		}
		
		/**
		 * Extract the contents of a given .zip file
		 * @param input_stream : the input file path
		 * @param target_directory : output directory
		 * @throws IOException 
		 */
		public static void unzip(String sourec_file_path, String target_directory) throws IOException {
			unzip(new File(sourec_file_path), target_directory);
		}
		
		/**
		 * Extract the contents of a given .zip file
		 * @param input_stream : the input file object
		 * @param target_directory : output directory
		 * @throws IOException 
		 */
		public static void unzip(File file, String target_directory) throws IOException {
			unzip(new FileInputStream(file), target_directory);
		}
		
		/**
		 * Extract the contents of a given .zip file
		 * @param input_stream : the input file data as a stream
		 * @param target_directory : output directory
		 * @throws IOException 
		 */
		public static void unzip(InputStream input_stream, String target_directory) throws IOException{
			byte[] buffer = new byte[1024];
			Environment.FileSystem.mkDir(target_directory);
			ZipInputStream zip_input_stream = new ZipInputStream(input_stream);
			ZipEntry zip_entry = zip_input_stream.getNextEntry();
			while (zip_entry!=null) {
				if(zip_entry.isDirectory()) {
					Environment.FileSystem.mkDir(target_directory + Session.FileSeparator() + zip_entry.getName());
				}else {
					File output_file = new File(target_directory + Session.FileSeparator() + zip_entry.getName());
					Environment.FileSystem.mkDir(output_file.getParentFile().getAbsolutePath());
					FileOutputStream file_output_stream = new FileOutputStream(output_file);
					int length;
					while ((length = zip_input_stream.read(buffer)) > 0) {
						file_output_stream.write(buffer, 0, length);
					}
					file_output_stream.close();
				}			
				zip_entry = zip_input_stream.getNextEntry();
			}
			zip_input_stream.closeEntry();
			zip_input_stream.close();
		}
		
		/**
		 * Compress a given file or a directory into a .zip file
		 * @param source_path : source file or directory path
		 * @param target_archive_path : output path for .zip archive to be created
		 * @throws IOException
		 */
		public static void zip(String source_path, String target_archive_path) throws IOException {
			zip(new File(source_path), target_archive_path);
		}
		
		/**
		 * Compress a given file or a directory into a .zip file
		 * @param source_path : source file or directory object
		 * @param target_archive_path : output path for .zip archive to be created
		 * @throws IOException
		 */
		public static void zip(File source_data, String target_archive_path) throws IOException {
			byte[] buffer = new byte[1024];
			FileOutputStream file_output_stream = new FileOutputStream(target_archive_path);
			ZipOutputStream zip_output_stream = new ZipOutputStream(file_output_stream);
			ZipEntry zip_entry;
			FileInputStream input_stream;
			if(source_data.isFile()) {
	    		zip_entry = new ZipEntry(source_data.getName());
	    		zip_output_stream.putNextEntry(zip_entry);
	    		input_stream = new FileInputStream(source_data.getAbsolutePath());
	    		int length;
	    		while ((length = input_stream.read(buffer)) > 0) {
	    			zip_output_stream.write(buffer, 0, length);
	    		}
	    		input_stream.close();
	    		zip_output_stream.closeEntry();
	    		zip_output_stream.close();
			}else {
				ArrayList<String> zip_file_index = new ArrayList<String>();
				generateZipIndex(zip_file_index, source_data, source_data.getAbsolutePath());				
		    	for(String file : zip_file_index){
		    		zip_entry = new ZipEntry(file);
		        	zip_output_stream.putNextEntry(zip_entry);
		        	input_stream = new FileInputStream(source_data.getAbsolutePath() + Session.FileSeparator() + file);
		       	   int length;
		        	while ((length = input_stream.read(buffer)) > 0) {
		        		zip_output_stream.write(buffer, 0, length);
		        	}
		        	input_stream.close();
		    	}
		    	zip_output_stream.closeEntry();
		    	zip_output_stream.close();
			}
		}

		private static void generateZipIndex(ArrayList<String> zip_file_index, File node, String source_folder) {
			if (node.isFile()) {
				zip_file_index.add(generateZipEntry(node.getAbsoluteFile().toString(), source_folder));
			}
			if (node.isDirectory()) {
				String[] subNote = node.list();
				for (String filename : subNote) {
					generateZipIndex(zip_file_index, new File(node, filename), source_folder);
				}
			}
		}

		private static String generateZipEntry(String file, String source_folder) {
			return file.substring(source_folder.length() + 1, file.length());
		}
		
		
		/**
		 * List the files and directories within a given directory
		 * @param dir_path : the path to the directory to be listed
		 * @param extenions : filter allowed file extensions - null : allows all
		 * @param starts_with : name starts-with filter
		 * @param contains : name contains filter
		 * @param ends_with : name ends-with filter
		 * @param include_hidden : include hidden files and directories
		 * @param exclude_files : exclude files from output list
		 * @param exclude_dirs : exclude directories from output list
		 * @return list of File objects
		 */
		public static File[] list(String dir_path, String[] extenions, String starts_with, String contains, String ends_with, boolean include_hidden, boolean exclude_files, boolean exclude_dirs) {
			if(check(dir_path)){
				if(checkDir(dir_path)) {					
					FileFilter filter = new FileFilter() {
						
						@Override
						public boolean accept(File file) {
							boolean proceed = true;
							if(file.isHidden() && (include_hidden == false)) {
								proceed = false;
							}else {
								proceed = true;
							}
							if(file.isFile() && !exclude_files && proceed) {
								String file_extenion = "";
								String file_name = file.getName();
								String[] filename_split = file_name.split("\\.");
								if(filename_split.length > 0) {
									if(file.getName().startsWith(".")) {
										file_name = "." + filename_split[filename_split.length -2];
									}else {
										file_name = filename_split[filename_split.length -2];
									}									
									file_extenion = filename_split[filename_split.length -1];
								}
								if(extenions != null) {
									if(Arrays.asList(extenions).contains(file_extenion)) {
										proceed = true;
									}else {
										proceed = false;
									}
								}
								if(proceed == true && starts_with != null) {
									if(file_name.startsWith(starts_with)) {
										proceed = true;
									}else {
										proceed = false;
									}
								}
								if(proceed == true && contains != null) {
									if(file_name.contains(contains)) {
										proceed = true;
									}else {
										proceed = false;
									}
								}
								if(proceed == true && ends_with != null) {
									if(file_name.endsWith(ends_with + file_extenion)) {
										proceed = true;
									}else {
										proceed = false;
									}
								}
							} else if(file.isDirectory() && !exclude_dirs && proceed) {
								String sirectory_name = file.getName();
								if(proceed == true && starts_with != null) {
									if(sirectory_name.startsWith(starts_with)) {
										proceed = true;
									}else {
										proceed = false;
									}
								}
								if(proceed == true && contains != null) {
									if(sirectory_name.contains(contains)) {
										proceed = true;
									}else {
										proceed = false;
									}
								}
								if(proceed == true && ends_with != null) {
									if(sirectory_name.endsWith(ends_with)) {
										proceed = true;
									}else {
										proceed = false;
									}
								}								
							}else {
								proceed = false;
							}
							return proceed;
						}
					};					
					return new File(dir_path).listFiles(filter);
				}else {
					return null;
				}				
			}else {
				return null;
			}
		}
		
		/**
		 * List the files within a given directory
		 * @param dir_path : the path to the directory to be listed
		 * @param extenions : filter allowed file extensions - null : allows all
		 * @param starts_with : name starts-with filter
		 * @param contains : name contains filter
		 * @param ends_with : name ends-with filter
		 * @param include_hidden : include hidden files
		 * @return list of File objects
		 */
		public static File[] listFiles(String dir_path, String[] extenions, String starts_with, String contains, String ends_with, boolean include_hidden) {
			return list(dir_path, extenions, starts_with, contains, ends_with, include_hidden, false, true);
		}
		
		/**
		 * List the files within a given directory
		 * @param dir_path : the path to the directory to be listed
		 * @param extenions : filter allowed file extensions - null : allows all
		 * @param starts_with : name starts-with filter
		 * @param contains : name contains filter
		 * @param ends_with : name ends-with filter
		 */
		public static File[] listFiles(String dir_path, String[] extenions, String starts_with, String contains, String ends_with) {
			return list(dir_path, extenions, starts_with, contains, ends_with, true, false, true);
		}
		
		/**
		 * List the files within a given directory
		 * @param dir_path : the path to the directory to be listed
		 * @param extenions : filter allowed file extensions - null : allows all
		 * @param include_hidden : include hidden files
		 */
		public static File[] listFiles(String dir_path, String[] extenions, boolean include_hidden) {
			return list(dir_path, extenions, null, null, null, include_hidden, false, true);
		}
		
		/**
		 * List the files within a given directory
		 * @param dir_path : the path to the directory to be listed
		 * @param extenions : filter allowed file extensions - null : allows all
		 */
		public static File[] listFiles(String dir_path, String[] extenions) {
			return list(dir_path, extenions, null, null, null, true, false, true);
		}
		
		/**
		 * List the files within a given directory
		 * @param dir_path : the path to the directory to be listed
		 * @param include_hidden : include hidden files
		 */
		public static File[] listFiles(String dir_path, boolean include_hidden) {
			return list(dir_path, null, null, null, null, include_hidden, false, true);
		}
		
		/**
		 * List the files within a given directory
		 * @param dir_path : the path to the directory to be listed
		 */
		public static File[] listFiles(String dir_path) {
			return list(dir_path, null, null, null, null, true, false, true);
		}
		
		/**
		 * List the directories within a given directory
		 * @param dir_path : the path to the directory to be listed
		 * @param starts_with : name starts-with filter
		 * @param contains : name contains filter
		 * @param ends_with : name ends-with filter
		 * @param include_hidden : include hidden directories
		 * @return list of File objects
		 */
		public static File[] listDirs(String dir_path, String starts_with, String contains, String ends_with, boolean include_hidden) {
			return list(dir_path, null, starts_with, contains, ends_with, include_hidden, true, false);
		}
		
		/**
		 * List the directories within a given directory
		 * @param dir_path : the path to the directory to be listed
		 * @param starts_with : name starts-with filter
		 * @param contains : name contains filter
		 * @param ends_with : name ends-with filter
		 * @return list of File objects
		 */
		public static File[] listDirs(String dir_path, String starts_with, String contains, String ends_with) {
			return list(dir_path, null, starts_with, contains, ends_with, true, true, false);
		}
		
		/**
		 * List the directories within a given directory
		 * @param dir_path : the path to the directory to be listed
		 * @param include_hidden : include hidden directories
		 * @return list of File objects
		 */
		public static File[] listDirs(String dir_path, boolean include_hidden) {
			return list(dir_path, null, null, null, null, include_hidden, true, false);
		}
		
		/**
		 * List the directories within a given directory
		 * @param dir_path : the path to the directory to be listed
		 * @return list of File objects
		 */
		public static File[] listDirs(String dir_path) {
			return list(dir_path, null, null, null, null, true, true, false);
		}
	}
	
	public static class Resources{
		
		/**
		 * <pre>
		 * Returns the contents a given text resource as a String
		 * 
		 * <b>Example:</b>
		 * <i>
		 * Prerequisite : A text resource file (e.g. 'my.resources.MyResourceFile.txt')
		 * with the text 'Hello world !!!' as the file contents.
		 * </i>
		 * 
		 * System.out.println(Environment.getStringFromResource("/my/resources/MyResourceFile.txt"));
		 * 
		 * <b>Output:</b>
		 * Hello world !!!
		 * </pre>
		 * @param resource_path
		 * @return Returns the resource contents as String
		 */
		public static String getStringFromResource(String resource_path) {
			return getStringFromInputStream(Environment.class.getResourceAsStream(resource_path));
		}
		
		/**
		 * <pre>
		 * Returns the contents a given text InputStream as a String
		 * 
		 * <b>Example:</b>
		 * <i>
		 * Prerequisite : A text resource file (e.g. 'my.resources.MyResourceFile.txt')
		 * with the text 'Hello world !!!' as the file contents.
		 * </i>
		 * 
		 * System.out.println(Environment.getStringFromInputStream(getClass().getResourceAsStream("/my/resources/MyResourceFile.txt")));
		 * 
		 * <b>Output:</b>
		 * Hello world !!!
		 * </pre>
		 * @param input_stream : A text InputStream 
		 * @return Returns the InputStream contents as String
		 */
		public static String getStringFromInputStream(InputStream input_stream) {
			BufferedReader buffered_reader = null;
			StringBuilder string_builder = new StringBuilder();
			String line;
			try {
				buffered_reader = new BufferedReader(new InputStreamReader(input_stream));
				while ((line = buffered_reader.readLine()) != null) {
					string_builder.append(line);
				}
			} catch (IOException exception) {
				exception.printStackTrace();
			} finally {
				if (buffered_reader != null) {
					try {
						buffered_reader.close();
					} catch (IOException exception) {
						exception.printStackTrace();
					}
				}
			}
			return string_builder.toString();
		}
		
		
		/**
		 * Returns an SWT image for a given image resource path
		 * @param resource_path : the resource path for the source image
		 * @return An SWT image object
		 */
		public static Image getImageFromResource(String resource_path) {
			return getImageFromInputStream(Environment.class.getResourceAsStream(resource_path));
		}
		
		/**
		 * Converts an input stream to an SWT image object
		 * @param input_stream : the image data input stream
		 * @return An SWT image object
		 */
		public static Image getImageFromInputStream(InputStream input_stream) {
			return new Image(null, input_stream);
		}
		
		/**
		 * Copies (converts) an input stream to an output stream
		 * @param input_stream
		 * @param output_stream
		 * @throws IOException
		 */
		public static void copyInputStreamToOutputStream(InputStream input_stream, OutputStream output_stream) throws IOException {
		    byte[] buffer = new byte[1024];
		    int read;
		    while ((read = input_stream.read(buffer)) != -1) {
		        output_stream.write(buffer, 0, read);
		    }
		}
		
		/**
		 * Returns an Object output stream from an input stream
		 * @param input_stream
		 * @return
		 * @throws IOException
		 */
		public static OutputStream getOutputStreamFromInputStream(InputStream input_stream) throws IOException {
			OutputStream output_stream = new ByteArrayOutputStream();
			copyInputStreamToOutputStream(input_stream, output_stream);
			return output_stream;
		}
		
		/**
		 * Returns a File Object from an input stream
		 * @param input_stream
		 * @return
		 * @throws IOException
		 */
		public static File getFileFromInputStream(InputStream input_stream) throws IOException {
			File output = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
			output.deleteOnExit();
			FileOutputStream output_stream = new FileOutputStream(output);
			copyInputStreamToOutputStream(input_stream, output_stream);
			output_stream.close();
			return output;
		}
	}
}
