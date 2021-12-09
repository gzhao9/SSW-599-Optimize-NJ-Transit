package testcase;

import huawei.exam.CardEnum;
import huawei.exam.CommandEnum;
import huawei.exam.OpResult;
import huawei.model.Command;
import huawei.model.OperationResult;
import huawei.storage.CardInfoStorage;
import huawei.util.CardIdCreator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import huawei.SubwaySECControlCenter;

/**
 * 自动化测试类，考生可以实现此类，进行测试。
 * @author
 * @version 1.0
 */
public class TestSubwayManager extends SubwaySECControlCenter
{
	@Before
	public void setUp() {
    	CardInfoStorage.getInstance().clear();
    	CardIdCreator.getInstance().clearRecord();
	}
	
    /**
     * 线路查询测试用例
     */
    @Test
    public void testQuerySubways()
    {
    	System.out.println("查询地铁线路——L：");
    	
        SubwaySECControlCenter impl = new SubwaySECControlCenter();
        Command command = new Command();
        command.setCommand(CommandEnum.CMD_QUERY_SITES);
        OperationResult responseBody = impl.execute(command);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_QUERY_SITES, responseBody).toString());
        
        Assert.assertEquals(OpResult.createReturnResult(CommandEnum.CMD_QUERY_SITES, responseBody).toString(),
            "0号线:S0<->S1<->S2<->S3<->S4<->S5<->S6<->S7<->S8  " +
                "1号线:S10<->S11<->S12<->S5<->S14<->S15<->S16<->S17<->S18  " +
                "2号线:S20<->S21<->S22<->S23<->S15<->S25<->S26<->S27<->S28  " +
                "3号线:S30<->S31<->S32<->S2<->S22<->S35<->S36<->S37<->S38");
    }
    
    /**
     * 单程票购买
     */
    @Test
    public void testBuySingleTicket()
    {
    	System.out.println("购买单程票——S [进站站点名称] [出站站点名称]：");
    	
        SubwaySECControlCenter impl = new SubwaySECControlCenter();
        
        Command command = new Command();
        command.setCommand(CommandEnum.CMD_ONCE);
        command.setEnterStation("S0");
        command.setExitStation("S8");
        
        OperationResult responseBody = impl.execute(command);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_ONCE, responseBody).toString());
        
        Assert.assertEquals(OpResult.createReturnResult(CommandEnum.CMD_ONCE, responseBody).toString(),
        					"购买单程票<成功><卡号=0><卡类型=单程卡><余额=5>");
    }
    
    /**
     * 办理乘车卡
     */
    @Test
    public void testBuyNormalCard()
    {
    	System.out.println("办理乘车卡——B[卡类型] [预存金额]：");
    	
        SubwaySECControlCenter impl = new SubwaySECControlCenter();
        
        Command command = new Command();
        command.setCommand(CommandEnum.CMD_BUY);
        command.setCardType(CardEnum.C);
        command.setMoney(50);
        
        OperationResult responseBody = impl.execute(command);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_BUY, responseBody).toString());
        
        Assert.assertEquals(OpResult.createReturnResult(CommandEnum.CMD_BUY, responseBody).toString(),
				"办理乘车卡<成功><卡号=0><卡类型=C:普通卡><余额=50>");
    }
    
    /**
     * 乘车扣费成功
     */
    @Test
    public void testConsumeSuccess()
    {
    	System.out.println("扣费——C [卡号] [进站时间] [进站站点名称] [出站时间] [出站站点名称]：");
    	
        SubwaySECControlCenter impl = new SubwaySECControlCenter();
        
        // 购买普通乘车卡, 用作乘车扣费测试
        Command commandB = new Command();
        commandB.setCommand(CommandEnum.CMD_BUY);
        commandB.setCardType(CardEnum.C);
        commandB.setMoney(50);
        
        // 乘车扣费
        Command command = new Command();
        command.setCommand(CommandEnum.CMD_CONSUME);
        command.setCardId("0");
        command.setEnterTime("11:00");
        command.setEnterStation("S0");
        command.setExitTime("11:30");
        command.setExitStation("S8");

        OperationResult responseBodyB = impl.execute(commandB);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_BUY, responseBodyB).toString());  
        
        OperationResult responseBody = impl.execute(command);               
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_CONSUME, responseBody).toString());
        
        Assert.assertEquals(OpResult.createReturnResult(CommandEnum.CMD_CONSUME, responseBody).toString(),
				"乘车卡扣费<成功><卡号=0><卡类型=C:普通卡><余额=45>");
    }
    
    /**
     * 乘车扣费失败（余额不足）
     */
    @Test
    public void testConsumeNotEnoughMoney()
    {
    	System.out.println("扣费——C [卡号] [进站时间] [进站站点名称] [出站时间] [出站站点名称]：");
    	
        SubwaySECControlCenter impl = new SubwaySECControlCenter();
        
        // 购买普通乘车卡, 用作乘车扣费测试
        Command commandB = new Command();
        commandB.setCommand(CommandEnum.CMD_BUY);
        commandB.setCardType(CardEnum.C);
        commandB.setMoney(1);
        
        // 乘车扣费
        Command command = new Command();
        command.setCommand(CommandEnum.CMD_CONSUME);
        command.setCardId("0");
        command.setEnterTime("11:00");
        command.setEnterStation("S0");
        command.setExitTime("11:30");
        command.setExitStation("S8");

        OperationResult responseBodyB = impl.execute(commandB);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_BUY, responseBodyB).toString());  
        
        OperationResult responseBody = impl.execute(command);               
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_CONSUME, responseBody).toString());
        
        Assert.assertEquals(OpResult.createReturnResult(CommandEnum.CMD_CONSUME, responseBody).toString(),
				"乘车卡扣费<失败><卡号=0><卡类型=C:普通卡><余额=1><失败原因=E02:余额不足>");
    }
    
    /**
     * 乘车扣费失败（提醒余额过低, 请及时充值）
     */
    @Test
    public void testConsumeWarningFund()
    {
    	System.out.println("扣费——C [卡号] [进站时间] [进站站点名称] [出站时间] [出站站点名称]：");
    	
        SubwaySECControlCenter impl = new SubwaySECControlCenter();
        
        // 购买普通乘车卡, 用作乘车扣费测试
        Command commandB = new Command();
        commandB.setCommand(CommandEnum.CMD_BUY);
        commandB.setCardType(CardEnum.C);
        commandB.setMoney(20);
        
        // 乘车扣费
        Command command = new Command();
        command.setCommand(CommandEnum.CMD_CONSUME);
        command.setCardId("0");
        command.setEnterTime("11:00");
        command.setEnterStation("S0");
        command.setExitTime("11:30");
        command.setExitStation("S8");

        OperationResult responseBodyB = impl.execute(commandB);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_BUY, responseBodyB).toString());  
        
        OperationResult responseBody = impl.execute(command);               
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_CONSUME, responseBody).toString());
        
        Assert.assertEquals(OpResult.createReturnResult(CommandEnum.CMD_CONSUME, responseBody).toString(),
				"乘车卡扣费<失败><卡号=0><卡类型=C:普通卡><余额=15><失败原因=E03:余额过低，请及时充值>");
    }
    
    /**
     * 充值
     */
    @Test
    public void testRecharge()
    {
    	System.out.println("充值——F [卡号] [预存金额]：");
    	
        SubwaySECControlCenter impl = new SubwaySECControlCenter();
        
        // 购买普通乘车卡, 用作充值测试
        Command commandB = new Command();
        commandB.setCommand(CommandEnum.CMD_BUY);
        commandB.setCardType(CardEnum.C);
        commandB.setMoney(1);
        
        // 充值
        Command command = new Command();
        command.setCommand(CommandEnum.CMD_RECHARGE);
        command.setCardId("0");
        command.setMoney(10);
        
        OperationResult responseBodyB = impl.execute(commandB);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_BUY, responseBodyB).toString());  
        
        OperationResult responseBody = impl.execute(command);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_RECHARGE, responseBody).toString());  
        
        Assert.assertEquals(OpResult.createReturnResult(CommandEnum.CMD_RECHARGE, responseBody).toString(),
				"乘车卡充值<成功><卡号=0><卡类型=C:普通卡><余额=11>");
    }
    
    /**
     * 注销地铁卡
     */
    @Test
    public void testReset() {
    	System.out.println("注销地铁卡——D [卡号]：");
    	
        SubwaySECControlCenter impl = new SubwaySECControlCenter();
        
        // 购买普通乘车卡, 用作注销地铁卡测试
        Command commandB = new Command();
        commandB.setCommand(CommandEnum.CMD_BUY);
        commandB.setCardType(CardEnum.C);
        commandB.setMoney(100);
        
        // 注销地铁卡
        Command command = new Command();
        command.setCommand(CommandEnum.CMD_DELETE);
        command.setCardId("0");
        
        OperationResult responseBodyB = impl.execute(commandB);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_BUY, responseBodyB).toString());  
        
        OperationResult responseBody = impl.execute(command);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_DELETE, responseBody).toString());  
        
        OperationResult responseBodyB2 = impl.execute(commandB);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_BUY, responseBodyB2).toString());  
        
        Assert.assertEquals(OpResult.createReturnResult(CommandEnum.CMD_DELETE, responseBody).toString(),
				"注销<成功><卡号=0><卡类型=C:普通卡>");
        // 注销后保证卡号能被回收利用
        Assert.assertEquals(OpResult.createReturnResult(CommandEnum.CMD_BUY, responseBodyB2).toString(),
				"办理乘车卡<成功><卡号=0><卡类型=C:普通卡><余额=100>");
    }
    
    /**
     * 查询历史消费记录
     */
    @Test
    public void testQueryRecord() {
    	System.out.println("查询历史消费记录——H [卡号]：");
    	
        SubwaySECControlCenter impl = new SubwaySECControlCenter();
        
        // 购买普通乘车卡, 用作查询历史消费记录测试
        Command commandB = new Command();
        commandB.setCommand(CommandEnum.CMD_BUY);
        commandB.setCardType(CardEnum.C);
        commandB.setMoney(99);
        
        // 消费乘车卡1 
        Command commandC1 = new Command();
        commandC1.setCommand(CommandEnum.CMD_CONSUME);
        commandC1.setCardId("0");
        commandC1.setEnterTime("7:40");
        commandC1.setEnterStation("S0");
        commandC1.setExitTime("8:25");
        commandC1.setExitStation("S8");
        
        // 消费乘车卡2 
        Command commandC2 = new Command();
        commandC2.setCommand(CommandEnum.CMD_CONSUME);
        commandC2.setCardId("0");
        commandC2.setEnterTime("18:50");
        commandC2.setEnterStation("S8");
        commandC2.setExitTime("19:00");
        commandC2.setExitStation("S6");
        
        // 查询历史消费记录
        Command command = new Command();
        command.setCommand(CommandEnum.CMD_QUERY_RECORD);
        command.setCardId("0");
        
        OperationResult responseBodyB = impl.execute(commandB);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_BUY, responseBodyB).toString());
        
        OperationResult responseBodyC1 = impl.execute(commandC1);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_CONSUME, responseBodyC1).toString());
        
        OperationResult responseBodyC2 = impl.execute(commandC2);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_CONSUME, responseBodyC2).toString());  

        OperationResult responseBody = impl.execute(command);
        System.out.println(OpResult.createReturnResult(CommandEnum.CMD_QUERY_RECORD, responseBody).toString());
        
        Assert.assertEquals(OpResult.createReturnResult(CommandEnum.CMD_QUERY_RECORD, responseBody).toString(),
				"查询<成功><卡号=0><卡类型=C:普通卡>" + "\n" +
				"<序号=1,进站时间=7:40,进站站点=S0,出站时间=8:25,出站站点=S8,消费金额=5>" + "\n" +
				"<序号=2,进站时间=18:50,进站站点=S8,出站时间=19:00,出站站点=S6,消费金额=3>");
    }
    
	@After
	public void tearDown() {
		System.out.println();
	}
}
