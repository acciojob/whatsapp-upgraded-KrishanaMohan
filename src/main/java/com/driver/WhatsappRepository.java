package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class WhatsappRepository {
    HashMap<String, String> UserDB;
    HashMap<String, List<User>> PersonDB;
    HashMap<String, List<User>> GroupDB;
    HashMap<Integer, Message> MsgDB;
    TreeMap<Date, Three> GroupDBMsg;

    public WhatsappRepository() {
        this.UserDB = new HashMap<>();
        this.PersonDB = new HashMap<>();
        this.GroupDB = new HashMap<>();
        this.MsgDB = new HashMap<>();
        this.GroupDBMsg = new TreeMap<>();
    }

    public String createUser(String name, String mobile) throws Exception {
        if(UserDB.containsKey(mobile))
            throw new Exception("User already exists");

        User user = new User(name, mobile);
        UserDB.put(mobile, name);
        return "SUCCESS";
    }
    public Group createGroup(List<User> users){
        Group group=new Group();
        if(users.size()==2){
             String name=users.get(1).getName();
             group.setName(name);
             group.setNumberOfParticipants(2);
            PersonDB.put(name,users);

        }
        else{
            int count=GroupDB.size()+1;

            String name="Group "+count;
            group.setName(name);
            group.setNumberOfParticipants(users.size());
            GroupDB.put(name,users);
        }
        return group;
    }

    public int createMessage(String content){
        int key=MsgDB.size()+1;
        Message message=new Message(key,content);
        MsgDB.put(key,message);
        return key;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        String name=group.getName();
        int totalMsg=0;
        if(!GroupDB.containsKey(name) && !PersonDB.containsKey(name)){
            throw new Exception("Group does not exist");
        }
        if((GroupDB.containsKey(name) && !GroupDB.get(name).contains(sender)) || (PersonDB.containsKey(name) && !PersonDB.get(name).contains(sender)))
            throw new Exception("You are not allowed to send message");

        if(GroupDB.containsKey(name) && GroupDB.get(name).contains(sender)){
            Three three=new Three(message,sender,group);
            GroupDBMsg.put(message.getTimestamp(),three);
            totalMsg=GroupDBMsg.size();
        }

        if(PersonDB.containsKey(name) && PersonDB.get(name).contains(sender)){
            Three three=new Three(message,sender,group);
            GroupDBMsg.put(message.getTimestamp(),three);
            totalMsg=GroupDBMsg.size();
        }
        return totalMsg;
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        String name=group.getName();


        if(!GroupDB.containsKey(name) && !PersonDB.containsKey(name)){
            throw new Exception("Group does not exist");
        }

        if(GroupDB.containsKey(name)){
            if(!GroupDB.get(group.getName()).get(0).equals(approver)) {
                throw new Exception("Approver does not have rights");
            }
            if(!GroupDB.get(group.getName()).contains(user)){
                throw new Exception("User is not a participant");
            }
            GroupDB.get(name).remove(user);
            GroupDB.get(name).add(0,user);
        }

        if(PersonDB.containsKey(name)){
            if(!PersonDB.get(name).get(0).equals(approver)) {
                throw new Exception("Approver does not have rights");
            }
            if(!PersonDB.get(name).contains(user)){
                throw new Exception("User is not a participant");
            }
            PersonDB.get(name).remove(user);
            PersonDB.get(name).add(0,user);
        }

        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception{
        int numberOfUser=0;

        for(List<User> list:GroupDB.values()){

            if(list.size()>0){
                if(list.get(0).equals(user))
                  throw new Exception("Cannot remove admin");

                for(User user1:list){
                    if(user1.equals(user)){
                        list.remove(user);
                        numberOfUser=list.size();
                        break;
                    }
                }
              }

        }
        for(List<User> list:PersonDB.values()){
            if(list.size()>0 && list.get(0).equals(user))
                throw new Exception("Cannot remove admin");

            for(User user1:list){
                if(user1.equals(user)){
                    list.remove(user);
                    numberOfUser=1;
                    break;
                    }
                }
        }

        if (numberOfUser==0){
             throw new Exception("User not found");
        }

        int a=GroupDBMsg.size();
        for(Map.Entry<Date, Three> map:GroupDBMsg.entrySet()){
            Three x=new Three();
            Date date=map.getKey();
            x=map.getValue();
            if(x.getUser().equals(user))
                GroupDBMsg.remove(date);
        }

        int b= GroupDBMsg.size();;

            return numberOfUser+a+b;
    }

    public String findMessage(Date start, Date end, int K) throws Exception{

       if(K>GroupDBMsg.size()){
           throw new Exception( "K is greater than the number of messages");
       }
       int count=0;
       String ans="";
       K=GroupDBMsg.size()-K+1;
        for(Map.Entry<Date, Three> map:GroupDBMsg.entrySet()){
            Three x=new Three();
            x=map.getValue();
            count++;
            if(count==K){
                ans=x.getMessage().getContent();
            }

        }
       return ans;
    }
}
