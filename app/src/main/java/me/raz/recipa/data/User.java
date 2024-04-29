package me.raz.recipa.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

        private String fullName;
        private String nickname;

        public User() {
        }

        public User(String fullName , String nickname) {
                this.fullName = fullName;
                this.nickname = nickname;
        }
}
