package module.api;

import module.api.services.SensorService;

public class Api {
    public SensorService sensor;

    /** Instance unique non préinitialisée */
    private static Api INSTANCE = null;
    private static Http defaultClient = new Http();

    /** Constructeur privé */
    private Api()
    {
        this.sensor = new SensorService(this.defaultClient);
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