package com.example.mynavdrawer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class  Transaction_record{
    @PrimaryKey(autoGenerate = true)//主鍵是否自動增長，預設為false
    private int id;
    private String username;
    private String date;
    private String type;
    private String total;
    private String money;
    private String note;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }


    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

}
