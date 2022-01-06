package module.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import org.json.JSONArray;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class Json<T> {
    private GsonBuilder GsonBuilder;
    final Class<T> generic;

    public Json(Class<T> genericClass) {
        GsonBuilder = new GsonBuilder();
        this.generic = genericClass;
        Field jsonDeserializerField;
        JsonDeserializer<T> s;
        try {
            jsonDeserializerField = this.generic.getField("jsonAdapter");
            s = (JsonDeserializer<T>) jsonDeserializerField.get(null);
            this.GsonBuilder.registerTypeAdapter(this.generic, s);
        } catch (NoSuchFieldException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<T> fromJson(JSONArray content) {
        try {
            Gson gson = this.GsonBuilder.create();
            Type type = TypeToken.getParameterized(List.class, this.generic).getType();
            return gson.fromJson(content.toString(), type);
        } catch (Exception e) {
            return new ArrayList<T>();
        }
    }

    public String toJson(List<T> serializable) {
        Gson gson = this.GsonBuilder.create();
        return gson.toJson(serializable);
    }
}