/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.dao.offer;

import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.offer.OfferState;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ivan.vlcek
 */
public class OfferDaoImpl extends GenericHibernateDao<Offer> implements OfferDao {

    @Override
    public long getOffersCountForSupplier(long supplierId, OfferState offerState) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("supplier", supplierId);
        params.put("state", offerState);
        return (Long) runNamedQueryForSingleResult("getOffersCountForSupplier", params);
    }
}
