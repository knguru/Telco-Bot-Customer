package com.ibm.watson.apis.conversation_enhanced.transaction;

import com.ibm.watson.apis.conversation_enhanced.payload.TopUsersPayload ;
import com.ibm.watson.apis.conversation_enhanced.payload.SubscriberPayload ;
import com.ibm.watson.apis.conversation_enhanced.payload.PlanPayload ;
import com.ibm.watson.apis.conversation_enhanced.payload.IndBillPayload ;
import com.ibm.watson.apis.conversation_enhanced.payload.YearlySpend ;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.ibm.watson.apis.conversation_enhanced.utils.QueryDash;

public class Transactions {
	
	public int executeSql(String sql) throws Exception
	{
		QueryDash qd = new QueryDash();
		return qd.executeStmt(sql);
	}

    public PlanPayload getSubscriberPlan(String customerId) throws Exception
	{
		int custId = Integer.parseInt(customerId) ;
		QueryDash qd = new QueryDash() ;
		ResultSet rs = qd.getPlan (custId) ;
		PlanPayload transactionPayload = null;
		if (rs!=null)
		{
			while (rs.next()) 
			{
				
				transactionPayload = new PlanPayload();				
				transactionPayload.name = rs.getString("NAME") ;
				transactionPayload.id = rs.getInt("ID") ;
				transactionPayload.desc = rs.getString("DESC") ;
									
			}
			rs.close();
			
		}


		return transactionPayload ;
	}
	public int getSubscriberCount(boolean active) throws Exception
	{
		QueryDash qd = new QueryDash();
		if (active)
		{
			return qd.getCount("TELCO_SUBSCRIBERS", "STATUS = 1");
		}
		else
		{
			return qd.getCount("TELCO_SUBSCRIBERS", "STATUS = 0");
		}

	}

	public List<IndBillPayload> getIndividualBills(String customerId,int duration) throws Exception
	{
		int custId = Integer.parseInt(customerId) ;

		QueryDash qd = new QueryDash() ;
		ResultSet rs = null;
		String selectFields = "SUB_ID,YEAR,MONTH,CALL_DUR,NUM_SMS,ROAMING_DUR,DATA,CALL_COST,SMS_COST,ROAMING_COST,DATA_COST,SVC_TAX,SURCHARGE,TOTAL";
		String where = "SUB_ID ="+custId+" ORDER BY (YEAR * 12 + MONTH) DESC";
		if (duration == 1)
		{
			//yearly data requested - so get 12 records
			rs = qd.fetchData (selectFields,"TELCO_IND_BILLING",where,12);
		}
		else if ((duration == 2) || (duration == 3))
		{
			int numRec = 1;
			if (duration == 3)
			{	
				numRec = 3; //get 3 records for quarter
			}
			rs = qd.fetchData (selectFields,"TELCO_IND_BILLING",where,numRec);

		}

		List<IndBillPayload> docs = null ;
		IndBillPayload totalPayload = new IndBillPayload();
		totalPayload.id = customerId;
		if (rs!=null)
		{
			docs = new ArrayList<IndBillPayload>();	
			int j = 0;
			while (rs.next())
			{
				IndBillPayload i = new IndBillPayload();
				i.year = rs.getInt("YEAR");
				i.month = rs.getInt("MONTH");
				i.id = customerId;
				//,,,,,,,,
				i.callDur = rs.getInt("CALL_DUR");
				totalPayload.callDur += i.callDur;
				i.sms = rs.getInt("NUM_SMS");
				totalPayload.sms += i.sms;
				i.roamDur= rs.getInt("ROAMING_DUR");
				totalPayload.roamDur += i.roamDur;
				i.data = rs.getInt("DATA");
				totalPayload.data += i.data;
				i.callCost = rs.getDouble("CALL_COST");
				totalPayload.callCost += i.callCost;
				i.smsCost = rs.getDouble("SMS_COST");
				totalPayload.smsCost += i.smsCost;
				i.roamCost = rs.getDouble("ROAMING_COST");
				totalPayload.roamCost += i.roamCost;
				i.dataCost = rs.getDouble("DATA_COST");
				totalPayload.dataCost += i.dataCost;
				i.svcTax = rs.getDouble("SVC_TAX");
				totalPayload.svcTax += i.svcTax;
				i.surcharge =  rs.getDouble("SURCHARGE");
				totalPayload.surcharge += i.surcharge;
				i.total = rs.getDouble("TOTAL");
				totalPayload.total += i.total;
				docs.add(j++,i);

			}
			if ((duration == 1) || (duration == 3))
			{
				docs.add(j,totalPayload);
			}
		}
		rs.close();
		return docs;
	}
	public YearlySpend getSpend(int duration, boolean past) throws Exception
	{
		QueryDash qd = new QueryDash() ;
		ResultSet rs = null;
		String selectFields = "YEAR,VOICE_SPEND,DATA_SPEND,VAS_SPEND,ROAMING, PLAN_CHARGES,SUM_TOTAL, VPC,DPC,VASPC,ROAMINGPC,PLANPC,RCTG,GOV,GMT,CEOIS";
		YearlySpend ys = new YearlySpend();
		if (duration == 1)
		{
			//yearly data requested
			String where = "1=1 ORDER BY YEAR DESC";
			if (past == Boolean.FALSE)
			{
				rs = qd.fetchData (selectFields,"YEARLY_SPEND",where,1);
			}
			else
			{
				rs = qd.fetchData (selectFields,"YEARLY_SPEND",where,2);
				if (rs != null)
				{
					rs.next(); //advance past the present year
				}
			}
		}
		else if ((duration == 2) || (duration == 3))
		{
			//monthly data it is
			String where = "1=1 ORDER BY (YEAR * 12 + MONTH) DESC";
			selectFields = "YEAR,MONTH, VOICE_SPEND,DATA_SPEND,VAS_SPEND,ROAMING, PLAN_CHARGES,SUM_TOTAL, VPC,DPC,VASPC,ROAMINGPC,PLANPC,RCTG,GOV,GMT,CEOIS";
			int numRec = 1;
			if (duration == 3)
			{	
				numRec = 3; //get 3 records for quarter
			}
			if (past == Boolean.FALSE)
			{
				rs = qd.fetchData (selectFields,"MONTHLY_SPEND",where,numRec * 1);
			}
			else
			{
				rs = qd.fetchData (selectFields,"MONTHLY_SPEND",where,numRec * 2);
				if (rs != null)
				{
					rs.next(); //advance past the present month
					if (duration == 3)
					{
						//advance a quarter
						rs.next();
						rs.next();
					}
				}
			}

		}

		
		if (rs!=null)
		{
			while (rs.next())
			{
				ys.year = rs.getInt("YEAR");
				if (duration >= 2)
				{
					ys.month = rs.getInt("MONTH");
				}
				ys.voiceSpend += rs.getInt("VOICE_SPEND");
				ys.dataSpend += rs.getInt("DATA_SPEND");
				ys.vasSpend += rs.getInt("VAS_SPEND");
				ys.roaming += rs.getInt("ROAMING");
				ys.plan += rs.getInt("PLAN_CHARGES");
				ys.total += rs.getInt("SUM_TOTAL");
				ys.vpc += rs.getInt("VPC");
				ys.dpc += rs.getInt("DPC");
				ys.vaspc += rs.getInt("VASPC");
				ys.rpc += rs.getInt("ROAMINGPC");
				ys.planpc += rs.getInt("PLANPC");
				ys.rctg += rs.getInt("RCTG");
				ys.gov += rs.getInt("GOV");
				ys.gmt += rs.getInt("GMT");
				ys.ceois += rs.getInt("CEOIS");
			}
		}
		rs.close();
		return ys;
	}

