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
package org.geosdi.geoplatform.experimental.connector.core.spring.connector;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.geosdi.geoplatform.core.model.GPAccountProject;
import org.geosdi.geoplatform.core.model.GPAuthority;
import org.geosdi.geoplatform.core.model.GPBBox;
import org.geosdi.geoplatform.core.model.GPFolder;
import org.geosdi.geoplatform.core.model.GPLayerInfo;
import org.geosdi.geoplatform.core.model.GPMessage;
import org.geosdi.geoplatform.core.model.GPOrganization;
import org.geosdi.geoplatform.core.model.GPProject;
import org.geosdi.geoplatform.core.model.GPRasterLayer;
import org.geosdi.geoplatform.core.model.GPUser;
import org.geosdi.geoplatform.core.model.GPVectorLayer;
import org.geosdi.geoplatform.core.model.GPViewport;
import org.geosdi.geoplatform.core.model.GeoPlatformServer;
import org.geosdi.geoplatform.experimental.connector.api.auth.token.OAuth2TokenBuilder;
import org.geosdi.geoplatform.experimental.connector.api.connector.AbstractClientConnector;
import org.geosdi.geoplatform.experimental.connector.api.settings.ConnectorClientSettings;
import org.geosdi.geoplatform.gui.shared.GPLayerType;
import org.geosdi.geoplatform.request.InsertAccountRequest;
import org.geosdi.geoplatform.request.PaginatedSearchRequest;
import org.geosdi.geoplatform.request.PutAccountsProjectRequest;
import org.geosdi.geoplatform.request.RequestByAccountProjectIDs;
import org.geosdi.geoplatform.request.SearchRequest;
import org.geosdi.geoplatform.request.folder.InsertFolderRequest;
import org.geosdi.geoplatform.request.folder.WSAddFolderAndTreeModificationsRequest;
import org.geosdi.geoplatform.request.folder.WSDDFolderAndTreeModifications;
import org.geosdi.geoplatform.request.folder.WSDeleteFolderAndTreeModifications;
import org.geosdi.geoplatform.request.layer.InsertLayerRequest;
import org.geosdi.geoplatform.request.layer.WSAddLayerAndTreeModificationsRequest;
import org.geosdi.geoplatform.request.layer.WSAddLayersAndTreeModificationsRequest;
import org.geosdi.geoplatform.request.layer.WSDDLayerAndTreeModificationsRequest;
import org.geosdi.geoplatform.request.layer.WSDeleteLayerAndTreeModificationsRequest;
import org.geosdi.geoplatform.request.message.MarkMessageReadByDateRequest;
import org.geosdi.geoplatform.request.organization.WSPutRolePermissionRequest;
import org.geosdi.geoplatform.request.organization.WSSaveRoleRequest;
import org.geosdi.geoplatform.request.project.ImportProjectRequest;
import org.geosdi.geoplatform.request.project.SaveProjectRequest;
import org.geosdi.geoplatform.request.server.WSSaveServerRequest;
import org.geosdi.geoplatform.request.viewport.InsertViewportRequest;
import org.geosdi.geoplatform.request.viewport.ManageViewportRequest;
import org.geosdi.geoplatform.responce.AccountProjectPropertiesDTO;
import org.geosdi.geoplatform.responce.FolderDTO;
import org.geosdi.geoplatform.responce.GetDataSourceResponse;
import org.geosdi.geoplatform.responce.MessageDTO;
import org.geosdi.geoplatform.responce.ProjectDTO;
import org.geosdi.geoplatform.responce.RasterPropertiesDTO;
import org.geosdi.geoplatform.responce.ServerDTO;
import org.geosdi.geoplatform.responce.ShortAccountDTOContainer;
import org.geosdi.geoplatform.responce.ShortLayerDTO;
import org.geosdi.geoplatform.responce.UserDTO;
import org.geosdi.geoplatform.responce.WSGetAccountProjectsResponse;
import org.geosdi.geoplatform.responce.authority.GetAuthorityResponse;
import org.geosdi.geoplatform.responce.collection.GuiComponentsPermissionMapData;
import org.geosdi.geoplatform.responce.collection.LongListStore;
import org.geosdi.geoplatform.responce.collection.TreeFolderElementsStore;
import org.geosdi.geoplatform.responce.role.WSGetRoleResponse;
import org.geosdi.geoplatform.responce.viewport.WSGetViewportResponse;
import org.geosdi.geoplatform.services.core.api.GPCoreServiceApi;

