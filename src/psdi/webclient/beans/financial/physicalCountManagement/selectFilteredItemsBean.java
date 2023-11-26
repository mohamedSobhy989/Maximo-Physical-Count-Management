
package psdi.webclient.beans.financial.physicalCountManagement;
import java.rmi.RemoteException;
import java.util.ArrayList;

import psdi.mbo.MboRemote;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;

public class selectFilteredItemsBean extends DataBean {

	public selectFilteredItemsBean() {
		super();
 	}
	
	private MboRemote owner = null;
	int flag = 0;
	public MboRemote setowner() throws RemoteException, MXException
	{
		owner = getMbo().getOwner();
		flag = 1;
		return owner;
		
	}
	/*
	public int search() throws MXException, RemoteException {
        System.out.println("search:::: bean class");
 
        owner = getMbo(); 
        
         //* if you add new column in database configuration and added it to application designer in our dialog
        // * define new attribute here by the same column name of database searchable 
         //* after define attribute and get his value add it to listOfValue list
         
        String itemnum = getString("ITEMNUM");
        String commodityGroup = owner.getString("COMMODITYGROUP");
        String commodityCode = owner.getString("COMMODITYCODE");
        String lotType = getString("LOTTYPE");
        String binNum = getString("BINNUM");
        String reorder = getString("REORDER");
        String conditionEnabled = getString("CONDITIONENABLED");
        String capitalized = getString("CAPITALIZED");
         System.out.println(" commodityGroup is : " + commodityGroup + " and binnum is " + binNum) ;
        listOfValue.add(itemnum);
        listOfValue.add(commodityGroup);
        listOfValue.add(commodityCode);
        listOfValue.add(lotType);
        listOfValue.add(binNum);
        listOfValue.add(reorder);
        listOfValue.add(conditionEnabled);
        listOfValue.add(capitalized);
        //          add your column name to listOfCulmn list 
         
        listOfCulmn.add("itemnum");
        listOfCulmn.add("commodityGroup");
        listOfCulmn.add("commodityCode");
        listOfCulmn.add("lotType");
        listOfCulmn.add("binNum");
        listOfCulmn.add("REORDER");
        listOfCulmn.add("conditionEnabled");
        listOfCulmn.add("capitalized");
        
        
        owner = getMbo(); 
        System.out.println(" owner : "  + owner );
        try {
			((itemPhyCntFind)owner).setup(listOfCulmn,listOfValue);
		} catch (Exception e) {
			System.out.println("exception : " + e.getMessage());
		}
        
        //this.app.getDataBean("receiptinputfind").save();
        System.out.println("search::2");
        DataBean selord = this.app.getDataBean("SELECTEDFILTER");
        System.out.println("search:3");
        selord.refreshTable();
        System.out.println("search:4");
        this.sessionContext.queueRefreshEvent();
        return 1;
    }
	*/
	
	
	
	
	 ArrayList<String> listOfValue = new ArrayList<String>();
     ArrayList<String> listOfCulmn = new ArrayList<String>();
}
