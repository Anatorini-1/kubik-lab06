package com.anatorini.lab06.Ocean.Core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ShipModel {
    private int x;
    private int y;
    private String state;
    private int timeout;
    private int id;
}
