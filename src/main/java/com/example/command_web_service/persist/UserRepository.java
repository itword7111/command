package com.example.command_web_service.persist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final int defaultValue = Integer.MAX_VALUE;
    private static Connection connection;
    private static PreparedStatement preparedStatement;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            //для localhost
//            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/habrdb", "user", "pass");
            //для облачного сервера
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/admin", "admin", "aston");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertUser(String userName, String groupName, String roleName) {
        try {
            connection.setAutoCommit(false);

            preparedStatement = connection
                    .prepareStatement("SELECT user_name FROM users WHERE user_name = ?");
            preparedStatement.setString(1, userName);
            ResultSet rs = preparedStatement.executeQuery();
            boolean userExists = false;
            while (rs.next()) {
                userExists = true;
            }
            rs.close();
            if (userExists) {
                connection.commit();
                return;
            }

            int groupId = getGroupId(groupName);
            if (groupId == defaultValue) return;

            int roleId = getRoleId(roleName);
            if (roleId == defaultValue) return;

            preparedStatement = connection
                    .prepareStatement("INSERT INTO users(user_name, group_id, role_id) VALUES (?, ?, ?)");
            preparedStatement.setString(1, userName);
            preparedStatement.setInt(2, groupId);
            preparedStatement.setInt(3, roleId);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateUser(String userName, String groupName, String roleName) {
        try {
            connection.setAutoCommit(false);

            int groupId = getGroupId(groupName);
            if (groupId == defaultValue) return;

            int roleId = getRoleId(roleName);
            if (roleId == defaultValue) return;

            preparedStatement = connection
                    .prepareStatement("UPDATE users SET group_id = ?, role_id = ? WHERE user_name = ?");
            preparedStatement.setInt(1, groupId);
            preparedStatement.setInt(2, roleId);
            preparedStatement.setString(3, userName);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteUser(String user_name) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM users WHERE user_name = ?");
            preparedStatement.setString(1, user_name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getUsersNamesByRoles(String role) {
        List<String> usersNamesList = new ArrayList<>();
        try {
            connection.setAutoCommit(false);

            int roleId = getRoleId(role);
            if (roleId == defaultValue) return usersNamesList;

            preparedStatement = connection.prepareStatement("SELECT user_name FROM users WHERE role_id = ?");
            preparedStatement.setInt(1, roleId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                usersNamesList.add(rs.getString(1));
            }
            connection.commit();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return usersNamesList;
    }

    public void insertGroup(String groupName) {
        try {
            connection.setAutoCommit(false);
            int groupId = getGroupId(groupName);
            if (groupId != defaultValue) return;

            preparedStatement = connection
                    .prepareStatement("INSERT INTO groups (id, name) VALUES (DEFAULT , ?)");
            preparedStatement.setString(1, groupName);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateGroup(String oldGroupName, String newGroupName) {
        try {
            connection.setAutoCommit(false);
            int groupId = getGroupId(oldGroupName);
            if (groupId == defaultValue) return;
            preparedStatement = connection
                    .prepareStatement("UPDATE groups SET name = ? WHERE id = ?");
            preparedStatement.setString(1, newGroupName);
            preparedStatement.setInt(2, groupId);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteGroup(String groupName) {
        try {
            connection.setAutoCommit(false);
            int groupId = getGroupId(groupName);
            if (groupId == defaultValue) return;

            preparedStatement = connection
                    .prepareStatement("DELETE FROM users WHERE group_id = ?");
            preparedStatement.setInt(1, groupId);
            preparedStatement.executeUpdate();

            preparedStatement = connection
                    .prepareStatement("DELETE FROM groups WHERE name = ?");
            preparedStatement.setString(1, groupName);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getGroups() {
        List<String> groupsNamesList = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT name FROM groups");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                groupsNamesList.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return groupsNamesList;
    }

    public List<String> getListOfUsersByGroupName(String groupName) {
        List<String> groupsNamesList = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            int groupId = getGroupId(groupName);
            if (groupId == defaultValue) return groupsNamesList;

            preparedStatement = connection
                    .prepareStatement("SELECT user_name FROM users WHERE group_id = ?");
            preparedStatement.setInt(1, groupId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                groupsNamesList.add(rs.getString(1));
            }
            rs.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return groupsNamesList;
    }

    public String getRoleNameByUserName(String userName) {
        String roleName = null;
        int roleId = defaultValue;
        ResultSet rs;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("SELECT role_id FROM users WHERE user_name = ?");
            preparedStatement.setString(1, userName);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                roleId = rs.getInt(1);
            }
            if (roleId == defaultValue) {
                rs.close();
                connection.commit();
                return null;
            }
            preparedStatement = connection.prepareStatement("SELECT name FROM roles WHERE id = ?");
            preparedStatement.setInt(1, roleId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                roleName = rs.getString(1);
            }
            rs.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roleName;
    }

    public List<String> getListOfChatIdByRoleName(String roleName) {
        List<String> listOfChatId = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            int roleId = getRoleId(roleName);
            if (roleId == defaultValue) return listOfChatId;

            preparedStatement = connection
                    .prepareStatement("SELECT chat_id FROM users WHERE role_id = ?");
            preparedStatement.setInt(1, roleId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                listOfChatId.add(rs.getString(1));
            }
            rs.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listOfChatId;
    }

    public List<String> getListOfUsers() {
        List<String> listOfUsers = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT user_name FROM users");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                listOfUsers.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listOfUsers;
    }

    public void putChatIdByUserName(String chatId, String userName) {
        try {
            connection.setAutoCommit(false);

            preparedStatement = connection
                    .prepareStatement("SELECT user_name FROM users WHERE user_name = ?");
            preparedStatement.setString(1, userName);
            ResultSet rs = preparedStatement.executeQuery();
            boolean userNotExists = true;
            while (rs.next()) {
                userNotExists = false;
            }
            rs.close();
            if (userNotExists) {
                connection.commit();
                return;
            }

            preparedStatement = connection
                    .prepareStatement("UPDATE users SET chat_id = ? WHERE user_name = ?");
            preparedStatement.setString(1, chatId);
            preparedStatement.setString(2, userName);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getChatIdByUserName(String userName) {
        ResultSet rs;
        String chatId = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("SELECT user_name FROM users WHERE user_name = ?");
            preparedStatement.setString(1, userName);
            rs = preparedStatement.executeQuery();
            boolean userExists = false;
            while (rs.next()) {
                userExists = true;
            }
            if (!userExists) {
                connection.commit();
                rs.close();
                return null;
            }

            preparedStatement = connection.prepareStatement("SELECT chat_id FROM users WHERE user_name = ?");
            preparedStatement.setString(1, userName);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                chatId = rs.getString(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return chatId;
    }

    private int getGroupId(String group) throws SQLException {
        ResultSet rs;
        int groupId = defaultValue;
        preparedStatement = connection
                .prepareStatement("SELECT id FROM groups WHERE name = ?");
        preparedStatement.setString(1, group);
        rs = preparedStatement.executeQuery();
        while (rs.next()) {
            groupId = rs.getInt(1);
            if (groupId == defaultValue) {
                connection.commit();
            }
        }
        rs.close();
        return groupId;
    }

    private int getRoleId(String role) throws SQLException {
        ResultSet rs;
        int roleId = defaultValue;
        preparedStatement = connection
                .prepareStatement("SELECT id FROM roles WHERE name = ?");
        preparedStatement.setString(1, role);
        rs = preparedStatement.executeQuery();
        while (rs.next()) {
            roleId = rs.getInt(1);
            if (roleId == defaultValue) {
                connection.commit();
            }
        }
        rs.close();
        return roleId;
    }
}
