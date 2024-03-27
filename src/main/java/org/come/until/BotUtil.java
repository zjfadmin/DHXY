package org.come.until;

import come.tool.FightingData.Battlefield;
import org.come.action.role.CreatPeople;
import org.come.bean.LoginResult;

import java.math.BigDecimal;
import java.util.*;

public class BotUtil {

    public static Map<Integer,String> SELL_BOT = new HashMap<>();

    public static List<Map<String,LoginResult>> RETREAT_STALL = new Vector<>();


    private static String[]mes11={"夏","傲","心","一梦","落","牧","落","你","爱","事","爱","不必","人瘦","花开","爱你","雪落","爱情","开"};
    private static String[]mes12={"末","鬼","花","长东","江水","樱花","之森","共粲"};

    public static List<LoginResult> addBot(int count, Boolean haveMount){
        List<LoginResult> list = new ArrayList<>();
        for (int i=0; i<=count; i++){
            String buffer1;
            buffer1= mes11[Battlefield.random.nextInt(mes11.length)]+ mes11[Battlefield.random.nextInt(mes11.length)]+mes12[Battlefield.random.nextInt(mes12.length)]+ mes11[Battlefield.random.nextInt(mes11.length)];
            LoginResult loginResult= CreatPeople.creatpeople(buffer1,i, haveMount ,1);
            list.add(loginResult);
        }
        return list;
    }

    public static List<LoginResult> addStallBot(int count, Long mapId,int x, int y){
        List<LoginResult> list = new ArrayList<>();
        for (int i=0; i<count; i++){
            String buffer1;
            buffer1= mes11[Battlefield.random.nextInt(mes11.length)]+ mes11[Battlefield.random.nextInt(mes11.length)]+mes12[Battlefield.random.nextInt(mes12.length)]+ mes11[Battlefield.random.nextInt(mes11.length)];
            LoginResult loginResult= CreatPeople.creatStallBot(buffer1,i, mapId,x ,y);
            list.add(loginResult);
        }
        return list;
    }

    public static LoginResult addStallBot( Long mapId,int x, int y){

        String buffer1;

        buffer1= mes11[Battlefield.random.nextInt(mes11.length)]+ mes11[Battlefield.random.nextInt(mes11.length)]+mes12[Battlefield.random.nextInt(mes12.length)]+ mes11[Battlefield.random.nextInt(mes11.length)];

        return CreatPeople.creatStallBot(buffer1,1, mapId,x ,y);
    }
}
