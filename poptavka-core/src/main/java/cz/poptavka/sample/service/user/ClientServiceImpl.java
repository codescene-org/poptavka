/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.service.user;

import com.googlecode.ehcache.annotations.Cacheable;
import cz.poptavka.sample.dao.user.ClientDao;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.service.GenericServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Excalibur
 * @author Juraj Martinka
 */
public class ClientServiceImpl extends GenericServiceImpl<Client, ClientDao> implements ClientService {

    @Cacheable(cacheName = "cache5min")
    public Client getCachedClientById(long id) {
        return searchById(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Client> searchByCriteria(ClientSearchCriteria clientSearchCritera) {
        return this.getDao().searchByCriteria(clientSearchCritera);
    }

    @Override
    public Client create(Client client) {
        return super.create(client);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
