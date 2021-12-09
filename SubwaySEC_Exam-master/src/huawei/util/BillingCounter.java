package huawei.util;

import huawei.biz.impl.CardManagerImpl;
import huawei.biz.impl.SubwayManagerImpl;
import huawei.exam.CardEnum;

public class BillingCounter {
	public static int calculateBilling(CardEnum cardEnum, String enterStation, String enterTime, String exitStation, String exitTime) {
    	int enterStationNum = Integer.parseInt(enterStation.substring(1));
    	int exitStationNum = Integer.parseInt(exitStation.substring(1));
		String[] enterTimeArr = enterTime.split(":");
		String[] exitTimeArr = exitTime.split(":");
		int enterTimeNum = Integer.valueOf(enterTime.replaceAll("[:]", ""));
		
    	int difference = enterStationNum - exitStationNum;
    	int increment = difference < 0 ? 1 : -1;  
    	int totalDistance = 0;	// 总间距
    	
    	String recentStation = enterStation;
    	for(int index = difference, nextStationNum = enterStationNum; 
    			index != 0; index += increment) {
    		nextStationNum = nextStationNum + increment;
    		String nextStation = "S" + nextStationNum;

    		SubwayManagerImpl subwayManagerImpl = new SubwayManagerImpl(new CardManagerImpl());
    		subwayManagerImpl.manageSubways();
    		totalDistance += subwayManagerImpl.querySubways().getStationDistances().get(recentStation, nextStation).getDistance();
    		
    		recentStation = nextStation;
    	}

    	// 计算总费用
    	int billing = 0;
    	
    	// 计算基本票价
		if(totalDistance > 0 && totalDistance <= 3000) {
    		billing = 2;
    	} else if (totalDistance > 3000 && totalDistance <= 5000) {
    		billing = 3;
    	} else if (totalDistance > 5000 && totalDistance <= 10000) {
    		billing = 4;
    	} else {
    		billing = 5;
    	}
		
		// 老年卡优惠
		if(cardEnum == CardEnum.B) {
			if(enterTimeNum >= 1000 && enterTimeNum <= 1500) {
				billing *= 0.8;
			}
		}
		
		// 计算扣费票价
		if(totalDistance == 0) {
			if(enterTime != null && exitTime != null) {
				int minutes = (Integer.valueOf(exitTimeArr[0]) - Integer.valueOf(enterTimeArr[0])) * 60 + 
								(Integer.valueOf(exitTimeArr[1]) - Integer.valueOf(enterTimeArr[1]));
				if(minutes > 30) {
					billing = 3;
				}
			}
		}
		
		return billing;
	}
}
