package json;

import com.google.gson.JsonDeserializer;

public abstract class Serialiazable<T> {
    public abstract T getJsonDeserializer();
}
