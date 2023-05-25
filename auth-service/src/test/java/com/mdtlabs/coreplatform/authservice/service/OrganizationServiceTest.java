package com.mdtlabs.coreplatform.authservice.service;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.mdtlabs.coreplatform.authservice.repository.OrganizationRepository;
import com.mdtlabs.coreplatform.authservice.service.impl.OrganizationServiceImpl;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.util.TestConstants;
import com.mdtlabs.coreplatform.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Organization service implementation class.
 * </p>
 *
 * @author Divya S created on March 03, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrganizationServiceTest {

    @InjectMocks
    OrganizationServiceImpl organizationService;

    @Mock
    private OrganizationRepository organizationRepository;

    @ParameterizedTest
    @ValueSource(strings = {Constants.COUNTRY, Constants.OPERATING_UNIT, Constants.ACCOUNT, Constants.SITE})
    void getChildOrganizations(String formName) {
        //given
        List<Organization> organizations = TestDataProvider.getOrganizations();
        organizations.get(0).setFormName(formName);
        Set<Long> childOrgIds = Set.of(TestConstants.ONE);

        //when
        when(organizationRepository.findByParentOrganizationId(TestConstants.ONE)).thenReturn(organizations);
        when(organizationRepository.findByParentOrganizationIdIn(childOrgIds)).thenReturn(organizations);

        //then
        Set<Long> actualChildOrganizationIds = organizationService.getChildOrganizations(TestConstants.ONE, formName);
        assertNotNull(childOrgIds);
        assertEquals(childOrgIds.size(), actualChildOrganizationIds.size());
        assertEquals(childOrgIds, actualChildOrganizationIds);
    }
}