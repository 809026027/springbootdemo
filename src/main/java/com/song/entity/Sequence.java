package com.song.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by feng on 2019/5/26.
 */
@Entity
@Table(name = "t_sequence")
public class Sequence implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Sequence{" +
                "id=" + id +
                ", count=" + count +
                '}';
    }
}