/**
 *
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
public class OAuth2CoreClientConnector extends AbstractClientConnector
        implements GPCoreServiceApi {

    public OAuth2CoreClientConnector(ConnectorClientSettings theClientSettings,
            Client theClient, OAuth2TokenBuilder theTokenBuilder) {
        super(theClientSettings, theClient, theTokenBuilder);
    }

    @Override
    public Long insertOrganization(GPOrganization organization) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean deleteOrganization(Long organizationID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long insertAccount(InsertAccountRequest insertAccountRequest)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long updateUser(GPUser user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean deleteAccount(Long accountID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPUser getUserDetail(Long userID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPUser getUserDetailByUsername(SearchRequest request) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPUser getUserDetailByUsernameAndPassword(String username,
            String plainPassword) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserDTO getShortUser(Long userID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserDTO getShortUserByUsername(SearchRequest request)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UserDTO> searchUsers(Long userID, PaginatedSearchRequest request)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ShortAccountDTOContainer getAllAccounts() {
        String accessToken = super.createToken();

        logger.debug("\n@@@@@@@@@@@@@@@@@@@@@@@@@ACQUIRE ACCESS_TOKEN VALUE "
                + ": {} for Method : getAllAccounts\n", accessToken);

        ClientResponse clientResponse = client.resource(
                super.getRestServiceURL().concat(
                        "jsonSecureAccount/accounts/getAllAccounts"))
                .header(HttpHeaders.AUTHORIZATION, "bearer ".concat(
                                accessToken))
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).get(
                        ClientResponse.class);

        try {
            String json = IOUtils.toString(clientResponse.getEntityInputStream());
            logger.info("\n\n@@@@@@@@@@@@@@@@@@@@@@@@@@@ECCOLA : {}", json);
        } catch (IOException ex) {
            logger.error("@@@@@@@@@@@@@@@@@@@IOException {}", ex);
        }

        return client.resource(super.getRestServiceURL().concat(
                "jsonSecureAccount/accounts/getAllAccounts"))
                .header(HttpHeaders.AUTHORIZATION, "bearer ".concat(
                                accessToken))
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).get(
                        ShortAccountDTOContainer.class);
    }

    @Override
    public ShortAccountDTOContainer getAccounts(String organization) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getAccountsCount(SearchRequest request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getUsersCount(String organization, SearchRequest request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GetAuthorityResponse getAuthorities(Long accountID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<GPAuthority> getAuthoritiesDetail(String accountNaturalID)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void forceTemporaryAccount(Long accountID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void forceExpiredTemporaryAccount(Long accountID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long insertAccountProject(GPAccountProject accountProject) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long updateAccountProject(GPAccountProject accountProject) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean deleteAccountProject(Long accountProjectID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPAccountProject getAccountProject(Long accountProjectID) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WSGetAccountProjectsResponse getAccountProjectsByAccountID(
            Long accountID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WSGetAccountProjectsResponse getAccountProjectsByProjectID(
            Long projectID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPAccountProject getAccountProjectByAccountAndProjectIDs(
            Long accountID, Long projectID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getAccountProjectsCount(Long accountID, SearchRequest request)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPAccountProject getDefaultAccountProject(Long accountID) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ProjectDTO> searchAccountProjects(Long accountID,
            PaginatedSearchRequest request) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPAccountProject getProjectOwner(Long projectID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean setProjectOwner(RequestByAccountProjectIDs request) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPProject getDefaultProject(Long accountID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ProjectDTO getDefaultProjectDTO(Long accountID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPProject updateDefaultProject(Long accountID, Long projectID) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean saveAccountProjectProperties(
            AccountProjectPropertiesDTO accountProjectProperties) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ShortAccountDTOContainer getAccountsByProjectID(Long projectID)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ShortAccountDTOContainer getAccountsToShareByProjectID(Long projectID)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean updateAccountsProjectSharing(
            PutAccountsProjectRequest apRequest) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long saveProject(SaveProjectRequest saveProjectRequest) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long insertProject(GPProject project) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long updateProject(GPProject project) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean deleteProject(Long projectID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPProject getProjectDetail(Long projectID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer getNumberOfElementsProject(Long projectID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setProjectShared(Long projectID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPViewport getDefaultViewport(Long accountProjectID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WSGetViewportResponse getAccountProjectViewports(
            Long accountProjectID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long insertViewport(InsertViewportRequest insertViewportReq) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long updateViewport(GPViewport viewport) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPViewport getViewportById(Long idViewport) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean deleteViewport(Long viewportID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveOrUpdateViewportList(ManageViewportRequest request) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void replaceViewportList(ManageViewportRequest request) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long insertFolder(InsertFolderRequest insertFolderRequest) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long updateFolder(GPFolder folder) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean deleteFolder(Long folderID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long saveFolderProperties(Long folderID, String folderName,
            boolean checked, boolean expanded) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long saveAddedFolderAndTreeModifications(
            WSAddFolderAndTreeModificationsRequest sftModificationRequest)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean saveDeletedFolderAndTreeModifications(
            WSDeleteFolderAndTreeModifications sdfModificationRequest) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean saveDragAndDropFolderAndTreeModifications(
            WSDDFolderAndTreeModifications sddfTreeModificationRequest) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FolderDTO getShortFolder(Long folderID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPFolder getFolderDetail(Long folderID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<FolderDTO> getChildrenFolders(Long folderID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TreeFolderElementsStore getChildrenElements(Long folderID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ProjectDTO getProjectWithRootFolders(Long projectID, Long accountID)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ProjectDTO getProjectWithExpandedFolders(Long projectID,
            Long accountID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ProjectDTO exportProject(Long projectID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long importProject(ImportProjectRequest impRequest) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long insertLayer(InsertLayerRequest layerRequest) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long updateRasterLayer(GPRasterLayer layer) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long updateVectorLayer(GPVectorLayer layer) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean deleteLayer(Long layerID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long saveAddedLayerAndTreeModifications(
            WSAddLayerAndTreeModificationsRequest addLayerRequest) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LongListStore saveAddedLayersAndTreeModifications(
            WSAddLayersAndTreeModificationsRequest addLayersRequest) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean saveDeletedLayerAndTreeModifications(
            WSDeleteLayerAndTreeModificationsRequest deleteLayerRequest) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean saveCheckStatusLayerAndTreeModifications(Long layerID,
            boolean checked) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean saveDragAndDropLayerAndTreeModifications(
            WSDDLayerAndTreeModificationsRequest ddLayerReq) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean saveLayerProperties(RasterPropertiesDTO layerProperties)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPRasterLayer getRasterLayer(Long layerID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPVectorLayer getVectorLayer(Long layerID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ShortLayerDTO getShortLayer(Long layerID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ShortLayerDTO> getLayers(Long projectID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPBBox getBBox(Long layerID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPLayerInfo getLayerInfo(Long layerID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPLayerType getLayerType(Long layerID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GetDataSourceResponse getLayersDataSourceByProjectID(Long projectID)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WSGetRoleResponse getAllRoles(String organization) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GuiComponentsPermissionMapData getAccountPermission(Long accountID)
            throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GuiComponentsPermissionMapData getRolePermission(String role,
            String organization) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean updateRolePermission(
            WSPutRolePermissionRequest putRolePermissionReq) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean saveRole(WSSaveRoleRequest saveRoleReq) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long insertServer(GeoPlatformServer server) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long updateServer(GeoPlatformServer server) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean deleteServer(Long serverID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ServerDTO> getAllServers(String organizazionName) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GeoPlatformServer getServerDetail(Long serverID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServerDTO getShortServer(String serverUrl) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GeoPlatformServer getServerDetailByUrl(String serverUrl) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServerDTO saveServer(WSSaveServerRequest saveServerReq) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long insertMessage(GPMessage message) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean insertMultiMessage(MessageDTO messageDTO) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean deleteMessage(Long messageID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GPMessage getMessageDetail(Long messageID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<GPMessage> getAllMessagesByRecipient(Long recipientID) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<GPMessage> getUnreadMessagesByRecipient(Long recipientID) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean markMessageAsRead(Long messageID) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean markAllMessagesAsReadByRecipient(Long recipientID) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean markMessagesAsReadByDate(
            MarkMessageReadByDateRequest markMessageAsReadByDateReq) throws
            Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getConnectorName() {
        return "GeoPlatform OAuth2 Core Client Connector";
    }
}
