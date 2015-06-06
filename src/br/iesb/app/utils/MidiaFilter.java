package br.iesb.app.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MidiaFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
	if (f.isDirectory()) {
	    return true;
	}
	String extension = Utils.getExtension(f);
	if (extension != null) {
	    for (String ext : Utils.MIDIA_FORMAT) {
		if (ext.equals(extension)) {
		    return true;
		}
	    }
	    return false;
	}
	return false;
    }

    @Override
    public String getDescription() {
	return "Apenas video e música (mp3, mkv, avi, mp4, wmv, rmvb, 3gp)";
    }

}
