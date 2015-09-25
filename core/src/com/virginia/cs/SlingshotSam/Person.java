package com.virginia.cs.SlingshotSam;

/**
 * Created by User on 9/19/2015.
 */
public class Person extends GameItem {
    protected float health;
    public void takeDamage(float dmg){
        health -= dmg;
    }
}
