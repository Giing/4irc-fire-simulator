package module.json;

import module.model.Sensor;

public class JsonMapper {
    public final Json<Sensor> sensor = new Json<>(Sensor.class);

    /** Instance unique non préinitialisée */
    private static JsonMapper INSTANCE = null;

    /** Constructeur privé */
    private JsonMapper()
    {}
     
     
    /** Point d'accès pour l'instance unique du singleton */
    public static synchronized JsonMapper getInstance()
    {           
        if (INSTANCE == null)
        {   INSTANCE = new JsonMapper(); 
        }
        return INSTANCE;
    }
}