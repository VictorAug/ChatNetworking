package br.iesb.app.utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Utils {

    public final static Set<String> IMAGE_FORMAT = new HashSet<String>(Arrays.asList("jpeg","jpg","gif","png","tiff"));
    public final static Set<String> MIDIA_FORMAT = new HashSet<String>(Arrays.asList("mp3","mkv","avi","mp4","wmv","rmvb","3gp"));
    public final static Set<String> DOCUMENT_FORMAT = new HashSet<String>(Arrays.asList("doc","docx","txt","pptx","xml","pdf","ppt","pps"));
	    
    public static String getExtension(File f) {
	String ext = null;
	String s = f.getName();
	int i = s.lastIndexOf('.');
	if (i > 0 && i < s.length() - 1) {
	    ext = s.substring(i + 1).toLowerCase();
	}
	return ext;
    }

}
