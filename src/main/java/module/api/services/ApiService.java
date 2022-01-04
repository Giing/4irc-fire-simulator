package module.api.services;

import java.util.List;

import module.api.Http;
import module.json.JsonMapper;

public class ApiService {
    protected Http client;
    protected JsonMapper mapper;

    public ApiService (Http client) {
        this.client = client;
        this.mapper = JsonMapper.getInstance();
    }

    public List getAll() {
        return null;
    }
    public List createOrUpdate(List toCreateOrUpdate) {
        return null;
    }
}
