package br.iesb.cliente.app.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Class ChatMessage.
 */
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1;

    /** Nome do cliente. */
    private String name;

    /** Texto da mensagem. */
    private String text;

    /** Nome do cliente que terá uma mensagem do tipo reservada. */
    private String nameReserved;

    /** Lista de todos os cliente online. */
    private Set<String> setOnlines = new HashSet<String>();

    /** Atributo action. */
    private Action action;

    public enum Action {
	CONNECT, DISCONNECT, SEND_ONE, SEND_ALL, USERS_ONLINE;
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

}
