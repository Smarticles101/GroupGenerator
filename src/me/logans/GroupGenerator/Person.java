package me.logans.GroupGenerator;

import javafx.beans.property.SimpleStringProperty;

public class Person {
    public SimpleStringProperty name = new SimpleStringProperty("");
    public SimpleStringProperty group = new SimpleStringProperty("");

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public final String getName() {
        return nameProperty().get();
    }

    public final void setName(String name) {
        nameProperty().set(name);
    }

    public SimpleStringProperty groupProperty() {
        return group;
    }

    public final String getGroup() {
        return groupProperty().get();
    }

    public final void setGroup(String group) {
        groupProperty().set(group);
    }
}
