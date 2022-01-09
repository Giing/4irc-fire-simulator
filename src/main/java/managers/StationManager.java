package managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import module.api.Api;
import module.api.Map;
import module.model.Coord;
import module.model.Emergency;
import module.model.Station;
import module.model.Team;
import module.socket.Subscriber;

public class StationManager extends Subscriber {
    private Api api;
    private Map mapApi;
    private List<Station> stations;
    private List<Emergency> unhandledEmergencies;

    public StationManager(Api api, Map mapApi) {
        this.api = api;
        this.mapApi = mapApi;

        this.stations = this.api.station.getAll();
        this.unhandledEmergencies =  this.api.emergency.getAll();
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
            List<Team> availableTeams = teams.stream().filter(team -> team.isAvailable() && !team.isHandling).collect(Collectors.toList());
            
            for (Emergency emergency : linkedEmergenciesToStation) {
                List<Team> teamsHandlingFire = this.api.team.getAllByEmergency(emergency.getId());

                if(teamsHandlingFire.isEmpty()) {
                    // the fire is new

                    if(!availableTeams.isEmpty()) {
                        // is any team available

                        Team handleTeam = availableTeams.get(0);
    
                        handleTeam.setEmergencyId(emergency.getId());
                        try {
                            List<Coord> steps = this.mapApi.getDirection(handleTeam.getLocation(), emergency.getLocation());
                            updateTeamDirection(Collections.enumeration(steps), handleTeam);
                        } catch (Exception e) {
                            
                        }

    
                        this.api.team.createOrUpdate(Arrays.asList(handleTeam));
                        availableTeams.remove(handleTeam);
                    } else {
                        // TODO: what to do if all teams are unvailable ???
                        this.unhandledEmergencies.add(emergency);
                    }
                } else {
                    // the fire is not new

                    if (emergency.getIntensity() == 0) {
                        for (Team team : teamsHandlingFire) {
                            try {
                                if(this.unhandledEmergencies.isEmpty()) {
                                    // back to station
                                    List<Coord> steps = this.mapApi.getDirection(team.getLocation(), station.getLocation());
                                    updateTeamDirection(Collections.enumeration(steps), team);
                                } else {
                                    handleNewEmergency(team);
                                }

                            } catch (Exception e) {
                                
                            }
                            this.api.team.reset(team.getId());
                        }
                    }
                }
            }
        }
    }

    private void handleNewEmergency(Team team) throws IOException { 
        Emergency newEmergency = this.unhandledEmergencies.get(0);
        this.unhandledEmergencies.remove(0);
        affectEmergencyToTeam(newEmergency, team);
    }

    private void affectEmergencyToTeam(Emergency emergency, Team team) throws IOException {
        team.setEmergencyId(emergency.getId());
        List<Coord> steps = this.mapApi.getDirection(team.getLocation(), emergency.getLocation());
        updateTeamDirection(Collections.enumeration(steps), team);
    }

    private void updateTeamDirection(Enumeration<Coord> steps, Team teamToUpdate) {
        if(!teamToUpdate.isHandling) {
            Api api = this.api;
            System.out.println("Dispatch team: " + teamToUpdate);
            teamToUpdate.isHandling = true;

            new Timer().scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){
                    if(steps.hasMoreElements()) {
                        Coord step = steps.nextElement();
                        teamToUpdate.setLocation(step);
                        api.team.createOrUpdate(Arrays.asList(teamToUpdate));
                    } else {
                        teamToUpdate.isHandling = false;
                        this.cancel();
                        return;
                    }
                }
            }, 0, 1000);
        }
    }
}