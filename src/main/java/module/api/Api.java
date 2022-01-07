package module.api;

import module.api.services.EmergencyService;
import module.api.services.SensorService;

public class Api {
    public SensorService sensor;
    public EmergencyService emergency;

    private Http defaultClient;

    /** Constructeur privé */
    public Api(String baseUrl, String tokenApi)
    {
        try {
            defaultClient = new Http(baseUrl + "/api/", tokenApi);
            this.sensor = new SensorService(this.defaultClient);
            this.emergency = new EmergencyService(this.defaultClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
