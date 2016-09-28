package demo;

import org.seckill.dto.Exposer;

/**
 * Created by User on 2016/7/10.
 */
class Parent {
    public Parent(){
        System.out.println("creating parent instance");
        this.name = "parent";
        this.age = "17";
    }
    protected String name;

    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void print(){
        synchronized (this){
            System.out.println("parentOperation");
        }
    }
}


class Son extends Parent{
    public Son(){
        super();
        System.out.println("creating son instance");
        this.name = "son";
        super.setName("parent again");
    }
    public void print(){
        synchronized (this){
            System.out.println("sonOperation");
            super.print();
        }
    }

}

public class Test {
    public static void main(String[] args){
        final Son son = new Son();
        System.out.println(son.getName());
        son.print();
    }
}
