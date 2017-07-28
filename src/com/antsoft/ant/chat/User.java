package com.antsoft.ant.chat;

import java.io.Serializable;
/**
 * User의 정보를 나타내는 클래스
 *
 * @author kim sang kyun
 */
public class User implements Serializable {
  private String id, passwd, email, homepage, skill, sex;

  public User(String id, String passwd, String email, String homepage, String skill, String sex) {
    this.id = id;
    this.passwd = passwd;
    this.email = email;
    this.homepage = homepage;
    this.skill = skill;
    this.sex = sex;
  }

  public String getID(){
    return id;
  }

  public String getPassword(){
    return passwd;
  }

  public String getEmail(){
    return email;
  }

  public String getHomePage(){
    return homepage;
  }

  public String getSkill(){
    return skill;
  }

  public String getSex(){
    return sex;
  }      

  public String toString(){
    return id;
  }

  public boolean equals(Object comp){
    User u = (User)comp;
    if(id.equals(u.id) && passwd.equals(u.passwd)) return true;
    else return false;
  }
}
