package br.iesb.cliente.app.frame;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.iesb.app.bean.ChatMessage;

public class DataBase {

    private static Map<String, Set<File>> mapFiles = new HashMap<String, Set<File>>();

    public static Map<String, Set<File>> getMapFiles() {
	return new HashMap<String, Set<File>>(mapFiles);
    }

    public static void addFilesFromMessage(ChatMessage message) {
	if (mapFiles.containsKey(message.getName())) {
	    mapFiles.get(message.getName()).addAll(message.getSetFiles());
	} else {
	    mapFiles.put(message.getName(), message.getSetFiles());
	}
    }

}