	public List<TopUsersPayload> getTopUsers() throws Exception
	{
		QueryDash qd = new QueryDash() ;
		ResultSet rs = qd.fetchData ("USER_ID,USER_NAME, BILL_AMT","TOP_USERS","1=1 ORDER BY BILL_AMT DESC",5);
		List<TopUsersPayload> docs = null ;
		if (rs!=null)
		{
			docs = new ArrayList<TopUsersPayload>();	
				
			int i=0;
			while (rs.next()) 
			{
				TopUsersPayload transactionPayload = new TopUsersPayload();
				transactionPayload.id = rs.getInt("USER_ID") ;
				transactionPayload.name = rs.getString("USER_NAME") ;
				transactionPayload.billAmt = rs.getInt("BILL_AMT");
				docs.add(i++,transactionPayload);					
			}
				
		}
		rs.close();
		return docs;

	}
	public SubscriberPayload getSubscriber(String customerId)  throws Exception 
	{			
		int custId = Integer.parseInt(customerId) ;
		QueryDash qd = new QueryDash() ;
		ResultSet rs = qd.fetchSubscribers ("SUB_NAME,SUB_ID,DIVN,GEO,STATUS,EMAIL,PLAN_ID","TELCO_SUBSCRIBERS",custId ) ;
		SubscriberPayload transactionPayload = null;
		if (rs!=null)
		{
			//docs = new ArrayList<SubscriberPayload>();	
			
			//int i=0;
			while (rs.next()) 
			{
				
				transactionPayload = new SubscriberPayload();				
				transactionPayload.name = rs.getString("SUB_NAME") ;
				transactionPayload.id = rs.getInt("SUB_ID") ;
				transactionPayload.divn = rs.getString("DIVN") ;
				transactionPayload.geo = rs.getString("GEO") ;
				transactionPayload.status= rs.getInt("STATUS") ;
				transactionPayload.email = rs.getString("EMAIL");
				transactionPayload.planid = rs.getInt("PLAN_ID") ;
			
									
			}
			rs.close();
			
		}


		return transactionPayload ;
	}


}
