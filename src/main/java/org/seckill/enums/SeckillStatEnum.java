package org.seckill.enums;

/**
 * Created by linyitian on 2016/6/22.
 */
public enum SeckillStatEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"end"),
    REPEAT_KILL(-1,"repeat kill"),
    INNER_ERROR(-2,"inner error"),
    DATA_REWRITE(-3,"data rewritten");
    private int state;
    private String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public static SeckillStatEnum stateOf(int index){
        for(SeckillStatEnum state:values()){
            if(state.getState()==index){
                return state;
            }
        }
        return null;
    }
}
