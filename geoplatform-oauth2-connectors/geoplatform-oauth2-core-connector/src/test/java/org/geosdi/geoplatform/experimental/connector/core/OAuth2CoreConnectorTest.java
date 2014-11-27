/*
 *  geo-platform
 *  Rich webgis framework
 *  http://geo-platform.org
 * ====================================================================
 *
 * Copyright (C) 2008-2014 geoSDI Group (CNR IMAA - Potenza - ITALY).
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. This program is distributed in the 
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. You should have received a copy of the GNU General 
 * Public License along with this program. If not, see http://www.gnu.org/licenses/ 
 *
 * ====================================================================
 *
 * Linking this library statically or dynamically with other modules is 
 * making a combined work based on this library. Thus, the terms and 
 * conditions of the GNU General Public License cover the whole combination. 
 * 
 * As a special exception, the copyright holders of this library give you permission 
 * to link this library with independent modules to produce an executable, regardless 
 * of the license terms of these independent modules, and to copy and distribute 
 * the resulting executable under terms of your choice, provided that you also meet, 
 * for each linked independent module, the terms and conditions of the license of 
 * that module. An independent module is a module which is not derived from or 
 * based on this library. If you modify this library, you may extend this exception 
 * to your version of the library, but you are not obligated to do so. If you do not 
 * wish to do so, delete this exception statement from your version. 
 *
 */
package org.geosdi.geoplatform.experimental.connector.core;

import javax.annotation.Resource;
import org.geosdi.geoplatform.experimental.connector.core.spring.connector.OAuth2CoreClientConnector;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class OAuth2CoreConnectorTest {

    private static final Logger logger = LoggerFactory.getLogger(
            OAuth2CoreConnectorTest.class);
    static final String CORE_CONNECTOR_KEY = "OAUTH2_CORE_FILE_PROP";
    //
    @Resource(name = "oauth2CoreClientConnector")
    private OAuth2CoreClientConnector oauth2CoreClientConnector;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty(CORE_CONNECTOR_KEY, "oauth2-core-test.prop");
    }

    @Before
    public void setUp() {
        Assert.assertNotNull(oauth2CoreClientConnector);
    }

    @Test
    public void coreSettingsTest() {
        logger.info("\n\n@@@@@@@@@@@@@@@@@@@@@@@@@@@OAUTH2 Settings : {}\n\n",
                this.oauth2CoreClientConnector.getClientSettings());
    }

    @Ignore(value = "OAUTH Server must be up")
    @Test
    public void oauth2GetAllAccountsTest() throws InterruptedException {
        logger.info("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@RETRIEVE ALL ACCOUNTS : {}",
                this.oauth2CoreClientConnector.getAllAccounts());
    }

    @AfterClass
    public static void afterClass() {
        System.clearProperty(CORE_CONNECTOR_KEY);
    }

}
