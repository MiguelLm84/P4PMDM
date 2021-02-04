package com.miguel_lm.appjsondata.modelo;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import java.util.List;

public class JsonLab {

    @SuppressLint("StaticFieldLeak")
    private static JsonLab jsonLab;

    private JsonDAO jsonDao;

    private JsonLab(Context context) {
        Context appContext = context.getApplicationContext();
        DBManagerRoom.AppDatabase database = Room.databaseBuilder(appContext, DBManagerRoom.AppDatabase.class, "JsonData").allowMainThreadQueries().build();
        jsonDao = database.getJsonDao();
    }

    public static JsonLab get(Context context) {
        if (jsonLab == null) {
            jsonLab = new JsonLab(context);
        }
        return jsonLab;
    }

    //////////////////////////////////
    // USUARIOS
    //////////////////////////////////

    public List<Posts> getPosts() {
        return jsonDao.getPosts();
    }

    public void insertPosts(Posts posts) {
        jsonDao.insert(posts);
    }

    public void updatePosts(Posts posts) {
        jsonDao.update(posts);
    }

    public void deletePosts(Posts posts) {
        jsonDao.delete(posts);
    }

    public List<Posts> searchPosts(String query) {
        return jsonDao.searchPosts(query);
    }


    //////////////////////////////////
    // USUARIOS
    //////////////////////////////////

    public List<Users> getUsers() {
        return jsonDao.getUsers();
    }

    public void insertUsers(Users users) {
        jsonDao.insert(users);
    }

    public void updateUsers(Users users) {
        jsonDao.update(users);
    }

    public void deleteUsers(Users users) {
        jsonDao.delete(users);
    }

    public Users searchUserById(String id) {
        return jsonDao.searchUserById(id);
    }
}
