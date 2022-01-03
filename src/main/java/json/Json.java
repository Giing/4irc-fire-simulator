package json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import org.json.JSONArray;

import model.Sensor;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class Json<T extends Serialiazable> {
    private GsonBuilder GsonBuilder;
    final Class<T> generic;

    public Json(Class<T> genericClass) {
        GsonBuilder = new GsonBuilder();
        this.generic = genericClass;
        Field jsonDeserializerField = this.generic.getField("jsonDeserializer");
        Serialiazable s = (Serialiazable) jsonDeserializerField.get(null); // null car champ statique, pas besoin d'une instance
        this.GsonBuilder.registerTypeAdapter(this.generic, s);
    }

    public ArrayList<T> fromJson(JSONArray content) {
        Gson gson = this.GsonBuilder.create();
        Type type = TypeToken.getParameterized(List.class, this.generic).getType();
        return gson.fromJson(content.toString(), type);
    }
}
