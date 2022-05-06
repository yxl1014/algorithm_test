package com.yxl.spf;

import java.math.BigDecimal;
import java.util.*;

public class Spf {
    public static class SpfNode{
        public int id;
        public double comingTime;    // 进程到达时间
        public double runningTime;  //进程执行时间
        public double rtime;          //进程开始时间
        public double finishTime;   //进程完成时间
        public double total;           //周转时间
        public double ptotal;        //带权周转时间（周转系数）

        public SpfNode(int id, double comingTime, double runningTime) {
            this.id = id;
            this.comingTime = comingTime;
            this.runningTime = runningTime;
        }

        public SpfNode() {
        }
    }


    private final List<SpfNode> spfNodes = new ArrayList<>();

    public boolean addSpfNode(SpfNode node) {
        if (spfNodes.contains(node))
            return false;
        spfNodes.add(node);
        return true;
    }

    public boolean removeSpfNode(SpfNode node) {
        if (!spfNodes.contains(node))
            return false;
        spfNodes.remove(node);
        return true;
    }

    public void run() {
        double now = 0.0;//现在的时间点
        double sum1 = 0.0;//周转总和
        double sum2 = 0.0;//带权周转总和
        int flag = -1;//状态 及 下标  若-1则代表现在没有进程正在执行 若为-2则代表执行最先到达的进程
        int tag = 0;//临时下标
        int cnt=spfNodes.size();//总共多少需要执行的进程数
        System.out.println("进程名\t\t提交时间\t\t执行\t\t开始\t\t完成\t\t周转\t\t带权周转");
        while (spfNodes.size() > 0) {//循环到list里没有任务
            for (int c = 0; c < spfNodes.size(); c++) {//判断在当前时间节点到达的进程
                if (spfNodes.get(c).comingTime <= now) {
                    flag = c;//若不为-1 这代表有等待进程
                }
            }
            if (flag == -1) {//若为-1 则代表没有等待的进程 需要找到下一个最先到来的进程
                tag = findMInFlag(spfNodes);//找到最先到来的的进程的下标
                flag = -2;//标明需要执行最先到达的进程任务
                now = spfNodes.get(tag).comingTime;//将现在的时间节点设置成即将到达的进程节点
            }
            SpfNode temp;//定义一个临时进程
            int index;//下标
            if (flag == -2) {//若为-2 则代表执行最先到达的进程
                index = tag;//需要执行的进程下标就是当前最先到达的下标
            } else {
                index = findMin(spfNodes, now);//若不为-2 则找到在当前时间节点之前到达并且运行时间最短的进程任务
            }
            flag = -1;//将flag重置
            spfNodes.get(index).rtime = now;//该任务开始时间为当前时间
            temp = spfNodes.get(index);
            temp.finishTime = temp.rtime + temp.runningTime;//该任务结束时间为开始时间加上运行时间
            now = temp.finishTime;//当前时间为该任务结束时间
            temp.total=temp.finishTime-temp.comingTime;//该任务周转时间为结束时间减去到达时间
            temp.ptotal=temp.total/temp.runningTime;//该任务带权周转时间为周转时间除以执行时间


            //保留小数点两位
            temp.comingTime=new BigDecimal(temp.comingTime).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            temp.rtime=new BigDecimal(temp.rtime).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            temp.total = new BigDecimal(temp.total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            temp.ptotal = new BigDecimal(temp.ptotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            temp.finishTime = new BigDecimal(temp.finishTime).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            //周转总和
            sum1+=temp.total;
            sum2+=temp.ptotal;
            System.out.println(temp.id+"\t\t"+temp.comingTime+"\t\t"+temp.runningTime+"\t\t"
                    +temp.rtime+"\t\t"+temp.finishTime+"\t\t"
                    +temp.total+"\t\t"+temp.ptotal);

            //删除掉执行过的进程任务
            spfNodes.remove(index);
        }


        //保留小数点两位
        sum1 = new BigDecimal(sum1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        sum2 = new BigDecimal(sum2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // System.out.println("平均周转时间"+sum1/cnt+"\t带权周转时间"+sum2/cnt);
        sum1 = new BigDecimal(sum1/cnt).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        sum2 = new BigDecimal(sum2/cnt).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println("取两位小数");
        System.out.println("平均周转时间"+sum1+"\t平均带权周转时间"+sum2);
    }

    public static int findMin(List<SpfNode> jcbs, double now) {//找到到达的进程中执行时间最短的进程编号
        int minIndex = 0;
        for (int i = 0; i < jcbs.size(); i++) {
            if (jcbs.get(i).runningTime < jcbs.get(minIndex).runningTime && jcbs.get(i).comingTime <= now) {//执行时间最短并且已经到达的进程任务
                minIndex = i;
            }
        }
        return minIndex;
    }

    //这是一个找最早提交时间的函数
    public static int findMInFlag(List<SpfNode> jcbs) {//找到提交时间最早的进程编号
        int minIndex = 0;
        for (int i = 0; i < jcbs.size(); i++) {
            if (jcbs.get(i).comingTime < jcbs.get(minIndex).comingTime) {
                minIndex = i;
            }
        }
        return minIndex;
    }

}
