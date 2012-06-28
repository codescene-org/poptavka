/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.message.PotentialDemandMessage;

public class PotentialDemandMessageConverter extends AbstractConverter<UserMessage, PotentialDemandMessage> {
    @Override
    public PotentialDemandMessage convertToTarget(UserMessage userMessage) {
        final PotentialDemandMessage potentialDemandMessage = new PotentialDemandMessage();
        potentialDemandMessage.setClientName(userMessage.getMessage().getDemand().getClient()
                .getBusinessUser().getBusinessUserData().getDisplayName());
        potentialDemandMessage.setClientRating(userMessage.getMessage().getDemand().getClient().getOveralRating());
        return potentialDemandMessage;

    }

    @Override
    public UserMessage converToSource(PotentialDemandMessage potentialDemandMessage) {
        throw new UnsupportedOperationException("Conversion from PotentialDemandMessage to domain object UserMessage "
                + "is not implemented yet!");
    }
}
