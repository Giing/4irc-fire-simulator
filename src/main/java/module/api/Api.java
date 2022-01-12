package module.api;

import module.api.services.EmergencyService;
import module.api.services.SensorService;
import module.api.services.StationService;
import module.api.services.TeamService;

public class Api {
    public SensorService sensor;
    public EmergencyService emergency;
    public StationService station;
    public TeamService team;

    private Http defaultClient;

    /** Constructeur privé */
    public Api(String baseUrl, String tokenApi)
    {
        try {
            defaultClient = new Http(baseUrl + "/api/", tokenApi);
            this.sensor = new SensorService(this.defaultClient);
            this.emergency = new EmergencyService(this.defaultClient);
            this.station = new StationService(this.defaultClient);
            this.team = new TeamService(this.defaultClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
