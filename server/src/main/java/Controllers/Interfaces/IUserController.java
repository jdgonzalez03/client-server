package Controllers.Interfaces;

import org.json.JSONObject;

public interface IUserController {
    JSONObject getAllUsersExcept(String email);
}
