package com.example.command_web_service;

import com.example.command_web_service.persist.UserRepository;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "com.example.command_web_service.CommandWs")
public class CommandWsImpl implements CommandWs {
    private final UserRepository userRepository = new UserRepository();

    @Override
    public void insertUser(@WebParam(name = "userName") String user,
                           @WebParam(name = "groupName") String group,
                           @WebParam(name = "roleName") String role) {
        userRepository.insertUser(user, group, role);
    }

    @Override
    public void updateUser(@WebParam(name = "userName") String user,
                           @WebParam(name = "newGroupName") String group,
                           @WebParam(name = "newRoleName") String role) {
        userRepository.updateUser(user, group, role);
    }

    @Override
    public void deleteUser(@WebParam(name = "userName") String user) {
        userRepository.deleteUser(user);
    }

    @Override
    public List<String> getUserNameListByRoleName(@WebParam(name = "roleName") String role) {
        return userRepository.getUsersNamesByRoles(role);
    }

    @Override
    public void insertGroup(@WebParam(name = "groupName") String groupName) {
        userRepository.insertGroup(groupName);
    }

    @Override
    public void updateGroup(@WebParam(name = "oldGroupName") String oldGroupName,
                            @WebParam(name = "newGroupName") String newGroupName) {
        userRepository.updateGroup(oldGroupName, newGroupName);
    }

    @Override
    public void deleteGroup(@WebParam(name = "groupName") String groupName) {
        userRepository.deleteGroup(groupName);
    }

    @Override
    public List<String> getGroups() {
        return userRepository.getGroups();
    }

    @Override
    public List<String> getListOfUsersByGroupName(@WebParam(name = "groupName") String groupName) {
        return userRepository.getListOfUsersByGroupName(groupName);
    }

    @Override
    public String getRoleNameByUserName(String userName) {

        return userRepository.getRoleNameByUserName(userName);
    }

    @Override
    public List<String> getListOfChatIdByRoleName(@WebParam(name = "roleName") String roleName) {
        return userRepository.getListOfChatIdByRoleName(roleName);
    }

    @Override
    public List<String> getListOfUsers() {
        return userRepository.getListOfUsers();
    }

    @Override
    public void putChatIdByUserName(@WebParam(name = "chatId") String chatId,
                                    @WebParam(name = "userName") String userName) {
        userRepository.putChatIdByUserName(chatId, userName);
    }

    @Override
    public String getChatIdByUserName(@WebParam(name = "userName") String userName) {
        return userRepository.getChatIdByUserName(userName);
    }
}
