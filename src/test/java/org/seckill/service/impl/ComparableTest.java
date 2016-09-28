package org.seckill.service.impl;

import java.util.HashSet;

/**
 * Created by linyitian on 2016/6/28.
 */
public class ComparableTest implements Comparable<ComparableTest>{
    private String id ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public int compareTo(Object o) {
//        if(o.hashCode()<this.hashCode()){
//            return 1;
//        }else if(o.hashCode()>this.hashCode()){
//            return -1;
//        }else return 0;
//    }
    public int compareTo(ComparableTest o) {
        if(o.getId().equals(this.getId())){
            return 0;
        }else return -1;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    public static void main(String [] args){
        ComparableTest o1 = new ComparableTest();
        o1.setId("1");
        ComparableTest o2 = new ComparableTest();
        o2.setId("1");

        System.out.println("hashCode toString = "+o1.toString());
        System.out.println("hashCode toString = "+o2.toString());

        HashSet<ComparableTest> set = new HashSet<ComparableTest>();
        System.out.println(o1.hashCode());
        System.out.println(o2.hashCode());
        set.add(o1);
        set.add(o2);

        System.out.println(set.size());

        System.out.println(o1.equals(o2));
    }

    @Override
    public boolean equals(Object obj) {
        ComparableTest ct = (ComparableTest)obj;
        if(this.hashCode()==ct.hashCode()){
            return true;
        }else return false;
    }
}
