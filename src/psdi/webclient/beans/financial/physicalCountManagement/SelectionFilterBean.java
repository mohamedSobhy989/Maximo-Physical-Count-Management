
package psdi.webclient.beans.financial.physicalCountManagement;
import java.rmi.RemoteException;

import psdi.app.financial.physicalcountmanagement.views.ItemPhyCntResult;
import psdi.app.inventory.InvBalancesSetRemote;
import psdi.mbo.MboRemote;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;

public class SelectionFilterBean extends DataBean{

	public SelectionFilterBean() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Override
	public synchronized int execute() throws MXException, RemoteException {
		System.out.println("execute");
		
		
		MboRemote mbo = getMbo();
		System.out.println("mbo = " + mbo );
		((ItemPhyCntResult)mbo).saveItemSet();
		return super.execute();
	}

	
	
	
}
