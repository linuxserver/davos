package io.linuxserver.davos.web;

import java.util.ArrayList;
import java.util.List;

public class Notifications {

    private List<Pushbullet> pushbullet = new ArrayList<Pushbullet>();
    private List<SNS> sns = new ArrayList<SNS>();

    public List<Pushbullet> getPushbullet() {
        return pushbullet;
    }

    public List<SNS> getSns() {
        return sns;
    }

    public void setPushbullet(List<Pushbullet> pushbullet) {
        this.pushbullet = pushbullet;
    }

    public void setSns(List<SNS> sns) {
        this.sns = sns;
    }
}
