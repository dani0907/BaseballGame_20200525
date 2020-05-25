package com.example.baseballgame_20200525.datas;

public class Message {

    private String content;  //메세지의 내용
    private String who;  //말한 사람, me, Cpu

    public Message(String content, String who) {
        this.content = content;
        this.who = who;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
