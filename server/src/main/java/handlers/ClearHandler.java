package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    private ClearService service;

    public ClearHandler(ClearService clearService) {
        service = clearService;
    }

    public String ClearDatabase(Request request, Response response) throws DataAccessException {
        service.clear();
        response.status(200);
        return new Gson().toJson(null);
    }
}
