package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(UserRPCService.URL)
public interface UserRPCService extends RemoteService {

    String URL = "service/user";

    UserDetail loginUser(String email, String password) throws RPCException;

    BusinessUserDetail getBusinessUserById(Long userId) throws RPCException;

    UserDetail getUserById(Long userId) throws RPCException;

    BusinessUserDetail getLoggedBusinessUser() throws RPCException;

    UserDetail getLoggedUser() throws RPCException;
}
