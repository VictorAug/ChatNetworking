package br.iesb.app.bean;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe ChatMessage. <br>
 * </br> Em vez de transferir somente a <code>String</code> de mensagem, o
 * conteúdo será um objeto que inclui várias informações:<br>
 * <br>
 * <code>String</code>: Nome do cliente<br>
 * <code>String</code>: Texto da mensagem<br>
 * <code>String</code>: Nome do cliente que receberá uma mensagem<br>
 * <code>Set<String></code>: Uma lista com o nome de todos os clientes online.
 */
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1;

    /** Nome do cliente. */
    private String name;

    /** Texto da mensagem. */
    private String text;

    /** Nome do cliente que receberá uma mensagem do tipo reservada. */
    private String nameReserved;

    /** Lista de todos os cliente online. */
    private Set<String> onlines = new HashSet<String>();

    private Set<String> fileNames = new HashSet<String>();

    private File file = null;

    /**
     * Para cada mensagem que o cliente envia p/ o servidor, ele vai dizer qual
     * é a ação que deseja executar.
     */
    private Action action;

    public enum Action {
	CONNECT, DISCONNECT, SEND_ONE, SEND_ALL, USERS_ONLINE, SEND_FILE, RECEIVE_FILE, UPLOAD_FILE;
    }

    public ChatMessage() {
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getNameReserved() {
	return nameReserved;
    }

    public void setNameReserved(String nameReserved) {
	this.nameReserved = nameReserved;
    }

    public Set<String> getOnlines() {
	return onlines;
    }

    public void setOnlines(Set<String> online) {
	this.onlines = online;
    }

    public Action getAction() {
	return action;
    }

    public void setAction(Action action) {
	this.action = action;
    }

    public Set<String> getFileNames() {
	return fileNames;
    }

    public void addAllFileNames(Set<String> fileNames) {
	fileNames.forEach(file -> this.fileNames.add(file));
    }

    public File getFile() {
	return file;
    }

    public void setFile(File file) {
	this.file = file;
	fileNames.add(file.getName());
    }

    @Override
    public String toString() {
	return "[name=" + name + ", files=" + file + ", fileNames=" + fileNames + ", nameReserved=" + nameReserved + ", onlines=" + onlines + "]";
    }

}
