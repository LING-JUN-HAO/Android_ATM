package com.example.mynavdrawer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)//主鍵是否自動增長，預設為false
    private int id;
    private String username;
    private String userID;
    private String pwd;
    private String email;
    private String cellphone;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
//這裡的getter/setter方法是必須的
//這裡的getter/setter方法是必須的
//這裡的getter/setter方法是必須的
//重要的事說三遍
}
