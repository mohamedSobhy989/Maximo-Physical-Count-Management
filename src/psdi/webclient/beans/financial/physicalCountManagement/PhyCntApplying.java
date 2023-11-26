package psdi.webclient.beans.financial.physicalCountManagement;

import java.rmi.RemoteException;
import java.sql.SQLException;

import psdi.app.financial.physicalcountmanagement.itemPhysicalCount;
import psdi.app.financial.physicalcountmanagement.phyCntMngLine.itemPhysicalCountLine;
import psdi.app.inventory.InvBalancesSetRemote;
import psdi.mbo.MboRemote;
import psdi.util.MXApplicationException;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;

public class PhyCntApplying extends DataBean {

	public PhyCntApplying() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void initialize() throws MXException, RemoteException {
		System.out.println("initialize");
		
	}
	
	@Override
	public synchronized int execute() throws MXException, RemoteException {
		System.out.println("execute fun .. apply new physicalcount ");
		MboRemote owner = getMbo().getOwner();
		System.out.println("owner " + owner);
		((itemPhysicalCount)owner).applyPhyCnt();
		 super.execute();
		 super.save(); 
		return 1;
	}

}
