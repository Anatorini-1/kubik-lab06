package com.anatorini.lab06.Util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Pair {
    private int x;
    private int y;
    @Override
    public String toString(){
        return "("+x+","+y+")<br>";
    }
}
