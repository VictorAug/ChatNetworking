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
    private Set<String> setOnlines = new HashSet<String>();

    private Set<File> setFiles = new HashSet<File>();

    /**
     * Para cada mensagem que o cliente envia p/ o servidor, ele vai dizer qual
     * é a ação que deseja executar.
     */
    private Action action;

    public enum Action {
	CONNECT, DISCONNECT, SEND_ONE, SEND_ALL, USERS_ONLINE, SEND_FILE;
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

    public Set<String> getSetOnlines() {
	return setOnlines;
    }

    public void setSetOnlines(Set<String> setOnline) {
	this.setOnlines = setOnline;
    }

    public Action getAction() {
	return action;
    }

    public void setAction(Action action) {
	this.action = action;
    }

    public Set<File> getSetFiles() {
	return setFiles;
    }
    
    public void addFiles(File[] files) {
	for (int i = 0; i < files.length; i++) {
	    this.setFiles.add(files[i]);
	}
    }

}
