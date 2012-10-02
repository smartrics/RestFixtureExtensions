package com.patternity.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for file manipulations
 * 
 * @author cyrille martraire
 */
public final class FileUtils {

	/**
	 * @return A String that represents the content of the file
	 */
    public static void writeFile(final File file, String content) {
        Writer writer;
        try {
            writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	public static File[] scanDirectory(final File path, final String extension) {
		if (!path.isDirectory()) {
			throw new IllegalArgumentException("Expected a directory path: " + path);
		}
		final FilenameFilter filenameFilter = new FilenameFilter() {

			public boolean accept(File file, String name) {
				return name.endsWith("." + extension);
			}

		};

        final List<File> files = new ArrayList<File>();
		final String[] filenames = path.list(filenameFilter);
		for (int i = 0; i < filenames.length; i++) {
			final String filename = filenames[i];
			try {
				final File file = new File(path, filename);
				if (file.exists() && file.canRead()) {
					files.add(file);
				}
			} catch (RuntimeException e) {
				System.err.println("Exception while scanning file " + filename);
				e.printStackTrace();
			}
		}
		return (File[]) files.toArray(new File[files.size()]);
	}

	/**
	 * @return A String that represents the content of the file
	 */
	public static String readFile(final File file) {
	    if(!file.exists()) {
            return "";
	    }
        try {
            return read(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        }
	}

    public static String readResource(String resourceName) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        return read(new InputStreamReader(is));
    }

    public static String read(final Reader reader) {
        String lineSep = System.getProperty("line.separator");
        final StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(reader);
            String str = null;
            while ((str = in.readLine()) != null) {
                buffer.append(str);
                buffer.append(lineSep);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static void makeDirs(File dir) {
    	if(dir == null) {
    		throw new IllegalArgumentException("null value for directory");
    	}
    	if(!dir.exists()) {
    		if(!dir.mkdirs()) {
    			throw new IllegalArgumentException("Unable to create directory: " + dir.getAbsolutePath());
    		}
    	}
    	if(!dir.isDirectory()) {
    		throw new IllegalArgumentException("not a directory: " + dir.getAbsolutePath());
    	}
    }
    
}
