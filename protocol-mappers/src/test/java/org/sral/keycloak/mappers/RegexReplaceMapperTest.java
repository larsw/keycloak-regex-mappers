package org.sral.keycloak.mappers;

import org.junit.Test;
import org.keycloak.models.GroupModel;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.mappers.FullNameMapper;
import org.keycloak.protocol.oidc.mappers.OIDCAttributeMapperHelper;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.AccessToken;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class RegexReplaceMapperTest {
    static final String CLAIM_NAME = "azc";

    @Test
    public void shouldTokenMapperDisplayCategory() {
        final String tokenMapperDisplayCategory = new FullNameMapper().getDisplayCategory();
        assertThat(new RegexReplaceMapper().getDisplayCategory()).isEqualTo(tokenMapperDisplayCategory);
    }

    @Test
    public void shouldHaveDisplayType() {
        assertThat(new RegexReplaceMapper().getDisplayType()).isNotBlank();
    }

    @Test
    public void shouldHaveHelpText() {
        assertThat(new RegexReplaceMapper().getHelpText()).isNotBlank();
    }

    @Test
    public void shouldHaveIdId() {
        assertThat(new RegexReplaceMapper().getId()).isNotBlank();
    }

    @Test
    public void shouldHaveAVeryLowPriority() { assertThat(new RegexReplaceMapper().getPriority()).isEqualTo(10000); }

    @Test
    public void shouldHaveProperties() {
        final List<String> configPropertyNames = new RegexReplaceMapper()
                .getConfigProperties()
                .stream()
                .map(ProviderConfigProperty::getName)
                .collect(Collectors.toList());
        assertThat(configPropertyNames)
                .containsExactly(
                        RegexReplaceMapper.TARGET_CLAIMS_PROPERTY,
                        RegexReplaceMapper.REPLACEMENT_MAP_PROPERTY,
                        OIDCAttributeMapperHelper.INCLUDE_IN_ID_TOKEN,
                        OIDCAttributeMapperHelper.INCLUDE_IN_ACCESS_TOKEN,
                        OIDCAttributeMapperHelper.INCLUDE_IN_USERINFO);
    }

    @Test
    public void shouldAddClaim() {
        var userModel = given();

    }

    private UserSessionModel given() {
        var userSession = Mockito.mock(UserSessionModel.class);
        var group1 = Mockito.mock(GroupModel.class);
        when(group1.getName()).thenReturn("myGroup");
        Set<GroupModel> groups = new HashSet<>();
        groups.add(group1);
        UserModel user = Mockito.mock(UserModel.class);
        when(user.getGroups()).thenReturn(groups);
        when(userSession.getUser()).thenReturn(user);
        return userSession;
    }

    private AccessToken transformAccessToken(UserSessionModel userSessionModel) {
        final ProtocolMapperModel mappingModel = new ProtocolMapperModel();
        mappingModel.setConfig(createConfig());
        return new RegexReplaceMapper().transformAccessToken(new AccessToken(), mappingModel, null, userSessionModel, null);
    }

    private Map<String, String> createConfig() {
        final Map<String, String> result = new HashMap<>();

        final Map<String, String> replacements = new HashMap<>();

        result.put(RegexReplaceMapper.TARGET_CLAIMS_PROPERTY, "azc");
        //result.put(RegexReplaceMapper.REPLACEMENT_MAP_PROPERTY, replacements);
        result.put(RegexMapper.REGEX_PATTERN_PROPERTY, "(.*)");
        result.put(RegexMapper.FULL_PATH_PROPERTY, "false");
        result.put(RegexMapper.MATCH_GROUP_NUMBER_OR_NAME_PROPERTY, "1");
        return result;
    }

}
