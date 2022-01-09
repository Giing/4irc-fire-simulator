package module.json;

import module.model.Emergency;
import module.model.Sensor;
import module.model.Station;
import module.model.Team;

public class JsonMapper {
    public final Json<Sensor> sensor = new Json<>(Sensor.class);
    public final Json<Emergency> emergency = new Json<>(Emergency.class);
    public final Json<Station> station = new Json<>(Station.class);
    public final Json<Team> team = new Json<>(Team.class);

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