package module.api.services;

import java.util.ArrayList;

import module.api.Http;
import module.json.JsonMapper;

public class ApiService {
    protected Http client;
    protected JsonMapper mapper;

    public ApiService (Http client) {
        this.client = client;
        this.mapper = JsonMapper.getInstance();
    }

    public ArrayList getAll() {
        return null;
    }
    public ArrayList createOrUpdate(ArrayList toCreateOrUpdate) {
        return null;
    }
}
