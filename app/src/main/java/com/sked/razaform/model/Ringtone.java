
package com.sked.razaform.model;


/**
 * Created by manish on 11/2/2015.
 */

public class Ringtone {
    private int ringtoneId;
    private String ringtoneName;

    public int getRingtoneId() {
        return ringtoneId;
    }

    public void setRingtoneId(int ringtoneId) {
        this.ringtoneId = ringtoneId;
    }

    public String getRingtoneName() {
        return ringtoneName;
    }

    public void setRingtoneName(String ringtoneName) {
        this.ringtoneName = ringtoneName;
    }

    public Ringtone(int ringtoneId, String ringtoneName) {
        this.ringtoneId = ringtoneId;
        this.ringtoneName = ringtoneName;
    }
    public Ringtone(){}
}

