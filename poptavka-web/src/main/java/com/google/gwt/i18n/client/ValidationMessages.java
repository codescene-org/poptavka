/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.i18n.client;

/**
 * Custom validation messages. Interface to represent the constants contained in
 * resource bundle:
 * 'com/google/gwt/sample/validation/client/ValidationMessages.properties'
 * TODO(nchalko) move this to the root package so client and server can share
 * the same properties files.
 */
public interface ValidationMessages extends ConstantsWithLookup {

    /**
     * Translated "Name must be at least {size} characters long.".
     *
     * @return translated "Name must be at least {min} characters long."
     */
    /**************************************************************************/
    /* Welcome Module - error messages.                                       */
    /**************************************************************************/
//    @DefaultStringValue("Name must be at least {min} characters long.")
//    @Key("custom.name.size.message")
//    String custom_name_size_message();
    @Key("address.notNull.state")
    String addressNotNullState();

    @Key("address.notNull.city")
    String addressNotNullCity();

    @Key("address.notNull.street")
    String addressNotNullStreet();

    @Key("address.notNull.zip")
    String addressNotNullZip();

    @Key("address.size.state")
    String addressSizeState();

    @Key("address.size.city")
    String addressSizeCity();

    @Key("address.size.street")
    String addressSizeStreet();

    @Key("address.size.zip")
    String addressSizeZip();

    /**************************************************************************/
    /* Create Demand Module - error messages.                                 */
    /**************************************************************************/
    //Preco musi byt ten key?
    @Key("demand.notNull.title")
    String demandNotNullTitle();

    @Key("demand.size.title")
    String demandSizeTitle();

    @Key("demand.notNull.price")
    String demandNotNullPrice();

    @Key("demand.pattern.price")
    String demandPatternPrice();

    @Key("demand.notNull.endDate")
    String demandNotNullEndDate();

    @Key("demand.future.endDate")
    String demandFutureEndDate();

    @Key("demand.notNull.validToDate")
    String demandNotNullValidToDate();

    @Key("demand.future.validToDate")
    String demandFutureValidToDate();

    @Key("demand.notNull.description")
    String demandNotNullDescription();

    @Key("demand.size.description")
    String demandSizeDescription();

    @Key("demand.pattern")
    String demandPattern();
}