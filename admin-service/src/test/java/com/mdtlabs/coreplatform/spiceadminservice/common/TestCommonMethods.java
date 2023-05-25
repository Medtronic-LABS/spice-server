package com.mdtlabs.coreplatform.spiceadminservice.common;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mockStatic;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.util.CommonUtil;

/**
 * <p>
 * This class has the common mock methods for static class.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
public class TestCommonMethods {

    private static MockedStatic<CommonUtil> commonUtil;
    private static MockedStatic<UserSelectedTenantContextHolder> userSelectedTenantContextHolder;

    public static void init() {
        commonUtil = mockStatic(CommonUtil.class);
        userSelectedTenantContextHolder = mockStatic(UserSelectedTenantContextHolder.class);
    }

    public static void getStaticMock() {
        commonUtil.when(CommonUtil::getAuthToken).thenReturn("BearerTest");
        userSelectedTenantContextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(1L);
    }

    public static void getStaticMockValidation(String searchTerm) {
        commonUtil.when(() -> CommonUtil.isValidSearchData(searchTerm, Constants.SEARCH_TERM)).thenReturn(true);
        commonUtil.when(() -> CommonUtil.isValidSearchData(searchTerm, Constants.REGEX_SEARCH_PATTERN)).thenReturn(true);
    }

    public static void getStaticMockValidationFalse(String searchTerm) {
        commonUtil.when(() -> CommonUtil.isValidSearchData(searchTerm, Constants.SEARCH_TERM)).thenReturn(false);
        commonUtil.when(() -> CommonUtil.isValidSearchData(searchTerm, Constants.REGEX_SEARCH_PATTERN)).thenReturn(false);
    }

    public static void cleanUp() {
        commonUtil.close();
        userSelectedTenantContextHolder.close();
    }

    public static ModelMapper setUp(Class<?> injectClass, String map, Object mockedObject) throws NoSuchFieldException, IllegalAccessException {
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        Field mapper = injectClass.getDeclaredField(map);
        mapper.setAccessible(true);
        mapper.set(mockedObject, modelMapper);
        return modelMapper;
    }
}
