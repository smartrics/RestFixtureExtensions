package com.patternity.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Writes a content to a file through an optional template file (head $0$, body $1$)
 * to be evaluated.
 * 
 * @author cyrille martraire
 */
public class TemplatedWriter {

    private boolean needsClose = false;

    private final OutputStream outStream;

    private String templateResourceName;

    private String templateContent;

    public TemplatedWriter(File outFile, File templateFile) {
        if (outFile == null) {
            throw new IllegalArgumentException("Output file must not be null");
        }
        try {
            this.outStream = new FileOutputStream(outFile);
            needsClose = true;
            if (templateFile != null) {
                templateContent = readFile(templateFile);
                templateResourceName = templateFile.getAbsolutePath();
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found: " + outFile.getAbsolutePath());
        }
    }

    public TemplatedWriter(File outFile) {
        this(outFile, "template.svg");
    }

    public TemplatedWriter(File outFile, String templateResourceName) {
        if (outFile == null) {
            throw new IllegalArgumentException("Output file must not be null");
        }
        try {
            this.outStream = new FileOutputStream(outFile);
            this.needsClose = true;
            this.templateResourceName = templateResourceName;
            this.templateContent = FileUtils.readResource(templateResourceName);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found: " + outFile.getAbsolutePath());
        }
    }

    public TemplatedWriter(OutputStream out, String templateResourceName) {
        if (out == null) {
            throw new IllegalArgumentException("Output stream must not be null");
        }
        this.needsClose = false;
        this.outStream = out;
        this.templateResourceName = templateResourceName;
        this.templateContent = FileUtils.readResource(templateResourceName);
    }

    public void write(String content, String header) {
        final String output = evaluateTemplate(content, header);
        try {
            outStream.write(output.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Unable to write", e);
        }
        if (needsClose) {
            try {
                outStream.close();
            } catch (IOException e) {
                // nothing to do
            }
        }
    }

	public String evaluateTemplate(String content, String header) {
		if (content == null) {
			throw new IllegalArgumentException("Content must not be null");
		}
		String output = content;
        if (templateContent != null) {
            output = templateContent;
			output = output.replaceFirst("\\$0\\$", (header == null ? "" : header));
			output = output.replaceFirst("\\$1\\$", content);
		}
		return output;
	}

	private static String readFile(File file) {
		return FileUtils.readFile(file);
	}

}
