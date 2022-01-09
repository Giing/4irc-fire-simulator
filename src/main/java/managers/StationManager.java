package managers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import module.api.Api;
import module.model.Emergency;
import module.model.Station;
import module.model.Team;
import module.socket.Subscriber;

public class StationManager extends Subscriber {
    private Api api;
    private List<Station> stations;

    public StationManager(Api api) {
        this.api = api;
        this.stations = this.api.station.getAll();
    }

    @Override
    public void onUpdateEmergencies(List<Emergency> emergencies) {
        // System.out.println("Update Emergencies !");
        // System.out.println(emergencies);

        for (Station station : this.stations) {
            // TODO: filter with isAlreadyHandled
            List<Emergency> linkedEmergenciesToStation = emergencies.stream().filter(emergency -> station.isInRadius(emergency.getLocation())).collect(Collectors.toList());

            System.out.println("Emergencies :");
            System.out.println(linkedEmergenciesToStation);
            System.out.println("is or will be handled by :");
            System.out.println(station);

            List<Team> teams = this.api.team.getAllByStation(station.getId());
            List<Team> availableTeams = teams.stream().filter(team -> team.isAvailable()).collect(Collectors.toList());
            
            for (Emergency emergency : linkedEmergenciesToStation) {
                List<Team> teamsHandlingFire = this.api.team.getAllByEmergency(emergency.getId());

                if(teamsHandlingFire.isEmpty()) {
                    // the fire is new

                    if(!availableTeams.isEmpty()) {
                        // is any team available

                        Team handleTeam = availableTeams.get(0);
    
                        handleTeam.setEmergencyId(emergency.getId());
                        handleTeam.setLocation(emergency.getLocation());
    
                        this.api.team.createOrUpdate(Arrays.asList(handleTeam));
                        availableTeams.remove(handleTeam);
                    } else {
                        // TODO: what to do if there is no team available ???
                    }
                } else {
                    // the fire is not new

                    if (emergency.getIntensity() == 0) {
                        for (Team team : teamsHandlingFire) {
                            team.setLocation(station.getLocation());
                            this.api.team.createOrUpdate(Arrays.asList(team));
                            this.api.team.reset(team.getId());
                        }
                    }
                }
            }
        }
    }
}