package ord.example.user;

import com.example.db.dao.UserDao;
import com.example.db.entity.UserEntity;
import com.example.rest.UserRestService;
import jakarta.ws.rs.core.Response;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRestServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    UserEntity userEntity;

    @InjectMocks
    private UserRestService userRestService;

    @BeforeEach
    void init() {
        userEntity = new UserEntity();
    }

    @Test
    @DisplayName("Should create user")
    void createUser_success() {
        userEntity.setName("User");
        userEntity.setEmailAddress("test@example.com");

        UserEntity persistedUser = new UserEntity();
        persistedUser.setId(1L);
        persistedUser.setName("User");
        persistedUser.setEmailAddress("test@example.com");

        when(userDao.createUser(Mockito.any(UserEntity.class))).thenReturn(persistedUser);

        Response response = userRestService.createUser(userEntity);
        UserEntity responseUser = (UserEntity) response.getEntity();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(persistedUser.getId(), responseUser.getId());
        assertEquals(persistedUser.getName(), responseUser.getName());
        assertEquals(persistedUser.getEmailAddress(), responseUser.getEmailAddress());

    }

    @Test
    @DisplayName("Should update user")
    void updateUser_success() throws NotFoundException {
        userEntity.setId(1L);
        userEntity.setName("Njabulo");
        userEntity.setEmailAddress("njabulo@example.com");
        UserEntity expectedResponse = userEntity;

        when(userDao.updateUser(Mockito.any(UserEntity.class))).thenReturn(userEntity);
        UserEntity userUpdteEntiy = new UserEntity();
        userUpdteEntiy.setId(1L);
        userUpdteEntiy.setName("Mbanjwa");
        Response response = userRestService.updateUser(userEntity.getId(), userUpdteEntiy);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(userEntity.getId(), expectedResponse.getId());
        assertEquals(userEntity.getName(), expectedResponse.getName());
        assertEquals(userEntity.getEmailAddress(), expectedResponse.getEmailAddress());

        Mockito.verify(userDao, Mockito.times(1)).updateUser(Mockito.any(UserEntity.class));
    }

    @Test
    @DisplayName("Should return nothing")
    void getUser_nonExistingId() {
        Long userId = 20L;
        when(userDao.getUserById(userId)).thenReturn(null);

        UserEntity result = userRestService.getUser(userId);

        assertNull(result);
        verify(userDao, times(1)).getUserById(userId);
    }

    @Test
    @DisplayName("Should return list of users")
    void getListOfUsers_success() {
        List<UserEntity> users = Arrays.asList(
                new UserEntity(1L, "test1", "test1@example.com"),
                new UserEntity(2L, "test2", "test2@example.com")
        );
        when(userDao.getUsers()).thenReturn(users);

        List<UserEntity> result = userRestService.getListOfUsers();

        assertEquals(users, result);
        verify(userDao, times(1)).getUsers();
    }

    @Test
    @DisplayName("Should return empty list")
    void getListOfUsers_emptyList() {
        List<UserEntity> users = List.of();
        when(userDao.getUsers()).thenReturn(users);

        List<UserEntity> result = userRestService.getListOfUsers();

        assertTrue(result.isEmpty());
        verify(userDao, times(1)).getUsers();
    }

    @Test
    @DisplayName("Should delete a user and return the deleted user's details")
    void deleteUser_existingId() {
        Long userId = 1L;
        UserEntity deletedUser = new UserEntity();
        deletedUser.setId(userId);
        deletedUser.setName("Test User to Delete");
        deletedUser.setEmailAddress("delete@example.com");

        when(userDao.deleteUserById(userId)).thenReturn(deletedUser);
        Response response = userRestService.deleteUser(userId);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(response.getEntity(), "User with id = 1 successful deleted");
        verify(userDao, times(1)).deleteUserById(userId);
    }

    @Test
    @DisplayName("Should return NOT_FOUND when trying to delete a non-existing user")
    void deleteUser_nonExistingId() {
        Long userId = 99L;
        when(userDao.deleteUserById(userId)).thenReturn(null);

        Response response = userRestService.deleteUser(userId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        String responseMessage = (String) response.getEntity();
        assertEquals("User not found", responseMessage);
        verify(userDao, times(1)).deleteUserById(userId);
    }

}