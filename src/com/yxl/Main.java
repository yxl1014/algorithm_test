package com.yxl;

import com.yxl.spf.Spf;

public class Main {
    public static void main(String[] args) {
        Spf spf = new Spf();
        spf.addSpfNode(new Spf.SpfNode(1,10.0,2.0));
        spf.addSpfNode(new Spf.SpfNode(2,10.2,1.0));
        spf.addSpfNode(new Spf.SpfNode(3,10.4,0.5));
        spf.addSpfNode(new Spf.SpfNode(4,10.5,0.3));
        spf.run();
    }
}
