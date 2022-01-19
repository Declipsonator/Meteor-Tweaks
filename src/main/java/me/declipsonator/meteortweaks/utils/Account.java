package me.declipsonator.meteortweaks.utils;


public class Account {
    String name;
    String id;
    String error;
    String errorMessage;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "Module {name= " + name + ", id= " + id + ", error= " + error + ", errorMessage= " + errorMessage + "}";
    }
}
