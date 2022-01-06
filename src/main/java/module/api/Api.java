package module.api;

import module.api.services.EmergencyService;
import module.api.services.SensorService;

public class Api {
    public SensorService sensor;
    public EmergencyService emergency;

    /** Instance unique non préinitialisée */
    private static Api INSTANCE = null;
    private static Http defaultClient;

    static {
        try {
            defaultClient = new Http();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Constructeur privé */
    private Api()
    {
        this.sensor = new SensorService(this.defaultClient);
        this.emergency = new EmergencyService(this.defaultClient);
    }
     
    /** Point d'accès pour l'instance unique du singleton */
    public static synchronized Api getInstance()
    {           
        if (INSTANCE == null)
        {   INSTANCE = new Api(); 
        }
        return INSTANCE;
    }
}
