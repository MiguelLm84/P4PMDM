package com.miguel_lm.appjsondata.modelo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface JsonDAO {


    //////////////////////////////////
    // USUARIOS
    //////////////////////////////////

    @Query("SELECT * FROM Posts")
    List<Posts> getPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Posts posts);

    @Delete
    void delete(Posts posts);

    @Update
    void update(Posts posts);

    @Query("SELECT * FROM Posts WHERE titulo LIKE "%:query%")
    public List<Posts> searchPosts(String query);




        //////////////////////////////////
    // USUARIOS
    //////////////////////////////////

    @Query("SELECT * FROM Users")
    List<Users> getUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Users users);

    @Delete
    void delete(Users users);

    @Update
    void update(Users users);

    @Query("SELECT * FROM Users WHERE id == :ide")
    Users searchUserById(String ide);
}
