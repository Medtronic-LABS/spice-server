package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.SideMenu;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.SideMenuRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.SideMenuServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * <p>
 * SideMenu Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SideMenuServiceTest {

    @InjectMocks
    private SideMenuServiceImpl sideMenuService;

    @Mock
    private SideMenuRepository sideMenuRepository;

    @Test
    void getSideMenus() {
        //given
        List<SideMenu> sideMenus = List.of(TestDataProvider.getSideMenu());

        //when
        when(sideMenuRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(sideMenus);

        //then
        List<SideMenu> sideMenuList = sideMenuService.getSideMenus();
        Assertions.assertNotNull(sideMenuList);

        //then
        sideMenuList = sideMenuService.getSideMenus();
        Assertions.assertFalse(Constants.SIDE_MENUS.isEmpty());
    }

    @Test
    void testGetSideMenus() {
        //given
        Constants.SIDE_MENUS = new ArrayList<>();
        List<SideMenu> sideMenus = List.of(TestDataProvider.getSideMenu());
        List<String> userRoles = List.of("testUserRole");

        //when
        when(sideMenuRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(sideMenus);

        //then
        List<SideMenu> sideMenuList = sideMenuService.getSideMenus(userRoles);
        Assertions.assertNotNull(sideMenuList);

        //then
        sideMenuList = sideMenuService.getSideMenus(userRoles);
        Assertions.assertFalse(Constants.SIDE_MENUS.isEmpty());
    }
}