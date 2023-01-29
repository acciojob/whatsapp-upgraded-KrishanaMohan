package com.driver;

import net.bytebuddy.asm.Advice;

public class Three {
    private Message message;
    private User user;
    private Group group;

    public Three(Message m,User u,Group g){
        this.message=m;
        this.user=u;
        this.group=g;
    }
    public Three(){

    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
