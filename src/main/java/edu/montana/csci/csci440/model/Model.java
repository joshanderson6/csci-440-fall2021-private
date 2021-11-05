package edu.montana.csci.csci440.model;

import edu.montana.csci.csci440.util.DB;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

// base class for entities
public class Model {

    List<String> _errors = new LinkedList<>();

    public boolean create() {
        System.out.println(this.getClass().getSimpleName().toLowerCase());
        System.out.println(Arrays.toString(this.getClass().getDeclaredFields()));
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO ? (*) VALUES (?)"
             )) {
            stmt.setString(1, this.getClass().toString().toLowerCase());
            stmt.setString(2, Arrays.toString(this.getClass().getDeclaredFields()));
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
        return true;
    }

    public boolean update() {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public void delete() {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public boolean verify() {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public void addError(String err) {
        _errors.add(err);
    }

    public List<String> getErrors() {
        return _errors;
    }

    public boolean hasErrors(){
        return _errors.size() > 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return  false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                if (!Objects.equals(declaredField.get(this), (declaredField.get(obj)))) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        List<Object> values = new LinkedList<>();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                values.add(declaredField.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return Objects.hash(values.toArray());
    }
}
