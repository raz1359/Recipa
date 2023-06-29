package com.example.myapplication;

public class Users {

        String FullName , Nickname;

        public Users() {
        }

        public Users(String fullName , String nickname) {
                this.FullName = fullName;
                this.Nickname = nickname;
        }
        public String getFullName() {
                return FullName;}

        public void setFullName(String fullName) {
                FullName = fullName;}

        public String getNickname() {
                return Nickname;}

        public void setNickname(String nickname) {
                Nickname = nickname;}
}
