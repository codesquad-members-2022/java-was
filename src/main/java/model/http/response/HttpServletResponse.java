package model.http.response;

import java.io.DataOutputStream;

public interface HttpServletResponse {
    DataOutputStream getDataOutputStream();
}
